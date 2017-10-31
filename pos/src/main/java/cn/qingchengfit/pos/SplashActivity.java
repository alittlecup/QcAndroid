package cn.qingchengfit.pos;

import android.content.Intent;
import android.os.Bundle;
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
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static cn.qingchengfit.pos.PosApp.context;

/**
 * Created by fb on 2017/10/18.
 */

public class SplashActivity extends BaseActivity{

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
    if (!sb.isUnsubscribed())
      sb.unsubscribe();
  }

  @Override protected void onStart() {
    super.onStart();
    HashMap<String, Object> params = new HashMap<>();
    params.put("imei","123456");
    sb = restRepository.createPostApi(PosApi.class)
        .qcGetGym(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<QcDataResponse<GymResponse>, Observable<QcDataResponse<UserWrap>>>() {
          @Override
          public Observable<QcDataResponse<UserWrap>> call(final QcDataResponse<GymResponse> gymQcDataResponse) {
            if (gymQcDataResponse.getStatus() == ResponseConstant.SUCCESS){
              // 注入GymWrapper信息
              CoachService coachService = new CoachService();
              coachService.setId(gymQcDataResponse.data.gym.id);
              coachService.setGym_id(gymQcDataResponse.data.gym.id);
              coachService.setName(gymQcDataResponse.data.gym.name);
              coachService.setPhoto(gymQcDataResponse.data.gym.photo);
              gymWrapper.setCoachService(coachService);
              gymWrapper.setCustumNo(gymQcDataResponse.data.customer_no);
              gymWrapper.setBrand(new Brand.Builder().name(gymQcDataResponse.data.gym.brand_name).build());
              gymWrapper.setSuperuser(new SuperUser(gymQcDataResponse.data.gym.superuser.username,
                gymQcDataResponse.data.gym.superuser.phone));

            }else{
              SplashActivity.this.runOnUiThread(new Runnable() {
                @Override public void run() {
                  showAlert(gymQcDataResponse.getStatus()+":"+gymQcDataResponse.getMsg());
                }
              });
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("gym_id", gymQcDataResponse.data.gym.id);
            return staffModel.getCurUser(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
          }
        })
      .subscribe(new NetSubscribe<QcDataResponse<UserWrap>>() {
        @Override public void onNext(QcDataResponse<UserWrap> userWrapQcDataResponse) {
          if (ResponseConstant.checkSuccess(userWrapQcDataResponse)) {
            loginStatus.setLoginUser(new Staff(userWrapQcDataResponse.getData().user));
            goMain();
          }else {
            showAlert(userWrapQcDataResponse.getStatus()+":"+userWrapQcDataResponse.getMsg());
          }
        }
      });


  }

  private void goMain() {
    Intent toMain;
    if (TextUtils.isEmpty(PreferenceUtils.getPrefString(context, "qingcheng.session", ""))) {
      toMain = new Intent(SplashActivity.this, LoginActivity.class);
    }else{
      toMain = new Intent(SplashActivity.this, MainActivity.class);
    }
    toMain.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(toMain);
    overridePendingTransition(0, 0);
    SplashActivity.this.finish();
    overridePendingTransition(0, 0);
  }

}
