package com.qingchengfit.fitcoach.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.android.pushservice.PushManager;
import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.NetWorkUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.RevenUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.NetworkBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout;
import com.qingchengfit.fitcoach.component.DrawerModuleItem;
import com.qingchengfit.fitcoach.component.SegmentLayout;
import com.qingchengfit.fitcoach.fragment.DataStatementFragment;
import com.qingchengfit.fitcoach.fragment.MeetingFragment;
import com.qingchengfit.fitcoach.fragment.MyCoursePlanFragment;
import com.qingchengfit.fitcoach.fragment.MyGymsFragment;
import com.qingchengfit.fitcoach.fragment.MyStudentFragment;
import com.qingchengfit.fitcoach.fragment.OriginWebFragment;
import com.qingchengfit.fitcoach.fragment.ScheduesFragment;
import com.qingchengfit.fitcoach.fragment.WebFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.DrawerGuide;
import com.qingchengfit.fitcoach.http.bean.DrawerModule;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcDrawerResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.http.bean.User;
import com.qingchengfit.fitcoach.reciever.PushReciever;
import com.qingchengfit.fitcoach.server.CalendarIntentService;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import javax.inject.Inject;

public class MainActivity extends BaseAcitivity implements OpenDrawerInterface {

    public static final String ACTION = "main_action";
    public static final int LOGOUT = 0;
    public static final int FINISH = 2;
    public static final int NOTIFICATION = 1;
    private static final String TAG = MainActivity.class.getName();
    private static int ids[] = {
            R.id.segmentbtn_0,
            R.id.segmentbtn_1,
            R.id.segmentbtn_2,
            R.id.segmentbtn_3,
            R.id.segmentbtn_4,
            R.id.segmentbtn_5,
            R.id.segmentbtn_6,
            R.id.segmentbtn_7,
            R.id.segmentbtn_8,
            R.id.segmentbtn_9,
    };
    FragmentManager mFragmentManager;
    @Bind(R.id.main_drawerlayout)
    DrawerLayout mainDrawerlayout;
    @Bind(R.id.main_fraglayout)
    FrameLayout mainFraglayout;
    @Bind(R.id.drawer_headerview)
    RelativeLayout drawerHeaderview;
    @Bind(R.id.drawer_radiogroup)
    CustomSetmentLayout drawerRadiogroup;
    @Bind(R.id.header_icon)
    ImageView headerIcon;
    @Bind(R.id.drawer_modules)
    LinearLayout drawerModules;
    @Bind(R.id.drawer_name)
    TextView drawerName;
    AsyncDownloader mDownloadThread;
    HashMap<String, Fragment> fragments = new HashMap<>();
    private boolean mGoMyhome = false;
    private User user;
    private Fragment topFragment;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();
    private String pathOri;
    private MaterialDialog dialog;
    private Gson gson;
    private Observable mMainObservabel;
    private Observable mNetworkObservabel;
    private MaterialDialog updateDialog;
    private MaterialDialog downloadDialog;
    private String url;
    private File newAkp;

    private ScheduesFragment mScheduesFragment;
    private DataStatementFragment mDataStatementFragment;
    private MyStudentFragment mMyStudentFragment;
    private MeetingFragment mMeetingFragment;
    private MyGymsFragment mMyGymsFragment;
    private MyCoursePlanFragment mMyCoursePlanFragment;

    //    @Override
//    protected void onXWalkReady() {
//
//    }
    private MaterialDialog loadingDialog;
    private SegmentLayout button;
    private SegmentLayout button2;
    private SegmentLayout button3;
    private DrawerModuleItem item;
    private DrawerModuleItem item1;
    private DrawerModuleItem item2;
    private Snackbar NonetworkSnack;

