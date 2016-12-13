package com.qingchengfit.fitcoach;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import cn.qingchengfit.widgets.utils.StringUtils;
import cn.qingchengfit.widgets.utils.ToastUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.activity.LoadResActivity;
import com.qingchengfit.fitcoach.component.DiskLruCache;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.qingchengfit.fitcoach.http.bean.User;
import im.fir.sdk.FIR;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import retrofit.http.HEAD;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;


//import com.qingchengfit.fitcoach.di.ApplicationComponet;

//import com.qingchengfit.fitcoach.di.ApplicationModule;

//import com.qingchengfit.fitcoach.di.ApplicationComponet;

//import rx.plugins.RxJavaErrorHandler;

//import com.qingchengfit.fitcoach.di.ApplicationComponet;
//import com.qingchengfit.fitcoach.di.ApplicationModule;
//import com.qingchengfit.fitcoach.di.DaggerApplicationComponet;


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
public class App extends Application {
    public static Context AppContex;
    public static boolean canXwalk;
    public static boolean gMainAlive = false;
    public static User gUser;
    public static int coachid;
    public static DiskLruCache diskLruCache;
    public static boolean gCanReload = false;

    //public static String staffId = "53";
    //    private ApplicationComponet componet;
//    private RefWatcher refWatcher;
    private String KEY_DEX2_SHA1 = "XXDSDSFHALJFDKLASF";
//    public ApplicationComponet getComponet() {
//        if (componet != null)
//            return componet;
//        else return null;
//    }

    public static void setgUser(User ser) {
        gUser = ser;
    }

    //
//    public void setComponet(ApplicationComponet componet) {
//        this.componet = componet;
//    }
//    public static RefWatcher getRefWatcher() {
//        App application = (App) App.AppContex;
//        return application.refWatcher;
//    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    //
    @Override
    public void onCreate() {
        FIR.init(this);
        super.onCreate();
        MultiDex.install(this);
//        LeakCanary.install(this);
        AppContex = getApplicationContext();
//        refWatcher = LeakCanary.install(this);
        if (!BuildConfig.DEBUG)
            CrashHandler.getInstance().init(this);
        ToastUtils.init(this);
        Configs.APP_ID = getString(R.string.wechat_code);
        String id = PreferenceUtils.getPrefString(this, "coach", "");
        if (TextUtils.isEmpty(id)) {
        } else {
            Coach coach = new Gson().fromJson(id, Coach.class);
            App.coachid = Integer.parseInt(coach.id);
        }

        ToastUtils.init(this);
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                if (e != null) {
                    LogUtil.e("rxError:" + e.getMessage() + e.getCause());
                    e.printStackTrace();
                }
            }
        });

//        DefaultAcceptConfiguration.getInstance().registerAcceptConfiguration(new DefaultAcceptConfiguration.OnDefaultAcceptConfiguration() {
//            @Override
//            public Executor applyAcceptExecutor() {
//                return acceptExecutor;
//            }
//
//            @Override
//            public Handler applyAcceptHandler() {
//                return handler;
//            }
//        });


    }

    private void setupWebView() {
        try {
            System.loadLibrary("xwalkcore");
            canXwalk = false;
        } catch (Error e) {
            canXwalk = false;
        }
    }


    //    public RefWatcher getRefWatcher(Context context) {
//        return refWatcher;
//    }
//
//    private  RefWatcher refWatcher;
//
    private void setupGraph() {

//        componet = DaggerApplicationComponet.builder()
//                .rxBusModule(new RxBusModule())
//                .applicationModule(new ApplicationModule(this))
//                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        if (!quickStart()) {
//            if (needWait(base)){
//                waitForDexopt(base);
//            }
//            MultiDex.install(this);
//        } else {
//            return;
//        }
    }

    public boolean quickStart() {
        if (StringUtils.contains(getCurProcessName(this), ":mini")) {
            LogUtil.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        LogUtil.d("loadDex", "dex2-sha1 " + flag);
        SharedPreferences sp = context.getSharedPreferences(
                AppUtils.getAppVer(context), MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !StringUtils.equals(flag, saveValue);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                AppUtils.getAppVer(context), MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        ComponentName componentName = new
                ComponentName("com.zongwu", LoadResActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                LogUtil.d("loadDex", "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void finishActivity() {

        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
