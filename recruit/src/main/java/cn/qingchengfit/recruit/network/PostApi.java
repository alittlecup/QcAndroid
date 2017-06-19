package cn.qingchengfit.recruit.network;

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.WorkExp;
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
    @POST("/api/user/job/favorites/") rx.Observable<QcResponse> starJob(@Body HashMap<String, Object> hashMap);

    //取消收藏职位
    @DELETE("/api/user/job/favorites/{job_id}/") rx.Observable<QcResponse> cancelStarJob(@Path("job_id") String jobid);

    //发送简历  body:{"job_id": xx}
    @POST("/api/user/job/deliveries/") rx.Observable<QcResponse> sendResume(@Body HashMap<String, Object> hashMap);

    /**
     * 职位邀请
     *
     * @param hashMap body:{"job_id": xx, "user_id": xx}
     */
    @POST("/api/user/job/invitations/") rx.Observable<QcResponse> inviteSome(@Body HashMap<String, Object> hashMap);

    /**
     * 编辑我的简历
     */
    @PUT("/api/user/resume/") rx.Observable<QcResponse> updateResume(@Body ResumeBody body);

    /**
     * 新增教育经历
     */
    @POST("/api/user/educations/") rx.Observable<QcResponse> addEducation(@Body Education education);

    @PUT("/api/user/educations/{id}/") rx.Observable<QcResponse> updateEducation(@Path("id") String educationid, @Body Education education);

    @DELETE("/api/user/educations/{id}/") rx.Observable<QcResponse> delEducations(@Path("id") String educationid);

    /**
     * 新增工作经历
     */
    @POST("/api/user/job/experiences/") rx.Observable<QcResponse> addWorkExp(@Body WorkExp workExp);

    @PUT("/api/user/job/experiences/{id}/") rx.Observable<QcResponse> updateWorkExp(@Path("id") String experience_id,
        @Body WorkExp workExp);

    @POST("/api/user/job/certificates/") rx.Observable<QcResponse> addCertificate(@Body Certificate certificate);

    ;

    @PUT("/api/user/job/certificates/{id}/") rx.Observable<QcResponse> updateCertificate(@Path("id") String id,
        @Body Certificate certificate);

    ;

    //新增组织
    @POST("/api/organizations/") rx.Observable<QcAddOrganizationResponse> qcAddOrganization(@Body OrganizationBean organizationBean);

}
