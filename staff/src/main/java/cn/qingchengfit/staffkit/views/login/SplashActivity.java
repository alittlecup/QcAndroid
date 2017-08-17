package cn.qingchengfit.staffkit.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.custom.CircleIndicator;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.umeng.analytics.MobclickAgent;
import dagger.android.AndroidInjection;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static cn.qingchengfit.staffkit.MainActivity.IS_SIGNLE;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.splash_viewpager) ViewPager splashViewpager;
    @BindView(R.id.splash_login_btn) TextView splashLoginBtn;
    @BindView(R.id.splash_registe_btn) TextView splashRegisteBtn;
    @BindView(R.id.splash_indicator) CircleIndicator splashIndicator;
    @BindView(R.id.main_loading) RelativeLayout mainLoading;
  @BindView(R.id.img_gif) ImageView imgGif;
    @Inject RestRepository restRepository;
    @Inject QCDbManager manager;
    @Inject LoginStatus loginStatus;
    String url;
    private Subscription sp;

    @Override protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        PushSettings.enableDebugMode(getApplicationContext(), BuildConfig.DEBUG);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
            getString(BuildConfig.DEBUG ? R.string.baidu_api_debug : R.string.baidu_api));
        cn.qingchengfit.utils.ToastUtils.init(getApplicationContext());
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
      Glide.with(this)
          .load(R.mipmap.qc_logo_staff)
          .dontAnimate()
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .into((new GlideDrawableImageViewTarget(imgGif, 1)));
        if (getIntent() != null && getIntent().getData() != null && getIntent().getScheme() != null && getIntent().getScheme()
            .equalsIgnoreCase("qcstaff")) {
            try {
                String path = getIntent().getData().toString();
                url = path.split("openurl/")[1];
                if (!url.startsWith("http")) url = "http://" + url;
            } catch (Exception e) {

            }
        }
    }

    @Override protected void onDestroy() {
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }

    @Override protected void onStart() {
        super.onStart();

        String staffid = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_ID, "");
        if (PreferenceUtils.getPrefString(SplashActivity.this, Configs.PREFER_SESSION, null) != null && !TextUtils.isEmpty(staffid)) {
            App.staffId = staffid;
            sp = restRepository.getGet_api()
                .qcGetSelfInfo(staffid).delay(2500, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<QcResponseData<StaffResponse>, Observable<QcResponseData<GymList>>>() {
                    @Override public Observable<QcResponseData<GymList>> call(QcResponseData<StaffResponse> staffResponseQcResponseData) {
                        if (ResponseConstant.checkSuccess(staffResponseQcResponseData)) {
                            loginStatus.setUserId(staffResponseQcResponseData.data.staff.user_id);
                            PreferenceUtils.getPrefString(SplashActivity.this, Configs.PREFER_USER_ID,
                                staffResponseQcResponseData.data.staff.user_id);
                        } else {
                            ToastUtils.show("服务器错误");
                        }
                        return restRepository.getGet_api()
                            .qcGetCoachService(App.staffId, null)
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Action1<QcResponseData<GymList>>() {
                    @Override public void call(QcResponseData<GymList> qcResponseGymList) {
                        if (ResponseConstant.checkSuccess(qcResponseGymList) && qcResponseGymList.data.services != null) {
                            GymBaseInfoAction.writeGyms(qcResponseGymList.data.services);
                            if (qcResponseGymList.data.services.size() == 0) {
                                goSplashViewpager();
                                overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
                            } else {
                                boolean isSingle = qcResponseGymList.data.services.size() == 1;
                                goMain(isSingle, isSingle ? qcResponseGymList.data.services.get(0) : null);
                            }
                        } else {
                            goSplashViewpager();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        ToastUtils.show("服务器错误");
                        goSplashViewpager();
                    }
                });
        } else {
            goSplashViewpager();
        }
    }

    private void goMain(boolean isSingle, CoachService coachService) {
        Intent toMain = new Intent(SplashActivity.this, MainActivity.class);
        toMain.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toMain.putExtra(IS_SIGNLE, isSingle);
        toMain.putExtra(MainActivity.OPEN_URL, url);
        toMain.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
        startActivity(toMain);
        overridePendingTransition(0, 0);
        SplashActivity.this.finish();
        overridePendingTransition(0, 0);
    }

    public void goSplashViewpager() {
        //goLogin(0);
        Intent toMain = new Intent(SplashActivity.this, MainActivity.class);
        toMain.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toMain.putExtra(IS_SIGNLE, true);
        startActivity(toMain);
        overridePendingTransition(0, 0);
        SplashActivity.this.finish();
        overridePendingTransition(0, 0);
    }

    @UiThread public void goLogin(int registe) {
        Intent toLogin = new Intent(this, LoginActivity.class);
        toLogin.putExtra("isRegiste", registe);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(toLogin);
        this.finish();
        overridePendingTransition(0, 0);
    }
}