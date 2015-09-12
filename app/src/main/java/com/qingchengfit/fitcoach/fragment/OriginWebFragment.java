package com.qingchengfit.fitcoach.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.PlatformInfo;

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
    private String base_url;
    private Gson gson;
    private Observable<NewPushMsg> mObservable;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_origin_web, container, false);

        ButterKnife.bind(this, view);


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


        });
        webview.setWebChromeClient(new WebChromeClient() {

        });
        webview.getSettings().setJavaScriptEnabled(true);
//        webview.setInitialScale(getScale());

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
        webview.goBack();
    }


    private void initCookie() {
        String sessionid = PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        if (sessionid != null)
            setCookie(Configs.ServerIp, "sessionid", sessionid);
        else {
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
