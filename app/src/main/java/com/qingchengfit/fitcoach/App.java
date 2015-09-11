package com.qingchengfit.fitcoach;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qingchengfit.fitcoach.di.ApplicationComponet;
import com.qingchengfit.fitcoach.di.ApplicationModule;
import com.qingchengfit.fitcoach.di.DaggerApplicationComponet;
import com.qingchengfit.fitcoach.di.RxBusModule;

import java.io.File;

import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;


/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/7/29 2015.
 */
public class App extends Application {
    public static Context AppContex;
    public static boolean canXwalk;
    private ApplicationComponet componet;

    public ApplicationComponet getComponet() {
        if (componet != null)
            return componet;
        else return null;
    }

//
public void setComponet(ApplicationComponet componet) {
    this.componet = componet;
}
//
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fresco.initialize(this);
//        CrashHandler.getInstance().init(this);
        setupFile();
//        setupGraph();
        setupWebView();

        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                Log.w("Error", e);
            }
        });
        AppContex = getApplicationContext();


    }

    private void setupWebView() {
        try {
            System.loadLibrary("xwalkcore");
            canXwalk = true;
        } catch (Error e) {
            canXwalk = false;
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
    }

    private void setupGraph() {

        componet = DaggerApplicationComponet.builder()
                .rxBusModule(new RxBusModule())
                .applicationModule(new ApplicationModule(this))
                .build();
    }


}
