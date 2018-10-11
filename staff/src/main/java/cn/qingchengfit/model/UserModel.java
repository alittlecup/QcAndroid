package cn.qingchengfit.model;

import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.saasbase.apis.UserApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saasbase.user.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.user.bean.EditUserBody;
import cn.qingchengfit.saasbase.user.bean.FixPhoneBody;
import cn.qingchengfit.saasbase.user.bean.GetCodeBody;
import cn.qingchengfit.saasbase.user.bean.ModifyPwBody;
import com.google.gson.JsonObject;
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
 * Created by Paper on 2018/2/7.
 */

public class UserModel implements IUserModel {
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  UserApi api;

  public UserModel(GymWrapper gymWrapper, LoginStatus loginStatus,
    QcRestRepository qcRestRepository) {
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    api = qcRestRepository.createGetApi(UserApi.class);
  }

  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return api.qcGetSelfInfo(loginStatus.staff_id());
  }

  @Override public Observable<QcDataResponse> editUser(String id, EditUserBody user) {
    return api.editSelfInfo(loginStatus.staff_id(), user);
  }

  @Override public Observable<QcDataResponse> newPw(ModifyPwBody newPw) {
    return api.qcMoidfyPw(loginStatus.staff_id(), newPw);
  }

  @Override public Observable<QcDataResponse> newPhone(FixPhoneBody phone) {
    return api.qcModifyPhoneNum(loginStatus.staff_id(), phone);
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


  @Override public Observable<QcDataResponse> unBindWx(CheckCodeBody body) {
    return api.unBindWx(body);
  }
}
