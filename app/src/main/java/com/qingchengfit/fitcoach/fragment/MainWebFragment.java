package com.qingchengfit.fitcoach.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.bean.Contact;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

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
public class MainWebFragment extends MainBaseFragment {
    @BindView(R.id.webview) WebView webview;
    CookieManager cookieManager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private String base_url;
    private Gson gson;
    private Observable<NewPushMsg> mObservable;
    private List<Integer> mlastPosition = new ArrayList<>();
    private Unbinder unbinder;

    public MainWebFragment() {
    }

    public static MainWebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(WebFragment.BASE_URL, url);
        MainWebFragment fragment = new MainWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        if (getArguments() != null) {
            base_url = getArguments().getString(WebFragment.BASE_URL);
        }
    }

    @SuppressLint("SetJavaScriptEnabled") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_web, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.setTitle("会议培训");
        //        webview.addJavascriptInterface(new JsInterface(), "NativeMethod");

        webview.setWebViewClient(new WebViewClient() {

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (!url.equalsIgnoreCase(base_url)) {
                    openDrawerInterface.goWeb(url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //                super.onReceivedError(view, errorCode, description, failingUrl);
                webview.loadUrl("");
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //                if (title.contains("clientJsonBegin")) {
                //                    String[] strings = title.split("clientJsonBegin");
                //                    toolbar.setTitle(strings[0]);
                //                    String jsonStr = strings[1].replace("clientJsonBegin", "");
                //                    Gson gson = new Gson();
                //                    TitleBean titleBean = gson.fromJson(jsonStr, TitleBean.class);
                //                    toolbar.getMenu().clear();
                //                    switch (titleBean.navIcon) {
                //                        case 0:
                //                            break;
                //                        case 1:
                //                            toolbar.setNavigationIcon(R.drawable.ic_cross_white);
                //                            break;
                //                        case 2:
                //                            toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                //                            break;
                //                        default:
                //                            break;
                //                    }
                //                    switch (titleBean.actionIcon) {
                //                        case 0:
                //                            break;
                //                        case 1:
                //                            toolbar.inflateMenu(R.menu.add);
                //                            break;
                //                        case 2:
                //                            break;
                //
                //                        default:
                //                            break;
                //                    }
                //
                //                } else {
                //                    toolbar.setTitle(title);
                //                }

            }

            @Override public void getVisitedHistory(ValueCallback<String[]> callback) {
                super.getVisitedHistory(callback);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        //        webview.setInitialScale(getScale());
        String s = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(s + " FitnessTrainerAssistant/0.2.5" + " Android");
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        // 开启DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = Configs.ExternalCache;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        webview.getSettings().setAppCacheEnabled(true);

        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        initCookie();
        mObservable = RxBus.getBus().register(NewPushMsg.class);
        mObservable.subscribe(newPushMsg -> webview.loadUrl("javascript:window.nativeLinkWeb.updateNotifications();"));
        webview.loadUrl(base_url);
        //        webview.loadUrl("http://www.baidu.com");
        return view;
    }

    private int getScale() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        //        Double val = new Double(width)/new Double(PIC_WIDTH);
        //        val = val * 100d;
        //        return val.intValue();
        return 1;
    }

    public void goBack() {
        WebBackForwardList webBackForwardList = webview.copyBackForwardList();
        webview.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
        mlastPosition.remove(mlastPosition.size() - 1);
        //        if (mlastPosition.size()>0){
        //            webview.goBackOrForward(-mlastPosition.get(mlastPosition.size()-1)+webBackForwardList.getCurrentIndex());
        //            mlastPosition.remove(mlastPosition.size()-1);
        //        }else
        //            webview.goBack();
    }

    public void startLoadUrl(String url) {
        if (webview != null) webview.loadUrl(url);
    }

    private void initCookie() {
        String sessionid = PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        if (sessionid != null) {
            setCookie(Configs.Server, "sessionid", sessionid);
            setCookie("", "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
        } else {
            ((MainActivity) getActivity()).logout();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        RxBus.getBus().unregister(NewPushMsg.class.getSimpleName(), mObservable);
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(key);
        sb.append("=");
        sb.append(value);
        cookieManager.setCookie(url, sb.toString());
    }

    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface public String getToken() {
            return PreferenceUtils.getPrefString(App.AppContex, "token", "");
        }

        @JavascriptInterface public String getContacts() {
            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
            Gson gson = new Gson();
            return gson.toJson(contacts);
        }

        @JavascriptInterface public String getPlatform() {
            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(getActivity()));
            Gson gson = new Gson();
            return gson.toJson(info);
        }

        @JavascriptInterface public void goBack() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getActivity().onBackPressed();
                });
            }
        }

        @JavascriptInterface public String getSessionId() {
            return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }
    }
}
