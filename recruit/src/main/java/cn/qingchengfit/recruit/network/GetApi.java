package cn.qingchengfit.recruit.network;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.EndFairTopsWrap;
import cn.qingchengfit.recruit.network.response.CertificateListWrap;
import cn.qingchengfit.recruit.network.response.ChatGymWrap;
import cn.qingchengfit.recruit.network.response.EduExpListWrap;
import cn.qingchengfit.recruit.network.response.GymListWrap;
import cn.qingchengfit.recruit.network.response.JobDetailWrap;
import cn.qingchengfit.recruit.network.response.JobFairOrderlistWrap;
import cn.qingchengfit.recruit.network.response.JobFairWrap;
import cn.qingchengfit.recruit.network.response.JobFariListWrap;
import cn.qingchengfit.recruit.network.response.JobIndexWrap;
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.recruit.network.response.OnePermissionWrap;
import cn.qingchengfit.recruit.network.response.PermisionnListWrap;
import cn.qingchengfit.recruit.network.response.PermissionUserWrap;
import cn.qingchengfit.recruit.network.response.ResumeHomeWrap;
import cn.qingchengfit.recruit.network.response.ResumeIntentsWrap;
import cn.qingchengfit.recruit.network.response.ResumeListWrap;
import cn.qingchengfit.recruit.network.response.WorkExpListWrap;
import cn.qingchengfit.recruit.network.response.WorkExpWrap;
import cn.qingchengfit.recruit.views.organization.QcSearchOrganResponse;
import cn.qingchengfit.recruit.views.organization.QcSerachGymRepsonse;
import cn.qingchengfit.saas.response.GymWrap;
import java.util.HashMap;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
 * Created by Paper on 2017/5/23.
 */

public interface GetApi {

  @GET("/api/user/jobs/") rx.Observable<QcDataResponse<JobListWrap>> queryJobList(
      @Query("page") int page, @QueryMap HashMap<String, Object> hashMap);

  @GET("/api/user/job/index/") rx.Observable<QcDataResponse<JobIndexWrap>> queryJobIndex(
      @QueryMap HashMap<String, Object> hashMap);

  //求职招聘主页
  @GET("/api/user/job/list/index/") rx.Observable<QcDataResponse<JobListIndex>> queryJobsIndex();

  //职位详情
  @GET("/api/user/jobs/{job_id}/") rx.Observable<QcDataResponse<JobDetailWrap>> queryJobDetail(
      @Path("job_id") String jobid);  //职位详情

  @GET("/api/staff/jobs/{job_id}/")
  rx.Observable<QcDataResponse<JobDetailWrap>> querystaffJobDetail(@Path("job_id") String jobid);

  //我的收藏
  @GET("/api/user/job/favorites/") rx.Observable<QcDataResponse<JobListWrap>> queryMyStared(
      @Query("page") int page);

  //我的投递
  @GET("/api/user/job/deliveries/") rx.Observable<QcDataResponse<JobListWrap>> queryMySent(
      @Query("page") int page);

  //我被邀请的
  @GET("/api/user/job/invitations/") rx.Observable<QcDataResponse<JobListWrap>> queryMyInvited(
      @Query("page") int page);

  /**
   * 公司详情
   */
  @GET("/api/job/gyms/{gym_id}/") rx.Observable<QcDataResponse<GymWrap>> queryGymInfo(
      @Path("gym_id") String gym_id);

  @GET("/api/staff/job/gyms/{gym_id}/") rx.Observable<QcDataResponse<GymWrap>> queryStaffGymInfo(
      @Path("gym_id") String gym_id);

  /**
   * 某公司所有招聘职位
   */
  @GET("/api/user/gyms/{gym_id}/jobs/") rx.Observable<QcDataResponse<JobListWrap>> queryGymJobs(
      @Path("gym_id") String gym_id, @Query("page") int page);

  /**
   * 某公司所有招聘职位 是否发布（关闭）
   */
  @GET("/api/user/gyms/{gym_id}/jobs/?show_all=1")
  rx.Observable<QcDataResponse<JobListWrap>> queryGymJobsAll(@Path("gym_id") String gym_id,
      @Query("page") int page);

  @GET("/api/staff/jobs/?show_all=1")
  rx.Observable<QcDataResponse<JobListWrap>> queryStaffGymJobsAll(@Query("gym_id") String gym_id);

  /**
   * 收到的简历(主动投递的)
   */
  @GET("/api/staff/job/delivery/resumes/")
  rx.Observable<QcDataResponse<ResumeListWrap>> queryRecieveResume(
      @QueryMap HashMap<String, Object> params);

  /**
   * (我邀约的)
   */
  @GET("/api/staff/job/invite/resumes/")
  rx.Observable<QcDataResponse<ResumeListWrap>> queryInvitedResume(
      @QueryMap HashMap<String, Object> params);

  /**
   * 收藏的简历
   */
  @GET("/api/staff/job/resumes/favorites/")
  rx.Observable<QcDataResponse<ResumeListWrap>> queryStarredResume(
      @QueryMap HashMap<String, Object> params);

  /**
   * 简历市场
   */
  @GET("/api/staff/user/resumes/") rx.Observable<QcDataResponse<ResumeListWrap>> queryResumeMarkets(
      @QueryMap HashMap<String, Object> params);

  /**
   * 我参加的专场招聘会（招聘版）
   */
  @GET("/api/staff/user/resumes/index/?show_all=1")
  rx.Observable<QcDataResponse<JobFariListWrap>> queryMyJobFairs();