    //    @Inject RxBus rxBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
//        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gson = new Gson();
        mFragmentManager = getSupportFragmentManager();
        mMainObservabel = RxBus.getBus().register(RxBus.OPEN_DRAWER);
        mMainObservabel.subscribe((Action1) o -> mainDrawerlayout.openDrawer(Gravity.LEFT));
        mNetworkObservabel = RxBus.getBus().register(NetworkBean.class.getName());
        mNetworkObservabel.subscribe(new Observer() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                if (o instanceof NetworkBean) {
                    if (NonetworkSnack == null) {
                        NonetworkSnack = Snackbar.make(mainFraglayout, "您的网络异常,请检查网络连接", Snackbar.LENGTH_INDEFINITE);
                    }
                    if (((NetworkBean) o).isAvaliable()) {
                        NonetworkSnack.dismiss();
                    } else NonetworkSnack.show();
                }

            }
        });


        initUser();
        initDialog();
        initDrawer();
        initVersion();
        initBDPush();
        initCalendar();

        App.gMainAlive = true;//main是否存活,为推送
        if (getIntent() != null && getIntent().getIntExtra(ACTION, -1) == NOTIFICATION) {
            String contetn = getIntent().getStringExtra("url");
            Intent toWeb = new Intent(this, WebActivity.class);
            toWeb.putExtra("url", contetn);
            startActivity(toWeb);
        }
    }

    private void initCalendar() {
        Long calendarid = PreferenceUtils.getPrefLong(this, "calendar_id", -1);
        long calendar_sync_time = PreferenceUtils.getPrefLong(this, "calendar_sync_time", 0);
        if (calendarid < 0) {
            calendarid = PhoneFuncUtils.insertCalendar(this);
            PreferenceUtils.setSettingLong(this, "calendar_id", calendarid);
        }
        LogUtil.e("sync time:"+new Date().getTime() +"   "+ calendar_sync_time );
        if (calendarid >= 0 && (new Date().getTime() - calendar_sync_time > DateUtils.HOUR_TIME)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("from_date", DateUtils.getServerDateDay(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 6);
            params.put("to_date", DateUtils.getServerDateDay(calendar.getTime()));
            LogUtil.e("sync calendar:");
            QcCloudClient.getApi().getApi.qcGetCoachSchedule(App.coachid, params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QcSchedulesResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(QcSchedulesResponse qcSchedulesResponse) {
                            CalendarIntentService.startActionWeek(MainActivity.this, new Date().getTime(), gson.toJson(qcSchedulesResponse));
                            LogUtil.e("save calendar!");
                            PreferenceUtils.setSettingLong(MainActivity.this, "calendar_sync_time", new Date().getTime());
                        }
                    });
        }
    }

    private void initBDPush() {
        if (!PreferenceUtils.getPrefBoolean(this, "hasPushId", false)) {
            String userid = PreferenceUtils.getPrefString(this, PushReciever.BD_USERLID, null);
            String channelid = PreferenceUtils.getPrefString(this, PushReciever.BD_CHANNELID, null);
            if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(channelid)) {
                PushBody pushBody = new PushBody();
                pushBody.channel_id = channelid;
                pushBody.push_id = userid;
                pushBody.device_type = "Android";
                QcCloudClient.getApi().postApi.qcPostPushId(App.coachid, pushBody)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<QcResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(QcResponse qcResponse) {
                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                    LogUtil.e("pushId:bundle");
                                    PreferenceUtils.setPrefBoolean(MainActivity.this, "hasPushId", true);
                                }
                            }
                        });
            } else {
                LogUtil.e("bdpush:empty");
            }
        } else {
            LogUtil.e("hasBunldle");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        QcCloudClient.getApi().getApi.qcGetCoach(App.coachid)
//                .subscribe(qcCoachRespone -> {
//                    runOnUiThread(() -> {
//                        Glide.with(App.AppContex)
//                                .load(qcCoachRespone.getData().getCoach().getAvatar())
//                                .asBitmap()
//                                .into(new CircleImgWrapper(headerIcon, App.AppContex));
//                        drawerName.setText(qcCoachRespone.getData().getCoach().getUsername());
//                    });
//                });


    }

    @Override
    protected void onStop() {
        super.onStop();
        mainDrawerlayout.closeDrawers();

    }

    @Override
    protected void onDestroy() {
        App.gMainAlive = false;
        super.onDestroy();
        if (mDownloadThread != null)
            mDownloadThread.cancel(true);
        RxBus.getBus().unregister(RxBus.OPEN_DRAWER, mMainObservabel);
        RxBus.getBus().unregister(NetworkBean.class.getName(), mNetworkObservabel);
    }

    /**
     * loading dialog
     */
