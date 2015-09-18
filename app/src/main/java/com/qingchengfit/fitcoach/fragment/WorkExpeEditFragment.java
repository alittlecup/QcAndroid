package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
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
    private String mTitle;
    private QcExperienceResponse.DataEntity.ExperiencesEntity experiencesEntity;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workexepedit, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.showToolbar();
        fragmentCallBack.onToolbarMenu(0, 0, mTitle);
        if (experiencesEntity != null) {
            workexpeditStartTime.setContent(DateUtils.getDateMonth(DateUtils.formatDateFromServer(experiencesEntity.getStart())));
            workexpeditStartEnd.setContent(DateUtils.getDateMonth(DateUtils.formatDateFromServer(experiencesEntity.getEnd())));
            workexpeditDescripe.setText(experiencesEntity.getDescription());
            workexpeditPosition.setContent(experiencesEntity.getPosition());
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
        QcCloudClient.getApi().postApi.qcAddExperience(new AddWorkExperience(
                App.coachid, "2015-07-01", "2015-08-30", "北京", "xxxx", "coach", "description", "12", "13", "14", "15", "16"
        )).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @OnClick(R.id.workexpedit_gym_name)
    public void onClickGym() {
        fragmentCallBack.onFragmentChange(new SearchFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
