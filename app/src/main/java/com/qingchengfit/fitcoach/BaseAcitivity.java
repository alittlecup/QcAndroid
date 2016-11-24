package com.qingchengfit.fitcoach;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.AppCompatDelegate;
import com.qingchengfit.fitcoach.component.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

//import com.qingchengfit.fitcoach.di.ApplicationComponet;

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
public class BaseAcitivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(this);
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

}
