package com.qingchengfit.fitcoach.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.Window;
import android.view.WindowManager;
import cn.qingchengfit.utils.LogUtil;
import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/10/21 2015.
 */
public class LoadResActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.activity_splash);
        new LoadDexTask().execute();
    }

    @Override public void onBackPressed() {
        //cannot backpress
    }

    class LoadDexTask extends AsyncTask {
        @Override protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                LogUtil.d("loadDex", "install finish");
                //((App) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                LogUtil.e("loadDex", e.getLocalizedMessage());
            }
            return null;
        }

        @Override protected void onPostExecute(Object o) {
            LogUtil.d("loadDex", "get install finish");
            finish();
            System.exit(0);
        }
    }
}