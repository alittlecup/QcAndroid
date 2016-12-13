package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.HidenBean;
import com.qingchengfit.fitcoach.http.bean.OneExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkExpDetailFragment extends BaseSettingFragment {


    @BindView(R.id.gym_img)
    ImageView gymImg;
    @BindView(R.id.gym_time)
    TextView gymTime;
    @BindView(R.id.workexp_detail_hiden)
    TextView workexpDetailHiden;
    @BindView(R.id.workexp_detail_position)
    TextView workexpDetailPosition;
    @BindView(R.id.workexp_detail_desc)
    TextView workexpDetailDesc;
    @BindView(R.id.workexp_detail_group_count)
    TextView workexpDetailGroupCount;
    @BindView(R.id.workexp_detail_group_server)
    TextView workexpDetailGroupServer;
    @BindView(R.id.workexp_detail_group_layout)
    LinearLayout workexpDetailGroupLayout;
    @BindView(R.id.workexp_detail_private_count)
    TextView workexpDetailPrivateCount;
    @BindView(R.id.workexp_detail_private_server)
    TextView workexpDetailPrivateServer;
    @BindView(R.id.workexp_detail_private_layout)
    LinearLayout workexpDetailPrivateLayout;
    @BindView(R.id.workexp_detail_sale)
    TextView workexpDetailSale;
    @BindView(R.id.workexp_detail_sale_layout)
    LinearLayout workexpDetailSaleLayout;
    @BindView(R.id.gym_name)
    TextView gymName;
    @BindView(R.id.gym_address)
    TextView gymAddress;
    @BindView(R.id.gym_identify)
    ImageView gymIdentify;
    private int mExpId;
    Subscription httpsub;
    private DialogSheet dialogSheet;
    private MaterialDialog delDialog;



    QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;
    private Unbinder unbinder;

    public WorkExpDetailFragment() {
    }

    public static WorkExpDetailFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        WorkExpDetailFragment fragment = new WorkExpDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mExpId = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_exp_detail, container, false);
        unbinder=ButterKnife.bind(this, view);

        freshData();
        return view;
    }

    private void freshData() {
        httpsub = QcCloudClient.getApi().getApi.qcGetExperience(mExpId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<OneExperienceResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OneExperienceResponse qcResponse) {
                        QcExperienceResponse.DataEntity.ExperiencesEntity.GymEntity gym = qcResponse.data.experience.getGym();
                        gymName.setText(gym.getName());
                        if (gym.getDistrict() != null && gym.getDistrict().city != null && !TextUtils.isEmpty(gym.getDistrict().city.name))
                            gymAddress.setText("|" + gym.getDistrict().city.name);
                        //设置工作时间
                        experiencesEntity = qcResponse.data.experience;
                        String start = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart()));
                        Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        c.setTime(d);
                        if (c.get(Calendar.YEAR) == 3000) {
                            start = start + "至今";
                        } else {
                            start = start+"至" + DateUtils.Date2YYYYMMDD(d);
                        }
                        gymTime.setText(start);
                        if (experiencesEntity.is_authenticated())
                            gymIdentify.setVisibility(View.VISIBLE);
                        else gymIdentify.setVisibility(View.GONE);
                        Glide.with(App.AppContex).load(gym.getPhoto()).asBitmap().into(new CircleImgWrapper(gymImg, App.AppContex));

                        //是否隐藏
                        if (experiencesEntity.is_authenticated()) { //认证条目
                            workexpDetailHiden.setVisibility(View.VISIBLE);
                            if (experiencesEntity.is_hidden()) {
                                workexpDetailHiden.setBackgroundResource(R.color.orange);
                                workexpDetailHiden.setText(getString(R.string.workexp_detail_hiden));
                                fragmentCallBack.onToolbarMenu(R.menu.menu_unhide, R.drawable.ic_arrow_left, "工作经历详情");
                                fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        //取消隐藏
                                        QcCloudClient.getApi().postApi.qcHidenExperience(mExpId, new HidenBean(false))
                                                .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                                                .subscribe(new Subscriber<QcResponse>() {
                                                    @Override
                                                    public void onCompleted() {

                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }

                                                    @Override
                                                    public void onNext(QcResponse qcResponse) {
                                                        if (qcResponse.status == ResponseResult.SUCCESS)
                                                            freshData();

                                                    }
                                                });
                                        return true;
                                    }
                                });
                            } else {
                                workexpDetailHiden.setBackgroundResource(R.color.green);
                                workexpDetailHiden.setText(getString(R.string.workexp_detail_unhiden));

                                fragmentCallBack.onToolbarMenu(R.menu.menu_hide, R.drawable.ic_arrow_left, "工作经历详情");
                                fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        //隐藏
                                        QcCloudClient.getApi().postApi.qcHidenExperience(mExpId, new HidenBean(true))
                                                .observeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                                                .subscribe(new Subscriber<QcResponse>() {
                                                    @Override
                                                    public void onCompleted() {

                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }

                                                    @Override
                                                    public void onNext(QcResponse qcResponse) {
                                                        if (qcResponse.status == ResponseResult.SUCCESS)
                                                            freshData();

                                                    }
                                                });
                                        return true;
                                    }
                                });
                            }
                        } else { //自己添加的条目
                            workexpDetailHiden.setVisibility(View.GONE);
                            fragmentCallBack.onToolbarMenu(R.menu.menu_logout, R.drawable.ic_arrow_left, "工作经历详情");
                            fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    //编辑和删除
                                    if (dialogSheet == null){
                                        dialogSheet = new DialogSheet(getContext());
                                        dialogSheet.addButton("编辑", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogSheet.dismiss();
                                                fragmentCallBack.onFragmentChange(new WorkExpeEditFragment().newInstance("编辑工作经历",experiencesEntity));
                                            }
                                        })
                                        .addButton("删除", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogSheet.dismiss();
                                                showDialog();
                                            }
                                        })
                                        ;
                                    }
                                    dialogSheet.show();
                                    return true;
                                }
                            });
                        }

                        workexpDetailPosition.setText(experiencesEntity.getPosition());
                        workexpDetailDesc.setText(experiencesEntity.getDescription());
                        if (experiencesEntity.getGroup_course() != 0) {
                            workexpDetailGroupLayout.setVisibility(View.VISIBLE);
                            workexpDetailGroupCount.setText(experiencesEntity.getGroup_course() + "");
                            workexpDetailGroupServer.setText(experiencesEntity.getGroup_user() + "");
                        } else workexpDetailGroupLayout.setVisibility(View.GONE);
                        if (experiencesEntity.getPrivate_course() != 0) {
                            workexpDetailPrivateLayout.setVisibility(View.VISIBLE);
                            workexpDetailPrivateCount.setText(experiencesEntity.getPrivate_course() + "");
                            workexpDetailPrivateServer.setText(experiencesEntity.getPrivate_user() + "");
                        } else workexpDetailPrivateLayout.setVisibility(View.GONE);
                        if (experiencesEntity.getSale() != 0) {
                            workexpDetailSaleLayout.setVisibility(View.VISIBLE);
                            workexpDetailSale.setText(experiencesEntity.getSale() + "");
                        } else workexpDetailSaleLayout.setVisibility(View.GONE);


                    }
                });
    }


    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .content("删除此条工作经历?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            fragmentCallBack.ShowLoading("请稍后");
                            QcCloudClient.getApi().postApi.qcDelExperience(experiencesEntity.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(qcResponse -> qcResponse.status == ResponseResult.SUCCESS)
                                    .subscribe(aBoolean -> {
                                        fragmentCallBack.hideLoading();
                                        if (aBoolean) {
                                            getActivity().onBackPressed();
                                            Toast.makeText(App.AppContex, "删除成功", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(App.AppContex, "删除失败", Toast.LENGTH_SHORT).show();

                                    }, throwable -> {
                                    }, () -> {
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
    public void onDestroyView() {
        if (httpsub != null && !httpsub.isUnsubscribed())
            httpsub.unsubscribe();
        super.onDestroyView();
        unbinder.unbind();
    }
}
