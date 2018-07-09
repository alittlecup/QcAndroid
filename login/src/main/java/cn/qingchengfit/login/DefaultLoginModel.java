package cn.qingchengfit.login;

import android.content.Context;
import android.net.Uri;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.login.LoginApi;
import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.login.views.LoginView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;

import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.EventFreshCoachService;
import com.google.gson.JsonObject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.List;
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

public class DefaultLoginModel implements ILoginModel {

  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  LoginApi api;
  //QcDbManager qcDbManager;
  //GymConfigApi gymConfigApi;

  public DefaultLoginModel(GymWrapper gymWrapper, LoginStatus loginStatus,
    QcRestRepository qcRestRepository) {
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    api = qcRestRepository.createGetApi(LoginApi.class);

  }


  @Override public void setStaffId(String staffid) {
    //为了兼容旧版app，staffid没注入的情况
    //App.staffId = staffid;
  }

  @Override public boolean isDebug() {
    return true;
  }

  @Override public void doOnLogin(Context ctx, Login login,LoginView mvpView) {
    //登录成功后一些初始化设置
    mvpView.onSuccess(1);
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