  @GET("/api/staff/job/fair/orders/?show_all=1")
  rx.Observable<QcDataResponse<JobFairOrderlistWrap>> queryStaffJobFairs(
      @Query("gym_id") String gymid);

  /**
   * 我参加的专场招聘会(求职版)
   */
  @GET("/api/user/job/fairs/?show_all=1")
  rx.Observable<QcDataResponse<JobFariListWrap>> queryUserMyJobFairs();

  /**
   * 管理场馆列表
   */
  @GET("/api/staff/job/gyms/") rx.Observable<QcDataResponse<GymListWrap>> queryManageGyms();

  /**
   * 查看场馆信息
   */
  @GET("/api/staff/job/gyms/{gym_id}") rx.Observable<QcDataResponse<GymWrap>> queryGymsDetail(
      @Path("gym_id") String gymId);

  /**
   * 专场招聘会列表
   */
  @GET("/api/staff/job/fairs/?show_all=1")
  rx.Observable<QcDataResponse<JobFariListWrap>> queryJobFairs(
      @QueryMap HashMap<String, Object> params);

  /*
   *=====================================权限部分===========================================
   */

  /**
   * 获取权限列表 可以根据 gym_id 和 key 删选
   */
  @GET("/api/staff/job/permissions/")
  rx.Observable<QcDataResponse<PermisionnListWrap>> queryRecruitPermission(
      @QueryMap HashMap<String, Object> hashMap);

  /**
   * 相应权限面对的用户
   */
  @GET("/api/staff/job/permission/users/")
  rx.Observable<QcDataResponse<PermissionUserWrap>> queryPermissionUser(
      @Query("gym_id") String gymid);

  @GET("/api/common/staffs/") rx.Observable<QcDataResponse<ChatGymWrap>> queryCommonStaffs(
      @Query("gym_id") String gymid);

  @GET("/api/staff/job/check/permission/")
  rx.Observable<QcDataResponse<OnePermissionWrap>> queryOnepermission(@Query("gym_id") String gymid,
      @Query("key") String key);

    /*
     *   =============================== 招聘会  ============================================
     */

  /**
   * 学员将staff 改成user 返回结果暂时一致
   */
  @GET("/api/staff/job/fairs/{fair_id}/")
  rx.Observable<QcDataResponse<JobFairWrap>> queryStaffJobFairDetail(
      @Path("fair_id") String fair_id);

  /**
   * 专场招聘会中的职位列表
   */
  @GET("/api/user/job/fairs/{fair_id}/jobs/")
  rx.Observable<QcDataResponse<JobListWrap>> queryJobFairJobs(@Path("fair_id") String fair_id,
      @QueryMap HashMap<String, Object> params);

  /*
   * 专场招聘会的简历列表
   */
  @GET("/api/staff/job/fairs/{fair_id}/resumes/")
  rx.Observable<QcDataResponse<ResumeListWrap>> queryJobFairResumes(@Path("fair_id") String fair_id,
      @QueryMap HashMap<String, Object> params);


    /*
     *   =============================== 简历部分============================================
     */

  /**
   * 我的简历详情
   */
  @GET("/api/user/resume/") rx.Observable<QcDataResponse<ResumeHomeWrap>> queryMyResumeHome();

  /**
   * 获取我的期望值
   */
  @GET("/api/user/resume/exp/")
  rx.Observable<QcDataResponse<ResumeIntentsWrap>> queryMyResumeIntents();

  /**
   * 教育经历列表
   * /api/user/educations/
   */
  @GET("/api/user/educations/") rx.Observable<QcDataResponse<EduExpListWrap>> queryEducations();

  /**
   * 工作经历
   */
  @GET("/api/user/job/experiences/") rx.Observable<QcDataResponse<WorkExpListWrap>> queryWorkExps();

  @GET("/api/user/job/experiences/{id}/") rx.Observable<QcDataResponse<WorkExpWrap>> queryWorkExp(
      @Path("id") String id);

  @GET("/api/user/job/certificates/")
  rx.Observable<QcDataResponse<CertificateListWrap>> queryCertifications();

  /**
   * 查看他人简历
   */
  @GET("api/staff/user/resumes/{resume_id}/")
  rx.Observable<QcDataResponse<ResumeHomeWrap>> qcGetOtherResumeDetail(
      @Path("resume_id") String resumeId, @QueryMap Map<String, Object> params);

  //搜索健身房
  @GET("/api/gym/search/") rx.Observable<QcSerachGymRepsonse> qcSearchGym(
      @QueryMap Map<String, String> params);        //搜索健身房

  //搜索热门健身房
  @GET("/api/gym/") rx.Observable<QcSerachGymRepsonse> qcHotGym(
      @QueryMap Map<String, String> params);

  //搜索机构
  @GET("/api/organizations/search/") rx.Observable<QcSearchOrganResponse> qcSearchOrganization(
      @QueryMap Map<String, String> params);

  //热门机构
  @GET("/api/organizations/") rx.Observable<QcSearchOrganResponse> qcHotOrganization(
      @QueryMap Map<String, String> params);

  /**
   * 获取可邀约的职位列表
   */
  @GET("/api/staff/job/permission/invite/")
  rx.Observable<QcDataResponse<JobListWrap>> qcGetInviteJobs();

  /**
   * 获取已结束未处理的招聘会
   */
  @GET("api/staff/job/check/fair/")
  rx.Observable<QcDataResponse<EndFairTopsWrap>> qcGetEndFair();

}
