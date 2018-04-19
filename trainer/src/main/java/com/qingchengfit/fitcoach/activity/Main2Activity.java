package com.qingchengfit.fitcoach.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.DiskLruCache;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.fragment.main.MainMsgFragment;
import com.qingchengfit.fitcoach.fragment.main.MainWebFragment;
import com.qingchengfit.fitcoach.fragment.mine.MineFragmentFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnLoginHomeFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnloginManageFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.reciever.PushReciever;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.ui.qcchat.LoginProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.UpdateVersion;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventCloseApp;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.events.EventSessionError;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.views.RecruitActivity;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.NetWorkUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.GuideWindow;
import cn.qingchengfit.widgets.TabViewNoVp;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tencent.tls.platform.TLSErrInfo;

import static com.qingchengfit.fitcoach.http.QcCloudClient.getApi;

public class Main2Activity extends BaseActivity implements WebActivityInterface {

  /**
   * 进入主页用途
   */
  public static final String ACTION = "main_action"; //key
  public static final int LOGOUT = 0;
  public static final int FINISH = 2;
  public static final int NOTIFICATION = 1;
  public static final int INIT = 3;
  public int ordersCount;
  /**
   * 退出弹窗提示
   */
  MaterialDialog logoutDialog;
  @Inject LoginStatus loginStatus;
  @Inject RepoCoachServiceImpl repoCoachService;
  @Inject BaseRouter baseRouter;
  AsyncDownloader mDownloadThread;
  private Gson gson;
  private Date mChooseDate;
  private GuideWindow gw;
  private Subscription spOrders;
  private Observable<EventInit> obPopWinEvent;
  private Observable<EventLoginChange> obLoginChange;

  private Observable<EventSessionError> obLogOut;
  private Snackbar NonetworkSnack;

  private MaterialDialog updateDialog;
  private MaterialDialog downloadDialog;
  private String url;
  private File newAkp;
  private boolean isShowFindRed = true;
  private DiskLruCache diskLruCache;
  private TabViewNoVp tabview;
  int curPageIndex = 0;//当前显示的页面
  //@BindView(R.id.order_studnet) View orderStudnet;
  //@BindView(R.id.web_position) View webPosition;
  TextView tvNotiCount;
  private FrameLayout layoutFrag;
  private LoginProcessor loginProcessor;

  public Date getChooseDate() {
    return mChooseDate;
  }

