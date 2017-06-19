package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddWorkExperience;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/8 2015.
 */
public class WorkExpeEditFragment extends BaseSettingFragment {

    @BindView(R.id.workexpedit_start_time) CommonInputView workexpeditStartTime;
    @BindView(R.id.workexpedit_start_end) CommonInputView workexpeditStartEnd;
    @BindView(R.id.workexpedit_city) CommonInputView workexpeditCity;
    @BindView(R.id.workexpedit_gym_name) TextView workexpeditGymName;
    @BindView(R.id.workexpedit_position) CommonInputView workexpeditPosition;
    @BindView(R.id.workexpedit_descripe) EditText workexpeditDescripe;
    @BindView(R.id.workexpedit_group_class) CommonInputView workexpeditGroupClass;
    @BindView(R.id.workexpedit_group_num) CommonInputView workexpeditGroupNum;
    @BindView(R.id.workexpedit_private_class) CommonInputView workexpeditPrivateClass;
    @BindView(R.id.workexpedit_private_num) CommonInputView workexpeditPrivateNum;
    @BindView(R.id.workexpedit_sale) CommonInputView workexpeditSale;
    @BindView(R.id.workexpedit_comfirm_btn) Button workexpeditComfirmBtn;
    @BindView(R.id.rootview) ScrollView rootview;
    TimeDialogWindow pwTime;
    @BindView(R.id.workexpedit_expe_layout) LinearLayout workexpeditExpeLayout;
    @BindView(R.id.host_img) ImageView hostImg;
    @BindView(R.id.host_qc_identify) ImageView hostQcIdentify;
    @BindView(R.id.host_address) TextView hostAddress;
    private String mTitle;
    private QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;
    private AddWorkExperience addWorkExperience;
    private MaterialDialog delDialog;
    private DialogSheet mDialogSheet;
    private boolean mIsAdd;
    private Unbinder unbinder;

