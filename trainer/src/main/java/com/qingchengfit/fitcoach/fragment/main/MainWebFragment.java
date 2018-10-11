package com.qingchengfit.fitcoach.fragment.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.WebFragment;
import com.qingchengfit.fitcoach.R;
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
 * Created by Paper on 2016/11/29.
 */

public class MainWebFragment extends WebFragment {

  public boolean isLoaded = false;

  public static MainWebFragment newInstance(String url) {
    Bundle args = new Bundle();

    args.putString("url", url);
    MainWebFragment fragment = new MainWebFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void initToolbar(Toolbar toolbar) {
    mTitle.setText(R.string.explore);
  }

  @Override public void onLoadedView() {
    super.onLoadedView();
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    //可见，但还没被初始化
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    mWebClose.setVisibility(View.GONE);
    return v;
  }

  @Override protected void onVisible() {
    SensorsUtils.track("AND_discover_tab_click", null, getContext());
  }

  @Override public void initWebClient() {
    mWebviewWebView.setWebViewClient(new WebViewClient() {

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtil.e(" start url:" + url);
      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      }

      @Override public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
      }

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
        if (!url.startsWith("http")) {
          if (url.contains("qcstaff")) {
            url = url.replace("qcstaff", "qccoach");
          }
          handleSchema(url);
          return true;
        }
        WebActivity.startWeb(url, getActivity());
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
}