//    private MaterialDialog loadingDialog;
    public void initLoadingDialog() {
//        View view = LayoutInflater.from(this).inflate(R.layout.loading_view, null);
//        ImageView img = (ImageView)view.findViewById(R.id.loading_img);
//        Glide.with(App.AppContex).load(R.drawable.ic_loading_gif).into(img);
//        loadingDialog = new MaterialDialog.Builder(this)
//                .customView(view,false)
//
//                .backgroundColor(getResources().getColor(R.color.transparent))
//                .autoDismiss(false)
//                .cancelable(false)
//                .build();
        loadingDialog = new MaterialDialog.Builder(this)
                .content("请稍后")
                .progress(true, 0)
                .build();

    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            initLoadingDialog();
        loadingDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void hideLoading() {
        if (loadingDialog.isShowing())
            loadingDialog.hide();
    }

    private void initVersion() {
        LogUtil.e("version:" + AppUtils.getAppVer(this));
        if (!NetWorkUtils.isNetworkAvailable(this)) {
            NonetworkSnack = Snackbar.make(mainFraglayout, "您的网络异常,请检查网络连接", Snackbar.LENGTH_INDEFINITE);
            NonetworkSnack.show();
        }
        FIR.checkForUpdateInFIR("f60e7b4d8b237b271ef3a7741372f220", new VersionCheckCallback() {
            @Override
            public void onSuccess(AppVersion appVersion, boolean b) {
                LogUtil.e(" fir:success" + appVersion);

                if (appVersion.getVersionCode() <= AppUtils.getAppVerCode(App.AppContex))
                    return;

                url = appVersion.getUpdateUrl();
                newAkp = new File(Configs.ExternalCache + getString(R.string.app_name) + "_" + appVersion.getVersionName() + ".apk");
                updateDialog = new MaterialDialog.Builder(MainActivity.this)
                        .title("前方发现新版本!!")
                        .content(appVersion.getChangeLog())
                        .positiveText("更新")
                        .negativeText("下次再说")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                updateDialog.dismiss();
                                if (url != null) {
                                    //TODO download app
                                    downloadDialog.show();
                                    mDownloadThread = new AsyncDownloader();
                                    mDownloadThread.execute(url);
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                updateDialog.dismiss();
                            }
                        })
                        .build();
                downloadDialog = new MaterialDialog.Builder(MainActivity.this)
                        .content("正在飞速为您下载")
                        .progress(false, 100)
                        .cancelable(false)
                        .positiveText("后台更新")
                        .negativeText("取消更新")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                mDownloadThread.cancel(true);
                            }
                        })
                        .build();
                updateDialog.show();
            }


            @Override
            public void onFail(Request request, Exception e) {
                super.onFail(request, e);
            }

            @Override
            public void onStart() {
                LogUtil.e(" fir:start");
            }

            @Override
            public void onFinish() {
                LogUtil.e(" fir:onFinish");
            }
        });
