package com.qingchengfit.fitcoach;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qingchengfit.fitcoach.di.ApplicationComponet;
import com.qingchengfit.fitcoach.di.ApplicationModule;
import com.qingchengfit.fitcoach.di.DaggerApplicationComponet;
import com.qingchengfit.fitcoach.di.RxBusModule;


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
        setupGraph();
    }

    private void setupGraph() {

        componet = DaggerApplicationComponet.builder()
                .rxBusModule(new RxBusModule())
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
