package com.qingchengfit.fitcoach.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.TabView;
import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.NetWorkUtils;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.NetworkBean;
import com.qingchengfit.fitcoach.bean.UpdateVersion;
import com.qingchengfit.fitcoach.fragment.WebFragment;
import com.qingchengfit.fitcoach.fragment.manage.ManageFragment;
import com.qingchengfit.fitcoach.fragment.mine.MineFragmentFragment;
import com.qingchengfit.fitcoach.fragment.schedule.MainScheduleFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.http.bean.User;
import com.qingchengfit.fitcoach.reciever.PushReciever;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Main2Activity extends BaseAcitivity implements WebActivityInterface {

    /**
     * 进入主页用途
     */
    public static final String ACTION = "main_action"; //key
    public static final int LOGOUT = 0;
    public static final int FINISH = 2;
    public static final int NOTIFICATION = 1;


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tabview)
    TabView tabview;

    /**
     * 退出弹窗提示
     */
    MaterialDialog logoutDialog;
    private Gson gson;
    private User user;

    private Snackbar NonetworkSnack;
    private Observable mMainObservabel;
    private Observable mNetworkObservabel;
    private MaterialDialog updateDialog;
    private MaterialDialog downloadDialog;
    private String url;
    private File newAkp;
    AsyncDownloader mDownloadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setupVp();
        gson = new Gson();
        logoutDialog = new MaterialDialog.Builder(this)
                .autoDismiss(true)
                .content("退出应用？")
                .positiveText("退出")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Main2Activity.this.finish();
                    }
                })
                .build();
        initUser();

        initBDPush();

        App.gMainAlive = true;//main是否存活,为推送
        if (getIntent() != null && getIntent().getIntExtra(ACTION, -1) == NOTIFICATION) {
            String contetn = getIntent().getStringExtra("url");
            Intent toWeb = new Intent(this, WebActivity.class);
            toWeb.putExtra("url", contetn);
            startActivity(toWeb);
        }

    }

    private void setupVp() {
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
        tabview.setViewPager(viewpager);
    }

    @Override
    public void onfinish() {

    }


    public class MainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
        private int[] mIconSelect = {R.drawable.ic_tabbar_schedule_active
                , R.drawable.ic_tabbar_manage_active
                , R.drawable.ic_tabbar_discover_active
                , R.drawable.ic_tabbar_account_active
        };
        private int[] mIconNormal = {R.drawable.ic_tabbar_schedule_normal
                , R.drawable.ic_tabbar_manage_normal
                , R.drawable.ic_tabbar_discover_normal
                , R.drawable.ic_tabbar_account_normal
        };
        private Context context;

        MainPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MainScheduleFragment();
            } else if (position == 1) {
                return new ManageFragment();
            } else if (position == 2) {
                return WebFragment.newInstance("http://www.baidu.com");
            } else
                return new MineFragmentFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int[] onIconSelect(int position) {
            int icon[] = new int[2];
            icon[0] = mIconSelect[position];
            icon[1] = mIconNormal[position];
            return icon;
        }

        @Override
        public String onTextSelect(int position) {
            return context.getResources().getStringArray(R.array.home_tab)[position];
        }

    }

    public void logout() {
        PreferenceUtils.setPrefBoolean(Main2Activity.this, "hasPushId", false);
        PreferenceUtils.setPrefString(App.AppContex, "session_id", null);
        PushManager.stopWork(App.AppContex);
        PreferenceUtils.setPrefBoolean(this, "first", true);
        Intent logout = new Intent(this, SplashActivity.class);
        logout.putExtra("isRegiste", 0);
        startActivity(logout);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (!logoutDialog.isShowing()) {
            logoutDialog.show();
        }

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


    private void initBDPush() {
        String userid = PreferenceUtils.getPrefString(this, PushReciever.BD_USERLID, null);
        String channelid = PreferenceUtils.getPrefString(this, PushReciever.BD_CHANNELID, null);
        if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(channelid)) {
            PushBody pushBody = new PushBody();
            pushBody.push_channel_id = channelid;
            pushBody.push_id = userid;
            pushBody.device_type = "android";
            pushBody.distribute = getString(R.string.oem_tag);
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
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                UpdateVersion updateVersion = new Gson().fromJson(s, UpdateVersion.class);
                if (BuildConfig.DEBUG) {
                    long oldupdate = PreferenceUtils.getPrefLong(Main2Activity.this, "update", 0);
                    if (updateVersion.updated_at <= oldupdate) {
                        return;
                    }
                    PreferenceUtils.setPrefLong(Main2Activity.this, "update", updateVersion.updated_at);

                } else {
                    if (updateVersion.version <= AppUtils.getAppVerCode(App.AppContex))
                        return;
                }

                url = updateVersion.direct_install_url;
                newAkp = new File(Configs.ExternalCache + getString(R.string.app_name) + "_" + updateVersion.version + ".apk");
                updateDialog = new MaterialDialog.Builder(Main2Activity.this)
                        .title("前方发现新版本!!")
                        .content(updateVersion.changelog)
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
                                    mDownloadThread = new Main2Activity.AsyncDownloader();
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
                downloadDialog = new MaterialDialog.Builder(Main2Activity.this)
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
            public void onFail(Exception e) {
                super.onFail(e);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    private void initUser() {

        String u = PreferenceUtils.getPrefString(this, "user_info", "");
        if (!TextUtils.isEmpty(u)) {
            user = gson.fromJson(u, User.class);
            App.setgUser(user);
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
                                Intent intent = new Intent(Main2Activity.this, FragActivity.class);
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
            Main2Activity.this.finish();

        } else if (intent.getIntExtra(ACTION, -1) == NOTIFICATION) {
            String contetn = intent.getStringExtra("url");
            Intent toWeb = new Intent(this, WebActivity.class);
            toWeb.putExtra("url", contetn);
            startActivity(toWeb);
        }
    }


    /**
     * 新版本下载
     */

    private class AsyncDownloader extends AsyncTask<String, Long, Boolean> {

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
                AppUtils.install(Main2Activity.this, newAkp.getAbsolutePath());
            } else {
                downloadDialog.dismiss();
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
