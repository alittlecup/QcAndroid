package cn.qingchengfit.views.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import cn.qingchengfit.views.fragments.WebFragment;
import cn.qingchengfit.views.fragments.WebFragmentNoFresh;
import cn.qingchengfit.widgets.R;
import com.tbruyelle.rxpermissions.RxPermissions;

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
 * Created by Paper on 15/10/12 2015.
 */

public class WebActivity extends BaseActivity {

  public static final String TOOLBAR_THEME_BLACK = "toolbar_theme_black";
  public WebFragment webfrag;

  public static void startWeb(String url, Context context) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra("url", url);
    context.startActivity(intent);
  }

  public static void startWeb(String url, boolean banRefresh, Context context) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("refresh", banRefresh);
    context.startActivity(intent);
  }

  public static void startWeb(String url, Context context, String toolbarTheme) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("theme", toolbarTheme);
    context.startActivity(intent);
  }

  public static void startWebForResult(String url, Context context, int requestcode) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("request", requestcode);
    if (context instanceof Activity) {
      ((Activity) context).startActivityForResult(intent, requestcode);
    }
  }

  public static void startWebForResult(String url, Fragment fragment, int requestcode) {
    Intent intent = new Intent(fragment.getContext(), WebActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("request", requestcode);
    fragment.startActivityForResult(intent, requestcode);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_web);

    String toUrl = getIntent().getStringExtra("url");
    boolean hideToolbar = false;
    if (getIntent() != null
        && getIntent().getData() != null
        && getIntent().getData().getHost() != null
        && getIntent().getData().getHost().equals("qcweb")) {
      toUrl = Uri.decode(getIntent().getData().getQueryParameter("url"));
      hideToolbar =
          TextUtils.equals(Uri.decode(getIntent().getData().getQueryParameter("hide_title")), "1");
    }
    if (TextUtils.isEmpty(toUrl)) {
      this.finish();
      return;
    }

    if (!toUrl.startsWith("http")) toUrl = "http://" + toUrl;
    String hide_nav = Uri.parse(toUrl).getQueryParameter("hide_nav");

    if (getIntent().getBooleanExtra("refresh", false)) {
      webfrag = WebFragmentNoFresh.newInstance(toUrl);
    } else if (TextUtils.equals(hide_nav, "1")) {
      webfrag = WebFragment.newInstanceHideNav(toUrl, true);
    } else {
      webfrag = WebFragment.newInstance(toUrl, hideToolbar);
    }

    new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE).subscribe();
    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, webfrag).commit();
  }

  public void onNewWeb(String url) {
    getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
            R.anim.slide_right_out)
        .add(R.id.student_frag, WebFragment.newInstance(url))
        .addToBackStack("")
        .commit();
  }

  @Override public void onBackPressed() {
    if (webfrag != null && webfrag.canGoBack()) {

    } else {
      super.onBackPressed();
    }
  }
}