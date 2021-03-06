package cn.qingchengfit.recruit.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitConstants;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.FragmentRecruitSeekPositionDetailBinding;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.JobPresenter;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.tencent.qcloud.timchat.ui.qcchat.AddConversationProcessor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    implements JobPresenter.MVPView, DialogSendResumeFragment.OnSendResumeListener,
    ResumePresenter.MVPView {

  protected Job job;
  //@BindView(R2.id.rv_demands) RecyclerView rvDemands;
  //@BindView(R2.id.rv_welfare) QcTagGroup rvWelfare;
  //@BindView(R2.id.img_recruit_gym_avatar) ImageView imgGym;
  //@BindView(R2.id.tv_recruit_gym_name) TextView tvGymName;
  //@BindView(R2.id.tv_address) TextView tvAddress;
  //@BindView(R2.id.img_right) ImageView imgRight;
  //@BindView(R2.id.toolbar) Toolbar toolbar;
  //@BindView(R2.id.toolbar_title) TextView toolbarTitile;
  //@BindView(R2.id.tv_position_name) TextView tvPositionName;
  //@BindView(R2.id.tv_salary) TextView tvSalary;
  //@BindView(R2.id.img_created_by) ImageView imgCreatedBy;
  //@BindView(R2.id.tv_created_by) TextView tvCreatedBy;
  //@BindView(R2.id.tv_starred) TextView tvStarred;
  //@BindView(R2.id.tv_position_desc) TouchyWebView tvPositionDesc;
  //@BindView(R2.id.tv_position_require) TouchyWebView tvPositionRequire;
  //@BindView(R2.id.tv_gym_des) TouchyWebView tvGymDes;
  //@BindView(R2.id.img_stared) ImageView imgStared;
  //@BindView(R2.id.btn_contact_him) Button btnContactHim;
  //@BindView(R2.id.btn_send_resume) Button btnSendResume;
  //@BindView(R2.id.tv_position_crated_at) TextView tvPositionCratedAt;
  //
  @Inject RecruitRouter router;
  @Inject JobPresenter presenter;
  @Inject ResumePresenter resumePresenter;
  @Inject QcRestRepository restRepository;
  @Inject LoginStatus loginStatus;

  protected FragmentRecruitSeekPositionDetailBinding db;
  private boolean isStarred;
  private ResumeHome resumeHome;

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

   void initdatabding(LayoutInflater inflater, ViewGroup container){
    db = DataBindingUtil.inflate(inflater,R.layout.fragment_recruit_seek_position_detail,container,false);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    //View view = inflater.inflate(R.layout.fragment_recruit_seek_position_detail, container, false);
    initdatabding(inflater,container);
    delegatePresenter(presenter, this);
    delegatePresenter(resumePresenter, this);

    ToolbarModel tm = new ToolbarModel("职位详情");
    tm.setMenu(R.menu.menu_share);
    tm.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        String title =
            job.gym != null ? job.gym.getBrand_name() + job.gym.getName() + "正在招聘" + job.name + "职位"
                : "";
        String content = "【薪资】"
            + RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary)
            + "\n【坐标】"
            + job.gym.getAddressStr();
        String pic = job.gym != null ? job.gym.photo
            : "https://img.qingchengfit.cn/977ad17699c4e4212b52000ed670091a.png";
        String url = restRepository.getHost() + "mobile/job/" + job.id + "/";
        ShareDialogFragment.newInstance(title, content, pic, url)
            .show(getChildFragmentManager(), "");
        return false;
      }
    });
    db.setToolbarModel(tm);
    initToolbar(db.layoutToolbar.toolbar);
    RxBusAdd(EventLoginChange.class).onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventLoginChange>() {
          @Override public void call(EventLoginChange eventLoginChange) {
            initData();
          }
        });
    initClick();
    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    onJobDetail(job);
    onGym(job.gym);
    initData();
  }

  private void initData() {
    presenter.queryJob(job.id);
    if (loginStatus.isLogined()) {
      resumePresenter.queryResumeHome();
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
  }

  @Override public void onEditOk() {

  }

  @Override public void onJobDetail(Job job) {
    if (job == null) return;
    if (job.name == null) return;
    this.job = job;
    db.tvPositionName.setText(job.name);
    db.tvSalary.setText(RecruitBusinessUtils.getSalary(job.min_salary, job.max_salary));
    if (job.favorited != null) {
      db.imgStared.setImageResource(
          job.favorited ? R.drawable.vd_recruit_job_starred : R.drawable.vd_recruit_job_star);
      db.tvStarred.setText(job.favorited ? "已收藏" : "收藏职位");
      isStarred = job.favorited;
      db.btnContactHim.setText(job.contacted ? "继续沟通" : "与TA沟通");
      db.btnSendResume.setEnabled(!job.deliveried);
      db.btnSendResume.setText(job.deliveried ? "已投递" : "投递简历");
    }
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
    db.rvDemands.setLayoutManager(layoutManager);
    db.rvDemands.setNestedScrollingEnabled(false);
    db.rvDemands.setAdapter(adapter);
    //福利
    if (job.welfare != null) {
      db.rvWelfare.setTags(job.welfare);
    }
    //职位描述
    if (!TextUtils.isEmpty(job.description)) {
      db.tvPositionDesc.loadData(CmStringUtils.getMobileHtml(job.description),
          "text/html; charset=UTF-8", null);
    }
    if (!TextUtils.isEmpty(job.requirement)) {
      db.tvPositionRequire.loadData(CmStringUtils.getMobileHtml(job.requirement),
          "text/html; charset=UTF-8", null);
    }
    if (job.gym.member_count != null) {
      //场馆人员信息
      getChildFragmentManager().beginTransaction()
          .replace(R.id.frag_gym_menber_info,
              RecruitGymMemberInfoFragmentBuilder.newRecruitGymMemberInfoFragment(
                  job.gym.member_count, job.gym.staff_count, job.gym.area, job.gym.coach_count))
          .commit();
    }
    //场馆设施
    if (!ListUtils.isEmpty(job.gym.facilities)) {
      getChildFragmentManager().beginTransaction()
          .replace(R.id.frag_gym_equipment,
              RecruitGymEquipmentFragmentBuilder.newRecruitGymEquipmentFragment(job.gym))
          .commit();
    }
    if (!TextUtils.isEmpty(job.gym.detail_description)) {
      db.tvGymDes.loadData(CmStringUtils.getMobileHtml(job.gym.detail_description),
          "text/html; charset=UTF-8", null);
    }

    //创建者信息
    if (job.created_by != null && job.created_at != null) {
      PhotoUtils.smallCircle(db.imgCreatedBy, job.created_by.avatar);
      db.tvCreatedBy.setText(job.created_by.username);
      db.tvPositionCratedAt.setText(
          DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(job.published_at)) + " 发布此职位");
    }
  }


  @Override public void starOK() {
    isStarred = true;
    ToastUtils.show("收藏成功");
    db.tvStarred.setText("已收藏");
    db.imgStared.setImageResource(R.drawable.vd_recruit_job_starred);
  }

  @Override public void unStarOk() {
    isStarred = false;
    db.tvStarred.setText("收藏职位");
    db.imgStared.setImageResource(R.drawable.vd_recruit_job_star);
  }

  @Override public void onPostResumeOk() {
    AddConversationProcessor addConversationProcessor =
        new AddConversationProcessor(getContext().getApplicationContext());
    Gson gson = new Gson();
    String resumeStr = "{\"userAction\":1002, \"data\":" + gson.toJson(
        resumePresenter.dealResumeMessage(resumeHome)) + "}";
    addConversationProcessor.sendResumeOrRecruit((getString(R.string.chat_user_id_header,job.created_by.id)), resumeStr, "");
    hideLoadingTrans();
    job.deliveried = true;
    db.btnSendResume.setText(job.deliveried ? "已投递" : "投递简历");
    db.btnSendResume.setEnabled(false);
    ToastUtils.show(R.drawable.vector_hook_white, "投递成功");
  }

  @Override public void onInviteOk() {

  }

  @Override public void toEditJob() {
    router.toPublishPosition(job.gym.id, job, RecruitPublishJobFragment.MODIFY_POSITION);
  }

  public void onCheckSuccessed(){

  }

  @Override public void onJobList(List<Job> jobList) {

  }

  @Override public void onGymDetail(Gym gym) {

  }

  public void onGym(Gym gym) {
    if (gym == null) return;
    db.tvRecruitGymName.setText(gym.name);
    db.tvRecruitAddress.setText(gym.getAddressStr());
    PhotoUtils.small(db.imgRecruitGymAvatar, gym.photo);
  }

  @Override public String getFragmentName() {
    return RecruitPositionDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
  void initClick(){
    db.layoutGymInfo.setOnClickListener(view1 -> onBtnGymClicked());
    db.btnStarred.setOnClickListener(view -> onBtnStarredClicked());
    db.btnContactHim.setOnClickListener(view -> onBtnContactHimClicked());
    db.btnSendResume.setOnClickListener(view -> onBtnSendResumeClicked());
  }
  /**
   * 健身房详情
   */
  public void onBtnGymClicked() {
    router.toGymDetial(job.gym);
  }

  /**
   * 收藏
   */
   public void onBtnStarredClicked() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    if (isStarred) {
      presenter.unstarPosition(job.id);
    } else {
      presenter.starPosition(job.id);
    }
  }

  /**
   * 与他联系
   *
   * 传给聊天页面参数中加入userAction，1001表示求职端职位，1002表示求职端简历，1003表示招聘端职位，1004表示招聘端简历
   */
  public void onBtnContactHimClicked() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    //AddConversationProcessor addConversationProcessor =
    //    new AddConversationProcessor(getContext().getApplicationContext());
    //Gson gson = new Gson();
    //String jobStr = "{userAction:1001, data:" + gson.toJson(presenter.getRecruitModel(job)) + "}";
    //addConversationProcessor.addRecruitConversation("qc_" + job.created_by.id, "", jobStr);
    Uri data = Uri.parse("imchat://chatactivity");
    //Intent intent = new Intent(Intent.ACTION_VIEW, data);
    Intent intent = new Intent(getActivity(), JobSearchChatActivity.class);
    //AddConversationProcessor addConversationProcessor =
    //    new AddConversationProcessor(getContext().getApplicationContext());
    Gson gson = new Gson();
    String jobStr =
        "{\"userAction\":1001, \"data\":" + gson.toJson(presenter.getRecruitModel(job)) + "}";
    String resumeStr = "{\"userAction\":1002, \"data\":" + gson.toJson(
        resumePresenter.dealResumeMessage(resumeHome)) + "}";
    //intent.putExtra("id", (BuildConfig.DEBUG ? "qctest_" : "qc_") + job.created_by.id);
    //intent.putExtra("datas", jobStr);
    intent.putExtra(RecruitConstants.IDENTIFY, getString(R.string.chat_user_id_header, job.created_by.id));
    intent.putExtra(RecruitConstants.TEMP_CONVERSATION_TYPE, RecruitConstants.C2C);
    intent.putExtra(RecruitConstants.CHAT_JOB_RESUME, resumeStr);
    intent.putExtra(RecruitConstants.CHAT_JOB_ID, job.id);
    intent.putExtra(RecruitConstants.CHAT_RECRUIT, jobStr);
    intent.putExtra(RecruitConstants.CHAT_JOB_SEARCH_OR_RECRUIT, RecruitConstants.JOB_SEARCH);
    intent.putExtra(RecruitConstants.CHAT_RECRUIT_STATE, job.deliveried);
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  /**
   * 发送简历
   */
  public void onBtnSendResumeClicked() {
    if (!loginStatus.isLogined()){
      BaseRouter.toLogin(this);
      return;
    }
    if (DialogSendResumeFragment.needShow(getContext())) {
      DialogSendResumeFragment.newCompletedSend(89, this)
          .show(getChildFragmentManager(), DialogSendResumeFragment.class.getName());
    } else {
      onSend();
    }
  }

  @Override public void onSend() {
    showLoadingTrans();
    presenter.sendResume(job.id);
  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {
    this.resumeHome = resumeHome;
  }

  @Override public void onWorkExpList(List<WorkExp> workExps) {

  }

  @Override public void onEduExpList(List<Education> eduExps) {

  }

  @Override public void onCertiList(List<Certificate> certificates) {

  }

  @Override public void starOk() {

  }

  @Override public void unStartOk() {

  }
}
