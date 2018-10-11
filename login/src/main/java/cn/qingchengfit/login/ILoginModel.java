package cn.qingchengfit.login;

import android.content.Context;
import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.login.views.LoginView;
import cn.qingchengfit.network.response.QcDataResponse;

import com.google.gson.JsonObject;
import java.util.HashMap;
import retrofit2.http.GET;
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
 * Created by Paper on 2018/2/5.
 */

public interface ILoginModel {
  /**
   * 这个方法纯粹是为了保证旧的功能可用
   */
  void setStaffId(String staffid);
  boolean isDebug();
  void doOnLogin(Context ctx,Login login,LoginView mvpView);

  rx.Observable<QcDataResponse<Login>> doLogin(LoginBody body);
  rx.Observable<QcDataResponse<Login>> doRegiste(RegisteBody body);
  rx.Observable<QcDataResponse<Login>> wxLogin(JsonObject body);
  rx.Observable<QcDataResponse> unBindWx(CheckCodeBody body);
  rx.Observable<QcDataResponse> bindWx(CheckCodeBody body);
  rx.Observable<QcDataResponse> getCode(GetCodeBody body);
  rx.Observable<QcDataResponse> checkCode(CheckCodeBody body);
  rx.Observable<QcDataResponse<JsonObject>> checkPhoneBind(CheckCodeBody body);

  //判断是否同意用户协议
  @GET(" /api/user/check/read_agreement/")
  rx.Observable<QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
    @QueryMap HashMap<String, Object> params);
}
