package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/6/7.
 */
public class RecruitPublishJobFragment extends BaseFragment {

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruit_publish_job, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public String getFragmentName() {
        return RecruitPublishJobFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 职位名称
     */
    @OnClick(R2.id.civ_position_name) public void onCivPositionNameClicked() {

    }

    /**
     * 薪资范围
     */
    @OnClick(R2.id.civ_salary_rank) public void onCivSalaryRankClicked() {
    }

    /**
     * 职位描述
     */
    @OnClick(R2.id.civ_position_desc) public void onCivPositionDescClicked() {
    }

    /**
     * 任职要求
     */
    @OnClick(R2.id.civ_position_demands) public void onCivPositionDemandsClicked() {
    }

    /**
     * 职位要求
     */
    @OnClick(R2.id.civ_position_require) public void onCivPositionRequireClicked() {
    }

    /**
     * 职位福利
     */
    @OnClick(R2.id.civ_position_welfare) public void onCivPositionWelfareClicked() {
    }

    /**
     * 场馆介绍
     */
    @OnClick(R2.id.civ_gym_desc) public void onCivGymDescClicked() {
    }

    /**
     * 场馆介绍
     */
    @OnClick(R2.id.btn_publish) public void onBtnPublishClicked() {
    }
}
