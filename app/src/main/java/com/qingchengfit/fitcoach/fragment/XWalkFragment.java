package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ShareUtils;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.qingchengfit.fitcoach.http.bean.MutiSysSession;

import org.xwalk.core.JavascriptInterface;
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
public class XWalkFragment extends Fragment {

    @Bind(R.id.webview)
    XWalkView mWebview;
    @Bind(R.id.web_floatbtn)
    FloatingActionsMenu webFloatbtn;
    FloatingActionButton btn1;
    FloatingActionButton btn2;
    //    private XWalkCookieManager mCookieManager;
    Gson gson;
    private XWalkCookieManager xWalkCookieManager;

    public XWalkFragment() {
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xwalk, container, false);
        ButterKnife.bind(this, view);
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
        mWebview.load("http://feature2.qingchengfit.cn/welcome/", null);
//        mWebview.load("http://192.168.31.154:8888/welcome/", null);
//        mWebview.load("http://mm.qingchengfit.cn/meetings/1/#/info",null);
        mWebview.addJavascriptInterface(new JsInterface(), "NativeMethod");
        mWebview.setUIClient(new MyUIClient(mWebview));
        mWebview.setNetworkAvailable(false);
        mWebview.setResourceClient(new XWalkResourceClient(mWebview) {
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                //TODO 错误监控
//                super.onReceivedLoadError(view, errorCode, description, failingUrl);
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
            ShareUtils.oneKeyShared(getActivity());
        });

        return view;
    }

    private void initCookie() {
        List<MutiSysSession> mutiSysSessions = gson.fromJson(PreferenceUtils.getPrefString(getActivity(), "sessions", ""), new TypeToken<List<MutiSysSession>>() {
        }.getType());
        for (MutiSysSession sysSession : mutiSysSessions) {
            setCookie(sysSession.url, "session_id", sysSession.session_id);
        }
    }

    public void startLoadUrl(String url) {
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

    public void openmainDrawer() {
        getActivity().runOnUiThread(() -> RxBus.getBus().send(new OpenDrawer()));
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
//        sb.append(xWalkCookieManager.getCookie(url));
//        sb.append(";");
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
