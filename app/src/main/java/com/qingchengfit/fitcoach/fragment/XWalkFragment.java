package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.bean.PlatformInfo;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkCookieManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class XWalkFragment extends WebFragment {


    @Bind(R.id.webview)
    XWalkView mWebview;
    @Bind(R.id.web_floatbtn)
    FloatingActionsMenu webFloatbtn;
    FloatingActionButton btn1;
    FloatingActionButton btn2;
    //    private XWalkCookieManager mCookieManager;
    Gson gson;
    private XWalkCookieManager xWalkCookieManager;
    private String base_url;

    public XWalkFragment() {

    }

//    public static XWalkFragment newInstance(String baseUrl) {
//        XWalkFragment fragment = new XWalkFragment();
//        Bundle args = new Bundle();
//        args.putString(WebFragment., baseUrl);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        if (getArguments() != null) {
            base_url = getArguments().getString(WebFragment.BASE_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xwalk, container, false);
        ButterKnife.bind(this, view);
//        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

//        XWalkSettings xWalkSettings = new XWalkSettings(mWebview.getContext(),null,false);
//
//        xWalkSettings.setAppCacheEnabled(true);
//        xWalkSettings.setDatabaseEnabled(true);
//        String appCacheDir = getActivity().getDir("cache", Context.MODE_PRIVATE).getPath();

//        xWalkSettings.setAppCachePath(appCacheDir);
//        xWalkSettings.setAllowFileAccess(true);

//        xWalkSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        if (NetWorkUtils.getConnectedType(getActivity()) >= 0)
//            mWebview.clearCache(true);

//        CommandLine.getInstance().appendSwitch("--disable-pull-to-refresh-effect");  //禁止下拉刷新


        xWalkCookieManager = new XWalkCookieManager();
        xWalkCookieManager.setAcceptCookie(true);
        initCookie();
//        mWebview.load("http://feature2.qingchengfit.cn/welcome/", null);
//        mWebview.load("http://192.168.31.154:8888/welcome/", null);
//        mWebview.load("http://mm.qingchengfit.cn/meetings/1/#/info",null);
        mWebview.load(base_url, null);

        mWebview.addJavascriptInterface(new JsInterface(), "NativeMethod");
        mWebview.setUIClient(new MyUIClient(mWebview));
        mWebview.setNetworkAvailable(false);
        mWebview.setVerticalScrollBarEnabled(false);
        mWebview.setHorizontalScrollBarEnabled(false);
        mWebview.setResourceClient(new XWalkResourceClient(mWebview) {
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                //TODO 错误监控
//                super.onReceivedLoadError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);

            }
        });


        btn1 = new FloatingActionButton(getActivity());
        btn1.setIcon(R.drawable.ic_baseinfo_city);

        btn2 = new FloatingActionButton(getActivity());
        btn2.setIcon(R.drawable.ic_baseinfo_phone);

        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        btn1.setOnClickListener(view1 -> {
            PhoneFuncUtils.queryCalender(getActivity());
//            mWebview.setNetworkAvailable(false);
        });
        btn2.setOnClickListener(view1 -> {
//            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
//            Gson gson = new Gson();
//            LogUtil.e(gson.toJson(contacts));

        });
        RxBus.getBus().toObserverable().subscribe(o -> {
            if (o instanceof NewPushMsg) {
                mWebview.load("javascript:window.nativeLinkWeb.updateNotifications();", null);
            }
        });
        return view;
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


    public void startLoadUrl(String url) {
        if (mWebview != null)
            mWebview.load(url, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebview != null) {
            mWebview.resumeTimers();
            mWebview.onShow();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initCookie();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebview != null) {
            mWebview.pauseTimers();
            mWebview.onHide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void removeCookie() {
        if (xWalkCookieManager != null) {
            xWalkCookieManager.removeAllCookie();
        }
    }

    @Override
    public Boolean canGoBack() {
        if (mWebview != null)
            return mWebview.getNavigationHistory().canGoBack();
        else return false;
    }

    public void goBack() {
        if (mWebview != null)
            mWebview.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
    }

    public void openmainDrawer() {
        getActivity().runOnUiThread(() -> RxBus.getBus().send(new OpenDrawer()));
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
        xWalkCookieManager.setCookie(url, sb.toString());
    }

    class MyUIClient extends XWalkUIClient {
        MyUIClient(XWalkView view) {
            super(view);
        }

        @Override
        public boolean onConsoleMessage(XWalkView view, String message, int lineNumber, String sourceId, ConsoleMessageType messageType) {
            return super.onConsoleMessage(view, message, lineNumber, sourceId, messageType);
        }

        @Override
        public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public void onPageLoadStarted(XWalkView view, String url) {
            //TODO 监控网络
            super.onPageLoadStarted(view, url);

        }


    }

    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return PreferenceUtils.getPrefString(getActivity(), "token", "");
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


    }

}
