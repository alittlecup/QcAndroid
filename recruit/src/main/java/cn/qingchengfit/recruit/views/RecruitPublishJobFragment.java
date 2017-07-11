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
@FragmentWithArgs public class RecruitPublishJobFragment extends BaseFragment
    implements JobPresenter.MVPView {

  public final static int PUBLISH_POSITION = 0x11;      //发布职位
  public final static int MODIFY_POSITION = 0x12;       //编辑职位

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.civ_position_name) CommonInputView civPositionName;
  @BindView(R2.id.civ_salary_rank) CommonInputView civSalaryRank;
  @BindView(R2.id.civ_position_desc) CommonInputView civPositionDesc;
  @BindView(R2.id.civ_position_demands) CommonInputView civPositionDemands;
  @BindView(R2.id.civ_position_require) CommonInputView civPositionRequire;
  @BindView(R2.id.civ_position_welfare) CommonInputView civPositionWelfare;
  @BindView(R2.id.civ_gym_desc) CommonInputView civGymDesc;
  @BindView(R2.id.btn_publish) Button btnPublish;

  @Inject RecruitRouter router;
  @Inject JobPresenter presenter;
  @Arg String gymId;
  @Arg int type;
  @Arg Job job;

  private JobBody body;

  private TwoScrollPicker twoScrollPicker;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_publish_job, container, false);
    unbinder = ButterKnife.bind(this, view);
    civGymDesc.setContent("详情");
    civGymDesc.setContentColor(getResources().getColor(R.color.qc_text_grey));
    delegatePresenter(presenter, this);
    initContent();
    initRxBus();
    initToolbar(toolbar);
    toolbarTitile.setText("发布职位");
    return view;
  }

  private void initContent() {
    if (type == MODIFY_POSITION) {
      if (job != null) {
        civPositionName.setContent(job.name);
        civSalaryRank.setContent(RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary));
        civPositionDemands.setContent(job.requirement);
        civPositionDesc.setContent(job.description);
        civPositionRequire.setContent(
            RecruitBusinessUtils.getWorkYear(job.min_work_year, job.max_work_year)
                + "/"
                + RecruitBusinessUtils.getGender(job.gender)
                + "/"
                + RecruitBusinessUtils.getDegree(getContext(), job.education));
        civPositionWelfare.setContent(RecruitBusinessUtils.getPositionDamen(job.welfare));
      }
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          return false;
        }
      });
      btnPublish.setVisibility(View.GONE);
    }
  }

  private void initRxBus() {
    RxBusAdd(EventPulishPosition.class).subscribe(new Action1<EventPulishPosition>() {
      @Override public void call(EventPulishPosition eventPulishPosition) {
        if (eventPulishPosition != null) {
          refreshCivContent(eventPulishPosition.damenMap);
          params.putAll(eventPulishPosition.damenMap);
        }
      }
    });
  }

  private HashMap<String, Object> refreshCivContent(HashMap<String, Object> map) {
    if (map.containsKey("name")) {
      civPositionName.setContent(map.get("name").toString());
      civPositionName.setContentColor(getResources().getColor(R.color.qc_text_grey));
      return map;
    }

    //用type区分职位描述／任职要求
    if (map.containsKey("description")) {
      if (map.get("type").equals("职位描述")) {
        civPositionDesc.setContent("详情");
        civPositionDesc.setContentColor(getResources().getColor(R.color.qc_text_grey));
        return map;
      }
      if (map.get("type").equals("任职要求")) {
        String tempStr = (String) map.get("description");
        map.remove("description");
        map.put("requirement", tempStr);
        civPositionDemands.setContent("详情");
        civPositionDemands.setContentColor(getResources().getColor(R.color.qc_text_grey));
        return map;
      }
    }
    if (map.containsKey("welfare")) {
      civPositionWelfare.setContent(
          RecruitBusinessUtils.getPositionDamen((List<String>) (map.get("welfare"))));
      civPositionWelfare.setContentColor(getResources().getColor(R.color.qc_text_grey));
      return map;
    }
    if (map.containsKey("education")) {
      civPositionRequire.setContent(RecruitBusinessUtils.getWorkYear((int) map.get("min_work_year"),
          (int) map.get("max_work_year")) + "/" + RecruitBusinessUtils.getGender(
          (int) map.get("gender")) + "/" + RecruitBusinessUtils.getDegree(getContext(),
          (int) map.get("education")));
      civPositionRequire.setContentColor(getResources().getColor(R.color.qc_text_grey));
      body.min_work_year = (Integer) map.get("min_work_year");
      body.max_work_year = (Integer) map.get("max_work_year");
      body.min_age = (Integer) map.get("min_age");
      body.max_age = (Integer) map.get("max_age");
      body.gender = (Integer) map.get("gender");
      body.education = (Integer) map.get("education");
      body.min_height = (Float) map.get("min_height");
      body.max_height = (Float) map.get("max_height");
      body.min_weight = (Float) map.get("min_weight");
      body.max_weight = (Float) map.get("max_weight");
      return map;
    }
    return map;
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
          //body.min_salary = -1000;
          //body.max_salary = -1000;
          params.put("min_salary", -1000);
          params.put("max_salary", -1000);
        } else if (left == 100) {
          civSalaryRank.setContent("100K以上");
          //body.min_salary = 100001;
          //body.max_salary = 100001;
          params.put("min_salary", 100001);
          params.put("max_salary", 100001);
        } else {
          if (left > right) {
            ToastUtils.show("请选择正确的薪水区间");
            return;
          }
          civSalaryRank.setContent((left - MIN_SALARY) + "-" + (right - MIN_SALARY + 2) + "K");
          //body.min_salary = (left - MIN_SALARY) * 1000;
          //body.max_salary = (right - MIN_SALARY + 2) * 1000;
          params.put("min_salary", (left - MIN_SALARY) * 1000);
          params.put("max_salary", (right - MIN_SALARY + 2) * 1000);
        }
      }
    });
    civSalaryRank.setContentColor(getResources().getColor(R.color.qc_text_grey));
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
    router.toEditRecruitDesc(
        params.containsKey("description") ? (String) params.get("description") : "", "职位描述");
  }

  /**
   * 任职要求
   */
  @OnClick(R2.id.civ_position_demands) public void onCivPositionDemandsClicked() {
    router.toEditRecruitDesc(
        params.containsKey("requirement") ? (String) params.get("requirement") : "", "任职要求");
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
    ArrayList<String> list = new ArrayList<>();
    if (params.containsKey("welfare") && ((ArrayList<String>) params.get("welfare")).size() > 0) {
      list = ((ArrayList<String>) params.get("welfare"));
    } else {
      list.add("提供住宿");
      list.add("提供保险");
    }
    router.toPositionWalfare(list, "职位福利");
  }

  /**
   * 场馆介绍
   */
  @OnClick(R2.id.civ_gym_desc) public void onCivGymDescClicked() {
    router.toWriteGymDetailDesc();
  }

  /**
   * 发布职位按钮
   */
  @OnClick(R2.id.btn_publish) public void onBtnPublishClicked() {
    if (!params.containsKey("name") || !params.containsKey("description") || !params.containsKey(
        "requirement") || !params.containsKey("min_salary") || !params.containsKey("max_salary")) {
      DialogUtils.showAlert(getContext(), "请完善职位信息");
      return;
    }
    showLoadingTrans();
    PublishPositionBody publishPositionBody = new PublishPositionBody(params);
    presenter.publishJob(gymId, publishPositionBody);
  }

  @Override public void onEditOk() {
    ToastUtils.show("发布成功");
    hideLoadingTrans();
    getActivity().onBackPressed();
  }

  @Override public void onJobDetail(Job job) {

  }

  @Override public void starOK() {

  }

  @Override public void unStarOk() {

  }

  @Override public void onPostResumeOk() {

  }

  @Override public void onInviteOk() {

  }
}
