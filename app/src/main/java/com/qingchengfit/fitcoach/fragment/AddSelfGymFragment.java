package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.GymCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChangeTimeActivity;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSelfGymFragment extends Fragment {
    public static final String TAG = AddSelfGymFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.addselfgym_name)
    CommonInputView addselfgymName;
    @Bind(R.id.addselfgym_time)
    CommonInputView addselfgymTime;
    @Bind(R.id.addselfgym_comfirm)
    Button addselfgymComfirm;
    int id = -1;
    private QcPrivateGymReponse reponse;
    private PostPrivateGym postPrivateGym;
    private boolean mIsNew;
    private MaterialDialog delDialog;
    private MaterialDialog loadingDialog;

    public AddSelfGymFragment() {
    }

    public static AddSelfGymFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        AddSelfGymFragment fragment = new AddSelfGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddSelfGymFragment newInstance(boolean isNew) {

        Bundle args = new Bundle();
        args.putBoolean("isNew", isNew);
        AddSelfGymFragment fragment = new AddSelfGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void ShowLoading(String content) {
        if (loadingDialog == null)
            loadingDialog = new MaterialDialog.Builder(getContext())
                    .content("请稍后")
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        if (content != null)
            loadingDialog.setContent(content);
        loadingDialog.show();
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .title("删除个人健身房?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            ShowLoading("正在删除,请稍后...");
                            QcCloudClient.getApi().postApi.qcDelPrivateGym(App.coachid)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<QcResponse>() {
                                        @Override
                                        public void onCompleted() {
                                            loadingDialog.dismiss();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            loadingDialog.dismiss();
                                            ToastUtils.show(R.drawable.ic_share_fail, "删除失败");
                                        }

                                        @Override
                                        public void onNext(QcResponse qcResponse) {
                                            if (qcResponse.status == ResponseResult.SUCCESS || getActivity() != null) {
                                                ToastUtils.show("删除成功");
                                                getActivity().finish();
                                            } else {
                                                ToastUtils.show(R.drawable.ic_share_fail, "删除失败");
                                            }
                                        }
                                    });
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            mIsNew = getArguments().getBoolean("isNew", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_self_gym, container, false);
        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        if (mIsNew) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //展示不可回退
                }
            });
        } else
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        if (id > 0) {
            toolbar.setTitle("修改个人健身房");
            toolbar.inflateMenu(R.menu.menu_delete);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showDialog();
                    return true;
                }
            });
        } else {
            toolbar.setTitle("添加个人健身房");
        }
        postPrivateGym = new PostPrivateGym();

        QcCloudClient.getApi().getApi.qcGetPrivateGym(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(qcPrivateGymReponse -> {
                    reponse = qcPrivateGymReponse;
                    postPrivateGym.open_time = new Gson().toJson(qcPrivateGymReponse.data.system.openTimes);
                    postPrivateGym.name = qcPrivateGymReponse.data.system.name;
                    Collections.sort(reponse.data.system.openTimes, new GymCompare());
                    StringBuffer sb = new StringBuffer();
                    for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                        if (openTime.day == 1) {
                            sb.append("周一").append(openTime.start).append("-")
                                    .append(openTime.end).append(",").append("周二...");
                            break;
                        }
                    }
                    return sb.toString();
                })
                .subscribe(s -> {
                    addselfgymName.setContent(reponse.data.system.name);
                    addselfgymTime.setContent(s);
                });
        return view;
    }


    @OnClick(R.id.addselfgym_comfirm)
    public void onComfirm() {
        if (TextUtils.isEmpty(addselfgymName.getContent())) {
            Toast.makeText(App.AppContex, "健身房名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        postPrivateGym.name = addselfgymName.getContent();
        if (id > 0) {
            QcCloudClient.getApi().postApi.qcPostPrivateGym(App.coachid, postPrivateGym)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<QcResponse, Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call(QcResponse qcResponse) {
                            if (qcResponse.status == ResponseResult.SUCCESS) {

                                return QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            } else {
                                ToastUtils.show(R.drawable.ic_share_fail, "修改失败");
                            }
                            return Observable.just(null);
                        }
                    }, new Func1<Throwable, Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call(Throwable throwable) {
                            ToastUtils.show(R.drawable.ic_share_fail, "修改失败");
                            return Observable.just(null);
                        }
                    }, new Func0<Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call() {
                            return Observable.just(null);
                        }
                    })
                    .filter(new Func1<QcCoachSystemResponse, Boolean>() {
                        @Override
                        public Boolean call(QcCoachSystemResponse qcCoachSystemResponse) {
                            if (qcCoachSystemResponse == null)
                                return false;
                            else return true;

                        }
                    })
                    .subscribe(new Subscriber<QcCoachSystemResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.show(R.drawable.ic_share_fail, getString(R.string.common_modify_success));
                        }

                        @Override
                        public void onNext(QcCoachSystemResponse qcCoachSystemResponse) {
                            if (getActivity() != null) {
                                PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachSystemResponse));
                                ToastUtils.show(getString(R.string.common_modify_failed));
                                getActivity().onBackPressed();
                            }
                        }
                    });
        } else {
            QcCloudClient.getApi().postApi.qcCreatePrivateGym(App.coachid, postPrivateGym)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<QcResponse, Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call(QcResponse qcResponse) {
                            if (qcResponse.status == ResponseResult.SUCCESS) {

                                return QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread());
                            } else {
                                ToastUtils.show(R.drawable.ic_share_fail, "添加个人健身房失败");
                            }
                            return Observable.just(null);
                        }
                    }, new Func1<Throwable, Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call(Throwable throwable) {
                            ToastUtils.show(R.drawable.ic_share_fail, "添加个人健身房失败");
                            return Observable.just(null);
                        }
                    }, new Func0<Observable<QcCoachSystemResponse>>() {
                        @Override
                        public Observable<QcCoachSystemResponse> call() {
                            return Observable.just(null);
                        }
                    })
                    .filter(new Func1<QcCoachSystemResponse, Boolean>() {
                        @Override
                        public Boolean call(QcCoachSystemResponse qcCoachSystemResponse) {
                            if (qcCoachSystemResponse == null)
                                return false;
                            else return true;

                        }
                    })
                    .subscribe(new Subscriber<QcCoachSystemResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.show(R.drawable.ic_share_fail, getString(R.string.common_modify_success));
                        }

                        @Override
                        public void onNext(QcCoachSystemResponse qcCoachSystemResponse) {
                            if (getActivity() != null) {
                                PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachSystemResponse));
                                ToastUtils.show(getString(R.string.common_modify_failed));
                                getActivity().onBackPressed();
                            }
                        }
                    });
        }


    }


    @OnClick(R.id.addselfgym_time)
    public void onChangeTime() {
        Intent it = new Intent(getActivity(), ChangeTimeActivity.class);
        it.putExtra("time", new Gson().toJson(reponse.data.system.openTimes));
        startActivityForResult(it, 222);
        getActivity().overridePendingTransition(R.anim.timepicker_anim_enter_bottom, R.anim.slide_hold);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {
            postPrivateGym.open_time = data.getStringExtra("time");
            Type listType = new TypeToken<ArrayList<QcPrivateGymReponse.OpenTime>>() {
            }.getType();

            reponse.data.system.openTimes = new Gson().fromJson(postPrivateGym.open_time, listType);
            StringBuffer sb = new StringBuffer();
            for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                if (openTime.day == 1) {
                    sb.append("周一").append(openTime.start).append("-")
                            .append(openTime.end).append(",").append("周二...");
                    break;
                }
            }
            addselfgymTime.setContent(sb.toString());

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
