package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.TextpaperUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SearchActivity;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddWorkExperience;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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


    @Bind(R.id.workexpedit_start_time)
    CommonInputView workexpeditStartTime;
    @Bind(R.id.workexpedit_start_end)
    CommonInputView workexpeditStartEnd;
    @Bind(R.id.workexpedit_city)
    CommonInputView workexpeditCity;
    @Bind(R.id.workexpedit_gym_name)
    CommonInputView workexpeditGymName;
    @Bind(R.id.workexpedit_position)
    CommonInputView workexpeditPosition;
    @Bind(R.id.workexpedit_descripe)
    EditText workexpeditDescripe;
    @Bind(R.id.workexpedit_group_class)
    CommonInputView workexpeditGroupClass;
    @Bind(R.id.workexpedit_group_num)
    CommonInputView workexpeditGroupNum;
    @Bind(R.id.workexpedit_private_class)
    CommonInputView workexpeditPrivateClass;
    @Bind(R.id.workexpedit_private_num)
    CommonInputView workexpeditPrivateNum;
    @Bind(R.id.workexpedit_sale)
    CommonInputView workexpeditSale;
    @Bind(R.id.workexpedit_comfirm_btn)
    Button workexpeditComfirmBtn;
    @Bind(R.id.rootview)
    ScrollView rootview;
    TimeDialogWindow pwTime;
    private String mTitle;
    private QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;
    private AddWorkExperience addWorkExperience;
    private MaterialDialog delDialog;

    public static WorkExpeEditFragment newInstance(String mTitle, QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity) {

        Bundle args = new Bundle();
        args.putString("title", mTitle);
        if (experiencesEntity != null)
            args.putParcelable("experience", experiencesEntity);
        WorkExpeEditFragment fragment = new WorkExpeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .title("是否确定删除")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            QcCloudClient.getApi().postApi.qcDelExperience(experiencesEntity.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(qcResponse -> qcResponse.status == ResponseResult.SUCCESS)
                                    .subscribe(aBoolean -> {
                                        if (aBoolean) {
                                            getActivity().onBackPressed();
                                            Toast.makeText(App.AppContex, "删除成功", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(App.AppContex, "删除失败", Toast.LENGTH_SHORT).show();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            experiencesEntity = getArguments().getParcelable("experience");
        }
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workexepedit, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.showToolbar();
        fragmentCallBack.onToolbarMenu(0, 0, mTitle);

        if (addWorkExperience == null)
            addWorkExperience = new AddWorkExperience(App.coachid);
        if (experiencesEntity != null) {
            fragmentCallBack.onToolbarMenu(R.menu.menu_delete, 0, mTitle);
            fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showDialog();
                    return true;
                }
            });
            workexpeditStartTime.setContent(DateUtils.getDateDay(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            workexpeditStartEnd.setContent(DateUtils.getDateDay(DateUtils.formatDateFromServer(experiencesEntity.getEnd())));
            workexpeditDescripe.setText(experiencesEntity.getDescription());
            workexpeditPosition.setContent(experiencesEntity.getPosition());
            if (experiencesEntity.getGym() != null) {
                workexpeditGymName.setContent(experiencesEntity.getGym().getName());
                addWorkExperience.setGym_id(experiencesEntity.getId());
            }
            workexpeditGroupClass.setContent(Integer.toString(experiencesEntity.getGroup_course()));
            workexpeditGroupNum.setContent(Integer.toString(experiencesEntity.getGroup_user()));
            workexpeditPrivateClass.setContent(Integer.toString(experiencesEntity.getPrivate_course()));
            workexpeditPrivateNum.setContent(Integer.toString(experiencesEntity.getPrivate_user()));
            workexpeditSale.setContent(Integer.toString(experiencesEntity.getSale()));
        } else {
            workexpeditStartTime.setContent(DateUtils.getDateDay(new Date()));
            workexpeditStartEnd.setContent(DateUtils.getDateDay(new Date()));

        }


        return view;
    }

    @OnClick(R.id.workexpedit_comfirm_btn)
    public void onComfirm() {
        String starttime = workexpeditStartTime.getContent();
        String endtime = workexpeditStartEnd.getContent();
        String postion = workexpeditPosition.getContent();
        String description = workexpeditDescripe.getText().toString();
        String groupCount = workexpeditGroupClass.getContent();
        String groupNum = workexpeditGroupNum.getContent();
        String privateClass = workexpeditPrivateClass.getContent();
        String privateNum = workexpeditPrivateNum.getContent();
        String sale = workexpeditSale.getContent();
        if (TextpaperUtils.isEmpty(starttime, endtime, postion, groupCount, groupNum, privateClass, privateNum,
                sale)) {
            Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addWorkExperience.getGym_id() == 0) {
            Toast.makeText(getContext(), "请选择健身房", Toast.LENGTH_SHORT).show();
            return;
        }
        addWorkExperience.setStart(DateUtils.formatDateToServer(starttime));
        addWorkExperience.setEnd(DateUtils.formatDateToServer(endtime));
        addWorkExperience.setDescription(description);
        addWorkExperience.setGroup_course(groupCount);
        addWorkExperience.setGroup_user(groupNum);
        addWorkExperience.setPosition(postion);
        addWorkExperience.setPrivate_course(privateClass);
        addWorkExperience.setPrivate_user(privateNum);
        addWorkExperience.setSale(sale);

        Action1 qcResponseAction = (Action1<QcResponse>) qcResponse -> {
            getActivity().runOnUiThread(() -> {
                if (qcResponse.status == ResponseResult.SUCCESS) {
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(App.AppContex, qcResponse.msg, Toast.LENGTH_SHORT).show();
                }
            });

        };
        if (experiencesEntity == null)
            QcCloudClient.getApi().postApi.qcAddExperience(addWorkExperience).subscribeOn(Schedulers.newThread()).subscribe(qcResponseAction);
        else
            QcCloudClient.getApi().postApi.qcEditExperience(experiencesEntity.getId(), addWorkExperience).subscribeOn(Schedulers.newThread()).subscribe(qcResponseAction);

    }


    @OnClick(R.id.workexpedit_expe_layout)
    public void onDescripte() {
        workexpeditDescripe.requestFocus();
    }

    @OnClick(R.id.workexpedit_gym_name)
    public void onClickGym() {
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_GYM);
        startActivityForResult(toSearch, 10010);
    }

    @OnClick(R.id.workexpedit_start_time)
    public void onStartTime() {
        pwTime.setOnTimeSelectListener(date -> {

            workexpeditStartTime.setContent(DateUtils.getDateDay(date));
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }

    @OnClick(R.id.workexpedit_start_end)
    public void onEndTime() {
        pwTime.setOnTimeSelectListener(date -> {
            workexpeditStartEnd.setContent(DateUtils.getDateDay(date));
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, new Date());
    }


    @OnClick(R.id.workexpedit_city)
    public void onCityClick() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010 && resultCode > 0) {
            workexpeditGymName.setContent(data.getStringExtra("name"));
            addWorkExperience.setGym_id(data.getIntExtra("id", 0));
        }
    }
}
