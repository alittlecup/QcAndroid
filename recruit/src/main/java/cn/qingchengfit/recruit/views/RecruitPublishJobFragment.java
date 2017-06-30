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
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TwoScrollPicker;
import java.util.ArrayList;
import javax.inject.Inject;

import static cn.qingchengfit.recruit.views.resume.ResumeIntentsFragment.MIN_SALARY;

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
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.civ_position_name) CommonInputView civPositionName;
  @BindView(R2.id.civ_salary_rank) CommonInputView civSalaryRank;
  @BindView(R2.id.civ_position_desc) CommonInputView civPositionDesc;
  @BindView(R2.id.civ_position_demands) CommonInputView civPositionDemands;
  @BindView(R2.id.civ_position_require) CommonInputView civPositionRequire;
  @BindView(R2.id.civ_position_welfare) CommonInputView civPositionWelfare;
  @BindView(R2.id.civ_gym_desc) CommonInputView civGymDesc;

  @Inject RecruitRouter router;

  private JobBody body;

  private TwoScrollPicker twoScrollPicker;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_publish_job, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public String getFragmentName() {
    return RecruitPublishJobFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
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
    twoScrollPicker.setListener(new TwoScrollPicker.TwoSelectItemListener() {
      @Override public void onSelectItem(int left, int right) {
        if (left == 0) {
          civSalaryRank.setContent("面议");
          body.min_salary = -1000;
          body.max_salary = -1000;
        } else if (left == 100) {
          civSalaryRank.setContent("100K以上");
          body.min_salary = 100001;
          body.max_salary = 100001;
        } else {
          if (left > right) {
            ToastUtils.show("请选择正确的薪水区间");
            return;
          }
          civSalaryRank.setContent((left - MIN_SALARY) + "-" + (right - MIN_SALARY + 2) + "K");
          body.min_salary = (left - MIN_SALARY) * 1000;
          body.max_salary = (right - MIN_SALARY + 2) * 1000;
        }
      }
    });
    final ArrayList<String> l = new ArrayList<>();
    final ArrayList<String> r = new ArrayList<>();
    l.add("面议");
    for (int i = 0; i < 100; i++) {
      l.add(i + "K");
      r.add((i + 1) + "K");
    }
    l.add("100K以上");
    twoScrollPicker.show(l, r, 0, 0);
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
    router.toRequireOfJob();
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
   * 发布职位按钮
   */
  @OnClick(R2.id.btn_publish) public void onBtnPublishClicked() {
  }
}
