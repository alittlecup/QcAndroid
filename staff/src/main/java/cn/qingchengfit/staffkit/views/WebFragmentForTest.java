package cn.qingchengfit.staffkit.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.WebFragment;
import com.tencent.smtt.sdk.CookieManager;
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
 * Created by Paper on 2018/3/14.
 */

public class WebFragmentForTest extends WebFragment {
  public static WebFragmentForTest newInstance(String url) {
    Bundle args = new Bundle();
    WebFragmentForTest fragment = new WebFragmentForTest();
    args.putString("url",url);
    fragment.setArguments(args);
    return fragment;
  }
  public void initWebClient() {
    this.mWebviewWebView.setWebViewClient(new WebViewClient() {


      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        initCookie(url);
        super.onPageStarted(view, url, favicon);
        LogUtil.e(" start url: test:" + url);
        LogUtil.e(" start url:" + CookieManager.getInstance().getCookie(mCurUrl));
      }

      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtil.e("page finish:session:" + CookieManager.getInstance().getCookie(mCurUrl));
      }

      public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
      }

      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
        if (url.startsWith("javascript")) {
          return super.shouldOverrideUrlLoading(view, url);
        } else if (!url.startsWith("http")) {
          handleSchema(url);
          return true;
        } else {
          initCookie(url);
          mCurUrl = url;
          LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
          return true;
        }
      }

      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        LogUtil.e("errorCode:" + errorCode);
      }
    });
  }
}
