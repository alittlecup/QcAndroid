//package com.qingchengfit.fitcoach.fragment;
//
//
//import android.annotation.TargetApi;
//import android.app.Fragment;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.gson.Gson;
//import cn.qingchengfit.model.common.Contact;
//import com.paper.paperbaselibrary.utils.AppUtils;
//import com.paper.paperbaselibrary.utils.LogUtil;
//import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
//import com.paper.paperbaselibrary.utils.PreferenceUtils;
//import com.qingchengfit.fitcoach.App;
//import com.qingchengfit.fitcoach.Configs;
//import com.qingchengfit.fitcoach.R;
//import com.qingchengfit.fitcoach.RxBus;
//import com.qingchengfit.fitcoach.activity.MainActivity;
//import com.qingchengfit.fitcoach.activity.NotificationActivity;
//import cn.qingchengfit.bean.NewPushMsg;
//import cn.qingchengfit.model.common.PlatformInfo;
//
//import org.xwalk.core.JavascriptInterface;
//import org.xwalk.core.XWalkNavigationHistory;
//import org.xwalk.core.XWalkResourceClient;
//import org.xwalk.core.XWalkUIClient;
//import org.xwalk.core.XWalkView;
//import org.xwalk.core.internal.XWalkCookieManager;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import rx.Observable;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class XWalkFragment extends WebFragment {
//
//
//    @BindView(R.id.webview)
//    XWalkView mWebview;
//    //    @BindView(R.id.web_floatbtn)
////    FloatingActionsMenu webFloatbtn;
////    FloatingActionButton btn1;
////    FloatingActionButton btn2;
//    //    private XWalkCookieManager mCookieManager;
//    Gson gson;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    private XWalkCookieManager xWalkCookieManager;
//    private String base_url;
//    private Observable<NewPushMsg> mObservable;
//
//    public XWalkFragment() {
//    }
//
////    public static XWalkFragment newInstance(String baseUrl) {
////        XWalkFragment fragment = new XWalkFragment();
////        Bundle args = new Bundle();
////        args.putString(WebFragment., baseUrl);
////        fragment.setArguments(args);
////        return fragment;
////    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        gson = new Gson();
//        if (getArguments() != null) {
//            base_url = getArguments().getString(WebFragment.BASE_URL);
//        }
//
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_xwalk, container, false);
//        unbinder=ButterKnife.bind(this, view);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
////      toolbarXWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
//
////        XWalkSettings xWalkSettings = new XWalkSettings(mWebview.getContext(),null,false);
////
////        xWalkSettings.setAppCacheEnabled(true);
////        xWalkSettings.setDatabaseEnabled(true);
////        String appCacheDir = getActivity().getDir("cache", Context.MODE_PRIVATE).getPath();
//
////        xWalkSettings.setAppCachePath(appCacheDir);
////        xWalkSettings.setAllowFileAccess(true);
//
////        xWalkSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
////        if (NetWorkUtils.getConnectedType(getActivity()) >= 0)
////            mWebview.clearCache(true);
//
////        CommandLine.getInstance().appendSwitch("--disable-pull-to-refresh-effect");  //禁止下拉刷新
//
////        Glide.with(App.AppContex).load(R.drawable.ic_loading_gif).into(loadingGif);
//        xWalkCookieManager = new XWalkCookieManager();
//        xWalkCookieManager.setAcceptCookie(true);
//        initCookie();
////        mWebview.load("http://feature2.qingchengfit.cn/welcome/", null);
////        mWebview.load("http://192.168.31.154:8888/welcome/", null);
////        mWebview.load("http://mm.qingchengfit.cn/meetings/1/#/info",null);
//        mWebview.load(base_url, null);
//
//        mWebview.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK)
//                    return false;
//                return false;
//            }
//        });
//        mWebview.addJavascriptInterface(new JsInterface(), "NativeMethod");
//        mWebview.setUIClient(new MyUIClient(mWebview));
//        mWebview.setNetworkAvailable(false);
//        mWebview.setVerticalScrollBarEnabled(false);
//        mWebview.setHorizontalScrollBarEnabled(false);
//        mWebview.setUserAgentString("crosswalk/" + mWebview.getXWalkVersion() + " " + mWebview.getAPIVersion() + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android");
//        mWebview.setResourceClient(new XWalkResourceClient(mWebview) {
//
//            @Override
//            public void onLoadStarted(XWalkView view, String url) {
//                LogUtil.d("onLoadStarted:" + url);
//
//                super.onLoadStarted(view, url);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
//                LogUtil.d("shouldOverrideUrlLoading:" + url);
//                if (mWebview != null && mWebview.getOriginalUrl() != null) {
//                    ((MainActivity) getActivity()).goXwalkfragment(url, mWebview.getOriginalUrl());
//                    return true;
//                }
//
////                return true;
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
//                //TODO 错误监控
////                super.onReceivedLoadError(view, errorCode, description, failingUrl);
//            }
//
//            @Override
//            public void onLoadFinished(XWalkView view, String url) {
//                super.onLoadFinished(view, url);
//                LogUtil.d("onLoadFinished:" + url);
////                new Thread(() -> {
////                    try {
////                        Thread.sleep(600);
////                    } catch (InterruptedException e) {
////                    }
////                    if (getActivity() != null)
////                        getActivity().runOnUiThread(() -> {
////                            if (loading != null)
////                                loading.setVisibility(View.GONE);
////                        });
////
////                }).start();
//
////                Observable.just(sleepThread(5000))
////                        .observeOn(Schedulers.newThread())
////                        .onBackpressureBuffer().subscribeOn(AndroidSchedulers.mainThread())
////                        .subscribe(s -> {
////                            if (getActivity() != null) {
////                                getActivity().runOnUiThread(() -> {
////                                    if (loading != null) {
////                                        loading.setVisibility(View.GONE);
////                                    }
////
////                                });
////                            }
////                        });
//            }
//
//            @Override
//            public void onProgressChanged(XWalkView view, int progressInPercent) {
//                super.onProgressChanged(view, progressInPercent);
//                LogUtil.d("onProgressChanged:");
////                loading.setAlpha(255 * (100 - progressInPercent) / 100);
////                LogUtil.e("percent:" + progressInPercent);
//            }
//        });
////        btn1 = new FloatingActionButton(getActivity());
////        btn1.setIcon(R.drawable.ic_baseinfo_city);
//
////        btn2 = new FloatingActionButton(getActivity());
////        btn2.setIcon(R.drawable.ic_baseinfo_phone);
////
////        webFloatbtn.addButton(btn1);
////        webFloatbtn.addButton(btn2);
////        btn1.setOnClickListener(view1 -> {
////            mWebview.load("javascript:window.nativeLinkWeb.updateNotifications();", null);
////            PhoneFuncUtils.queryCalender(getActivity());
////            mWebview.setNetworkAvailable(false);
////        });
////        btn2.setOnClickListener(view1 -> {
////            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
////            Gson gson = new Gson();
////            LogUtil.e(gson.toJson(contacts));
//
////        });
//        mObservable = RxBus.getBus().register(NewPushMsg.class);
//        mObservable.subscribe(newPushMsg -> {
//            if (mWebview != null)
//                LogUtil.e("recieve Notificate");
//            mWebview.load("javascript:window.nativeLinkWeb.updateNotifications();", null);
//        });
//
//        return view;
//    }
//
//
//    private void initCookie() {
//        String sessionid = PreferenceUtils.getPrefString(getActivity(), "session_id", "");
//        if (sessionid != null) {
//            setCookie(Configs.ServerIp, "sessionid", sessionid);
//            setCookie("", "qc_session_id", sessionid);
//            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
//            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
//        } else {
//            ((MainActivity) getActivity()).logout();
//        }
////        List<MutiSysSession> mutiSysSessions = gson.fromJson(PreferenceUtils.getPrefString(getActivity(), "sessions", ""), new TypeToken<List<MutiSysSession>>() {
////        }.getType());
////        for (MutiSysSession sysSession : mutiSysSessions) {
////            setCookie(sysSession.url, "sessionid", sysSession.session_id);
////        }
//    }
//
//    private String sleepThread(int ms) {
//        try {
//            Thread.sleep(ms);
//        } catch (InterruptedException e) {
//        } finally {
//            return "";
//        }
//    }
//
//    public void startLoadUrl(String url) {
//        if (mWebview != null && !mWebview.getUrl().equalsIgnoreCase(url))
//            mWebview.load(url, null);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden){
//            if (mWebview != null) {
//
//                mWebview.onHide();
//
//            }
//        }else {
//            if (mWebview != null) {
//
//                mWebview.onShow();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        initCookie();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//        RxBus.getBus().unregister(NewPushMsg.class, mObservable);
//    }
//
//    @Override
//    public void removeCookie() {
//        if (xWalkCookieManager != null) {
//            xWalkCookieManager.removeAllCookie();
//        }
//    }
//
//    @Override
//    public Boolean canGoBack() {
//        if (mWebview != null)
//            return mWebview.getNavigationHistory().canGoBack();
//        else return false;
//    }
//
//    public void goBack() {
//
//        if (mWebview != null)
//            mWebview.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
//    }
//
//    public void openmainDrawer() {
//        getActivity().runOnUiThread(() -> RxBus.getBus().post(RxBus.OPEN_DRAWER));
//    }
//
//    public void setCookie(String url, String key, String value) {
//        StringBuffer sb = new StringBuffer();
////        String oriCookie = xWalkCookieManager.getCookie(url);
////        if (oriCookie != null){
////        sb.append(xWalkCookieManager.getCookie(url));
////        sb.append(";");}
//        sb.append(key);
//        sb.append("=");
//        sb.append(value);
//        xWalkCookieManager.setCookie(url, sb.toString());
//    }
//
//    class MyUIClient extends XWalkUIClient {
//
//        MyUIClient(XWalkView view) {
//            super(view);
//        }
//
//
//        @Override
//        public boolean onConsoleMessage(XWalkView view, String message, int lineNumber, String sourceId, ConsoleMessageType messageType) {
//            return super.onConsoleMessage(view, message, lineNumber, sourceId, messageType);
//        }
//
//        @Override
//        public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
//            super.onUnhandledKeyEvent(view, event);
//        }
//
//        @Override
//        public void onPageLoadStarted(XWalkView view, String url) {
//            //TODO 监控网络
//            super.onPageLoadStarted(view, url);
//        }
//
//
//    }
//
//    public class JsInterface {
//
//        JsInterface() {
//        }
//
//        @JavascriptInterface
//        public String getToken() {
//            return PreferenceUtils.getPrefString(getActivity(), "token", "");
//        }
//
//
//        @JavascriptInterface
//        public void openDrawer() {
//            openmainDrawer();
//
//        }
//
//        @JavascriptInterface
//        public void openMsg() {
//
//            getActivity().runOnUiThread(() -> {
//                startActivity(new Intent(getActivity(), NotificationActivity.class));
//                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
//            });
//
//        }
//
//        @JavascriptInterface
//        public String getContacts() {
//            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
//            Gson gson = new Gson();
//            return gson.toJson(contacts);
//        }
//
//        @JavascriptInterface
//        public String getPlatform() {
//            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(getActivity()));
//            Gson gson = new Gson();
//            return gson.toJson(info);
//        }
//
//        @JavascriptInterface
//        public void goBack() {
//
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(() -> {
//                    getActivity().onBackPressed();
//                });
//
//            }
//        }
//
//        @JavascriptInterface
//        public void guideTo(String url) {
//
//            if (getActivity() != null) {
//                ((MainActivity) getActivity()).guideTo(url);
//            }
//        }
//
//        @JavascriptInterface
//        public String getSessionId() {
//            return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
//        }
//
//        @JavascriptInterface
//        public void shareInfo(String title, String link, String imgurl, String desc) {
//        }
//
//
//    }
//
//}
