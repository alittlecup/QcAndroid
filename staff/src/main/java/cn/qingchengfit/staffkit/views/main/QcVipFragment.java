package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.WebFragment;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    try {
      View view = super.onCreateView(inflater, container, savedInstanceState);
      commonToolbar.setVisibility(View.VISIBLE);
      mTitle.setText(getString(R.string.home_tab_special));
      if (isSingle == true) {
        mTitle.setText("天猫双11");
      }
      mToolbar.setNavigationIcon(null);
      if (BuildConfig.DEBUG) {
        mToolbar.getMenu().add("刷新").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        RxMenuItem.clicks(mToolbar.getMenu().getItem(0))
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Void>() {
              @Override public void call(Void aVoid) {
                mWebviewWebView.reload();
              }
            }, new Action1<Throwable>() {
              @Override public void call(Throwable throwable) {

              }
            });
      }
      return view;
    } catch (Exception e) {
      Timber.e(e, "qcvip_fragment");
      return new View(getContext());
    }
  }

  @Override public boolean canSwipeRefreshChildScrollUp() {
    return true;
    //try {
    //    return mWebviewWebView.getWebScrollY() > 0;
    //}catch (Exception e){
    //    return true;
    //}
  }

  @Override protected void onVisible() {
    super.onVisible();
    SensorsUtils.track("AND_discover_tab_click", null, getContext());
  }

  @Override public void initWebClient() {

    mWebviewWebView.setWebViewClient(new WebViewClient() {

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        cn.qingchengfit.utils.LogUtil.e("url start:" + url);
        if (mRefreshSwipeRefreshLayout != null) {
          mRefreshSwipeRefreshLayout.setRefreshing(true);
        }
      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mRefreshSwipeRefreshLayout != null) {
          mRefreshSwipeRefreshLayout.setRefreshing(false);
        }
        onWebFinish();
      }

      @Override public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
        Log.d("TAG", "doUpdateVisitedHistory: " + s + "/" + b);
      }

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        cn.qingchengfit.utils.LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
        try {
          if (url.startsWith("http")) {
            if(!TextUtils.isEmpty(QcRestRepository.getSession(getContext()))){
              WebActivity.startWeb(url, getContext());
            }else{
              cookieManager.removeAllCookie();
            }
          } else {
            Intent to = new Intent();
            to.setAction(Intent.ACTION_VIEW);
            to.setData(Uri.parse(url));
            to.setPackage(getContext().getPackageName());
            to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(to);
          }
        } catch (Exception e) {

        }
        return true;
      }

      @Override public void onReceivedError(WebView view, int errorCode, String description,
          String failingUrl) {
        LogUtil.e("errorCode:" + errorCode);
        mTitle.setText("");
        showNoNet();
      }
    });
  }

  @Override public void initCookie(String url) {
    if (TextUtils.isEmpty(QcRestRepository.getSession(getContext()))) {
      cookieManager.removeAllCookie();
      return;
    }
    super.initCookie(url);
  }

  @Override public void onPause() {
    super.onPause();
    mWebviewWebView.loadUrl(mCurUrl);
  }
}
