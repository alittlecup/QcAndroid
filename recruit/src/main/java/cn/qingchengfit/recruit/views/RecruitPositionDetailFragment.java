package cn.qingchengfit.recruit.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.TagItem;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.SeekPositionPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.tencent.qcloud.timchat.ui.qcchat.AddConversationProcessor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/5/26.
 */
public class RecruitPositionDetailFragment extends BaseFragment
    implements SeekPositionPresenter.MVPView {

  @BindView(R2.id.rv_demands) RecyclerView rvDemands;
  @BindView(R2.id.rv_welfare) RecyclerView rvWelfare;
  @BindView(R2.id.img_gym) ImageView imgGym;
  @BindView(R2.id.tv_gym_name) TextView tvGymName;
  @BindView(R2.id.tv_address) TextView tvAddress;
  @BindView(R2.id.img_right) ImageView imgRight;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
  @BindView(R2.id.tv_position_name) TextView tvPositionName;
  @BindView(R2.id.tv_salary) TextView tvSalary;
  @BindView(R2.id.img_created_by) ImageView imgCreatedBy;
  @BindView(R2.id.tv_created_by) TextView tvCreatedBy;
  @BindView(R2.id.tv_position_crated_at) TextView tvPositionCratedAt;
  @Inject RecruitRouter router;
  @Inject SeekPositionPresenter presenter;
  @BindView(R2.id.tv_position_desc) TextView tvPositionDesc;
  @BindView(R2.id.tv_position_require) TextView tvPositionRequire;
  private Job job;

  public static RecruitPositionDetailFragment newInstance(Job job) {
    Bundle args = new Bundle();
    args.putParcelable("job", job);
    RecruitPositionDetailFragment fragment = new RecruitPositionDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    job = getArguments().getParcelable("job");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_seek_position_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    //onJob(job);
    //onGym(job.gym);
    presenter.queryDetail(job.id);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("职位详情");
    //toolbar.inflateMenu(R.dimen.);
  }

  @Override
  public void onJob(Job job) {
    if (job == null) return;
    this.job = job;
    tvPositionName.setText(job.name);
    tvSalary.setText(RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary));

    //要求
    List<Pair<Integer, String>> demandsData = new ArrayList<>();
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_experience,
        RecruitBusinessUtils.getWorkYear(job.min_work_year, job.max_work_year)));
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_gender,
        RecruitBusinessUtils.getGender(job.gender)));
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_age,
        RecruitBusinessUtils.getAge(job.min_age, job.max_age)));
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_education,
        RecruitBusinessUtils.getDegree(getContext(), job.education)));
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_height,
        RecruitBusinessUtils.getHeight(job.min_height, job.max_height)));
    demandsData.add(new Pair<Integer, String>(R.drawable.vd_recruit_jobintro_weight,
        RecruitBusinessUtils.getWeight(job.min_weight, job.max_weight)));
    DemandAdapter adapter = new DemandAdapter(getContext(), demandsData);
    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setFlexWrap(FlexWrap.WRAP);
    layoutManager.setAlignItems(AlignItems.STRETCH);
    rvDemands.setLayoutManager(layoutManager);
    rvDemands.setNestedScrollingEnabled(false);
    rvDemands.setAdapter(adapter);
    //福利
    if (job.welfare != null) {
      CommonFlexAdapter welfareAdapter = new CommonFlexAdapter(new ArrayList());
      rvWelfare.setLayoutManager(new GridLayoutManager(getContext(), 4));
      int colorOrange = Color.parseColor("#fbaa72");
      for (String s : job.welfare) {
        welfareAdapter.addItem(new TagItem(s, colorOrange));
      }
      rvWelfare.setNestedScrollingEnabled(false);
      rvWelfare.setAdapter(welfareAdapter);
    }
    //职位描述
    tvPositionDesc.setText(job.description);
    tvPositionRequire.setText(job.requirement);
    //场馆人员信息
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_gym_menber_info,
            RecruitGymMemberInfoFragmentBuilder.newRecruitGymMemberInfoFragment(
                job.gym.member_count, job.gym.staff_count, job.gym.area, job.gym.coach_count))
        .commit();
    //场馆设施
    getChildFragmentManager().beginTransaction()
        .replace(R.id.frag_gym_equipment,
            RecruitGymEquipmentFragmentBuilder.newRecruitGymEquipmentFragment(job.gym))
        .commit();

    //创建者信息
    if (job.created_by != null && job.created_at != null) {
      PhotoUtils.small(imgCreatedBy, job.created_by.avatar);
      tvCreatedBy.setText(job.created_by.username);
      tvPositionCratedAt.setText(
          DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(job.created_at)) + " 发布此职位");
    }
  }

  @Override public void onList(List<Job> jobs, int page, int totalCount) {

  }

  @Override public void starOK() {

  }

  @Override public void unStarOk() {

  }

  /**
   * 发送简历成功
   */
  @Override public void sendResumeOk() {

  }

  @Override
  public void onJobsIndex(Number completed, int fair_count, String avatar, String fiar_banner) {

  }

  public void onGym(Gym gym) {
    tvGymName.setText(gym.name);
    tvAddress.setText(gym.getAddressStr());
    PhotoUtils.small(imgGym, gym.photo);
  }

  @Override public String getFragmentName() {
    return RecruitPositionDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 健身房详情
   */
  @OnClick(R2.id.layout_gym_info) public void onBtnGymClicked() {
    router.toGymDetial(job.gym);
  }

  /**
   * 收藏
   */
  @OnClick(R2.id.btn_starred) public void onBtnStarredClicked() {
    presenter.starPosition(job.id);
  }

  /**
   * 与他联系
   */
  @OnClick(R2.id.btn_contact_him) public void onBtnContactHimClicked() {
    AddConversationProcessor addConversationProcessor = new AddConversationProcessor(getContext().getApplicationContext());
    String jobStr = "{userAction:15, type:2, address:\"北京朝阳区\", name:\"trytrytry\", iconUrl:\"http://zoneke-img.b0.upaiyun.com/45accede5e36007f542a60327cb5cde2.png!120x120\",max_salary:15,min_salary:10,min_height:100,max_height:150,min_work_year:3,max_work_year:8,gender:1,min_age:20,max_age:32}";
    addConversationProcessor.addRecruitConversation("qctest_" + job.created_by.id, "", jobStr);
  }

  /**
   * 发送简历
   */
  @OnClick(R2.id.btn_send_resume) public void onBtnSendResumeClicked() {
    DialogSendResumeFragment.newCompletedSend(89)
        .show(getChildFragmentManager(), DialogSendResumeFragment.class.getName());
  }
}
