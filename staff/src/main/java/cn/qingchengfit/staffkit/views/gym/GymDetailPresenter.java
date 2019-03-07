package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import cn.qingchengfit.bean.GymSettingInfo;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/1 2016.
 */
public class GymDetailPresenter extends BasePresenter {
  GymDetailView gymDetailView;
  GymUseCase gymUseCase;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject QcDbManager qcDbManager;
  @Inject SerPermisAction serPermisAction;
  private Subscription sp;
  private Subscription spWelcome;
  private StaffRespository restRepository;
  private Subscription spUnreadCount;

  @Inject public GymDetailPresenter(GymUseCase gymUseCase, StaffRespository restRepository) {
    this.gymUseCase = gymUseCase;
    this.restRepository = restRepository;
  }

  @Override public void onStart() {

  }

  @Override public void onStop() {

  }

  @Override public void onPause() {

  }

  @Override public void attachView(PView v) {
    gymDetailView = (GymDetailView) v;
    RxRegiste(qcDbManager.queryAllFunctions()
        .onBackpressureBuffer()
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .subscribe(strings -> gymDetailView.onModule(strings),
            throwable -> Timber.d(throwable.getMessage())));
  }

  @Override public void attachIncomingIntent(Intent intent) {

  }

  @Override public void onCreate() {

  }

  @Override public void unattachView() {
    super.unattachView();
    gymDetailView = null;
    if (sp != null) sp.unsubscribe();
    if (spWelcome != null) spWelcome.unsubscribe();
    if (spUnreadCount != null && spUnreadCount.isUnsubscribed()) {
      spUnreadCount.unsubscribe();
    }
  }

  void manageBrand() {
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetBrands(loginStatus.staff_id())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
          @Override public void call(QcDataResponse<BrandsResponse> brandsResponse) {
            if (brandsResponse.getData().brands != null) {
              for (Brand brand : brandsResponse.getData().brands) {
                if (brand.id.equals(gymWrapper.brand_id())) {
                  gymWrapper.setBrand(brand);
                  gymDetailView.onManageBrand();
                }
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  void updatePermission() {

    RxRegiste(restRepository.getStaffAllApi()
        .qcPermission(loginStatus.staff_id(), gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcResponsePermission>() {
          @Override public void onNext(QcResponsePermission qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse) && qcResponse.data.permissions != null) {
              serPermisAction.writePermiss(qcResponse.data.permissions);
              getGymWelcome();
            }
          }
        }));
  }

  public void getGymWelcome() {
    spWelcome = gymUseCase.getGymWelcom(gymWrapper.id(), gymWrapper.model(),
        new Action1<QcDataResponse<GymDetail>>() {
          @Override public void call(QcDataResponse<GymDetail> qcResponseGymDetail) {
            if (ResponseConstant.checkSuccess(qcResponseGymDetail)) {
              gymDetailView.onGymInfo(qcResponseGymDetail.data.gym);
              gymDetailView.onSuperUser(qcResponseGymDetail.data.superuser);
              if (qcResponseGymDetail.data.banners != null) {
                gymDetailView.setBanner(qcResponseGymDetail.data.banners);
              }
              gymDetailView.setInfo(qcResponseGymDetail.data.stat);

              gymDetailView.studentPreview(qcResponseGymDetail.data.welcome_url,
                  qcResponseGymDetail.data.hint_url);
              String price = "";
              if (qcResponseGymDetail.data.gym.first_month_favorable_info != null) {
                price = qcResponseGymDetail.data.gym.first_month_favorable_info.favorable_price;
              }
              if (qcResponseGymDetail.getData().gym.miniProgram != null) {
                gymDetailView.onMiniProgram(qcResponseGymDetail.getData().gym.miniProgram);
              }
              gymWrapper.setHasFirst(qcResponseGymDetail.data.gym.has_first_month_favorable);
              gymWrapper.setFirstPrice(price);
              gymDetailView.setRecharge(qcResponseGymDetail.data.recharge,
                  qcResponseGymDetail.data.gym.has_first_month_favorable, price);
              gymDetailView.onSpecialPoint(qcResponseGymDetail.data.qingcheng_activity_count);
              gymDetailView.onPartnter(qcResponseGymDetail.data.partner_status);
              if (qcResponseGymDetail.data.gym.module_custom != null) {
                try {
                  qcDbManager.insertFunction(
                      (List<String>) qcResponseGymDetail.data.gym.module_custom);
                } catch (Exception e) {
                  qcDbManager.insertFunction(null);
                  Timber.d(e.getMessage());
                  CrashUtils.sendCrash(e);
                }
              }
              App.gCanReload = true;
            } else {
              gymDetailView.onFailed();
            }
          }
        });
  }

  public void quitGym() {
    RxRegiste(restRepository.getStaffAllApi()
        .qcQuitGym(loginStatus.staff_id(), gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              gymDetailView.onQuitGym();
            } else {
              ToastUtils.show(qcResponse.getMsg());
              gymDetailView.onFailed();
            }
            ;
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            gymDetailView.onFailed();
          }
        })

    );
  }

  public void loadGymSettingInfo() {
    RxRegiste(restRepository.getStaffAllApi()
        .qcGetGymSettingInfo(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            GymSettingInfo gymSettingInfo = new GymSettingInfo();
            gymSettingInfo.has_teacher = false;
            gymSettingInfo.open_checkin = false;
            gymSettingInfo.has_private = false;
            gymSettingInfo.has_mall = false;
            gymSettingInfo.has_team = false;
            gymSettingInfo.skip_window = false;
            gymSettingInfo.gym_type = "健身工作室";
            gymDetailView.showGymFirstSettingDialog(gymSettingInfo);
          } else {
            ToastUtils.show(response.getMsg());
            gymDetailView.onFailed();
          }
        }));
  }
}
