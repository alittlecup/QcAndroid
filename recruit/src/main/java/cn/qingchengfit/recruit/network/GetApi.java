package cn.qingchengfit.recruit.network;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.network.response.CertificateListWrap;
import cn.qingchengfit.recruit.network.response.EduExpListWrap;
import cn.qingchengfit.recruit.network.response.GymWrap;
import cn.qingchengfit.recruit.network.response.JobDetailWrap;
import cn.qingchengfit.recruit.network.response.JobIndexWrap;
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.recruit.network.response.ResumeHomeWrap;
import cn.qingchengfit.recruit.network.response.WorkExpListWrap;
import cn.qingchengfit.recruit.views.organization.QcSearchOrganResponse;
import cn.qingchengfit.recruit.views.organization.QcSerachGymRepsonse;
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

    @GET("/api/user/jobs/") rx.Observable<QcDataResponse<JobListWrap>> queryJobList(@Query("page") int page);


    @GET("/api/user/job/index/") rx.Observable<QcDataResponse<JobIndexWrap>> queryJobIndex(@QueryMap HashMap<String, Object> hashMap);

    //求职招聘主页
    @GET("/api/user/job/list/index/") rx.Observable<QcDataResponse<JobListIndex>> queryJobsIndex();

    //职位详情
    @GET("/api/user/jobs/{job_id}/") rx.Observable<QcDataResponse<JobDetailWrap>> queryJobDetail(@Path("job_id") String jobid);

    //我的收藏
    @GET("/api/user/job/favorites/") rx.Observable<QcDataResponse<JobListWrap>> queryMyStared(@Query("page") int page);

    //我的投递
    @GET("/api/user/job/deliveries/") rx.Observable<QcDataResponse<JobListWrap>> queryMySent(@Query("page") int page);

    //我被邀请的
    @GET("/api/user/job/invitations/") rx.Observable<QcDataResponse<JobListWrap>> queryMyInvited(@Query("page") int page);

    /**
     * 公司详情
     */
    @GET("/api/job/gyms/{gym_id}/") rx.Observable<QcDataResponse<GymWrap>> queryGymInfo(@Path("gym_id") String gym_id);

    /**
     * 某公司所有招聘职位
     */
    @GET("/api/user/gyms/{gym_id}/jobs/") rx.Observable<QcDataResponse<JobListWrap>> queryGymJobs(@Path("gym_id") String gym_id,
        @Query("page") int page);




    /*
     *   ===============================简历部分============================================
     */

    /**
     * 我的简历详情
     */
    @GET("/api/user/resume/") rx.Observable<QcDataResponse<ResumeHomeWrap>> queryMyResumeHome();

    /**
     * 教育经历列表
     * /api/user/educations/
     */
    @GET("/api/user/educations/") rx.Observable<QcDataResponse<EduExpListWrap>> queryEducations();

    /**
     * 工作经历
     */
    @GET("/api/user/job/experiences/") rx.Observable<QcDataResponse<WorkExpListWrap>> queryWorkExps();

    @GET("/api/user/job/certificates/") rx.Observable<QcDataResponse<CertificateListWrap>> queryCertifications();

    //搜索健身房
    @GET("/api/gym/search/") rx.Observable<QcSerachGymRepsonse> qcSearchGym(@QueryMap Map<String, String> params);        //搜索健身房

    //搜索热门健身房
    @GET("/api/gym/") rx.Observable<QcSerachGymRepsonse> qcHotGym(@QueryMap Map<String, String> params);

    //搜索机构
    @GET("/api/organizations/search/") rx.Observable<QcSearchOrganResponse> qcSearchOrganization(@QueryMap Map<String, String> params);

    //热门机构
    @GET("/api/organizations/") rx.Observable<QcSearchOrganResponse> qcHotOrganization(@QueryMap Map<String, String> params);

}
