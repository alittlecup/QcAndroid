package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.WebFragment;
import rx.functions.Action1;

public class WebContainerActivity extends BaseActivity implements FragCallBack {

    public static void startWeb(Activity context, String url, int request) {
        Intent it = new Intent(context, WebContainerActivity.class);
        it.putExtra("url", url);
        context.startActivityForResult(it, request);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_container);
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_container, WebFragment.newInstance(getIntent().getStringExtra("url")))
                .commit();
        }
    }

    @Override public void onBackPressed() {
        //        if (getSupportFragmentManager().getFragments().size() >1){
        //            getSupportFragmentManager().popBackStackImmediate();
        //        }else
        super.onBackPressed();
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
