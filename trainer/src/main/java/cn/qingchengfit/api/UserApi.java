package cn.qingchengfit.api;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.user.bean.EditUserBody;
import cn.qingchengfit.saasbase.user.bean.FixPhoneBody;
import cn.qingchengfit.saasbase.user.bean.ModifyPwBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
 * Created by Paper on 2018/2/8.
 */

public interface UserApi {
  //工作人员详情
  //@GET("/api/staffs/{id}/") rx.Observable<QcDataResponse<UserWrap>> qcGetSelfInfo(@Path("id") String id);
  @GET("/api/coaches/{id}/") rx.Observable<QcDataResponse<UserWrap>> qcGetCoach(@Path("id") String id);


  //修改教练信息
  @PUT("/api/coaches/{id}/") rx.Observable<QcDataResponse> qcModifyCoach(@Path("id") String id, @Body
    EditUserBody coachInfo);

  //修改密码
  @POST("/api/coaches/{id}/change/password/") rx.Observable<QcDataResponse> qcMoidfyPw(@Path("id") String id, @Body
    ModifyPwBody modifyPwBean);

  //修改电话号码
  @POST("/api/coaches/{id}/change/phone/") rx.Observable<QcDataResponse> qcModifyPhoneNum(@Path("id") String id,
    @Body FixPhoneBody fixPhoneBody);

}