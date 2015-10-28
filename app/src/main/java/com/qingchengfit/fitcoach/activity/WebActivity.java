package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.OriginWebFragment;

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
 * Created by Paper on 15/10/12 2015.
 */
public class WebActivity extends BaseAcitivity implements WebActivityInterface {
    OriginWebFragment originWebFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            originWebFragment = (OriginWebFragment) OriginWebFragment.newInstance(url);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.web_frag_layout, originWebFragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        originWebFragment.removeCookie();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if (originWebFragment.canGoBack()) {
            originWebFragment.goBack();
        } else {
            setResult(-1);
            this.finish();
        }

    }

    @Override
    public void onfinish() {

        setResult(1001);
        this.finish();
    }
}
