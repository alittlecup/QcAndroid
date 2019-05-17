package cn.qingchengfit.views.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.LoadingDialog;
import cn.qingchengfit.widgets.LoadingDialogTransParent;
import cn.qingchengfit.widgets.R;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.umeng.analytics.MobclickAgent;
import dagger.android.AndroidInjection;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

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
  private DialogList dialogList;
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
  private Observable<NetWorkDialogEvent> obLoading;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    try {
      AndroidInjection.inject(this);
    } catch (Exception e) {
      LogUtil.e("not find in AppCompot ");
    }
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isFitSystemBar()) {
      Window window = getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
      );
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
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

    getSupportFragmentManager().registerFragmentLifecycleCallbacks(fcb,false);
  }

  protected boolean isFitSystemBar(){
    return true;
  }

  boolean hasFrag = false;
  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    if (intent != null && intent.getData() != null)
      LogUtil.d("router",intent.getData().toString());
    Uri uri = intent.getData();
    if (uri == null)
      return;
    boolean notStack = false;
    if (uri != null && uri.getBooleanQueryParameter("nostack",false))
      notStack = true;
    if (preHandle(intent)) return;
    FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
    if (hasFrag)
      tr.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
          R.anim.slide_right_out);
    else tr.setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold);
    Fragment fragment = getRouterFragment(intent);
    tr.replace(R.id.web_frag_layout, fragment);
    if (hasFrag && !notStack) tr.addToBackStack(null);
    try {
      tr.commit();
    } catch (Exception e) {
      CrashUtils.sendCrash(e);
      tr.commitAllowingStateLoss();
    }
    hasFrag = true;
  }
  protected boolean preHandle(Intent intent){
    return false;
  }

  protected  Fragment getRouterFragment(Intent intent){
    return new Fragment();
  };

  public void setStatusTextColor(boolean dark){
    if(Build.VERSION.SDK_INT >= 23) {
      Window window = getWindow();
      if (dark)
        window.addFlags(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      else window.clearFlags(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }
  }
  @Override protected void onDestroy() {
    getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fcb);
    super.onDestroy();
    checkCloseAlert();
  }

  @Override protected void onResume() {
    super.onResume();

    MobclickAgent.onResume(this);
  }

  @Override protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
    checkLoading();
  }

  @Override protected void onStart() {
    super.onStart();
    initBus();
  }

  @Override protected void onStop() {
    super.onStop();
    RxBus.getBus().unregister(NetWorkDialogEvent.class.getName(),obLoading);
    RxBus.getBus().unregister(EventNetWorkError.class.getName(), obNetError);
  }

  private void initBus(){
    obLoading = RxBus.getBus().register(NetWorkDialogEvent.class);
       obLoading.onBackpressureLatest()
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

    obNetError = RxBus.getBus().register(EventNetWorkError.class);
    obNetError.subscribe(new Action1<EventNetWorkError>() {
      @Override public void call(EventNetWorkError eventNetWorkError) {
        if (eventNetWorkError.errCode > 0){
          try {
            showAlert(eventNetWorkError.errCode);
          }catch (Exception e){
            CrashUtils.sendCrash(e);
          }
        }
        hideLoading();
        hideLoadingTransparent();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        LogUtil.e(throwable.getMessage());
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

  public void checkCloseAlert(){if (mAlert != null && mAlert.isShowing()){
    mAlert.dismiss();
  }
  }

  public void showAlert(String res) {
    if (mAlert == null) {
      mAlert = new MaterialDialog.Builder(this).positiveText("知道了")
          .autoDismiss(true)
          .canceledOnTouchOutside(true)
          .build();
    }
    if (mAlert.isShowing()) mAlert.dismiss();
    mAlert.setContent(res);
    mAlert.show();
  }

  public void showDialogList(String title,List<String> strings, AdapterView.OnItemClickListener listener){
    if (dialogList == null){
      dialogList = new DialogList(this);
    }
    if (dialogList.isShowing())
      dialogList.dismiss();
    if (!CmStringUtils.isEmpty(title)){
      dialogList.title(title);
    }
    dialogList.list(strings,listener);
    dialogList.show();
  }


  public void showLoadingTransparent() {
    if (loadingDialogTrans == null) loadingDialogTrans = new LoadingDialogTransParent(this);
    if (loadingDialogTrans.isShowing()) loadingDialogTrans.dismiss();
    if (!isFinishing()) {
      loadingDialogTrans.show();
    }
  }

  TimeDialogWindow timeDialogWindow;
  public void chooseTime(TimePopupWindow.Type type,int start,int end ,Date date ,TimeDialogWindow.OnTimeSelectListener listener){
    if (timeDialogWindow == null){
      timeDialogWindow = new TimeDialogWindow(this,type);
      timeDialogWindow.setRange(start == 0?1990:start,end == 0? 2100 : end);
    }
    timeDialogWindow.setOnTimeSelectListener(listener);
    timeDialogWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0,date);

  }

  public void hideLoadingTransparent() {
    if (loadingDialogTrans != null) loadingDialogTrans.dismiss();
  }

  @Override public void onBackPressed() {
    if (backPress == null || !backPress.onFragmentBackPress()) super.onBackPressed();
  }

  public String getModuleName(){
    return "";
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

  /**
   * 仅仅给一些陈旧页面使用
   */
  @Deprecated
  public void initToolbar(@NonNull Toolbar toolbar) {
    toolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup ) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
        MeasureUtils.getStatusBarHeight(this), 0, 0);
    }
  }

  public interface FragmentBackPress {
    boolean onFragmentBackPress();
  }
}
