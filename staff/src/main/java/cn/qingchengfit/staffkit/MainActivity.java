package cn.qingchengfit.staffkit;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.qingchengfit.utils.DialogUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.TIMManager;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.common.AppData;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventInitApp;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.events.EventSessionError;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.PushBody;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.model.responese.UpdateVersion;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.BusEventThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.reciever.PushReciever;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventBrandChange;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachService;
import cn.qingchengfit.staffkit.rxbus.event.EventGoNotification;
import cn.qingchengfit.staffkit.rxbus.event.RxCloseAppEvent;
import cn.qingchengfit.staffkit.rxbus.event.UpdateEvent;
import cn.qingchengfit.staffkit.views.MainFirstFragment;
import cn.qingchengfit.staffkit.views.login.SplashActivity;
import cn.qingchengfit.staffkit.views.main.MainMsgFragment;
import cn.qingchengfit.staffkit.views.main.QcVipFragment;
import cn.qingchengfit.staffkit.views.main.SettingFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.weex.utils.WeexUtil;
import cn.qingchengfit.widgets.TabViewNoVp;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements FragCallBack {
  public static final String IS_SIGNLE = "is_signle";
  public static final String OPEN_URL = "open_url";
  public static final String MAIN_ACTION = "main.action";
  public static final int REOPEN_APP = -1;
  TabViewNoVp tabview;
  FrameLayout fragChooseBrand;
  FrameLayout layoutBrands;
  TextView tvNotiCount;
  @Inject RestRepository restRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject BaseRouter baseRouter;
  @Inject GymBaseInfoAction gymBaseInfoAction;
  String[] tags = new String[] { "gyms", "find", "msg", "setting" };
  private int[] mIconSelect = {
      R.drawable.vd_tabbar_manage_active, R.drawable.vd_tabbar_discover_active,
      R.drawable.vd_tabbar_message_active, R.drawable.vd_tabbar_mine_active
  };
  private int[] mIconNormal = {
      R.drawable.vd_tabbar_manage_normal, R.drawable.vd_tabbar_discover_normal,
      R.drawable.vd_tabbar_message_normal, R.drawable.vd_tabbar_mine_normal
  };
  private boolean isDownloading = false;
  private DownloadManager downloadManager;
  private Subscription updateSp;
  private Observable<EventLoginChange> logoutObaserable;
  private Observable<RxCloseAppEvent> mCloseOb;
  private MaterialDialog updateDialog;
  private MaterialDialog exitDialog;
  private Observable<EventGoNotification> mNotiOb;
  private long myDownloadReference;
  private Observable<EventFreshCoachService> mFreshCoachService;
  private Subscription sp;
  private Observable<EventBrandChange> brandChangeOb;
  private Observable<EventInitApp> mBackMainOb;
  /**
   * 更新下载进度
   */
  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
      if (myDownloadReference == reference) {
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
        myDownloadQuery.setFilterById(reference);
        Cursor myDownload = downloadManager.query(myDownloadQuery);
        if (myDownload.moveToFirst()) {
          int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
          String fileUri = myDownload.getString(fileUriIdx);
          AppUtils.install(context, Uri.parse(fileUri).getPath());
          isDownloading = false;
        }
        myDownload.close();
      }
    }
  };
  private Observable<EventSessionError> obLogOut;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tabview = (TabViewNoVp) findViewById(R.id.tabview);
    fragChooseBrand = (FrameLayout) findViewById(R.id.frag_choose_brand);
    layoutBrands = (FrameLayout) findViewById(R.id.layout_brands);
    tvNotiCount = (TextView) findViewById(R.id.tv_noti_count);
    findViewById(R.id.layout_brands).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBgClick();
      }
    });

    initInject();
    initRouter();
    registeGlobleEvent();
    askPermission();
    update(false);
    onNewIntent(getIntent());
    tabview.setupTabView(mIconSelect, mIconNormal);
    tabview.setPoint(1);
    tabview.setListener(pos -> {
      if (pos == 1) {
        tabview.clearPoint(1);
      }
      showPage(pos);
    });
    showPage(0);
    //聊天的初始化

    if (Constant.ACCOUNT_TYPE == 0) {
      initIM();
    }

    MyApplication myApplication = new MyApplication(getApplication());
    if (loginStatus.isLogined()) initBDPush();


    /*
     * 全局监听App关闭的消息
     */
    mCloseOb = RxBus.getBus().register(RxCloseAppEvent.class);
    mCloseOb.subscribe(rxCloseAppEvent -> {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        finishAndRemoveTask();
      } else {
        finishAffinity();
      }
      System.exit(0);
    });
    /*
     * App异常时重新回到主页
     */
    mBackMainOb = RxBus.getBus().register(EventInitApp.class);
    mBackMainOb.subscribe(new Action1<EventInitApp>() {
      @Override public void call(EventInitApp eventInitApp) {
        try {
          Intent toMain = new Intent();
          toMain.setPackage(getPackageName());
          toMain.setAction("cn.qingcheng.main");
          startActivity(toMain);
        } catch (Exception e) {
          LogUtil.e("麻蛋 新的error : " + e.getMessage());
        }
      }
    });


    /*
     * 全局监听刷新 场馆
     */
    mFreshCoachService = RxBus.getBus().register(EventFreshCoachService.class);
    mFreshCoachService.subscribe(eventFreshCoachService -> sp = restRepository.getGet_api()
        .qcGetCoachService(loginStatus.staff_id(), null)
        .delay(2, TimeUnit.SECONDS)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(qcResponseGymList -> {
          if (ResponseConstant.checkSuccess(qcResponseGymList)
              && qcResponseGymList.data.services != null) {
            gymBaseInfoAction.writeGyms(qcResponseGymList.data.services);
          }
        }));
    /*
     * 全局监听品牌
     */
    brandChangeOb = RxBus.getBus().register(EventBrandChange.class);
    brandChangeOb.observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventBrandChange>() {
          @Override public void call(EventBrandChange eventBrandChange) {
            PreferenceUtils.setPrefString(MainActivity.this, Configs.CUR_BRAND_ID,
                gymWrapper.brand_id());
            onBgClick();
          }
        });
  }

  private void initIM() {
    Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
    Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
    Constant.setXiaomiPushAppid("2882303761517568688");
    Constant.setBussId(BuildConfig.DEBUG ? 609 : 604);
    Constant.setXiaomiPushAppkey("5651756859688");
    Constant.setHuaweiBussId(606);
    Constant.setUsername(getString(R.string.chat_user_id_header, loginStatus.getUserId()));
    Constant.setHost(Uri.parse(Configs.Server).getHost());
  }

  private void showPage(int pos) {
    FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
    for (int i = 0; i < tags.length; i++) {
      Fragment f = getSupportFragmentManager().findFragmentByTag(tags[i]);

      if (i == pos) {
        if (f == null) {
          f = generateFragment(pos);
          ts.add(R.id.frag_main, f, tags[i]);
        } else {
          ts.show(f);
        }
      } else {
        if (f != null) ts.hide(f);
      }
    }
    ts.commit();
  }

  public Fragment generateFragment(int pos) {
    switch (pos) {
      case 1:
        return QcVipFragment.newInstance(Configs.URL_QC_FIND.replace("http", "https"));
      case 2:
        return new MainMsgFragment();
      case 3:
        return new SettingFragment();
      default:
        return new MainFirstFragment();
    }
  }

  public void freshNotiCount(int count) {
    tvNotiCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    tvNotiCount.setText(count > 99 ? "..." : Integer.toString(count));
  }

  private void askPermission() {
    new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .flatMap((Func1<Boolean, Observable<?>>) aBoolean -> {
          if (aBoolean) {
            WeexUtil.loadAndSaveData(Configs.WEEX_RELEASE_PATH, Configs.WEEX_TEST_PATH,
                Configs.WEEX_PAGE_INDEX,BuildConfig.DEBUG);
          }
          return new RxPermissions(MainActivity.this).request(
              Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS,
              Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        })
        .subscribe();
  }

  private void initRouter() {
    baseRouter.registeRouter("recruit", RecruitActivity.class);
    baseRouter.registeRouter("saas", SaasContainerActivity.class);
  }

  private void initBDPush() {
    String userid = PreferenceUtils.getPrefString(this, PushReciever.BD_USERLID, null);
    String channelid = PreferenceUtils.getPrefString(this, PushReciever.BD_CHANNELID, null);
    if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(channelid)) {
      PushBody pushBody = new PushBody();
      pushBody.push_channel_id = channelid;
      pushBody.push_id = userid;
      pushBody.device_type = "android";
      pushBody.distribute = getString(R.string.oem);
      restRepository.getPost_api()
          .qcPostPushId(loginStatus.staff_id(), pushBody)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(new Subscriber<QcResponse>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(QcResponse qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                PreferenceUtils.setPrefBoolean(MainActivity.this, "hasPushId", true);
              }
            }
          });
    } else {
      Timber.e("bdpush:empty");
    }
  }

  protected void initInject() {
    String staffid = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_ID, "");
    String staffname = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_NAME, "");
    String session = QcRestRepository.getSession(this);
    String user_id = PreferenceUtils.getPrefString(this, Configs.PREFER_USER_ID, "");

    Staff staff = new Staff();
    staff.setId(staffid);
    staff.setUsername(staffname);
    loginStatus.setLoginUser(staff);
    if (!TextUtils.isEmpty(user_id)) loginStatus.setUserId(user_id);
    loginStatus.setSession(session);
  }

  private void registeGlobleEvent() {
    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    registerReceiver(receiver, filter);

    updateSp = RxBus.getBus().register(UpdateEvent.class).subscribe(new Action1<UpdateEvent>() {
      @Override public void call(UpdateEvent updateEvent) {
        update(true);
      }
    });
  }

  private void initBus() {
    logoutObaserable = RxBus.getBus().register(EventLoginChange.class);
    logoutObaserable.observeOn(AndroidSchedulers.mainThread())
        .throttleFirst(1, TimeUnit.SECONDS)
        .subscribe(new Action1<EventLoginChange>() {
          @Override public void call(EventLoginChange eventLoginChange) {
            if (loginStatus.isLogined()) {
              initInject();
              initBDPush();
            } else {
              freshNotiCount(0);
            }
          }
        });
    obLogOut =
        RxBus.getBus().register(EventSessionError.class).observeOn(AndroidSchedulers.mainThread());
    obLogOut.subscribe(eventSessionError -> {
      logout();
      ToastUtils.show("登录过期");
      RxBus.getBus().post(new EventLoginChange());
      goLogin();
    }, new BusEventThrowable());
  }

  void goLogin() {
    baseRouter.toLogin(this);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (intent.getIntExtra(MAIN_ACTION, 0) == REOPEN_APP) {
      startActivity(new Intent(this, SplashActivity.class));
      finish();
    }
    String openUlr = getIntent().getStringExtra(OPEN_URL);
    if (!StringUtils.isEmpty(openUlr) && openUlr.startsWith("http")) {
      WebActivity.startWeb(openUlr, this);
    }
  }

  @Override protected void onStart() {
    super.onStart();
    update(false);
    initBus();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @Override public void onBackPressed() {
    if (!getSupportFragmentManager().popBackStackImmediate()) {
      if (exitDialog == null) {
        exitDialog = DialogUtils.initConfirmDialog(this, "退出应用?", "", (dialog, action) -> {
          dialog.dismiss();
          if (action == DialogAction.POSITIVE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              finishAndRemoveTask();
            } else {
              finishAffinity();
            }
            System.exit(0);
          }
        });
      }
      exitDialog.show();
    }
  }

  /**
   * fir 更新检查
   */
  private void update(final boolean show) {
    if (!isDownloading) {
      try {
        FIR.checkForUpdateInFIR(
            getString(BuildConfig.DEBUG ? R.string.fir_token_beta : R.string.fir_token_release),
            new VersionCheckCallback() {
              @Override public void onFail(Exception e) {
                super.onFail(e);
                Timber.d(e.getMessage());
              }

              @Override public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                  final UpdateVersion updateVersion = new Gson().fromJson(s, UpdateVersion.class);
                  if (BuildConfig.DEBUG) {
                    if (BuildConfig.FLAVOR.equalsIgnoreCase("internaltest")) {
                      long oldupdate = PreferenceUtils.getPrefLong(MainActivity.this, "update", 0);
                      if (oldupdate == 0) {
                        oldupdate = updateVersion.updated_at;
                        PreferenceUtils.setPrefLong(MainActivity.this, "update",
                            updateVersion.updated_at);
                      }
                      if (updateVersion.updated_at > oldupdate) {
                        PreferenceUtils.setPrefLong(MainActivity.this, "update",
                            updateVersion.updated_at);
                        isDownloading = true;
                        Toast.makeText(MainActivity.this,
                            DateUtils.Date2YYYYMMDDHHmm(new Date(updateVersion.updated_at * 1000))
                                + "已在后台下载...", Toast.LENGTH_LONG).show();
                        downloadManager =
                            (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request =
                            new DownloadManager.Request(Uri.parse(updateVersion.installUrl));
                        request.setDestinationInExternalFilesDir(MainActivity.this,
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                            getString(R.string.app_name) + "_" + updateVersion.version + ".apk");
                        myDownloadReference = downloadManager.enqueue(request);
                      }
                    }
                  } else {

                    if (updateVersion.version > AppUtils.getAppVerCode(App.context)) {
                      if (updateDialog == null) {
                        MaterialDialog.Builder builder =
                            new MaterialDialog.Builder(MainActivity.this).title(
                                R.string.new_version)
                                .positiveText(R.string.update)
                                .content(updateVersion.changelog)
                                .positiveColorRes(R.color.colorPrimary)
                                .negativeColorRes(R.color.text_grey)
                                //                                        .cancelable(false)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                  @Override public void onClick(@NonNull MaterialDialog dialog,
                                      @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    isDownloading = true;
                                    ToastUtils.showDefaultStyle("已在后台下载...");
                                    downloadManager = (DownloadManager) getSystemService(
                                        Context.DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(
                                        Uri.parse(updateVersion.installUrl));
                                    request.setDestinationInExternalFilesDir(MainActivity.this,
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                                        getString(R.string.app_name)
                                            + "_"
                                            + updateVersion.version
                                            + ".apk");
                                    myDownloadReference = downloadManager.enqueue(request);
                                  }
                                });

                        if (updateVersion.version % 10 == 0) {
                          builder.canceledOnTouchOutside(false);
                          builder.autoDismiss(true);
                          builder.cancelListener(new DialogInterface.OnCancelListener() {
                            @Override public void onCancel(DialogInterface dialog) {
                              MainActivity.this.finish();
                            }
                          });
                        } else {
                          builder.autoDismiss(true);
                          builder.negativeText(R.string.next_time);
                        }
                        updateDialog = builder.build();
                      }
                      if (!updateDialog.isShowing()) updateDialog.show();
                    } else {
                      if (show) ToastUtils.showS(getString(R.string.is_latest_version));
                    }
                  }
                } catch (Exception e) {
                  CrashUtils.sendCrash(e);
                }
              }
            });
      } catch (Exception e) {
        CrashUtils.sendCrash(e);
      }
    }
  }

  @Override protected void onDestroy() {
    RxBus.getBus().unregister(EventLoginChange.class.getName(), logoutObaserable);
    RxBus.getBus().unregister(RxCloseAppEvent.class.getName(), mCloseOb);
    RxBus.getBus().unregister(EventFreshCoachService.class.getName(), mFreshCoachService);
    RxBus.getBus().unregister(EventBrandChange.class.getName(), brandChangeOb);
    RxBus.getBus().unregister(EventInitApp.class.getName(), mBackMainOb);
    RxBus.getBus().unregister(EventSessionError.class.getName(), obLogOut);
    if (updateSp != null) updateSp.unsubscribe();
    if (sp != null) sp.unsubscribe();
    unregisterReceiver(receiver);
    super.onDestroy();
  }

  public void logout() {
    loginStatus.logout(this);
    App.staffId = "";
    PushManager.stopWork(this.getApplicationContext());
    AppData.clear(this);
    TIMManager.getInstance().logout();
    QcRestRepository.clearSession(this);
    PreferenceUtils.setPrefString(this, Configs.PREFER_SESSION, "");
    PreferenceUtils.setPrefString(this, Configs.PREFER_WORK_ID, "");
    PreferenceUtils.setPrefString(this, Configs.CUR_BRAND_ID, "");
    MiPushClient.unregisterPush(this.getApplicationContext());
  }

  @Override public int getFragId() {
    return R.id.frag;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {

  }

  @Override public void cleanToolbar() {

  }

  @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

  }

  @Override public void onChangeFragment(BaseFragment fragment) {

  }

  ///**
  // * 点击选择品牌
  // */
  //@OnClick({ R.id.toolbar_title, R.id.title_down }) public void onClickTitle() {
  //    getSupportFragmentManager().beginTransaction().replace(R.id.frag_choose_brand, new ChooseBrandFragment()).commit();
  //    ViewCompat.setPivotY(fragChooseBrand, 0);
  //    if (layoutBrands.getVisibility() == View.VISIBLE) {
  //        onBgClick();
  //    } else {
  //        layoutBrands.setVisibility(View.VISIBLE);
  //        ViewCompat.animate(layoutBrands).alpha(1).setDuration(300).start();
  //        ViewCompat.animate(fragChooseBrand).scaleY(1).setDuration(300).start();
  //    }
  //}

  @Override public void setBar(ToolbarBean bar) {

  }

  ///**
  // * 添加场馆
  // */
  //@OnClick(R.id.btn_left) public void onClickLeft() {
  //    if (gymWrapper.inBrand()) {
  //        if (!gymWrapper.getBrand().has_add_permission) {
  //            ToastUtils.show("您没有改场馆管理权限");
  //            return;
  //        }
  //        Intent toBrandManage = new Intent(this, BrandManageActivity.class);
  //        toBrandManage.putExtra(Configs.EXTRA_BRAND, gymWrapper.getBrand());
  //        startActivity(toBrandManage);
  //    } else {
  //        Intent toGuide = new Intent(this, GuideActivity.class);
  //        toGuide.putExtra("isAdd", true);
  //        startActivity(toGuide);
  //    }
  //}

  /**
   * 关闭选择品牌窗口
   */
  public void onBgClick() {
    if (layoutBrands.getVisibility() == View.VISIBLE) {

      ViewCompat.animate(layoutBrands)
          .alpha(0)
          .setDuration(300)
          .setListener(new ViewPropertyAnimatorListener() {
            @Override public void onAnimationStart(View view) {

            }

            @Override public void onAnimationEnd(View view) {
              if (layoutBrands.getAlpha() == 0) {
                layoutBrands.setVisibility(View.GONE);
              }
            }

            @Override public void onAnimationCancel(View view) {

            }
          })
          .start();
      ViewCompat.animate(fragChooseBrand).scaleY(0).setDuration(300).start();
    }
  }
}
