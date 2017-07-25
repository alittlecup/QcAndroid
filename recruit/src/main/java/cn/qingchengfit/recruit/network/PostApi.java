package cn.qingchengfit.recruit.network;

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.body.EditPermissionBody;
import cn.qingchengfit.recruit.network.body.InviteBody;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.network.body.MarkResumeBody;
import cn.qingchengfit.recruit.network.body.RecruitGymBody;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import cn.qingchengfit.recruit.views.organization.OrganizationBean;
import cn.qingchengfit.recruit.views.organization.QcAddOrganizationResponse;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

public interface PostApi {

  //收藏职位
  @POST("/api/user/job/favorites/") rx.Observable<QcResponse> starJob(
      @Body HashMap<String, Object> hashMap);

  //取消收藏职位
  @DELETE("/api/user/job/favorites/{job_id}/") rx.Observable<QcResponse> cancelStarJob(
      @Path("job_id") String jobid);

  //发送简历  body:{"job_id": xx}
  @POST("/api/user/job/deliveries/") rx.Observable<QcResponse> sendResume(
      @Body HashMap<String, Object> hashMap);

  /**
   * 职位邀请
   *
   * @param hashMap body:{"job_id": xx, "user_id": xx}
   */
  @POST("/api/user/job/invitations/") rx.Observable<QcResponse> inviteSome(
      @Body HashMap<String, Object> hashMap);

  /**
   * 编辑职位 关闭{"published": false}, 打开{"published": true}
   * body:{"name": xx, "description": xx,
   * "requirement": xx, "min_work_year": xx,
   * "max_work_year":xx, "min_age": xx, "max_age": xx,
   * "min_salary": xx, "max_salary": xx, "gender": xx,
   * "min_height": xx, "max_height": xx, "min_weight": xx,
   * "max_weight": xx, "education": xx,
   * "welfare": ["xx", "xx"]
   * }
   */
  @PUT("/api/staff/jobs/{job_id}/") rx.Observable<QcResponse> editPosition(
      @Path("job_id") String jobid, @Body JobBody jobBody);

  /**
   * 标记投递简历
   */
  @PUT("/api/staff/job/{tp}/resumes/") rx.Observable<QcResponse> markResume(@Path("tp") String tp,
      @Body MarkResumeBody body);

  @PUT("/api/staff/job/gyms/{gym_id}/") rx.Observable<QcResponse> editGymIntro(
      @Path("gym_id") String gymId, @Body RecruitGymBody body);

  @POST("/api/staff/job/permissions/") rx.Observable<QcResponse> editpermsiion(
      @Body EditPermissionBody body);





  /*
   *-===========================简历相关 ===================
   */

  /**
   * "gym_id": "xxx",
   * "fair_id": "xxx"
   */
  @POST("/api/staff/job/fair/orders/") rx.Observable<QcResponse> joinFair(
      @Body HashMap<String, Object> body);


  /*
   *-===========================简历相关 ===================
   */

  /**
   * 编辑我的简历
   */
  @PUT("/api/user/resume/") rx.Observable<QcResponse> updateResume(@Body ResumeBody body);

  /**
   * 新增教育经历
   */
  @POST("/api/user/educations/") rx.Observable<QcResponse> addEducation(@Body Education education);

  @PUT("/api/user/educations/{id}/") rx.Observable<QcResponse> updateEducation(
      @Path("id") String educationid, @Body Education education);

  @DELETE("/api/user/educations/{id}/") rx.Observable<QcResponse> delEducations(
      @Path("id") String educationid);

  /**
   * 新增工作经历
   */
  @POST("/api/user/job/experiences/") rx.Observable<QcResponse> addWorkExp(@Body WorkExp workExp);

  @PUT("/api/user/job/experiences/{id}/") rx.Observable<QcResponse> updateWorkExp(
      @Path("id") String experience_id, @Body WorkExp workExp);

  @DELETE("/api/user/job/experiences/{id}/") rx.Observable<QcResponse> delWorkExp(
      @Path("id") String experience_id);

  @POST("/api/user/job/certificates/") rx.Observable<QcResponse> addCertificate(
      @Body Certificate certificate);

  ;

  @PUT("/api/user/job/certificates/{id}/") rx.Observable<QcResponse> updateCertificate(
      @Path("id") String id, @Body Certificate certificate);

  ;

  //新增组织
  @POST("/api/organizations/") rx.Observable<QcAddOrganizationResponse> qcAddOrganization(
      @Body OrganizationBean organizationBean);

  /**
   * 发布职位
   */
  @POST("/api/staff/jobs/") rx.Observable<QcResponse> qcPublishPosition(@Body JobBody jobBody);

  @PUT("/api/staff/jobs/{job_id}/") rx.Observable<QcResponse> qcEditPosition(
      @Path("job_id") String id,
      @Body JobBody jobBody);

  /**
   * 发送邀约
   */
  @POST("api/staff/job/permission/invite/") rx.Observable<QcResponse> qcInvitePosition(
      @Body InviteBody inviteBody);

  /**
   * 收藏简历
   */
  @POST("api/staff/job/resumes/favorites/") rx.Observable<QcResponse> favoriteResume(
      @Body HashMap<String, Object> params);

  /**
   * 取消收藏
   */
  @DELETE("api/staff/job/resumes/favorites/{resume_id}/")
  rx.Observable<QcResponse> cancelStarResume(@Path("resume_id") String resumeId);
}
