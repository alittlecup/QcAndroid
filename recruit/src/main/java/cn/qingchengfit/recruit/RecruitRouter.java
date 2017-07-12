package cn.qingchengfit.recruit;

import android.support.v4.app.FragmentManager;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Organization;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.views.ChooseGymInRecruitFragment;
import cn.qingchengfit.recruit.views.JobFairsAllFragment;
import cn.qingchengfit.recruit.views.MyPositionsInfoFragment;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.recruit.views.RecruitGymDetailEmployerFragment;
import cn.qingchengfit.recruit.views.RecruitGymDetailFragment;
import cn.qingchengfit.recruit.views.RecruitManageFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDemanFragmentBuilder;
import cn.qingchengfit.recruit.views.RecruitPositionDetailEmployerFragment;
import cn.qingchengfit.recruit.views.RecruitPositionDetailFragment;
import cn.qingchengfit.recruit.views.RecruitPublishJobFragment;
import cn.qingchengfit.recruit.views.RecruitPublishJobFragmentBuilder;
import cn.qingchengfit.recruit.views.RecruitRichTextEditFragmentBuilder;
import cn.qingchengfit.recruit.views.RecruitStaffMyJobFairFragment;
import cn.qingchengfit.recruit.views.RecruitUserMyJobFairFragment;
import cn.qingchengfit.recruit.views.RecruitWelFareFragmentBuilder;
import cn.qingchengfit.recruit.views.RecruitWriteGymIntroFragmentBuilder;
import cn.qingchengfit.recruit.views.ResumeMarketHomeFragment;
import cn.qingchengfit.recruit.views.ResumeRecievedFragmentBuilder;
import cn.qingchengfit.recruit.views.ResumeStarredFragment;
import cn.qingchengfit.recruit.views.SeekPositionHomeFragment;
import cn.qingchengfit.recruit.views.SetNameCommenFragment;
import cn.qingchengfit.recruit.views.jobfair.JobfairDetailFragment;
import cn.qingchengfit.recruit.views.jobfair.JobfairSignUpFragment;
import cn.qingchengfit.recruit.views.resume.AddEduExpFragment;
import cn.qingchengfit.recruit.views.resume.AddEduExpFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.RecordEditFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.RecruitGymEquipFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.RecruitPermissionFragment;
import cn.qingchengfit.recruit.views.resume.ResumeBaseInfoFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeCertificateListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeDetailFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeEditDescFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeEduExpListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeHomeFragment;
import cn.qingchengfit.recruit.views.resume.ResumeIntentJobsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsCitiesFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeIntentsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeShowImgsFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.ResumeWorkExpListFragment;
import cn.qingchengfit.recruit.views.resume.ResumeWorkExpPreviewFragment;
import cn.qingchengfit.recruit.views.resume.WorkExpSyncDetailFragmentBuilder;
import cn.qingchengfit.recruit.views.resume.WorkExpeEditFragmentBuilder;
import cn.qingchengfit.router.InnerRouter;
import cn.qingchengfit.saas.views.fragments.EditGymInfoFragment;
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
 * Created by Paper on 2017/5/31.
 */

public class RecruitRouter extends InnerRouter {

  public static final int RESULT_POSISTION_NAME = 2001;
  public static final int RESULT_POSISTION_DESC = 2003;
  public static final int RESULT_POSISTION_DEMANDS = 2004;
  public static final int RESULT_POSISTION_REQUIRE = 2005;

  @Inject RecruitActivity activity;

  @Inject public RecruitRouter() {
  }

  @Override public FragmentManager getFragmentManager() {
    return activity.getSupportFragmentManager();
  }

  @Override public int getFragId() {
    return R.id.frag_recruit;
  }

  /**
   * 求职版主页
   */
  public void home() {
    init(new SeekPositionHomeFragment());
  }

  public void goJobDetail(Job job) {
    add(RecruitPositionDetailFragment.newInstance(job));
  }

  public void initJobDetail(Job job) {
    init(RecruitPositionDetailFragment.newInstance(job));
  }

  public void mySent() {
    add(MyPositionsInfoFragment.newMySent());
  }

  public void myStarred() {
    add(MyPositionsInfoFragment.newMyStared());
  }

  public void myInvited() {
    add(MyPositionsInfoFragment.newMyInvited());
  }

  public void toGymDetial(Gym service) {
    add(RecruitGymDetailFragment.newInstance(service));
  }


  /**
   * 人才市场 人才浏览 或者叫 招聘版主页
   */
  public void resumeMarketHome() {
    init(new ResumeMarketHomeFragment());
    //getFragmentManager().beginTransaction()
    //    .setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out,
    //        R.anim.card_flip_left_in, R.anim.card_flip_left_out)
    //    .replace(getFragId(), new ResumeMarketHomeFragment())
    //    .addToBackStack(null)
    //    .commit();
  }

  public void resumeHome() {
    init(new ResumeHomeFragment());
  }

  public void toMyResume() {
    add(new ResumeHomeFragment());
  }

  public void editResumeInfo(ResumeBody body) {
    add(new ResumeBaseInfoFragmentBuilder(body).build());
  }

  public void editImages(List<String> imgs) {
    add(new ResumeShowImgsFragmentBuilder(new ArrayList<String>(imgs)).build());
  }

  public void workExpPreview(WorkExp workexp) {
    add(ResumeWorkExpPreviewFragment.newInstance(workexp.id));
  }

  public void editEduExp(Education education) {
    add(new AddEduExpFragmentBuilder().education(education).build());
  }

  public void addEduExp() {
    add(new AddEduExpFragment());
  }

  public void addWorkExp(Gym gym) {
    add(new WorkExpeEditFragmentBuilder(gym).build());
  }

