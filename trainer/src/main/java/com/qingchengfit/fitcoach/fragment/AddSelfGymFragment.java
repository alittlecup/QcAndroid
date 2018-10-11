package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.GymCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChangeTimeActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.LoadingDialog;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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
	Toolbar toolbar;
	CommonInputView addselfgymName;
	CommonInputView addselfgymTime;
	Button addselfgymComfirm;
    int id = -1;
    private QcPrivateGymReponse reponse;
    private PostPrivateGym postPrivateGym;
    private boolean mIsNew;
    private MaterialDialog delDialog;
    private LoadingDialog loadingDialog;


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
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext(),content);
        }
        loadingDialog.show();
    }

    private void showDialog() {
        DialogUtils.showConfirm(getContext(),"","删除个人健身房?",(dialog,action)->{
            dialog.dismiss();
            switch (action){
                case POSITIVE:deleteGym();break;
            }
        });
    }

    private void deleteGym(){
        ShowLoading("正在删除,请稍后...");
        QcCloudClient.getApi().postApi.qcDelPrivateGym(App.coachid)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(
                (Func1<QcResponse, Observable<QcCoachSystemResponse>>) qcResponse -> {
                    if (qcResponse.status == ResponseResult.SUCCESS || getActivity() != null) {
                        return QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid)
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    } else {
                        return Observable.just(null);
                    }
                }, throwable -> Observable.just(null), () -> Observable.just(null))
            .filter(qcCoachSystemResponse -> {
                if (qcCoachSystemResponse == null) {
                    return false;
                } else {
                    return true;
                }
            })
            .subscribe(new Subscriber<QcCoachSystemResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {
                    if (loadingDialog != null) loadingDialog.dismiss();
                    ToastUtils.show(R.drawable.ic_share_fail, getString(R.string.common_modify_failed));
                }

                @Override public void onNext(QcCoachSystemResponse qcCoachSystemResponse) {
                    if (getActivity() != null) {
                        loadingDialog.dismiss();
                        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems",
                            new Gson().toJson(qcCoachSystemResponse));
                        ToastUtils.show("删除成功");
                        getActivity().finish();
                    }
                }
            });
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id", 0);
            mIsNew = getArguments().getBoolean("isNew", false);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_self_gym, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      addselfgymName = (CommonInputView) view.findViewById(R.id.addselfgym_name);
      addselfgymTime = (CommonInputView) view.findViewById(R.id.addselfgym_time);
      addselfgymComfirm = (Button) view.findViewById(R.id.addselfgym_comfirm);
      view.findViewById(R.id.addselfgym_comfirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });
      view.findViewById(R.id.addselfgym_time).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onChangeTime();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        if (mIsNew) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //展示不可回退
                    DialogUtils.showAlert(getContext(),"设置好一个健身房后，就能开始使用「健身教练助手」了",(dialog,action)->{
                        dialog.dismiss();
                    });
                }
            });
        } else {
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }

        postPrivateGym = new PostPrivateGym();
        if (id > 0) {
            toolbar.setTitle("编辑健身房");
            toolbar.inflateMenu(R.menu.menu_delete);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    showDialog();
                    return true;
                }
            });
        } else {
            toolbar.setTitle("设置健身房");
        }

        QcCloudClient.getApi().getApi.qcGetPrivateGym(App.coachid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .map(qcPrivateGymReponse -> {
            reponse = qcPrivateGymReponse;
            postPrivateGym.open_time = new Gson().toJson(qcPrivateGymReponse.data.system.openTimes);
            postPrivateGym.name = qcPrivateGymReponse.data.system.name;
            Collections.sort(reponse.data.system.openTimes, new GymCompare());
            StringBuffer sb = new StringBuffer();
            for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                if (openTime.day == 1) {
                    sb.append("周一").append(openTime.start).append("-").append(openTime.end).append(",").append("周二...");
                    break;
                    }
            }
            return sb.toString();
        }).subscribe(s -> {
            if (id > 0) addselfgymName.setContent(reponse.data.system.name);
            addselfgymTime.setContent(s);
        }, new HttpThrowable(), () -> {
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    //    @OnClick(R.id.addselfgym_name)
    //    public void onClickGym(){
    //        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
    //        toSearch.putExtra("type", SearchFragment.TYPE_GYM);
    //        startActivityForResult(toSearch, 10010);
    //    }

 public void onComfirm() {
        if (TextUtils.isEmpty(addselfgymName.getContent())) {
            Toast.makeText(App.AppContex, "健身房名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        postPrivateGym.name = addselfgymName.getContent();
        if (id > 0) {
            QcCloudClient.getApi().postApi.qcPostPrivateGym(App.coachid, postPrivateGym)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<QcResponse, Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS) {

                            return QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                                .onBackpressureBuffer()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        } else {
                            ToastUtils.show(R.drawable.ic_share_fail, "修改失败");
                        }
                        return Observable.just(null);
                    }
                }, new Func1<Throwable, Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call(Throwable throwable) {
                        ToastUtils.show(R.drawable.ic_share_fail, "修改失败");
                        return Observable.just(null);
                    }
                }, new Func0<Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call() {
                        return Observable.just(null);
                    }
                })
                .filter(new Func1<QcCoachServiceResponse, Boolean>() {
                    @Override public Boolean call(QcCoachServiceResponse qcCoachSystemResponse) {
                        if (qcCoachSystemResponse == null) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .subscribe(new Subscriber<QcCoachServiceResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        ToastUtils.show(R.drawable.ic_share_fail, getString(R.string.common_modify_failed));
                    }

                    @Override public void onNext(QcCoachServiceResponse qcCoachSystemResponse) {
                        if (getActivity() != null) {
                            PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachSystemResponse));
                            ToastUtils.show(getString(R.string.common_modify_success));
                            RxBus.getBus().post(RxBus.BUS_REFRESH);
                            getActivity().onBackPressed();
                        }
                    }
                });
        } else {
            QcCloudClient.getApi().postApi.qcCreatePrivateGym(App.coachid, postPrivateGym)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<QcResponse, Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS) {

                            return QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                                .onBackpressureBuffer()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        } else {
                            ToastUtils.show(R.drawable.ic_share_fail, "添加个人健身房失败");
                        }
                        return Observable.just(null);
                    }
                }, new Func1<Throwable, Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call(Throwable throwable) {
                        ToastUtils.show(R.drawable.ic_share_fail, "添加个人健身房失败");
                        return Observable.just(null);
                    }
                }, new Func0<Observable<QcCoachServiceResponse>>() {
                    @Override public Observable<QcCoachServiceResponse> call() {
                        return Observable.just(null);
                    }
                })
                .filter(new Func1<QcCoachServiceResponse, Boolean>() {
                    @Override public Boolean call(QcCoachServiceResponse qcCoachSystemResponse) {
                        if (qcCoachSystemResponse == null) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .subscribe(new Subscriber<QcCoachServiceResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        //                            ToastUtils.show(R.drawable.ic_share_fail, ));
                    }

                    @Override public void onNext(QcCoachServiceResponse qcCoachSystemResponse) {
                        if (getActivity() != null) {
                            PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachSystemResponse));
                            ToastUtils.show("添加成功");
                            //                                if (mIsNew){
                            for (CoachService system : qcCoachSystemResponse.data.services) {
                                if (system.model.equalsIgnoreCase("service")) {
                                    Intent intent = new Intent(getActivity(), FragActivity.class);
                                    intent.putExtra("id", system.id);
                                    intent.putExtra("model", system.model);
                                    intent.putExtra("type", 6);
                                    startActivityForResult(intent, 11);
                                }
                            }

                            //                                }
                            getActivity().onBackPressed();
                        }
                    }
                });
        }
    }

 public void onChangeTime() {
        Intent it = new Intent(getActivity(), ChangeTimeActivity.class);
        it.putExtra("time", new Gson().toJson(reponse.data.system.openTimes));
        startActivityForResult(it, 222);
        //        getActivity().overridePendingTransition(R.anim.timepicker_anim_enter_bottom, R.anim.slide_hold);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode > 0) {
            postPrivateGym.open_time = data.getStringExtra("time");
            Type listType = new TypeToken<ArrayList<QcPrivateGymReponse.OpenTime>>() {
            }.getType();

            reponse.data.system.openTimes = new Gson().fromJson(postPrivateGym.open_time, listType);
            StringBuffer sb = new StringBuffer();
            for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                if (openTime.day == 1) {
                    sb.append("周一").append(openTime.start).append("-").append(openTime.end).append(",").append("周二...");
                    break;
                }
            }
            addselfgymTime.setContent(sb.toString());
        } else if (requestCode == 10010 && resultCode > 0) {
            addselfgymName.setContent(data.getStringExtra("username"));
            //            addWorkExperience.setGym_id(data.getIntExtra("id", 0));
            //            boolean isAuth = data.getBooleanExtra("isauth",false);
            //            if (isAuth)
            //                hostQcIdentify.setVisibility(View.VISIBLE);
            //            else hostQcIdentify.setVisibility(View.GONE);
            //            String address = data.getStringExtra("address");
            //            if (TextUtils.isEmpty(address)){
            //                hostAddress.setVisibility(View.GONE);
            //            }else {
            //                hostAddress.setVisibility(View.VISIBLE);
            //                hostAddress.setText(address);
            //            }
            //            Glide.with(App.AppContex).load(data.getStringExtra("pic")).asBitmap().into(new CircleImgWrapper(hostImg,App.AppContex));

        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
