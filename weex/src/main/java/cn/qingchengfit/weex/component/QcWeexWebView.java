/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.qingchengfit.weex.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXLogUtils;

public class QcWeexWebView implements IWebView {

  private Context mContext;
  private WebView mWebView;
  private ProgressBar mProgressBar;
  private boolean mShowLoading = true;

  private OnErrorListener mOnErrorListener;
  private OnPageListener mOnPageListener;
  private FrameLayout root;

  public QcWeexWebView(Context context) {
    mContext = context;
  }

  @Override public View getView() {
    root = new FrameLayout(mContext);
    root.setBackgroundColor(Color.WHITE);

    mWebView = new WebView(mContext);//mContext.getApplicationContext();
    FrameLayout.LayoutParams wvLayoutParams =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
    wvLayoutParams.gravity = Gravity.CENTER;
    mWebView.setLayoutParams(wvLayoutParams);
    root.addView(mWebView);
    initWebView(mWebView);

    mProgressBar = new ProgressBar(mContext);
    showProgressBar(false);
    FrameLayout.LayoutParams pLayoutParams =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT);
    mProgressBar.setLayoutParams(pLayoutParams);
    pLayoutParams.gravity = Gravity.CENTER;
    root.addView(mProgressBar);
    return root;
  }

  @Override public void destroy() {
    if (getWebView() != null) {
      getWebView().removeAllViews();
      getWebView().destroy();
      mWebView = null;
    }
  }

  public void loadData(String data, String miniType, String encoding) {
    if (getWebView() == null) return;
    getWebView().loadData(data, miniType, encoding);
  }

  @Override public void loadUrl(String url) {
    if (getWebView() == null) return;
    getWebView().loadUrl(url);
  }

  @Override public void loadDataWithBaseURL(String source) {

  }

  @Override public void reload() {
    if (getWebView() == null) return;
    getWebView().reload();
  }

  @Override public void goBack() {
    if (getWebView() == null) return;
    getWebView().goBack();
  }

  @Override public void goForward() {
    if (getWebView() == null) return;
    getWebView().goForward();
  }

  @Override public void postMessage(Object msg) {

  }

  @JavascriptInterface public void sendDocumentHeight(final String hei, final String wid) {
    root.post(new Runnable() {
      @Override public void run() {
        int width = root.getWidth();
        int height = (int) (Integer.valueOf(hei) / Float.valueOf(wid) * width);
        ViewGroup.LayoutParams childLayoutParams = root.getLayoutParams();
        childLayoutParams.width=width;
        childLayoutParams.height=height;
        root.setLayoutParams(childLayoutParams);
      }
    });

  }

  @Override public void setShowLoading(boolean shown) {
    mShowLoading = shown;
  }

  @Override public void setOnErrorListener(OnErrorListener listener) {
    mOnErrorListener = listener;
  }

  @Override public void setOnPageListener(OnPageListener listener) {
    mOnPageListener = listener;
  }

  @Override public void setOnMessageListener(OnMessageListener listener) {

  }

  private void showProgressBar(boolean shown) {
    if (mShowLoading) {
      mProgressBar.setVisibility(shown ? View.VISIBLE : View.GONE);
    }
  }

  private void showWebView(boolean shown) {
    mWebView.setVisibility(shown ? View.VISIBLE : View.INVISIBLE);
  }

  public @Nullable WebView getWebView() {
    //TODO: remove this, duplicate with getView semantically.
    return mWebView;
  }

  private void initWebView(WebView wv) {
    WebSettings settings = wv.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setAppCacheEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setSupportZoom(false);
    //settings.setLoadWithOverviewMode(true);
    settings.setBuiltInZoomControls(false);
    wv.addJavascriptInterface(this, "weex");
    wv.setWebViewClient(new WebViewClient() {

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        WXLogUtils.v("tag", "onPageOverride " + url);
        return true;
      }

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        WXLogUtils.v("tag", "onPageStarted " + url);
        if (mOnPageListener != null) {
          mOnPageListener.onPageStart(url);
        }
      }

      @Override public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);
        WXLogUtils.v("tag", "onPageFinished " + url);
        String javascript = "javascript:(function () {\n"
            + "  var style = document.createElement('style');\n"
            + "  var content = '* {max-width: 100% !important; margin:0; padding: 0;}';\n"
            + "  var contentNode = document.createTextNode(content);\n"
            + "  style.type = 'text/css';\n"
            + "  style.appendChild(contentNode);\n"
            + "  document.head.appendChild(style);\n"
            + "})();";
        String loadHeight =
            "javascript:weex.sendDocumentHeight(document.body.offsetHeight,document.body.offsetWidth)";
        view.loadUrl(javascript);
        view.loadUrl(loadHeight);
        if (mOnPageListener != null) {
          mOnPageListener.onPageFinish(url, view.canGoBack(), view.canGoForward());
        }
      }

      @Override public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (mOnErrorListener != null) {
          mOnErrorListener.onError("error", "page error");
        }
      }

      @Override public void onReceivedHttpError(WebView view, WebResourceRequest request,
          WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        if (mOnErrorListener != null) {
          mOnErrorListener.onError("error", "http error");
        }
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        if (mOnErrorListener != null) {
          mOnErrorListener.onError("error", "ssl error");
        }
      }
    });
    wv.setWebChromeClient(new WebChromeClient() {
      @Override public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        showWebView(newProgress == 100);
        showProgressBar(newProgress != 100);
        WXLogUtils.v("tag", "onPageProgressChanged " + newProgress);
      }

      @Override public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (mOnPageListener != null) {
          mOnPageListener.onReceivedTitle(view.getTitle());
        }
      }
    });
  }
}
