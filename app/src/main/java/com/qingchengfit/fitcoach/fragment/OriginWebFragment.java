package com.qingchengfit.fitcoach.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.PlatformInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class OriginWebFragment extends WebFragment {
    public static final String TAG = OriginWebFragment.class.getName();
    @Bind(R.id.webview)
    WebView webview;
    CookieManager cookieManager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String base_url;
    private Gson gson;
    private Observable<NewPushMsg> mObservable;
    private List<Integer> mlastPosition = new ArrayList<>();
    public OriginWebFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        if (getArguments() != null) {
            base_url = getArguments().getString(BASE_URL);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_web, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        webview.addJavascriptInterface(new JsInterface(), "NativeMethod");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("shouldOverrideUrlLoading" + url);
                WebBackForwardList webBackForwardList = webview.copyBackForwardList();
                mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                return super.shouldOverrideUrlLoading(view, url);
            }



        });


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_delete);
            }

            @Override
            public void getVisitedHistory(ValueCallback<String[]> callback) {
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

    @Override
    public Boolean canGoBack() {

        if (webview != null)
            return webview.canGoBack();
        else return false;
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
        if (webview != null)
            webview.loadUrl(url);

    }

    private void initCookie() {
        String sessionid = PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        if (sessionid != null) {
            setCookie(Configs.ServerIp, "sessionid", sessionid);
            setCookie("", "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
        } else {
            ((MainActivity) getActivity()).logout();
        }
//        List<MutiSysSession> mutiSysSessions = gson.fromJson(PreferenceUtils.getPrefString(getActivity(), "sessions", ""), new TypeToken<List<MutiSysSession>>() {
//        }.getType());
//        for (MutiSysSession sysSession : mutiSysSessions) {
//            setCookie(sysSession.url, "sessionid", sysSession.session_id);
//        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        RxBus.getBus().unregister(NewPushMsg.class, mObservable);
    }


    @Override
    public void removeCookie() {
        if (cookieManager != null) {
            cookieManager.removeAllCookie();
        }
    }

    public void openmainDrawer() {
        getActivity().runOnUiThread(() -> RxBus.getBus().post(RxBus.OPEN_DRAWER));
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
//        String oriCookie = xWalkCookieManager.getCookie(url);
//        if (oriCookie != null){
//        sb.append(xWalkCookieManager.getCookie(url));
//        sb.append(";");}
        sb.append(key);
        sb.append("=");
        sb.append(value);
        cookieManager.setCookie(url, sb.toString());
    }


    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return PreferenceUtils.getPrefString(App.AppContex, "token", "");
        }


        @JavascriptInterface
        public void openDrawer() {
            openmainDrawer();

        }

        @JavascriptInterface
        public String getContacts() {
            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
            Gson gson = new Gson();
            return gson.toJson(contacts);
        }

        @JavascriptInterface
        public String getPlatform() {
            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(getActivity()));
            Gson gson = new Gson();
            return gson.toJson(info);
        }

        @JavascriptInterface
        public void goBack() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getActivity().onBackPressed();
                });
            }
        }

        @JavascriptInterface
        public String getSessionId() {
            return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }

//        @JavascriptInterface
//        public void resize(final float height) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    webview.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
//                }
//            });
//        }

    }
}
