package com.qingchengfit.fitcoach.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ShareDialogFragment;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.ToolbarAction;
import com.qingchengfit.fitcoach.component.WebFragment;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/2/22.
 */

public class WebForResumeFragment extends WebFragment {
    private int pageLv = 0;

    public static WebForResumeFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebForResumeFragment fragment = new WebForResumeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mTitle.setText("我的简历");
        mToolbar.inflateMenu(R.menu.personal_home);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    Intent toSetting = new Intent(getContext(), SettingActivity.class);
                    toSetting.putExtra("to", 7);
                    startActivity(toSetting);
                } else if (item.getItemId() == R.id.action_share) {
                    if (App.gUser != null) {
                        ShareDialogFragment.newInstance(getString(R.string.sb_trainer_resume, App.gUser.getUsername()),
                            getString(R.string.check_sb_resume, App.gUser.getUsername()), App.gUser.avatar, mCurUrl)
                            .show(getFragmentManager(), "");
                    } else {
                        ToastUtils.show("用户信息丢失");
                    }
                }
                return true;
            }
        });
        return view;
    }

    @Override public void setToolbarTitle(String s) {

    }

    public void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                LogUtil.e(" start url:" + url);
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mCurUrl = url;
                if (mRefreshSwipeRefreshLayout != null) {
                    mRefreshSwipeRefreshLayout.setRefreshing(false);
                }
                onWebFinish();
            }

            @Override public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
                if (!url.startsWith("http")) {
                    handleSchema(url);
                    return true;
                }
                if (mCurUrl != null && url != null && url.equals(mCurUrl)) {
                    mWebviewWebView.goBack();
                    return true;
                } else {
                    pageLv++;
                }
                if (pageLv > 1) {
                    WebActivity.startWeb(url, getContext());
                    return true;
                }

                initCookie(url);
                view.loadUrl(url);
                mCurUrl = url;
                return true;
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.e("errorCode:" + errorCode);
                setToolbarTitle("");
                showNoNet();
            }
        });
    }

    /**
     * 复写以屏蔽 web端对actionbar的控制
     */
    @Override public void setAction(ToolbarAction toolStr) {
    }
}
