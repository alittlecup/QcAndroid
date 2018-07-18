package cn.qingchengfit.saasbase.user.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.login.ILoginModel;
import  cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saasbase.user.bean.EditUserBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserEditPresenter extends BasePresenter<UserEditPresenter.MVPView> {

  @Inject IWXAPI wxapi;
  @Inject IUserModel userModel;
  @Inject ILoginModel loginModel;
  User user;

  @Inject public UserEditPresenter() {
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void getCurUser() {
    RxRegiste(userModel.getCurUser()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<UserWrap>>() {
        @Override public void onNext(QcDataResponse<UserWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            setUser(qcResponse.getData().user);
            mvpView.onUserInfo(qcResponse.getData().user);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 修改用户信息
   */
  public void editUser(EditUserBody body) {
    RxRegiste(userModel.editUser(user.id, body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {

          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void bindWxtoPhone(String code) {
    RxRegiste(loginModel.bindWx(CheckCodeBody.newBuilder()
      .phone(user.getPhone()).area_code(user.area_code).code(code)
      .build())
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            getCurUser();
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 绑定微信
   */
  public void bindWx() {
    if (!wxapi.isWXAppInstalled()) {
      mvpView.showAlert("您还未安装微信客户端");
      return;
    }
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = AppUtils.getCurAppName(mvpContext);
    wxapi.sendReq(req);
  }

  /**
   * 解绑微信
   */
  public void unBind() {
    RxRegiste(loginModel.unBindWx(
      new CheckCodeBody.Builder().phone(user.phone).area_code(user.area_code).build())
      .onBackpressureDrop()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            getCurUser();
            mvpView.onShowError("解绑成功");
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public interface MVPView extends CView {
    void onUserInfo(User user);
  }
}
