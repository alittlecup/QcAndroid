package cn.qingchengfit.pos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.di.model.SuperUser;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.login.LoginActivity;
import cn.qingchengfit.pos.login.model.GymResponse;
import cn.qingchengfit.pos.login.presenter.LoginPresenter;
import cn.qingchengfit.pos.main.MainActivity;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/18.
 */

public class SplashActivity extends BaseActivity {

  @Inject QcRestRepository restRepository;
  @Inject GymWrapper gymWrapper;
  @Inject LoginPresenter presenter;
  @Inject IStaffModel staffModel;
  @Inject LoginStatus loginStatus;
  private Subscription sb;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
      //结束你的activity
      finish();
      return;
    }
    setContentView(R.layout.activity_splash);
    ButterKnife.bind(this);
    ToastUtils.init(getApplicationContext());
    MobclickAgent.setDebugMode(BuildConfig.DEBUG);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (!sb.isUnsubscribed()) sb.unsubscribe();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @SuppressLint("MissingPermission") @Override protected void onStart() {
    super.onStart();
    sb = new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE)
      .flatMap(new Func1<Boolean, Observable<String>>() {
        @Override public Observable<String> call(Boolean aBoolean) {
          if (aBoolean) {
            return Observable.just(
              ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
          } else {
            showAlert("请打开查看设备状态权限");
            return null;
          }
        }
      })
      .filter(new Func1<String, Boolean>() {
        @Override public Boolean call(String s) {
          return s != null;
        }
      })
      .flatMap(new Func1<String, Observable<QcDataResponse<GymResponse>>>() {
        @Override public Observable<QcDataResponse<GymResponse>> call(String s) {
          HashMap<String, Object> params = new HashMap<>();
          params.put("imei", s);
          return restRepository.createPostApi(PosApi.class)
            .qcGetGym(params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io());
        }
      })
      .flatMap(new Func1<QcDataResponse<GymResponse>, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(final QcDataResponse<GymResponse> gymQcDataResponse) {
          if (gymQcDataResponse.getStatus() == ResponseConstant.SUCCESS) {
            // 注入GymWrapper信息
            CoachService coachService = new CoachService();
            coachService.setId(gymQcDataResponse.data.gym.id);
            coachService.setGym_id(gymQcDataResponse.data.gym.id);
            coachService.setName(gymQcDataResponse.data.gym.name);
            coachService.setPhoto(gymQcDataResponse.data.gym.photo);
            gymWrapper.setCoachService(coachService);
            gymWrapper.setCustumNo(gymQcDataResponse.data.customer_no);
            gymWrapper.setSocket_channel_id(gymQcDataResponse.data.socket_channel_id);
            gymWrapper.setBrand(
              new Brand.Builder().name(gymQcDataResponse.data.gym.brand_name).build());
            gymWrapper.setSuperuser(new SuperUser(gymQcDataResponse.data.gym.superuser.username,
              gymQcDataResponse.data.gym.superuser.phone));
          } else {
            SplashActivity.this.runOnUiThread(new Runnable() {
              @Override public void run() {
                showAlert(gymQcDataResponse.getStatus() + ":" + gymQcDataResponse.getMsg());
              }
            });
            return  Observable.just(false);
          }
          if (TextUtils.isEmpty(QcRestRepository.getSession(SplashActivity.this))) {
            return Observable.just(true);
          } else {
            return staffModel.getCurUser()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .flatMap(new Func1<QcDataResponse<UserWrap>, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(QcDataResponse<UserWrap> userWrapQcDataResponse) {
                  if (ResponseConstant.checkSuccess(userWrapQcDataResponse)) {
                    Staff staff = new Staff(userWrapQcDataResponse.getData().user);
                    loginStatus.setLoginUser(staff);
                    loginStatus.setUserId(staff.id);
                    staff.id = userWrapQcDataResponse.data.staff_id;
                    return Observable.just(true);
                  } else {
                    if (userWrapQcDataResponse.error_code.equalsIgnoreCase("400001"))
                      QcRestRepository.clearSession(SplashActivity.this);
                    showAlert(
                      userWrapQcDataResponse.getStatus() + ":" + userWrapQcDataResponse.getMsg());
                    return Observable.just(true);
                  }
                }
              });
          }
        }
      })
      .subscribe(new NetSubscribe<Boolean>() {
        @Override public void onNext(Boolean isgo) {
          //if (isgo) {
            goMain();
          //} else {
          //  LogUtil.e("splash error");
          //}
        }
      });
  }

  private void goMain() {
    Intent toMain;
    if (TextUtils.isEmpty(QcRestRepository.getSession(this))) {
      toMain = new Intent(SplashActivity.this, LoginActivity.class);
    } else {
      toMain = new Intent(SplashActivity.this, MainActivity.class);
    }
    toMain.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(toMain);
    overridePendingTransition(0, 0);
    SplashActivity.this.finish();
    overridePendingTransition(0, 0);
  }
}
