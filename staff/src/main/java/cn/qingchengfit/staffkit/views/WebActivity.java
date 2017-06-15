package cn.qingchengfit.staffkit.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

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

public class WebActivity extends BaseActivity implements FragCallBack {

    public static final String TOOLBAR_THEME_BLACK = "toolbar_theme_black";

    private WebFragment webfrag;

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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (!CompatUtils.less21()) {
        //    Window window = getWindow();
        //    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        //        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //    window.setStatusBarColor(Color.TRANSPARENT);
        //    window.setNavigationBarColor(Color.BLACK);
        //}
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        String toUrl = getIntent().getStringExtra("url");
        boolean hideToolbar = false;
        if (getIntent() != null && getIntent().getData() != null && getIntent().getData().getHost() != null && getIntent().getData()
            .getHost()
            .equals("qcweb")) {
            toUrl = Uri.decode(getIntent().getData().getQueryParameter("url"));
            hideToolbar = TextUtils.equals(Uri.decode(getIntent().getData().getQueryParameter("hide_title")), "1");
        }
        if (TextUtils.isEmpty(toUrl)) {
            this.finish();
            return;
        }

        if (!toUrl.startsWith("http")) toUrl = "http://" + toUrl;

        if (getIntent().getBooleanExtra("refresh", false)) {
            webfrag = WebFragmentNoFresh.newInstance(toUrl);
        } else {
            webfrag = WebFragment.newInstance(toUrl, hideToolbar);
        }
        new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE).subscribe();
        getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, webfrag).commit();
    }

    public void onNewWeb(String url) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
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

    @Override public int getFragId() {
        return 0;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }
}
