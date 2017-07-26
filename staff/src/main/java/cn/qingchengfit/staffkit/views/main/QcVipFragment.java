package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.LogUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.WebFragment;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import timber.log.Timber;

public class QcVipFragment extends WebFragment {
    private boolean isSingle = false;

    public static QcVipFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        QcVipFragment fragment = new QcVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static QcVipFragment newInstance(String url, boolean isSingle) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putBoolean("isSingle", isSingle);
        QcVipFragment fragment = new QcVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurUrl = getArguments().getString("url");
            isSingle = getArguments().getBoolean("isSingle");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            commonToolbar.setVisibility(View.VISIBLE);
            mTitle.setText(getString(R.string.home_tab_special));
            mToolbar.setNavigationIcon(null);

            return view;
        } catch (Exception e) {
            Timber.e(e, "qcvip_fragment");
            return new View(getContext());
        }
    }

    @Override protected void onVisible() {
        super.onVisible();
      SensorsUtils.track("AND_discover_tab_click", null, getContext());
    }

    @Override public void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
              //if (mRefreshSwipeRefreshLayout != null) {
              //    mRefreshSwipeRefreshLayout.setRefreshing(false);
              //}
                onWebFinish();
            }

            @Override public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
              LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
                try {
                    if (url.startsWith("http")) {
                        WebActivity.startWeb(url, getContext());
                    } else {
                        Intent to = new Intent();
                        to.setAction(Intent.ACTION_VIEW);
                        to.setData(Uri.parse(url));
                      to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(to);
                    }
                } catch (Exception e) {

                }
                return true;
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtils.e("errorCode:" + errorCode);
                mTitle.setText("");
                showNoNet();
            }
        });
    }
}
