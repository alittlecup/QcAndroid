package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.signin.bean.SignChooseWeekEvent;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeFrameBean;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimePayOnceEvent;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeUploadBean;
import cn.qingchengfit.staffkit.views.signin.bean.SignTimeFrameBeanWrapper;
import cn.qingchengfit.utils.ToastUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class SignInTimeSettingVM extends BaseViewModel {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StaffRespository staffRespository;
  public MutableLiveData<Boolean> delResult = new MutableLiveData<>();
  public MutableLiveData<SignChooseWeekEvent> civChooseWeek = new MutableLiveData<>();
  public MutableLiveData<SignInTimePayOnceEvent> civPayOnceEvent = new MutableLiveData<>();
  public MutableLiveData<SignInTimeFrameBean> uploadResult = new MutableLiveData<>();
  public MutableLiveData<List<SignInCardCostBean.CardCost>> cardCosts = new MutableLiveData<>();

  @Inject public SignInTimeSettingVM() {
    autoClear(RxBus.getBus()
        .register(SignChooseWeekEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          civChooseWeek.setValue(event);
        }));
    autoClear(RxBus.getBus()
        .register(SignInTimePayOnceEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          civPayOnceEvent.setValue(event);
        }));
  }

  public void delTimeFrame(String id) {
    staffRespository.getStaffAllApi()
        .qcDelCheckInTimeFrame(loginStatus.staff_id(), id, gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            delResult.setValue(true);
          } else {
            delResult.setValue(false);
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
          ToastUtils.show("网络连接异常");
        });
  }

  public void postTimeFrame(SignInTimeUploadBean bean,String id) {
    if (checkUploadBean(bean)) {
      Observable<QcDataResponse<SignTimeFrameBeanWrapper>> qcUploadObserver;
      if (TextUtils.isEmpty(id)) {
        qcUploadObserver = staffRespository.getStaffAllApi()
            .qcPostCheckInTimeFrame(loginStatus.staff_id(), bean, gymWrapper.getParams());
      } else {
        qcUploadObserver = staffRespository.getStaffAllApi()
            .qcPutCheckInTimeFrame(loginStatus.staff_id(), id, bean, gymWrapper.getParams());
      }
      qcUploadObserver.compose(RxHelper.schedulersTransformer()).subscribe(response -> {
        if (ResponseConstant.checkSuccess(response)) {
          uploadResult.setValue(response.getData().timeFrameBean);
        } else {
          uploadResult.setValue(null);
          ToastUtils.show(response.getMsg());
        }
      }, throwable -> {
        ToastUtils.show("网络连接异常");
      });
    }
  }

  public void loadCardCosts() {
    staffRespository.getStaffAllApi()
        .qcGetSignInCostConfig(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            cardCosts.setValue(response.getData().card_costs);
          } else {
            cardCosts.setValue(null);
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
          ToastUtils.show("网络连接异常");
        });
  }

  private boolean checkUploadBean(SignInTimeUploadBean bean) {
    return true;
  }
}
