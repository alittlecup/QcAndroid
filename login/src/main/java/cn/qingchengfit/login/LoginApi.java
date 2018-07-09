package cn.qingchengfit.login;

import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.network.response.QcDataResponse;

import com.google.gson.JsonObject;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

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
 * Created by Paper on 2018/2/6.
 */

public interface LoginApi {

  @POST("/api/user/login/?session_config=true") Observable<QcDataResponse<Login>> qcLogin(
    @Body LoginBody loginBody);

  //注册
  @POST("/api/user/register/?session_config=true") Observable<QcDataResponse<Login>> qcRegister(
    @Body RegisteBody params);

  //获取电话验证码
  @POST("/api/send/verify/") Observable<QcDataResponse> qcGetCode(@Body GetCodeBody account);
  /**
   * 验证验证码
   */
  @POST("api/check/verify/") Observable<QcDataResponse> qcCheckCode(@Body CheckCodeBody body);

  //判断是否同意用户协议
  @GET(" /api/user/check/read_agreement/")
  Observable<QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
    @QueryMap HashMap<String, Object> params);

  /**
   * 微信登录  code换取用户信息
   */
  @POST("/api/user/wechat/code/")
  Observable<QcDataResponse<Login>> wxLogin(@Body JsonObject object);

  /**
   * 检查是否注册过
   * @return "data": {
                      "status": 0,
                    }
  status: 0 -> 该手机号【未注册】过青橙用户
  status: 1 -> 该手机号【已经注册】过青橙用户并且【没有绑定】微信
  status: 2 -> 该用户【已经注册】过青橙用户【已经绑定微信】但是与【当前微信不一致】
   *
   */
  @POST("/api/user/wechat/check/")
  Observable<QcDataResponse<JsonObject>> checkRegiste(@Body CheckCodeBody body);

  /**
   * 解绑微信
   */
  @POST("/api/user/wechat/unbind/")
  Observable<QcDataResponse> unBindWx(@Body CheckCodeBody body);

 /**
   * 解绑微信
   */
  @POST("/api/user/wechat/bind/")
  Observable<QcDataResponse> bindWx(@Body CheckCodeBody body);


}
