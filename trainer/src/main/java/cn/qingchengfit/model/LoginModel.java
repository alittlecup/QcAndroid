package cn.qingchengfit.model;

import android.content.Context;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.login.LoginApi;
import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.login.views.LoginView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.BuildConfig;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

  @Override public void doOnLogin(Context context, Login login, LoginView loginView) {
    User user=new User(login.user.username,login.user.phone,login.user.avatar,login.user.area_code,login.user.gender);
    user.setId(login.user.id);
    App.gUser = user;
    PreferenceUtils.setPrefString(context, "coach",
        new Gson().toJson(login.coach));
    App.coachid = Integer.parseInt(login.coach.id);
    PreferenceUtils.setPrefBoolean(context, "first", false);
    PreferenceUtils.setPrefString(context,
        login.coach.id + "hostarray", "");

    loginStatus.setUserId(login.user.getId());
    loginStatus.setSession(login.session_id);
    loginStatus.setLoginUser(new Staff(App.gUser, App.coachid + ""));
    loginView.onSuccess(1);
  }

  @Override public Observable<QcDataResponse<Login>> doLogin(LoginBody body) {
    return api.qcLogin(body);
  }

  @Override public Observable<QcDataResponse<Login>> doRegiste(RegisteBody body) {
    return api.qcRegister(body);
  }

  @Override public Observable<QcDataResponse<Login>> wxLogin(JsonObject body) {
    return api.wxLogin(body);
  }

  @Override public Observable<QcDataResponse> unBindWx(CheckCodeBody body) {
    return api.unBindWx(body);
  }

  @Override public Observable<QcDataResponse> bindWx(CheckCodeBody body) {
    return api.bindWx(body);
  }

  @Override public Observable<QcDataResponse> getCode(GetCodeBody body) {
    return api.qcGetCode(body);
  }

  @Override public Observable<QcDataResponse> checkCode(CheckCodeBody body) {
    return api.qcCheckCode(body);
  }

  @Override public Observable<QcDataResponse<JsonObject>> checkPhoneBind(CheckCodeBody body) {
    return api.checkRegiste(body);
  }

  @Override public Observable<QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
    HashMap<String, Object> params) {
    return api.qcCheckProtocol(params);
  }
}