  public void setChooseDate(Date chooseDate) {
    mChooseDate = chooseDate;
  }
  MyApplication mChatApplication;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    tabview = findViewById(R.id.tabview);
    layoutFrag = findViewById(R.id.frag_main);
    tvNotiCount = findViewById(R.id.tv_noti_count);
    initRouter();
    RxBus.getBus().register(EventCloseApp.class)
      .onBackpressureDrop()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventCloseApp>() {
        @Override public void onNext(EventCloseApp eventCloseApp) {
          closeApp();
        }
      });

    new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .subscribe(aBoolean -> {
        if (aBoolean) {
          setupFile();
          initVersion();
        } else {
          ToastUtils.showDefaultStyle("请开启存储空间权限");
        }
      });

    new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR,
      Manifest.permission.WRITE_CALENDAR).subscribe(aBoolean -> {

    });

    setupVp();
    gson = new Gson();
    logoutDialog = new MaterialDialog.Builder(this).autoDismiss(true)
      .content("退出应用？")
      .positiveText("退出")
      .negativeText("取消")
      .onPositive((dialog, which) -> {
        closeApp();
      })
      .build();

    changeLogin();
    obLoginChange = RxBus.getBus().register(EventLoginChange.class);
    obLoginChange.observeOn(AndroidSchedulers.mainThread())
      .subscribe(eventLoginChange -> changeLogin());

    App.gMainAlive = true;//main是否存活,为推送
    if (getIntent() != null && getIntent().getIntExtra(ACTION, -1) == NOTIFICATION) {
      String contetn = getIntent().getStringExtra("url");
      Intent toWeb = new Intent(this, WebActivity.class);
      toWeb.putExtra("url", contetn);
      startActivity(toWeb);
    }
    obPopWinEvent = RxBus.getBus().register(EventInit.class);
    obPopWinEvent.subscribe(eventInit -> {
      if (eventInit.show) {
        switch (eventInit.pos) {
          case 3:
            Boolean isInit = PreferenceUtils.getPrefBoolean(this, "guide_3", false);
            if (!isInit && (gw == null || !gw.isShowing())) {
              if (gw == null) gw = new GuideWindow(this, "使用「课程排期」安排课程", GuideWindow.UP);
              if (tabview != null && tabview.getChildCount() > 1 && loginStatus.isLogined()) {
                gw.show(tabview.getChildAt(1));
              }
            }
            break;
          default:
            break;
        }
      } else {
        switch (eventInit.pos) {
          case 3:
            if (gw != null && gw.isShowing()) gw.dismiss();
            PreferenceUtils.setPrefBoolean(this, "guide_3", true);
            break;
          default:
        }
      }
    });

    if (getIntent() != null && getIntent().getScheme() != null && getIntent().getScheme()
      .equals("qccoach")) {
      try {
        String path = getIntent().getData().toString();
        url = path.split("openurl/")[1];
        showPage(2);
        if (!url.startsWith("http")) url = "http://" + url;
        WebActivity.startWeb(url, this);
      } catch (Exception e) {

      }
    }
  }

  /**
   * 完全关闭App
   */
  void closeApp() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      finishAndRemoveTask();
    } else {
      finishAffinity();
    }
    System.exit(0);
  }

  private void loginIM(){
    //聊天系统初始化
    mChatApplication = new MyApplication(getApplication());

    if (loginStatus.isLogined()){
      try {
        Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
        Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
        Constant.setXiaomiPushAppid(
          BuildConfig.DEBUG ? "2882303761517418101" : "2882303761517418101");
        Constant.setBussId(BuildConfig.DEBUG ? 611 : 605);
        Constant.setXiaomiPushAppkey("5361741818101");
        Constant.setHuaweiBussId(612);
        if (loginProcessor == null) {
          loginProcessor = new LoginProcessor(getApplicationContext(),
            getString(R.string.chat_user_id_header, loginStatus.getUserId()),
            Uri.parse(com.qingchengfit.fitcoach.Configs.Server).getHost(), new LoginProcessor.OnLoginListener() {
            @Override public void onLoginSuccess() {
              LogUtil.d("IM:  登录成功");
            }

            @Override public void onLoginFailed(TLSErrInfo t) {
              LogUtil.e("IM ::::"+t.Title+"   "+ t.Msg+"   "+t.ExtraMsg);
            }
          });
        }
        if (!loginProcessor.isLogin())
          loginProcessor.sientInstall();
      }catch (Exception e){
        CrashUtils.sendCrash(e);
      }
    }
  }

  @Override protected boolean isFitSystemBar() {
    return false;
  }

  private void initRouter() {
    baseRouter.registeRouter("recruit", RecruitActivity.class);
  }

  public void changeLogin() {
    if (loginStatus.isLogined()) {
      initUser();
      initBDPush();
    } else {
      freshNotiCount(0);
    }
  }

  private void setupVp() {
    //默认设置发现页面有红点
    tabview.setupTabView(mIconSelect,mIconNormal);
    tabview.setPoint(1);
    tabview.setListener(pos -> {
      if (pos == 1 || pos == 2) {
        tabview.clearPoint(pos);
      }
      showPage(pos);
    });
    showPage(0);
    tabview.setPointStatu(2, isShowFindRed);
    getSupportFragmentManager().registerFragmentLifecycleCallbacks(new android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks() {
      @Override
      public void onFragmentViewCreated(android.support.v4.app.FragmentManager fm, Fragment f,
        View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        beginTranste = false;
      }
    },false);
  }
  boolean beginTranste = false;//切换页面
  @Override public void onfinish() {

  }

  /**
   *
   * 主页当前页面
   *
   */
  public int getCurrrentPage() {
      return curPageIndex;
  }

  @Override protected void onResume() {
    super.onResume();
    loginIM();
    if (loginStatus.isLogined()) {
      if (spOrders != null && !spOrders.isUnsubscribed()) spOrders.unsubscribe();
      spOrders = QcCloudClient.getApi().getApi.qcGetOrderList()
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(qcResponsePage -> {
          tabview.setPointStatu(4,
            (!PreferenceUtils.getPrefBoolean(this, App.coachid + "_has_show_orders", false)
              && qcResponsePage.data.total_count > 0));
          ordersCount = qcResponsePage.data.total_count;
        });
    }
  }

  @Override public void onBackPressed() {

    if (getCurrrentPage() != 0)
      tabview.setCurrentItem(0);

    if (!logoutDialog.isShowing()) {
      logoutDialog.show();
    }
  }

  @Override protected void onDestroy() {
    App.gMainAlive = false;
    super.onDestroy();
    if (mDownloadThread != null) mDownloadThread.cancel(true);
    //RxBus.getBus().unregister(RxBus.OPEN_DRAWER, mMainObservabel);
    //RxBus.getBus().unregister(NetworkBean.class.getName(), mNetworkObservabel);
    RxBus.getBus().unregister(EventInit.class.getName(), obPopWinEvent);
    RxBus.getBus().unregister(EventLoginChange.class.getName(), obLoginChange);
  }

  private void initBDPush() {
    String userid = PreferenceUtils.getPrefString(this, PushReciever.BD_USERLID, null);
    String channelid = PreferenceUtils.getPrefString(this, PushReciever.BD_CHANNELID, null);
    if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(channelid)) {
      PushBody pushBody = new PushBody();
      pushBody.push_channel_id = channelid;
      pushBody.push_id = userid;
      pushBody.device_type = "android";
      pushBody.distribute = getString(R.string.oem_tag);
      getApi().postApi.qcPostPushId(App.coachid, pushBody)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<QcResponse>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(QcResponse qcResponse) {
            if (qcResponse.status == ResponseResult.SUCCESS) {
              PreferenceUtils.setPrefBoolean(Main2Activity.this, "hasPushId", true);
            }
          }
        });
    } else {
      LogUtil.e("bdpush:empty");
    }
  }

  private void initVersion() {
    LogUtil.e("version:" + AppUtils.getAppVer(this));
    if (!NetWorkUtils.isNetworkAvailable(this)) {
      NonetworkSnack = Snackbar.make(layoutFrag, "您的网络异常,请检查网络连接", Snackbar.LENGTH_INDEFINITE);
      NonetworkSnack.show();
    }

    FIR.checkForUpdateInFIR(
      getString(BuildConfig.DEBUG ? R.string.fir_token_debug : R.string.fir_token),
      new VersionCheckCallback() {
        @Override public void onSuccess(String s) {
          super.onSuccess(s);
          UpdateVersion updateVersion = new Gson().fromJson(s, UpdateVersion.class);
          if (BuildConfig.DEBUG && BuildConfig.FLAVOR.startsWith("internal")) {
            long oldupdate = PreferenceUtils.getPrefLong(Main2Activity.this, "update", 0);
            if (updateVersion.updated_at <= oldupdate) {
              return;
            }
            PreferenceUtils.setPrefLong(Main2Activity.this, "update", updateVersion.updated_at);
          } else {
            if (updateVersion.version <= AppUtils.getAppVerCode(App.AppContex)) return;
          }

          url = updateVersion.direct_install_url;
          newAkp = new File(getExternalCacheDir().getAbsolutePath()
            + getString(R.string.app_name)
            + "_"
            + updateVersion.version
            + ".apk");
          MaterialDialog.Builder builder =
            new MaterialDialog.Builder(Main2Activity.this).title("前方发现新版本!!")
              .content(updateVersion.changelog)
              .positiveText("更新")
              .autoDismiss(true)
              .onPositive((dialog, which) -> {
                if (url != null) {
                  downloadDialog.show();
                  mDownloadThread = new AsyncDownloader();
                  mDownloadThread.execute(url);
                }
                dialog.dismiss();
              });
          if (updateVersion.version % 10 != 0) {
            builder.negativeText("下次再说");
            builder.autoDismiss(false);
            builder.canceledOnTouchOutside(false);
            builder.onNegative((dialog, which) -> {
                dialog.dismiss();
            });
          }else {
            builder.autoDismiss(false);
            builder.canceledOnTouchOutside(false);
            builder.cancelable(false);
          }

          updateDialog = builder.build();
          downloadDialog = new MaterialDialog.Builder(Main2Activity.this).content("正在飞速为您下载")
            .progress(false, 100)
            .cancelable(false)
            .positiveText("后台更新")
            .callback(new MaterialDialog.ButtonCallback() {
              @Override public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
              }

              @Override public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                mDownloadThread.cancel(true);
              }
            })
            .build();
          updateDialog.show();
        }

        @Override public void onFail(Exception e) {
          super.onFail(e);
        }

        @Override public void onStart() {
          super.onStart();
        }

        @Override public void onFinish() {
          super.onFinish();
        }
      });
  }

  public void freshNotiCount(int count) {
    tvNotiCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    tvNotiCount.setText(count > 99 ? "…" : Integer.toString(count));

  }

  private void initUser() {

    String u = PreferenceUtils.getPrefString(this, "user_info", "");
    if (!TextUtils.isEmpty(u)) {
      User user = gson.fromJson(u, User.class);
      App.setgUser(user);
      try {
        JSONObject properties = new JSONObject();
        properties.put("qc_app_name", "Trainer");
        properties.put("qc_user_id", user.id);
        //properties.put("qc_user_phone", user.phone);
        SensorsDataAPI.sharedInstance(getApplicationContext()).registerSuperProperties(properties);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * create dir in SDcard
   */
  private void setupFile() {
    File fileCache = getExternalCacheDir();
    if (!fileCache.exists()) {
      fileCache.mkdir();
    }
    try {
      diskLruCache = DiskLruCache.open(fileCache, 1, 2000, 10000000);
    } catch (IOException e) {
      //TODO 没有存储的情况
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    if (intent != null && intent.getScheme() != null && intent.getScheme().equals("qccoach")) {
      try {
        String path = intent.getData().toString();
        url = path.split("openurl/")[1];
        showPage(2);
        if (!url.startsWith("http")) url = "http://" + url;
        WebActivity.startWeb(url, this);
      } catch (Exception e) {

      }
    }
    if (intent.getIntExtra(ACTION, -1) == LOGOUT) {
      //Deprecated

    } else if (intent.getIntExtra(ACTION, -1) == FINISH) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        finishAndRemoveTask();
      } else {
        finishAffinity();
      }
      System.exit(0);
    } else if (intent.getIntExtra(ACTION, -1) == NOTIFICATION) {
      String contetn = intent.getStringExtra("url");
      Intent toWeb = new Intent(this, WebActivity.class);
      toWeb.putExtra("url", contetn);
      startActivity(toWeb);
    } else if (intent.getIntExtra(ACTION, -1) == INIT) {

    }
  }

  public void install(Context context, File file) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    Uri uri = FileProvider.getUriForFile(context,
      context.getApplicationContext().getPackageName() + ".provider", file);
    grantUriPermission(getApplicationContext().getPackageName(), uri,
      Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    i.setDataAndType(uri, "application/vnd.android.package-archive");
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    context.startActivity(i);
  }

  private int[] mIconSelect = {
    R.drawable.ic_tabbar_schedule_active, R.drawable.ic_tabbar_manage_active,
    R.drawable.ic_tabbar_discover_active, R.drawable.vd_tab_noti_activte,
    R.drawable.ic_tabbar_account_active
  };
  private int[] mIconNormal = {
    R.drawable.ic_tabbar_schedule_normal, R.drawable.ic_tabbar_manage_normal,
    R.drawable.ic_tabbar_discover_normal, R.drawable.vd_tab_noti_nomal,
    R.drawable.ic_tabbar_account_normal
  };


  //private void showPage(int position) {
  //  Flowable.just(position)
  //    .throttleFirst(800, TimeUnit.MILLISECONDS)
  //    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
  //    .subscribe(pos -> {
  //      curPageIndex = pos;
  //      String[] tags = getResources().getStringArray(R.array.home_tab);
  //      FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
  //      Fragment f = fragments.get(tags[pos]);
  //      if (f == null) {
  //        f = generateFragment(pos);
  //        fragments.put(tags[pos],f);
  //      }
  //      ts.setCustomAnimations(R.anim.anim_fade_in,R.anim.anim_fade_out);
  //      if (f.isAdded())
  //        ts.remove(f);
  //      ts.replace(R.id.frag_main, f, tags[pos]);
  //      ts.commitNowAllowingStateLoss();
  //    },throwable -> {});
  //
  //}

  private void showPage(int pos) {
    String[] tags = getResources().getStringArray(R.array.home_tab);
    FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
    ts.setCustomAnimations(R.anim.anim_fade_in,R.anim.anim_fade_out);
    for (int i = 0; i < tags.length; i++) {
      Fragment f = getSupportFragmentManager().findFragmentByTag(tags[i]);
      if (i == pos) {
        if (f == null) {
          f = generateFragment(pos);
          ts.add(R.id.frag_main, f, tags[i]);
        } else ts.show(f);
      } else {
        if (f != null) ts.hide(f);
      }
    }
    ts.commit();
  }


  HashMap<String,Fragment> fragments = new HashMap<>();
  public Fragment generateFragment(int position){
    if (position == 0) {
      return new UnLoginHomeFragment();
    } else if (position == 1) {
      return new UnloginManageFragment();
    } else if (position == 2) {
      return MainWebFragment.newInstance(Configs.Server.replace("http","https") + "mobile/coach/discover/");
    } else if (position == 3) {
      return new MainMsgFragment();
    } else {
      return new MineFragmentFragment();
    }
  }






  /**
   * 新版本下载
   */

  private class AsyncDownloader extends AsyncTask<String, Long, Boolean> {

    @Override protected Boolean doInBackground(String... params) {
      OkHttpClient httpClient = new OkHttpClient();
      Call call = httpClient.newCall(new Request.Builder().url(params[0]).get().build());
      try {
        Response response = call.execute();
        if (response.code() == 200) {
          InputStream inputStream = null;
          OutputStream output = new FileOutputStream(newAkp);
          try {
            inputStream = response.body().byteStream();
            byte[] buff = new byte[1024 * 4];
            long downloaded = 0;
            long target = response.body().contentLength();

            publishProgress(0L, target);
            while (true) {
              int readed = inputStream.read(buff);
              if (readed == -1) {
                break;
              }
              output.write(buff, 0, readed);
              output.flush();
              //write buff
              downloaded += readed;
              publishProgress(downloaded, target);
              if (isCancelled()) {
                return false;
              }
            }
            //                        FileUtils.getFileFromBytes(response.body().bytes(), newAkp.getAbsolutePath());
            return downloaded == target;
          } catch (IOException ignore) {
            return false;
          } finally {
            if (inputStream != null) {
              inputStream.close();
            }
            if (output != null) {
              output.close();
            }
          }
        } else {
          return false;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    @Override protected void onProgressUpdate(Long... values) {
      downloadDialog.setProgress((int) (values[0] * 100 / values[1]));
    }

    @Override protected void onPostExecute(Boolean result) {
      if (result) {
        downloadDialog.dismiss();
        if (Build.VERSION.SDK_INT >= 24) {
          install(Main2Activity.this, newAkp);
        } else {
          AppUtils.install(Main2Activity.this, newAkp.getAbsolutePath());
        }
      } else {
        downloadDialog.dismiss();
        Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
