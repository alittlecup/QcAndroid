package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.WebActivity;
import cn.qingchengfit.staffkit.views.WebFragment;
import cn.qingchengfit.utils.LogUtils;
import cn.qingchengfit.utils.SensorsUtils;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            //if (isSingle) {
            commonToolbar.setVisibility(View.VISIBLE);
            mTitle.setText(getString(R.string.home_tab_special));
            mToolbar.setNavigationIcon(null);

            return view;
        } catch (Exception e) {
            Timber.e(e, "qcvip_fragment");
            return new View(getContext());
        }
    }

    @Override protected void onVisible() {
        super.onVisible();
        SensorsUtils.track("AND_discover_tab_click", null);
    }

    @Override public void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mRefreshSwipeRefreshLayout != null) {
                    mRefreshSwipeRefreshLayout.setRefreshing(false);
                }
                onWebFinish();
            }

            @Override public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("http")) {
                        WebActivity.startWeb(url, getContext());
                    } else {
                        Intent to = new Intent();
                        to.setAction(Intent.ACTION_VIEW);
                        to.setData(Uri.parse(url));
                        startActivity(to);
                    }
                } catch (Exception e) {

                }
                return true;
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtils.e("errorCode:" + errorCode);
                mTitle.setText("");
                showNoNet();
            }
        });
    }

    //
    //    @BindView(R.id.webview)
    //    WebView mWebview;
    //    @BindView(R.id.refresh)
    //    CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    //    @BindView(R.id.refresh_network)
    //    Button mRefresh;
    //    @BindView(R.id.no_newwork)
    //    LinearLayout mNoNetwork;
    //    @BindView(R.id.webview_root)
    //    RelativeLayout webviewRoot;
    //
    //    @BindView(R.id.toolbar_title)
    //    TextView toolbarTitile;
    //
    //    @BindView(R.id.fl_common_toolbar)
    //    FrameLayout commonToolbar;
    //    @BindView(R.id.toolbar)
    //    Toolbar toolbar;
    //    @BindView(R.id.schedule_notification_count)
    //    TextView scheduleNotificationCount;
    //
    //
    //    private CookieManager cookieManager;
    //    private IWXAPI msgApi;
    //    private ValueCallback<Uri> mValueCallback;
    //    private ValueCallback<Uri[]> mValueCallbackNew;
    //    private String sessionid;
    //    private String mCurUrl;
    //    private boolean isSingle = false;
    //
    //    public static QcVipFragment newInstance(String url) {
    //        Bundle args = new Bundle();
    //        args.putString("url", url);
    //        QcVipFragment fragment = new QcVipFragment();
    //        fragment.setArguments(args);
    //        return fragment;
    //    }
    //
    //    public static QcVipFragment newInstance(String url, boolean isSingle) {
    //        Bundle args = new Bundle();
    //        args.putString("url", url);
    //        args.putBoolean("isSingle", isSingle);
    //        QcVipFragment fragment = new QcVipFragment();
    //        fragment.setArguments(args);
    //        return fragment;
    //    }
    //
    //    @Override
    //    public void onCreate(@Nullable Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        if (getArguments() != null) {
    //            mCurUrl = getArguments().getString("url");
    //            isSingle = getArguments().getBoolean("isSingle");
    //        }
    //    }
    //
    //    @Nullable
    //    @Override
    //    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //        View view = inflater.inflate(R.layout.fragment_qcvip, container, false);
    //        unbinder = ButterKnife.bind(this, view);
    //        if (isSingle) {
    //            commonToolbar.setVisibility(View.VISIBLE);
    //            toolbarTitile.setText(getString(R.string.home_tab_special));
    //            toolbar.inflateMenu(R.menu.menu_notification);
    //            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
    //                @Override
    //                public boolean onMenuItemClick(MenuItem item) {
    //                    RxBus.getBus().post(new EventGoNotification());
    //                    return false;
    //                }
    //            });
    //
    //        } else {
    //            commonToolbar.setVisibility(View.GONE);
    //        }
    //        mRefresh.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                mNoNetwork.setVisibility(View.GONE);
    //                mWebview.reload();
    //            }
    //        });
    //        mRefreshSwipeRefreshLayout.setCanChildScrollUpCallback(this);
    //        initWebSetting();
    //
    //        mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    //
    //            @Override
    //            public void onGlobalLayout() {
    //                mRefreshSwipeRefreshLayout.setRefreshing(true);
    //                CompatUtils.removeGlobalLayout(mRefreshSwipeRefreshLayout.getViewTreeObserver(), this);
    //                /**
    //                 * init web
    //                 */
    //
    //                initWebClient();
    //                initChromClient();
    //                mWebview.setOnLongClickListener(new View.OnLongClickListener() {
    //                    @Override
    //                    public boolean onLongClick(View v) {
    //                        return true;
    //                    }
    //                });
    //                mWebview.addJavascriptInterface(new JsInterface(), "NativeMethod");
    //
    //                loadUrl();
    //
    //                mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    //                mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    //                    @Override
    //                    public void onRefresh() {
    //                        mWebview.reload();
    //                    }
    //                });
    //
    //                msgApi = WXAPIFactory.createWXAPI(App.context, Configs.APP_ID);
    //                msgApi.registerApp(Configs.APP_ID);
    //
    //            }
    //        });
    //
    //
    //        RxRegiste(RxBus.getBus().register(PayEvent.class)
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribeOn(Schedulers.io())
    //                .subscribe(new Subscriber<PayEvent>() {
    //                    @Override
    //                    public void onCompleted() {
    //
    //                    }
    //
    //                    @Override
    //                    public void onError(Throwable e) {
    //
    //                    }
    //
    //                    @Override
    //                    public void onNext(PayEvent payEvent) {
    //                        if (payEvent.result == 0) {
    //                            if (mWebview != null)
    //                                mWebview.loadUrl("javascript:window.paySuccessCallback();");
    //                        } else {
    //                            if (mWebview != null)
    //                                mWebview.loadUrl("javascript:window.payErrorCallback(" + payEvent.result + ");");
    //                        }
    //                    }
    //                }));
    //
    ////        Logger.e("tbs-version:" + mWebview.getTbsCoreVersion(getActivity()));
    //        RxBusAdd(QcResponseNotification.class)
    //                .subscribe(new Action1<QcResponseNotification>() {
    //                    @Override
    //                    public void call(QcResponseNotification qcResponseNotification) {
    //                        if (ResponseConstant.checkSuccess(qcResponseNotification)) {
    //                            if (qcResponseNotification.data.unread_count > 0) {
    //                                scheduleNotificationCount.setVisibility(View.VISIBLE);
    //                                scheduleNotificationCount.setText(qcResponseNotification.data.unread_count + "");
    //                            } else {
    //                                scheduleNotificationCount.setVisibility(View.GONE);
    //                            }
    //                        }
    //                    }
    //                });
    //        return view;
    //    }
    //
    //    @Override
    //    public void onResume() {
    //        super.onResume();
    //        RxBus.getBus().post(new EventNewPush());
    //    }
    //
    //    private void loadUrl() {
    //        if (getArguments() != null) {
    //            String url = getArguments().getString("url");
    //            if (!url.startsWith("http")) {
    //                url = "http://" + url;
    //            }
    //            CookieSyncManager.createInstance(getActivity());
    //            cookieManager = CookieManager.getInstance();
    //            cookieManager.setAcceptCookie(true);
    //            initCookie(url);
    //            mWebview.loadUrl(url);
    //        }
    //    }
    //
    //    private void initWebSetting() {
    //        WebStorage webStorage = WebStorage.getInstance();
    //        WebSettings webSetting = mWebview.getSettings();
    //        webSetting.setJavaScriptEnabled(true);
    //        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
    //        webSetting.setAllowFileAccess(true);
    //        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    //        webSetting.setSupportZoom(false);
    //        webSetting.setBuiltInZoomControls(false);
    //        webSetting.setUseWideViewPort(true);
    //        webSetting.setSupportMultipleWindows(true);
    //        webSetting.setLoadWithOverviewMode(true);
    //        webSetting.setAppCacheEnabled(true);
    //        webSetting.setDatabaseEnabled(true);
    //        webSetting.setDomStorageEnabled(true);
    //        webSetting.setGeolocationEnabled(true);
    //        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
    //        webSetting.setUserAgentString(webSetting.getUserAgentString() + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.context) + " Android  OEM:" + getString(R.string.oem_tag) + "  QingchengApp/Staff");
    //        Logger.d("uA:" + webSetting.getUserAgentString());
    //        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
    //        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    //        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    //    }
    //
    //
    //    private void initCookie(String url) {
    //        sessionid = PreferenceUtils.getPrefString(App.context, Configs.PREFER_SESSION, "");
    //
    //        if (sessionid != null) {
    //            try {
    //                URI uri = new URI(url);
    //                setCookie(uri.getHost(), "qc_session_id", sessionid);
    //                setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
    //            } catch (Exception e) {
    //                //e.printStackTrace();
    //            }
    //            setCookie(Configs.Server, "sessionid", sessionid);
    //        } else {
    //        }
    //    }
    //
    //    public void setCookie(String url, String key, String value) {
    //        StringBuffer sb = new StringBuffer();
    //        sb.append(key);
    //        sb.append("=");
    //        sb.append(value).append(";");
    //        cookieManager.setCookie(url, sb.toString());
    //    }
    //
    //
    //    private void initChromClient() {
    //
    //        mWebview.setWebChromeClient(new WebChromeClient() {
    //
    //            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
    //                mValueCallback = valueCallback;
    //            }
    //
    //            @Override
    //            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
    //                mValueCallbackNew = valueCallback;
    //                return true;
    //            }
    //
    //            @Override
    //            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
    //                new MaterialDialog.Builder(getActivity())
    //                        .content(message)
    //                        .cancelable(false)
    //                        .positiveText(R.string.common_i_konw)
    //                        .callback(new MaterialDialog.ButtonCallback() {
    //                            @Override
    //                            public void onPositive(MaterialDialog dialog) {
    //                                super.onPositive(dialog);
    //                                result.confirm();
    //                            }
    //                        })
    //                        .show();
    //                return true;
    //            }
    //
    //            @Override
    //            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
    //                new MaterialDialog.Builder(getActivity())
    //                        .content(message)
    //                        .positiveText("确定")
    //                        .negativeText("取消")
    //                        .cancelable(false)
    //                        .callback(new MaterialDialog.ButtonCallback() {
    //                            @Override
    //                            public void onPositive(MaterialDialog dialog) {
    //                                super.onPositive(dialog);
    //                                result.confirm();
    //                                dialog.dismiss();
    //                            }
    //
    //                            @Override
    //                            public void onNegative(MaterialDialog dialog) {
    //                                super.onNegative(dialog);
    //                                result.cancel();
    //                                dialog.dismiss();
    //                            }
    //                        })
    //                        .show();
    //                return true;
    //            }
    //
    //            @Override
    //            public void onReceivedTitle(WebView view, String title) {
    //                super.onReceivedTitle(view, title);
    //                mRefreshSwipeRefreshLayout.setRefreshing(false);
    //            }
    //
    //        });
    //
    //    }
    //
    //    private void initWebClient() {
    //        mWebview.setWebViewClient(new WebViewClient() {
    //
    //            @Override
    //            public void onPageStarted(WebView view, String url, Bitmap favicon) {
    //                super.onPageStarted(view, url, favicon);
    //                Logger.d("url:" + url);
    //            }
    //
    //            @Override
    //            public void onPageFinished(WebView view, String url) {
    //                super.onPageFinished(view, url);
    //                mRefreshSwipeRefreshLayout.setRefreshing(false);
    //            }
    //
    //            @Override
    //            public void onLoadResource(WebView view, String url) {
    //                super.onLoadResource(view, url);
    //            }
    //
    //            // 点击页面中的链接会调用这个方法
    //            @Override
    //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //                Logger.d("shouldOverrideUrlLoading:" + url + " :");
    //                if (!url.contains("http")) {
    //                    return true;
    //                }
    //                // POST 事件--更新首页导航小红点
    //                RxBus.getBus().post(new SpecialPointEvent(SpecialPointEvent.ACTION_UPDATE));
    //                WebActivity.startWeb(url, getContext(), WebActivity.TOOLBAR_THEME_BLACK);
    //                return true;
    //            }
    //
    //            @Override
    //            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    //                Logger.e("errorCode:" + errorCode);
    //                mRefreshSwipeRefreshLayout.setRefreshing(false);
    //                showNoNet();
    //
    //            }
    //
    //
    //        });
    //
    //    }
    //
    //    private void showNoNet() {
    //        mNoNetwork.setVisibility(View.VISIBLE);
    //    }
    //
    //
    //    @Override
    //    public String getFragmentName() {
    //        return null;
    //    }
    //
    //    @Override
    //    public void onDestroyView() {
    //        super.onDestroyView();
    //    }
    //
    //    @Override
    //    public boolean canSwipeRefreshChildScrollUp() {
    //        try {
    //            return mWebview.getWebScrollY() > 0;
    //        } catch (Exception e) {
    //            return true;
    //        }
    //    }
    //
    //
    //    public class JsInterface {
    //
    //        JsInterface() {
    //        }
    //
    //        @JavascriptInterface
    //        public String getToken() {
    //            return PreferenceUtils.getPrefString(App.context, "token", "");
    //        }
    //
    //        @JavascriptInterface
    //        public void shareInfo(String json) {
    //            try {
    //                ShareBean bean = new Gson().fromJson(json, ShareBean.class);
    //                ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link)
    //                        .show(getFragmentManager(), "");
    //            } catch (Exception e) {
    //
    //            }
    //        }
    //
    //        @JavascriptInterface
    //        public void wechatPay(String info) {
    //
    //            // 将该app注册到微信
    //            try {
    //                JSONObject object = new JSONObject(info);
    //
    //                PayReq request = new PayReq();
    //                request.appId = Configs.APP_ID;
    //                request.partnerId = object.getString("partnerid");
    //
    //                request.prepayId = object.getString("prepayid");
    //                request.packageValue = "Sign=WXPay";
    //                request.nonceStr = object.getString("noncestr");
    //                request.timeStamp = object.getString("timestamp");
    //                request.sign = object.getString("sign");
    ////                LogUtil.e("xxx:"+request.checkArgs() );
    //                msgApi.sendReq(request);
    //            } catch (JSONException e) {
    //                e.printStackTrace();
    //                Logger.e("wechat pay error");
    //            }
    //
    //        }
    //
    //        @JavascriptInterface
    //        public void openDrawer() {
    //
    //
    //        }
    //
    //        @JavascriptInterface
    //        public void setAction(String s) {
    //        }
    //
    //        @JavascriptInterface
    //        public void completeAction() {
    //            getActivity().finish();
    //        }
    //
    //        @JavascriptInterface
    //        public void setTitle(final String s) {
    //
    //        }
    //
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
    //            getActivity().runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    getActivity().onBackPressed();
    //                }
    //            });
    //        }
    //
    //        @JavascriptInterface
    //        public String getSessionId() {
    //            return PreferenceUtils.getPrefString(App.context, Configs.PREFER_SESSION, "");
    //        }
    //
    //        @JavascriptInterface
    //        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {
    //
    //        }
    //
    //    }
}
