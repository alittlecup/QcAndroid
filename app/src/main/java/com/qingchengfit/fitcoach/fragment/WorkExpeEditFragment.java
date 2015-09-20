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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    TimePopupWindow pwTime;
    private String mTitle;
    private QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;
    private AddWorkExperience addWorkExperience;

    public static WorkExpeEditFragment newInstance(String mTitle, QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity) {

        Bundle args = new Bundle();
        args.putString("title", mTitle);
        if (experiencesEntity != null)
            args.putParcelable("experience", experiencesEntity);
        WorkExpeEditFragment fragment = new WorkExpeEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            experiencesEntity = getArguments().getParcelable("experience");
        }
        pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
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
                    return false;
                }
            });
            workexpeditStartTime.setContent(DateUtils.getDateMonth(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            workexpeditStartEnd.setContent(DateUtils.getDateMonth(DateUtils.formatDateFromServer(experiencesEntity.getEnd())));
            workexpeditDescripe.setText(experiencesEntity.getDescription());
            workexpeditPosition.setContent(experiencesEntity.getPosition());
            workexpeditGymName.setContent(experiencesEntity.getGym().getName());
            workexpeditGroupClass.setContent(Integer.toString(experiencesEntity.getGroup_course()));
            workexpeditGroupNum.setContent(Integer.toString(experiencesEntity.getGroup_user()));
            workexpeditPrivateClass.setContent(Integer.toString(experiencesEntity.getPrivate_course()));
            workexpeditPrivateNum.setContent(Integer.toString(experiencesEntity.getPrivate_user()));
            workexpeditSale.setContent(Integer.toString(experiencesEntity.getSale()));
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
        addWorkExperience.setStart(starttime);
        addWorkExperience.setEnd(endtime);
        addWorkExperience.setDescription(description);
        addWorkExperience.setGroup_course(groupCount);
        addWorkExperience.setGroup_user(groupNum);
        addWorkExperience.setPosition(postion);
        addWorkExperience.setPrivate_course(privateClass);
        addWorkExperience.setPrivate_user(privateNum);
        addWorkExperience.setSale(sale);
        QcCloudClient.getApi().postApi.qcAddExperience(addWorkExperience).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @OnClick(R.id.workexpedit_gym_name)
    public void onClickGym() {
//        startActivity(new Intent(getContext(), SplashActivity.class));
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_GYM);
        startActivityForResult(toSearch, 10010);
    }

    @OnClick(R.id.workexpedit_start_time)
    public void onStartTime() {
        pwTime.setOnTimeSelectListener(date -> {

            workexpeditStartTime.setContent(DateUtils.getDateDay(date));
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.workexpedit_start_end)
    public void onEndTime() {
        pwTime.setOnTimeSelectListener(date -> {
            workexpeditStartEnd.setContent(DateUtils.getDateDay(date));
        });
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
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