    public static WorkExpeEditFragment newInstance(String mTitle, QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity) {

        Bundle args = new Bundle();
        args.putString("title", mTitle);
        if (experiencesEntity != null) args.putParcelable("experience", experiencesEntity);
        WorkExpeEditFragment fragment = new WorkExpeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WorkExpeEditFragment newInstance(String mTitle, QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity,
        boolean add) {

        Bundle args = new Bundle();
        args.putString("title", mTitle);
        if (experiencesEntity != null) args.putParcelable("experience", experiencesEntity);
        args.putBoolean("add", add);
        WorkExpeEditFragment fragment = new WorkExpeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
                .content("删除此条工作经历?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override public void onPositive(MaterialDialog dialog) {
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
                                } else {
                                    Toast.makeText(App.AppContex, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }, throwable -> {
                            }, () -> {
                            });
                        dialog.dismiss();
                    }

                    @Override public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();
        }
        delDialog.show();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            experiencesEntity = getArguments().getParcelable("experience");
            mIsAdd = getArguments().getBoolean("add");
        }
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workexepedit, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentCallBack.showToolbar();
        fragmentCallBack.onToolbarMenu(0, 0, mTitle);

        if (addWorkExperience == null) {
            addWorkExperience = new AddWorkExperience(App.coachid);
        }
        if (experiencesEntity != null) {
            fragmentCallBack.onToolbarMenu(0, 0, mTitle);
            //            fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item) {
            //                    showDialog();
            //                    return true;
            //                }
            //            });
            workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            Date d = DateUtils.formatDateFromServer(experiencesEntity.getEnd());
            Calendar c = Calendar.getInstance(Locale.getDefault());
            c.setTime(d);
            if (c.get(Calendar.YEAR) == 3000) {
                workexpeditStartEnd.setContent("至今");
            } else {
                workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(d));
            }

            workexpeditDescripe.setText(experiencesEntity.getDescription());
            workexpeditPosition.setContent(experiencesEntity.getPosition());
            if (experiencesEntity.getGym() != null) {
                workexpeditGymName.setText(experiencesEntity.getGym().getName());
                addWorkExperience.setGym_id((int) experiencesEntity.getGym().getId());
                Glide.with(App.AppContex)
                    .load(experiencesEntity.getGym().getPhoto())
                    .asBitmap()
                    .into(new CircleImgWrapper(hostImg, App.AppContex));
                //                if (experiencesEntity.getGym().is_authenticated()){
                //                    hostQcIdentify.setVisibility(View.VISIBLE);
                //                }else hostQcIdentify.setVisibility(View.GONE);
                hostAddress.setText(experiencesEntity.getGym().getAddress());
            }
            workexpeditGroupClass.setContent(Integer.toString(experiencesEntity.getGroup_course()));
            workexpeditGroupNum.setContent(Integer.toString(experiencesEntity.getGroup_user()));
            workexpeditPrivateClass.setContent(Integer.toString(experiencesEntity.getPrivate_course()));
            workexpeditPrivateNum.setContent(Integer.toString(experiencesEntity.getPrivate_user()));
            workexpeditSale.setContent(experiencesEntity.getSale());
        } else {
            workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
        }

        return view;
    }

    @OnClick(R.id.workexpedit_comfirm_btn) public void onComfirm() {

        String starttime = DateUtils.formatDateToServer(workexpeditStartTime.getContent());
        String endtime = DateUtils.formatDateToServer(workexpeditStartEnd.getContent());
        String postion = workexpeditPosition.getContent();
        String description = workexpeditDescripe.getText().toString();
        String groupCount = workexpeditGroupClass.getContent();
        String groupNum = workexpeditGroupNum.getContent();
        String privateClass = workexpeditPrivateClass.getContent();
        String privateNum = workexpeditPrivateNum.getContent();
        String sale = workexpeditSale.getContent();
        String gym = workexpeditGymName.getText().toString().trim();

        if (TextUtils.isEmpty(postion)) {
            Toast.makeText(getContext(), "请填写职位信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addWorkExperience.getGym_id() == 0 || isEmpty(gym)) {
            Toast.makeText(getContext(), "请选择健身房", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endtime.equalsIgnoreCase("至今")) {
            endtime = "3000-1-1";
        }
        if (DateUtils.formatDateFromYYYYMMDD(starttime).getTime() > DateUtils.formatDateFromYYYYMMDD(endtime).getTime()) {
            Toast.makeText(getContext(), "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        addWorkExperience.setStart(starttime);
        addWorkExperience.setEnd(endtime);
        addWorkExperience.setDescription(description);
        addWorkExperience.setGroup_course(groupCount);
        addWorkExperience.setGroup_user(groupNum);
        addWorkExperience.setPosition(postion);
        addWorkExperience.setPrivate_course(privateClass);
        addWorkExperience.setPrivate_user(privateNum);
        addWorkExperience.setSale(sale);

        Observer<QcResponse> qcResponseAction = new Observer<QcResponse>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {
                fragmentCallBack.hideLoading();
                Toast.makeText(App.AppContex, "提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override public void onNext(QcResponse qcResponse) {
                fragmentCallBack.hideLoading();
                if (qcResponse.status == ResponseResult.SUCCESS) {
                    fragmentCallBack.fixCount();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(App.AppContex, qcResponse.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
        fragmentCallBack.ShowLoading("请稍后");
        if (mIsAdd) {
            QcCloudClient.getApi().postApi.qcAddExperience(addWorkExperience)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponseAction);
        } else {
            QcCloudClient.getApi().postApi.qcEditExperience(experiencesEntity.getId(), addWorkExperience)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponseAction);
        }
    }

    @OnClick(R.id.workexpedit_expe_layout) public void onDescripte() {
        workexpeditDescripe.requestFocus();
    }

    @OnClick(R.id.host_layout) public void onClickGym() {
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_GYM);
        startActivityForResult(toSearch, 10010);
    }

    @OnClick(R.id.workexpedit_start_time) public void onStartTime() {
        pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
        pwTime.setOnTimeSelectListener(date -> {
            if (date.getTime() > new Date().getTime()) {
                Toast.makeText(App.AppContex, "起始时间不能晚于今天", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.equals("至今", workexpeditStartEnd.getContent())
                && DateUtils.formatDateFromYYYYMMDD(workexpeditStartEnd.getContent()).getTime() < DateUtils.formatDateFromYYYYMMDD(
                workexpeditStartTime.getContent()).getTime()) {
                Toast.makeText(App.AppContex, "起始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                return;
            }

            workexpeditStartTime.setContent(DateUtils.Date2YYYYMMDD(date));
            pwTime.dismiss();
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    @OnClick(R.id.workexpedit_start_end) public void onEndTime() {
        if (mDialogSheet == null) {
            mDialogSheet = DialogSheet.builder(getContext()).addButton("至今", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mDialogSheet.dismiss();
                    workexpeditStartEnd.setContent("至今");
                }
            }).addButton("选择日期", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mDialogSheet.dismiss();
                    pwTime.setRange(1900, Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR));
                    pwTime.setOnTimeSelectListener(date -> {
                        if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(workexpeditStartTime.getContent()).getTime()) {
                            Toast.makeText(App.AppContex, "结束时间不能早于结束时间", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        workexpeditStartEnd.setContent(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    });
                    pwTime.setRange(1900, 2100);
                    pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
                }
            });
        }

        mDialogSheet.show();
    }

    @OnClick(R.id.workexpedit_city) public void onCityClick() {
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010 && resultCode > 0) {
            workexpeditGymName.setText(data.getStringExtra("username"));
            addWorkExperience.setGym_id(data.getIntExtra("id", 0));
            boolean isAuth = data.getBooleanExtra("isauth", false);
            if (isAuth) {
                hostQcIdentify.setVisibility(View.VISIBLE);
            } else {
                hostQcIdentify.setVisibility(View.GONE);
            }
            String address = data.getStringExtra("address");
            if (isEmpty(address)) {
                hostAddress.setVisibility(View.GONE);
            } else {
                hostAddress.setVisibility(View.VISIBLE);
                hostAddress.setText(address);
            }
            Glide.with(App.AppContex).load(data.getStringExtra("pic")).asBitmap().into(new CircleImgWrapper(hostImg, App.AppContex));
        }
    }
}