//        QcCloudClient.getApi().getApi.qcGetVersion()
//                .subscribe(new Observer<QcVersionResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.e("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.e("onError");
//                    }
//
//                    @Override
//                    public void onNext(QcVersionResponse qcVersionResponse) {
//                        if (qcVersionResponse.getData().getVersion().getAndroid().getRelease() > AppUtils.getAppVerCode(getApplication())) {
//
//
//                            url = qcVersionResponse.getData().getDownload().getAndroid();
//                            newAkp = new File(Configs.ExternalCache + getString(R.string.app_name) + "_" + qcVersionResponse.getData().getVersion().getAndroid().getVersion() + ".apk");
//                            if (!newAkp.exists()) {
//                                try {
//                                    boolean ret = newAkp.createNewFile();
//                                } catch (IOException e) {
//                                }
//                            }
//                            runOnUiThread(() -> {
//
//                            });
//
//                        }
//                    }
//                });

    }

    private void initUser() {

        String u = PreferenceUtils.getPrefString(this, "user_info", "");
        if (!TextUtils.isEmpty(u)) {
            user = gson.fromJson(u, User.class);
            App.setgUser(user);
            drawerName.setText(user.username);
        } else {
        }

        //初始化initCoach
        String id = PreferenceUtils.getPrefString(this, "coach", "");
        if (TextUtils.isEmpty(id)) {
            //TODO error
        }
        Coach coach = gson.fromJson(id, Coach.class);
        App.coachid = Integer.parseInt(coach.id);

        //获取用户拥有的系统
        QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QcCoachSystemResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcCoachSystemResponse qcCoachSystemResponse) {
                        if (qcCoachSystemResponse.status == ResponseResult.SUCCESS) {
                            if (qcCoachSystemResponse.date == null || qcCoachSystemResponse.date.systems == null ||
                                    qcCoachSystemResponse.date.systems.size() == 0) {
                                Intent intent = new Intent(MainActivity.this, FragActivity.class);
                                intent.putExtra("type", 3);
                                intent.putExtra("isNew", true);
                                startActivity(intent);
                            } else {
                                PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", gson.toJson(qcCoachSystemResponse));

                            }
                        } else if (qcCoachSystemResponse.error_code.equalsIgnoreCase(ResponseResult.error_no_login)) {
                            logout();
                        }
                    }
                });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra(ACTION, -1) == LOGOUT) {
            logout();
        } else if (intent.getIntExtra(ACTION, -1) == FINISH) {
            MainActivity.this.finish();

        } else if (intent.getIntExtra(ACTION, -1) == NOTIFICATION) {
            String contetn = intent.getStringExtra("url");
            Intent toWeb = new Intent(this, WebActivity.class);
            toWeb.putExtra("url", contetn);
            startActivity(toWeb);
        }
    }


    public void logout() {
        PreferenceUtils.setPrefBoolean(MainActivity.this, "hasPushId", false);
        PreferenceUtils.setPrefString(App.AppContex, "session_id", null);
        PushManager.stopWork(App.AppContex);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        removeCookies(cookieManager);
        PreferenceUtils.setPrefBoolean(this, "first", true);
        Intent logout = new Intent(this, SplashActivity.class);
        logout.putExtra("isRegiste", 0);
        startActivity(logout);
        this.finish();
    }

    private void removeCookies(CookieManager cookieManager) {
        String h = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "hostarray", "");
        if (!TextUtils.isEmpty(h)) {
            ArrayList<String> hosts = gson.fromJson(h, new TypeToken<ArrayList<String>>() {
            }.getType());
            for (String host : hosts) {
                LogUtil.e(host + "  " + cookieManager.getCookie(host));
                cookieManager.setCookie(host, "sessionid" + "=" + ";expires=Mon, 03 Jun 0000 07:01:29 GMT;");
                cookieManager.setCookie(host, "qc_session_id" + "=" + ";expires=Mon, 03 Jun 0000 07:01:29 GMT;");
                LogUtil.e(host + "  " + cookieManager.getCookie(host));
            }

        }
        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "hostarray", "");
    }


    public void initDialog() {
        dialog = new MaterialDialog.Builder(this)
                .content("是否确认退出?")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .positiveText("退出")
                .negativeText("不要")
                .build();

    }


    /**
     * 初始化侧滑,从后台拉去
     */
    private void initDrawer() {


        mScheduesFragment = new ScheduesFragment();
        mDataStatementFragment = new DataStatementFragment();
        mMyStudentFragment = new MyStudentFragment();
//        mMeetingFragment = MainWebFragment.newInstance("http://teacher.qingchengfit.cn/meetings/");
        mMeetingFragment = new MeetingFragment();
        mMyGymsFragment = new MyGymsFragment();
        mMyCoursePlanFragment = new MyCoursePlanFragment();

        button = new SegmentLayout(this);
        button.setText("日程安排");
        button.setId(ids[0]);
        button.setDrawables(R.drawable.ic_drawer_schedule_normal, R.drawable.ic_drawer_schedule_checked);
        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        button2 = new SegmentLayout(this);
        button2.setId(ids[1]);
        button2.setText("数据报表");
        button2.setDrawables(R.drawable.ic_drawer_statistic_normal, R.drawable.ic_drawer_statistic_checked);
        drawerRadiogroup.addView(button2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        button3 = new SegmentLayout(this);
        button3.setText("会议培训");
        button3.setId(ids[2]);
        button3.setDrawables(R.drawable.ic_drawer_meeting_normal, R.drawable.ic_drawer_meeting_checked);
        drawerRadiogroup.addView(button3, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));

        button.setListener(v -> {
            changeFragment(mScheduesFragment);
        });

        button2.setListener(v -> {
            changeFragment(mDataStatementFragment);
        });

        button3.setListener(v1 -> changeFragment(mMeetingFragment));

        button.performClick();
        item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item.setTitle("我的学员");
        item.setCount("0");
        drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        item1 = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item1.setTitle(getString(R.string.my_course_template));
        item1.setCount("0");
        drawerModules.addView(item1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        item2 = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item2.setTitle("我的健身房");
        item2.setCount("0");
        drawerModules.addView(item2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        item.setOnClickListener(v -> {
            changeFragment(mMyStudentFragment);
            drawerRadiogroup.cleanCheck();
        });
        item1.setOnClickListener(v -> {
            changeFragment(mMyCoursePlanFragment);
            drawerRadiogroup.cleanCheck();
        });
        item2.setOnClickListener(v -> {
            changeFragment(mMyGymsFragment);
            drawerRadiogroup.cleanCheck();
        });
        mainDrawerlayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                QcCloudClient.getApi().getApi.qcGetDrawerInfo(App.coachid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<QcDrawerResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.show(R.drawable.ic_share_fail, "网络错误");
                            }

                            @Override
                            public void onNext(QcDrawerResponse qcDrawerResponse) {
                                Glide.with(App.AppContex).load(qcDrawerResponse.data.coach.avatar).asBitmap().into(new CircleImgWrapper(headerIcon, App.AppContex));
                                drawerName.setText(qcDrawerResponse.data.coach.username);
                                item.setCount(qcDrawerResponse.data.user_count);
                                item1.setCount(qcDrawerResponse.data.plan_count);
                                item2.setCount(qcDrawerResponse.data.system_count);
                                PreferenceUtils.setPrefString(App.AppContex, App.coachid + "drawer_info", new Gson().toJson(qcDrawerResponse));

                            }
                        });

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mGoMyhome) {
//                    startActivityForResult(new Intent(MainActivity.this, MyHomeActivity.class), 9);
                    mGoMyhome = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        /**
         * load cache
         */
        String cache = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "drawer_info", "");
        if (!TextUtils.isEmpty(cache)) {
            QcDrawerResponse qcDrawerResponse = gson.fromJson(cache, QcDrawerResponse.class);
            Glide.with(App.AppContex).load(qcDrawerResponse.data.coach.avatar).asBitmap().into(new CircleImgWrapper(headerIcon, App.AppContex));
            drawerName.setText(qcDrawerResponse.data.coach.username);
            item.setCount(qcDrawerResponse.data.user_count);
            item1.setCount(qcDrawerResponse.data.plan_count);
            item2.setCount(qcDrawerResponse.data.system_count);
            PreferenceUtils.setPrefString(App.AppContex, App.coachid + "drawer_info", new Gson().toJson(qcDrawerResponse));
        }
    }

    public void changeFragment(Fragment fragment) {
        mainDrawerlayout.closeDrawers();
        if (topFragment != fragment) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mFragmentManager.getFragments() == null || !mFragmentManager.getFragments().contains(fragment))
                fragmentTransaction.add(R.id.main_fraglayout, fragment);
            if (topFragment != null)
                fragmentTransaction.hide(topFragment);

            fragmentTransaction.show(fragment);
            fragmentTransaction.commitAllowingStateLoss();
            topFragment = fragment;
        }
    }

    public void guideTo(String url) {
        if (TextUtils.isEmpty(url)) {
            if (drawerRadiogroup.getChildCount() > 0) {
                runOnUiThread(() -> {
                    drawerRadiogroup.getChildAt(0).performClick();
                });
            }
        } else {
            goXwalkfragment(url, "");
        }
    }


    @UiThread
    private void setupModules(List<DrawerModule> modules) {
        for (DrawerModule module : modules) {
            DrawerModuleItem item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
            item.setTitle(module.title);
            item.setCount(module.text);
            item.setOnClickListener(view -> {
                goXwalkfragment(module.url, null);
                mainDrawerlayout.closeDrawers();
            });
            urls.add(module.url);
            drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        }
    }

    public void setupBtn(DrawerGuide btnInfo, int count) {
        SegmentLayout button = new SegmentLayout(this);

        List<Drawable> drawables = new ArrayList<>();
        Observable.just(btnInfo.drawableOff, btnInfo.drawableOn)
                .flatMap(s -> {
                    File f = new File(Configs.ExternalPath, s.substring(s.length() - 20, s.length()));
                    if (!f.exists()) {
//                        Response response = QcCloudClient.getApi().downLoadApi
//                                .qcDownload(s);
                        OkHttpClient httpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(s).build();

                        try {
                            Response response = httpClient.newCall(request).execute();
                            FileUtils.getFileFromBytes(response.body().bytes(), f.getAbsolutePath());
//                            FileOutputStream output = new FileOutputStream(f);
//                            IOUtils.write(response.body().bytes(), output);
//                            FileUtils.copyInputStreamToFile(response.body().byteStream(),f);

                        } catch (FileNotFoundException e) {
                            RevenUtils.sendException("initDrawer", TAG, e);
                        } catch (IOException e) {
                            RevenUtils.sendException("initDrawer", TAG, e);
                        }
                    }
                    return Observable.just(f.getAbsolutePath());
                })
                .flatMap(s2 -> {
                            drawables.add(Drawable.createFromPath(s2));
                            return Observable.just("");
                        }
                )
                .last()
                .subscribe(s1 -> {
                    runOnUiThread(() -> {
//                        StateListDrawable drawable1 = DynamicSelector.getSelector(drawables.get(0), drawables.get(1));
                        button.setText(btnInfo.guideText);
                        button.setDrawables(drawables.toArray(new Drawable[2]));
//                        button.setButtonDrawable(drawable1);
//                        button.setPadding(MeasureUtils.dpToPx(15f, getResources()), 0, 0, 0);
                        button.setId(View.generateViewId());
                        button.setListener(view -> {
                            mainDrawerlayout.closeDrawer(Gravity.LEFT);
                            goXwalkfragment(btnInfo.intentUrl, null);
                        });
                        urls.add(btnInfo.intentUrl);
//                        fragments.put( btnInfo.intentUrl,  WebFragment.newInstance(btnInfo.intentUrl));
                        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));

                        if (count == 0) {
//                            drawerRadiogroup.getChildAt(0).performClick();
                            drawerRadiogroup.check(0);
                            Fragment fragment = OriginWebFragment.newInstance(btnInfo.intentUrl);
                            mFragmentManager.beginTransaction().replace(R.id.main_fraglayout, fragment, btnInfo.intentUrl)
                                    .commit();
                            topFragment = fragment;
                        }
                    });

                })
        ;
    }

    @OnClick(R.id.drawer_headerview)
    public void onHeadClick() {
//        mGoMyhome = true;
//        mainDrawerlayout.closeDrawers();
        startActivityForResult(new Intent(MainActivity.this, MyHomeActivity.class), 9);
    }

    public void goXwalkfragment(String url, String from) {

        WebFragment fragment = (WebFragment) mFragmentManager.findFragmentByTag("one");
        FragmentTransaction fr = mFragmentManager.beginTransaction();
        if (fragment == null) {
            fragment = WebFragment.newInstance(url);
            fr.replace(R.id.main_fraglayout, fragment, "one");
            fr.commit();
            topFragment = fragment;
        } else {
            ((OriginWebFragment) fragment).startLoadUrl(url);
        }

    }


    @Override
    public void onBackPressed() {
        if (topFragment != mScheduesFragment) {
//            changeFragment(mScheduesFragment);
            button.performClick();
        } else
            dialog.show();
    }

    @Override
    public void onOpenDrawer() {
        mainDrawerlayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void goWeb(String url) {
        Intent toWeb = new Intent(this, WebActivity.class);
        toWeb.putExtra("url", url);
        startActivity(toWeb);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode > 0) {
        switch (resultCode) {
            case 1:
//                    changeFragment(mScheduesFragment);
                button.performClick();
                break;
            case 2:
//                    changeFragment(mDataStatementFragment);
                button2.performClick();
                break;
            case 3:
//                    changeFragment(mMeetingFragment);
                button3.performClick();
                break;
            case 4:
                item.performClick();
//                    changeFragment(mMyStudentFragment);
                break;
            case 5:
                item1.performClick();
//                    changeFragment(mMyCoursePlanFragment);
                break;
            case 6:
                item2.performClick();
//                    changeFragment(mMyGymsFragment);
                break;
            default:
//                    changeFragment(mScheduesFragment);
                break;


//            }
        }
    }

    /**
     * 新版本下载
     */

    private class AsyncDownloader extends AsyncTask<String, Long, Boolean> {
//        private final String URL = "file_url";

        @Override
        protected Boolean doInBackground(String... params) {
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

        @Override
        protected void onProgressUpdate(Long... values) {
            downloadDialog.setProgress((int) (values[0] * 100 / values[1]));

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                downloadDialog.dismiss();
                AppUtils.install(MainActivity.this, newAkp.getAbsolutePath());
                //TODO 安装应用
            } else {
                downloadDialog.dismiss();
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
