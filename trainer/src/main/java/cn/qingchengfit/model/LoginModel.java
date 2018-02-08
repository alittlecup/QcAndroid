package cn.qingchengfit.model;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.BuildConfig;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.login.LoginApi;
import cn.qingchengfit.saasbase.login.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.saasbase.login.bean.Login;
import cn.qingchengfit.saasbase.login.bean.LoginBody;
import cn.qingchengfit.saasbase.login.bean.RegisteBody;
import cn.qingchengfit.saasbase.login.views.CheckProtocolModel;
import com.qingchengfit.fitcoach.App;
import java.util.HashMap;
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
 * Created by Paper on 2018/2/8.
 */

public class LoginModel implements ILoginModel {

  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  LoginApi api;

  public LoginModel(GymWrapper gymWrapper, LoginStatus loginStatus,
    QcRestRepository qcRestRepository) {
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    api = qcRestRepository.createGetApi(LoginApi.class);
  }


  @Override public void setStaffId(String staffid) {
    App.coachid = Integer.parseInt(staffid);
  }

  @Override public boolean isDebug() {
    return BuildConfig.DEBUG;
  }

  @Override public Observable<QcDataResponse<Login>> doLogin(LoginBody body) {
    return api.qcLogin(body);
  }

  @Override public Observable<QcDataResponse<Login>> doRegiste(RegisteBody body) {
    return api.qcRegister(body);
  }

  @Override public Observable<QcDataResponse> doWxtoken(LoginBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> getCode(GetCodeBody body) {
    return api.qcGetCode(body);
  }

  @Override public Observable<QcDataResponse> checkCode(CheckCodeBody body) {
    return api.qcCheckCode(body);
  }

  @Override public Observable<QcDataResponse> checkPhoneBind(CheckCodeBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
    HashMap<String, Object> params) {
    return api.qcCheckProtocol(params);
  }
}
