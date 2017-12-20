package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.utils.LogUtil;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@Leaf(module = "gymconfig", path = "/notisetting/")
public class NotiSettingChannelPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  private MVPView view;
  private boolean hasWXbinded = false;
  private int toStudentMethod = 0;
  private int toStaffMethod = 0;
  private Integer notiStaffId, notiUserId;

  @Inject public NotiSettingChannelPresenter() {
  }

  public int getRealSendMethodType(int x) {
    if (hasWXbinded) {
      return x;
    } else {
      return 1;
    }
  }

  public int getToStudentMethod() {
    return toStudentMethod;
  }

  public void setToStudentMethod(int toStudentMethod) {
    this.toStudentMethod = toStudentMethod;
  }

  public int getToStaffMethod() {
    return toStaffMethod;
  }

  public void setToStaffMethod(int toStaffMethod) {
    this.toStaffMethod = toStaffMethod;
  }

  public boolean isHasWXbinded() {
    return hasWXbinded;
  }

  public void setHasWXbinded(boolean hasWXbinded) {
    this.hasWXbinded = hasWXbinded;
  }

  public void postSendStudentNoti(final int x) {
    ShopConfigBody.Config cfg = new ShopConfigBody.Config(notiUserId + "", x + "");
    List<ShopConfigBody.Config> l = new ArrayList<>();
    l.add(cfg);
    RxRegiste(qcRestRepository.createPostApi(Post_Api.class)
        .qcShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), new ShopConfigBody(l))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              LogUtil.d("修改发送会员通知方法成功：" + x);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void postSendStaffNoti(final int x) {
    ShopConfigBody.Config cfg = new ShopConfigBody.Config(notiStaffId + "", x + "");
    List<ShopConfigBody.Config> l = new ArrayList<>();
    l.add(cfg);
    RxRegiste(qcRestRepository.createPostApi(Post_Api.class)
        .qcShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), new ShopConfigBody(l))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              LogUtil.d("修改发送工作人员通知方法成功：" + x);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryCurSMSleft() {
    RxRegiste(qcRestRepository.createGetApi(Get_Api.class)
        .qcGetShopConfig(loginStatus.staff_id(), ShopConfigs.SMS_NUM, gymWrapper.getParams())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
          @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.configs != null && qcResponse.data.configs.size() > 0) {
                view.onSMSleft(qcResponse.data.configs.get(0).getValueInt());
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void querySendChannel() {
    RxRegiste(qcRestRepository.createGetApi(Get_Api.class)
        .qcGetShopConfig(loginStatus.staff_id(),
            ShopConfigs.NOTI_TO_STAFF + "," + ShopConfigs.NOTIFY_TO_MEMBER_METHOD,
            gymWrapper.getParams())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
          @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.configs != null && qcResponse.data.configs.size() > 0) {
                for (SignInConfig.Config config : qcResponse.data.configs) {
                  if (config.getKey().equalsIgnoreCase(ShopConfigs.NOTI_TO_STAFF)) {
                    view.sendToStaffNoti(config.getValueInt() - 1);//都断对应为 1234、展示层为0123
                    notiStaffId = config.getId();
                  } else if (config.getKey()
                      .equalsIgnoreCase(ShopConfigs.NOTIFY_TO_MEMBER_METHOD)) {
                    view.sendToStudentNoti(config.getValueInt() - 1);
                    notiUserId = config.getId();
                  }
                }
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }


  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onSMSleft(int count);

    void onWeChatleft(boolean authored);

    void sendToStudentNoti(int x);

    void sendToStaffNoti(int x);
  }
}
