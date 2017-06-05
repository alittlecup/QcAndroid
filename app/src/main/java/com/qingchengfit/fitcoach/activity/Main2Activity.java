package com.qingchengfit.fitcoach.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.NetWorkUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.BaseAcitivity;
import cn.qingchengfit.widgets.GuideWindow;
import cn.qingchengfit.widgets.TabView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.NetworkBean;
import com.qingchengfit.fitcoach.bean.UpdateVersion;
import com.qingchengfit.fitcoach.component.DiskLruCache;
import com.qingchengfit.fitcoach.event.EventInit;
import com.qingchengfit.fitcoach.fragment.main.MainMsgFragment;
import com.qingchengfit.fitcoach.fragment.main.MainWebFragment;
import com.qingchengfit.fitcoach.fragment.mine.MineFragmentFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnLoginHomeFragment;
import com.qingchengfit.fitcoach.fragment.unlogin.UnloginManageFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.reciever.PushReciever;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.tbruyelle.rxpermissions.RxPermissions;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.inject.Inject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.qingchengfit.fitcoach.App.diskLruCache;
import static com.qingchengfit.fitcoach.http.QcCloudClient.getApi;

public class Main2Activity extends BaseAcitivity implements WebActivityInterface {

    /**
     * 进入主页用途
     */
    public static final String ACTION = "main_action"; //key
    public static final int LOGOUT = 0;
    public static final int FINISH = 2;
    public static final int NOTIFICATION = 1;
    public static final int INIT = 3;
    public int ordersCount;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.tabview) TabView tabview;
    /**
     * 退出弹窗提示
     */
    MaterialDialog logoutDialog;
    @BindView(R.id.order_studnet) View orderStudnet;
    @BindView(R.id.web_position) View webPosition;
    @BindView(R.id.tv_noti_count) TextView tvNotiCount;
    @Inject LoginStatus loginStatus;
    @Inject RepoCoachServiceImpl repoCoachService;
    AsyncDownloader mDownloadThread;
    private Gson gson;
    private Date mChooseDate;
    private GuideWindow gw;
    private Subscription spOrders;
    private Observable<EventInit> obPopWinEvent;
    private Snackbar NonetworkSnack;
    private Observable mMainObservabel;
    private Observable mNetworkObservabel;
    private MaterialDialog updateDialog;
    private MaterialDialog downloadDialog;
    private String url;
    private File newAkp;
    private int mGwShowNum = 0;
    private boolean isShowFindRed = true;

    public Date getChooseDate() {
        return mChooseDate;
    }

    public void setChooseDate(Date chooseDate) {
        mChooseDate = chooseDate;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override public void call(Boolean aBoolean) {
                if (aBoolean) {
                    setupFile();
                    initVersion();
                } else {
                    ToastUtils.showDefaultStyle("请开启存储空间权限");
                }
            }
        });

        RxPermissions.getInstance(this)
            .request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR)
            .subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean aBoolean) {

                }
            });

        setupVp();
        gson = new Gson();
        logoutDialog = new MaterialDialog.Builder(this).autoDismiss(true)
            .content("退出应用？")
            .positiveText("退出")
            .negativeText("取消")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Main2Activity.this.finish();
                }
            })
            .build();
        changeLogin();

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
                            if (tabview != null && tabview.getChildCount() > 1) {
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
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                if (position != 0 && gw != null) {
                    gw.dismiss();
                    PreferenceUtils.setPrefBoolean(Main2Activity.this, "guide_3", true);
                }

                if (position == 2) {
                    tabview.setPointStatu(2, false);
                    isShowFindRed = false;
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(viewpager.getViewTreeObserver(), this);
            }
        });

        if (getIntent() != null && getIntent().getScheme() != null && getIntent().getScheme().equals("qccoach")) {
            try {
                String path = getIntent().getData().toString();
                url = path.split("openurl/")[1];
                viewpager.setCurrentItem(2);
                if (!url.startsWith("http")) url = "http://" + url;
                WebActivity.startWeb(url, this);
            } catch (Exception e) {

            }
        }
    }

    public void changeLogin() {

    }

    private void setupVp() {
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), Main2Activity.this));
        tabview.setViewPager(viewpager);
        //默认设置发现页面有红点
        tabview.setPointStatu(2, isShowFindRed);
    }

    @Override public void onfinish() {

    }

    public int getCurrrentPage() {
        if (viewpager != null) {
            return viewpager.getCurrentItem();
        } else {
            return -1;
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (loginStatus.isLogined()) {
            if (spOrders != null && !spOrders.isUnsubscribed()) spOrders.unsubscribe();
            spOrders = QcCloudClient.getApi().getApi.qcGetOrderList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(qcResponsePage -> {
                    tabview.setPointStatu(4, (!PreferenceUtils.getPrefBoolean(this, App.coachid + "_has_show_orders", false)
                        && qcResponsePage.data.total_count > 0));
                    ordersCount = qcResponsePage.data.total_count;
                });
        }
    }

    @Override public void onBackPressed() {
        if (viewpager != null && viewpager.getCurrentItem() > 0) {
            viewpager.setCurrentItem(0);
            return;
        }

        if (!logoutDialog.isShowing()) {
            logoutDialog.show();
        }
    }

    @Override protected void onDestroy() {
        App.gMainAlive = false;
        super.onDestroy();
        if (mDownloadThread != null) mDownloadThread.cancel(true);
        RxBus.getBus().unregister(RxBus.OPEN_DRAWER, mMainObservabel);
        RxBus.getBus().unregister(NetworkBean.class.getName(), mNetworkObservabel);
        RxBus.getBus().unregister(EventInit.class.getName(), obPopWinEvent);
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
            getApi().postApi.qcPostPushId(App.coachid, pushBody).subscribeOn(Schedulers.io()).subscribe(new Subscriber<QcResponse>() {
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
            NonetworkSnack = Snackbar.make(viewpager, "您的网络异常,请检查网络连接", Snackbar.LENGTH_INDEFINITE);
            NonetworkSnack.show();
        }

        FIR.checkForUpdateInFIR(getString(BuildConfig.DEBUG ? R.string.fir_token_debug : R.string.fir_token), new VersionCheckCallback() {
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
                newAkp = new File(Configs.ExternalCache + getString(R.string.app_name) + "_" + updateVersion.version + ".apk");
                MaterialDialog.Builder builder = new MaterialDialog.Builder(Main2Activity.this).title("前方发现新版本!!")
                    .content(updateVersion.changelog)
                    .positiveText("更新")
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (url != null) {
                                downloadDialog.show();
                                mDownloadThread = new AsyncDownloader();
                                mDownloadThread.execute(url);
                            }
                        }
                    });
                if (updateVersion.version % 10 != 0) {
                    builder.negativeText("下次再说");
                    builder.autoDismiss(false);
                    builder.canceledOnTouchOutside(false);
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
            //// TODO: 2017/5/18 神策数据追踪需要修改
            User user = gson.fromJson(u, User.class);
            App.setgUser(user);
            try {
                JSONObject properties = new JSONObject();
                properties.put("qc_app_name", "Trainer");
                properties.put("qc_user_id", user.id);
                properties.put("qc_user_phone", user.phone);
                SensorsDataAPI.sharedInstance(getApplicationContext()).registerSuperProperties(properties);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create dir in SDcard
     */
    private void setupFile() {
        File file = new File(Configs.ExternalPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File fileCache = new File(Configs.ExternalCache);
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
                viewpager.setCurrentItem(2);
                if (!url.startsWith("http")) url = "http://" + url;
                WebActivity.startWeb(url, this);
            } catch (Exception e) {

            }
        }
        if (intent.getIntExtra(ACTION, -1) == LOGOUT) {//Deprecated
        } else if (intent.getIntExtra(ACTION, -1) == FINISH) {
            Main2Activity.this.finish();
        } else if (intent.getIntExtra(ACTION, -1) == NOTIFICATION) {
            String contetn = intent.getStringExtra("url");
            Intent toWeb = new Intent(this, WebActivity.class);
            toWeb.putExtra("url", contetn);
            startActivity(toWeb);
        } else if (intent.getIntExtra(ACTION, -1) == INIT) {
            // TODO: 2017/5/17 初始化
            //mCoachService = intent.getParcelableExtra("service");
            //if (viewpager != null && viewpager.getAdapter() != null && viewpager.getAdapter().getCount() > 2) {
            //    viewpager.setCurrentItem(1);
            //    if (viewpager.getAdapter() instanceof MainPagerAdapter) {
            //        if (((MainPagerAdapter) viewpager.getAdapter()).getItem(1) instanceof ManageFragment) {
            //            ((ManageFragment) ((MainPagerAdapter) viewpager.getAdapter()).getItem(1)).setCoachService(mCoachService);
            //            ((ManageFragment) ((MainPagerAdapter) viewpager.getAdapter()).getItem(1)).initView();
            //            ((ManageFragment) ((MainPagerAdapter) viewpager.getAdapter()).getItem(1)).getServer();
            //        }
            //    }
            //}
        }
    }

    public void install(Context context, File file) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        grantUriPermission(getApplicationContext().getPackageName(), uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(i);
    }

    public class MainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
        private int[] mIconSelect = {
            R.drawable.ic_tabbar_schedule_active, R.drawable.ic_tabbar_manage_active, R.drawable.ic_tabbar_discover_active,
            R.drawable.vd_tab_noti_activte, R.drawable.ic_tabbar_account_active
        };
        private int[] mIconNormal = {
            R.drawable.ic_tabbar_schedule_normal, R.drawable.ic_tabbar_manage_normal, R.drawable.ic_tabbar_discover_normal,
            R.drawable.vd_tab_noti_nomal, R.drawable.ic_tabbar_account_normal
        };
        private Context context;

        MainPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override public Fragment getItem(int position) {
            if (position == 0) {
                return new UnLoginHomeFragment();
            } else if (position == 1) {
                return new UnloginManageFragment();
            } else if (position == 2) {
                return MainWebFragment.newInstance(Configs.Server + "mobile/coach/discover/");
            } else if (position == 3) {
                return new MainMsgFragment();
            } else {
                return new MineFragmentFragment();
            }
        }

        @Override public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override public int getCount() {
            return 5;
        }

        @Override public int[] onIconSelect(int position) {
            int icon[] = new int[2];
            icon[0] = mIconSelect[position];
            icon[1] = mIconNormal[position];
            return icon;
        }

        @Override public String onTextSelect(int position) {
            return context.getResources().getStringArray(R.array.home_tab)[position];
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
