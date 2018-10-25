package com.qingchengfit.fitcoach.fragment.manage;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.HomeStatement;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.GymUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponsePermission;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ManageViewModel extends BaseViewModel {
  public MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
  public MutableLiveData<QcResponsePermission.Data> permissionData = new MutableLiveData<>();
  public MutableLiveData<CoachService> coachServiceData = new MutableLiveData<>();
  public MutableLiveData<HomeStatement> chartData=new MutableLiveData<>();
  public MutableLiveData<Boolean> hasPrivate =new MutableLiveData<>();
  public MutableLiveData<Boolean> hasGroup =new MutableLiveData<>();
  public final ActionLiveEvent quitAction = new ActionLiveEvent();
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject RepoCoachServiceImpl repoCoachService;

  @Inject public ManageViewModel() {

  }

  public void loadPremission(String id) {
    if (loginStatus.isLogined()) {
      showLoading.setValue(true);
      QcCloudClient.getApi().getApi.qcGetPermission(id, gymWrapper.getParams())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnTerminate(() -> showLoading.setValue(false))
          .subscribe(response -> {
            if (ResponseConstant.checkSuccess(response)) {
              permissionData.setValue(response.data);
            } else {
              ToastUtils.show("权限更新失败");
            }
          }, new NetWorkThrowable());
    }
  }

  @SuppressLint("CheckResult") public void loadCoachService(String coachId) {
    QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            List<CoachService> coachServices = response.data.services;
            if (coachServices != null && coachServices.size() > 0) {
              gymWrapper.setCoachService(coachServices.get(0));
              for (CoachService coachService : coachServices) {
                if (coachService.getId().equals(coachId)) {
                  gymWrapper.setCoachService(coachService);
                }
              }
              coachServiceData.setValue(gymWrapper.getCoachService());
            } else {
              RxBus.getBus().post(new EventLoginChange());
            }
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, new NetWorkThrowable());
  }

  public void quitGym(String coachId) {
    showLoading.setValue(true);
    QcCloudClient.getApi().postApi.qcQuitGym(coachId,
        GymUtils.getParams(gymWrapper.getCoachService()))
        .compose(RxHelper.schedulersTransformer())
        .doOnTerminate(() -> showLoading.setValue(false))
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            ToastUtils.show("退出健身房成功!");
            repoCoachService.deleteServiceByIdModel(gymWrapper.id(), gymWrapper.model());
            quitAction.call();
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, new NetWorkThrowable());
  }

  public void loadGymWelcomeDeta() {
    QcCloudClient.getApi().getApi.qcGetGymWelcome(loginStatus.staff_id(), gymWrapper.id(),
        gymWrapper.model()).compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        chartData.setValue(response.data.stat);
        hasPrivate.setValue(response.data.has_private);
        hasGroup.setValue(response.data.has_team);
      } else {
        ToastUtils.show(response.getMsg());
      }
    }, new NetWorkThrowable());
  }
}
