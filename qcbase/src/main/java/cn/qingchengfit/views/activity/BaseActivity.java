package cn.qingchengfit.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.LoadingDialog;
import cn.qingchengfit.widgets.LoadingDialogTransParent;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.umeng.analytics.MobclickAgent;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/7/29 2015.
 */
public class BaseActivity extends AppCompatActivity {
  public static final String ROUTER = "cn.qingchengfit.router";

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Inject GymWrapper gymWrapper;
  private LoadingDialog loadingDialog;
  private MaterialDialog mAlert;
  private LoadingDialogTransParent loadingDialogTrans;
  private Observable<EventNetWorkError> obNetError;
  private FragmentBackPress backPress;
  private FragmentManager.FragmentLifecycleCallbacks fcb = new FragmentManager.FragmentLifecycleCallbacks() {
    @Override public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
      super.onFragmentAttached(fm, f, context);
      if (!(f instanceof SupportRequestManagerFragment))
        LogUtil.d("router",f.getClass().getName());
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    try {
      AndroidInjection.inject(this);
    } catch (Exception e) {
      LogUtil.e("not find in AppCompot ");
    }
    super.onCreate(savedInstanceState);
    try {
      if (getIntent().getParcelableExtra(Constants.EXTRA_GYM_SERVICE) != null) {
        gymWrapper.setCoachService(
            (CoachService) getIntent().getParcelableExtra(Constants.EXTRA_GYM_SERVICE));
      }
      if (getIntent().getParcelableExtra(Constants.EXTRA_BRAND) != null) {
        gymWrapper.setBrand((Brand) getIntent().getParcelableExtra(Constants.EXTRA_BRAND));
      }
    } catch (Exception e) {
      CrashUtils.sendCrash(e);
    }
    obNetError = RxBus.getBus().register(EventNetWorkError.class);
    obNetError.subscribe(new Action1<EventNetWorkError>() {
      @Override public void call(EventNetWorkError eventNetWorkError) {
        hideLoading();
        hideLoadingTransparent();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        LogUtil.e(throwable.getMessage());
      }
    });
    getSupportFragmentManager().registerFragmentLifecycleCallbacks(fcb,false);
  }

  @Override protected void onDestroy() {
    getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fcb);

    RxBus.getBus().unregister(EventNetWorkError.class.getName(), obNetError);
    super.onDestroy();
  }

  @Override protected void onResume() {
    super.onResume();
    initBus();
    MobclickAgent.onResume(this);
  }

  @Override protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
    checkLoading();
  }

  private void initBus(){
    RxBus.getBus().register(NetWorkDialogEvent.class)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<NetWorkDialogEvent>() {
      @Override public void call(NetWorkDialogEvent netWorkDialogEvent) {
        if (netWorkDialogEvent.type == NetWorkDialogEvent.EVENT_GET) {
          showLoadingTransparent();
        } else if (netWorkDialogEvent.type == NetWorkDialogEvent.EVENT_POST) {
          showLoading();
        } else {
          checkLoading();
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        CrashUtils.sendCrash(throwable);
      }
    });
  }

  private void checkLoading(){
    if (loadingDialog != null && loadingDialog.isShowing()){
      hideLoading();
    }
    if(loadingDialogTrans!= null && loadingDialogTrans.isShowing()){
      hideLoadingTransparent();
    }
  }

  public void showLoading() {
    if (loadingDialog == null) loadingDialog = new LoadingDialog(this);
    if (loadingDialog.isShowing()) loadingDialog.dismiss();
    if (!isFinishing())
      loadingDialog.show();
  }

  public void hideLoading() {
    if (loadingDialog != null) loadingDialog.dismiss();
  }

  public void showAlert(int res) {
    showAlert(getString(res));
  }

  public void showAlert(String res) {
    if (mAlert == null) ;
    mAlert = new MaterialDialog.Builder(this).positiveText("知道了")
        .autoDismiss(true)
        .canceledOnTouchOutside(true)
        .build();
    if (mAlert.isShowing()) mAlert.dismiss();
    mAlert.setContent(res);
    mAlert.show();
  }

  public void showLoadingTransparent() {
    if (loadingDialogTrans == null) loadingDialogTrans = new LoadingDialogTransParent(this);
    if (loadingDialogTrans.isShowing()) loadingDialogTrans.dismiss();
    if (!isFinishing()) {
      loadingDialogTrans.show();
    }
  }

  public void hideLoadingTransparent() {
    if (loadingDialogTrans != null) loadingDialogTrans.dismiss();
  }

  @Override public void onBackPressed() {
    if (backPress == null || !backPress.onFragmentBackPress()) super.onBackPressed();
  }

  public FragmentBackPress getBackPress() {
    return backPress;
  }

  public void setBackPress(FragmentBackPress backPress) {
    this.backPress = backPress;
  }

  protected String getRouter() {
    if (getIntent() != null && getIntent().getExtras() != null) {
      return getIntent().getStringExtra(ROUTER);
    } else {
      return "";
    }
  }

  public @IdRes int getFragId() {
    return 0;
  }

  public interface FragmentBackPress {
    boolean onFragmentBackPress();
  }
}