  public void editWorkExp(WorkExp workExp) {
    if (workExp.is_authenticated) {
      add(new WorkExpSyncDetailFragmentBuilder(workExp).build());
    } else {
      add(new WorkExpeEditFragmentBuilder(workExp.gym).workExp(workExp).build());
    }
  }

  public void listCertifaciton() {
    add(new ResumeCertificateListFragment());
  }

  public void listEdulist() {
    add(new ResumeEduExpListFragment());
  }

  public void listWorkList() {
    add(new ResumeWorkExpListFragment());
  }

  public void addCertification(int type, Organization organization) {
    add(new RecordEditFragmentBuilder(organization, type).build());
  }

  public void editCertification(int type, Organization organization, Certificate certificate) {
    add(new RecordEditFragmentBuilder(organization, type).certificatesEntity(certificate).build());
  }

  public void toIntentCities(List<CityBean> listcity) {
    add(new ResumeIntentsCitiesFragmentBuilder(new ArrayList<CityBean>(listcity)).build());
  }

  public void toIntentPosition(List<String> jobs) {
    add(new ResumeIntentJobsFragmentBuilder(new ArrayList<String>(jobs)).build());
  }

  public void toExpect(ResumeHome resumeHome) {
    add(new ResumeIntentsFragmentBuilder().build());
  }

  public void brief(String content) {
    add(new ResumeEditDescFragmentBuilder(content).build());
  }

  /**
   * 招聘管理页
   */
  public void toManageRecruit() {
    add(new RecruitManageFragment());
  }

  /**
   * 专场招聘会专场
   */
  public void toJobFairs(String id) {

  }

  /**
   * 场馆详情 工作人员版
   */
  public void toGymdetailEmployer(Gym gym) {
    add(RecruitGymDetailEmployerFragment.newInstance(gym));
  }

  /**
   * 场馆介绍
   */
  public void toWriteGymIntro(Gym gym) {
    add(new RecruitWriteGymIntroFragmentBuilder(gym).build());
  }

  /**
   * 场馆设备
   */
  public void toGymEquip(ArrayList<String> facilities) {
    add(RecruitGymEquipFragmentBuilder.newRecruitGymEquipFragment(facilities));
  }

  /**
   * 场馆详情介绍
   */
  public void toWriteGymDetailDesc(String s) {
    // TODO: 2017/7/4 使用EditRecruitDesc
  }

  /**
   * 收藏的人才
   */
  public void toMyStarredResume() {

  }

  /**
   * 权限设置
   */
  public void toPermssion(String gymid) {
    add(RecruitPermissionFragment.newInstance(gymid));
  }

  /**
   * 职位要求
   */
  public void toRequireOfJob(JobBody jobBody) {
    add(new RecruitPositionDemanFragmentBuilder(jobBody).build());
  }

  /**
   * 简历详情
   */
  public void toResumeDetail(Resume resume) {

  }

  /**
   * 收藏的人才
   */
  public void toStarredResumes() {
    add(new ResumeStarredFragment());
  }

  /**
   * 主动投递人才
   */
  public void toRecieveResumes(String jobid) {
    add(ResumeRecievedFragmentBuilder.newResumeRecievedFragment(jobid, 0));
  }

  /**
   * 我邀约的人才
   */
  public void toInvitedResumes(String jobid) {
    add(ResumeRecievedFragmentBuilder.newResumeRecievedFragment(jobid, 1));
  }

  /**
   * 选择场馆
   */
  public void chooseGym() {
    add(new ChooseGymInRecruitFragment());
  }

  /**
   * 招聘版本职位
   */
  public void tojobDetailEmployer(Job job) {
    add(RecruitPositionDetailEmployerFragment.newInstance(job));
  }

  /*
   * =============================== 招聘会 ==================================
   */

  /**
   * 求职者参加的招聘会
   */
  public void myUserJobFair() {
    add(new RecruitUserMyJobFairFragment());
  }

  public void myStaffJobFair() {
    add(new RecruitStaffMyJobFairFragment());
  }

  public void toAllJobFair() {
    add(new JobFairsAllFragment());
  }

  public void toStaffJobFairDetail(JobFair jobFair) {
    add(JobfairDetailFragment.newStaffJobFair(jobFair));
  }

  public void toUserJobFairDetail(JobFair jobFair) {
    add(JobfairDetailFragment.newUserJobFair(jobFair));
  }

  /**
   * 报名参加专场招聘会
   */
  public void toSignUpFair(JobFair jobFair) {
    addNoStack(JobfairSignUpFragment.newInstance(jobFair));
  }

  public void toJoinFairSuc() {

  }

  public void toPublishPosition(String gymId) {
    add(new RecruitPublishJobFragmentBuilder(gymId, null,
        RecruitPublishJobFragment.PUBLISH_POSITION).build());
  }

  /**
   * 职位名称填写
   */
  public void toSetRequireName(String name, String hint) {
    add(SetNameCommenFragment.newInstance(name, hint));
  }

  /**
   * 职位描述填写
   */
  public void toEditRecruitDesc(String content, String title, int type) {
    add(new RecruitRichTextEditFragmentBuilder(content, title).type(type).build());
  }

  /**
   * 职位福利填写
   */
  public void toPositionWalfare(ArrayList<String> tags, String title) {
    add(new RecruitWelFareFragmentBuilder(tags, title).build());
  }

  /**
   * 简历详情
   */
  public void toResumeDetail(String resumeId, String url) {
    add(new ResumeDetailFragmentBuilder(resumeId, url).build());
  }

  /**
   * 修改场馆基本信息
   */
  public void editGymInfo(String gymid) {
    add(EditGymInfoFragment.newInstance(gymid));
  }

}
