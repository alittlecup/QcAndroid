package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Contact;
import cn.qingchengfit.model.responese.PlatformInfo;
import cn.qingchengfit.model.responese.ShareBean;
import cn.qingchengfit.model.responese.ToolbarAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshUnloginAd;
import cn.qingchengfit.staffkit.rxbus.event.PayEvent;
import cn.qingchengfit.staffkit.views.custom.CustomSwipeRefreshLayout;
import cn.qingchengfit.staffkit.views.login.LoginActivity;
import cn.qingchengfit.staffkit.views.setting.SettingActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.LogUtils;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;
import java.net.URI;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by peggy on 16/5/24.
 */

public class WebFragment extends BaseFragment implements CustomSwipeRefreshLayout.CanChildScrollUpCallback {
    public static final int RESULT_LOGIN = 1;
    @BindView(R.id.toobar_action) public TextView mToobarActionTextView;
    @BindView(R.id.toolbar_titile) public TextView mTitle;
    @BindView(R.id.webview) public WebView mWebviewWebView;
    public String mCurUrl;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.refresh) protected CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    @BindView(R.id.common_toolbar) protected RelativeLayout commonToolbar;
    @BindView(R.id.refresh_network) Button mRefresh;
    @BindView(R.id.no_newwork) LinearLayout mNoNetwork;
    @BindView(R.id.webview_root) LinearLayout webviewRoot;
    private CookieManager cookieManager;
    private IWXAPI msgApi;
    private ChoosePictureFragmentDialog choosePictureFragmentDialog;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallbackNew;
    private String sessionid;
    private boolean hideToolbar = false;
    private boolean lastIsRedrect = false;
    private boolean isTouchWebView;   //用以判断是否是点击事件 用以区分 重定向
    private String webAction;   //用以返回给

    public static WebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WebFragment newInstance(String url, boolean hideToolbar) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putBoolean("hide", hideToolbar);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurUrl = getArguments().getString("url");
            hideToolbar = getArguments().getBoolean("hide", false);
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_web, container, false);
            unbinder = ButterKnife.bind(this, view);
            mRefresh.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mNoNetwork.setVisibility(View.GONE);
                    mWebviewWebView.reload();
                }
            });
            mRefreshSwipeRefreshLayout.setCanChildScrollUpCallback(this);
            initWebSetting();

            initToolbar();

            mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override public void onGlobalLayout() {
                    mRefreshSwipeRefreshLayout.setRefreshing(true);
                    //                mRefreshSwipeRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    CompatUtils.removeGlobalLayout(mRefreshSwipeRefreshLayout.getViewTreeObserver(), this);
                    /**
                     * init web
                     */

                    initWebClient();
                    initChromClient();
                    mWebviewWebView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override public boolean onLongClick(View v) {
                            return true;
                        }
                    });
                    mWebviewWebView.setOnTouchListener(new View.OnTouchListener() {
                        @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                            isTouchWebView = true;
                            return false;
                        }
                    });
                    mWebviewWebView.addJavascriptInterface(new JsInterface(), "NativeMethod");
                    mToobarActionTextView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            if (mWebviewWebView != null) {
                                mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
                            }
                        }
                    });
                    if (getArguments() != null) {
                        String url = getArguments().getString("url");

                        CookieSyncManager.createInstance(getActivity());
                        cookieManager = CookieManager.getInstance();
                        cookieManager.setAcceptCookie(true);

                        initCookie(url);
                        mWebviewWebView.loadUrl(url);
                        try {
                            Timber.e("session:" + cookieManager.getCookie(url));
                        } catch (Exception e) {

                        }
                    }

                    mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
                    mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override public void onRefresh() {
                            mNoNetwork.setVisibility(View.GONE);
                            mWebviewWebView.reload();
                        }
                    });
                    //

                    msgApi = WXAPIFactory.createWXAPI(getContext(), Configs.APP_ID);
                    msgApi.registerApp(Configs.APP_ID);

                    choosePictureFragmentDialog = ChoosePictureFragmentDialog.newInstance();
                    choosePictureFragmentDialog.setResult(new ChoosePictureFragmentDialog.ChoosePicResult() {
                        @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
                            if (isSuccess) {
                                if (mValueCallback != null) mValueCallback.onReceiveValue(Uri.fromFile(new File(filePath)));
                                if (mValueCallbackNew != null) {
                                    Uri[] uris = new Uri[1];
                                    uris[0] = Uri.fromFile(new File(filePath));
                                    mValueCallbackNew.onReceiveValue(uris);
                                }
                            } else {
                                if (mValueCallback != null) mValueCallback.onReceiveValue(null);
                                if (mValueCallbackNew != null) mValueCallbackNew.onReceiveValue(null);
                            }
                        }
                    });

                    onLoadedView();
                }
            });

            RxRegiste(RxBus.getBus()
                .register(PayEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PayEvent>() {
                    @Override public void onCompleted() {
                        onLoadedView();
                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(PayEvent payEvent) {
                        if (payEvent.result == 0) {
                            if (mWebviewWebView != null) mWebviewWebView.loadUrl("javascript:window.paySuccessCallback();");
                        } else {
                            if (mWebviewWebView != null) {
                                mWebviewWebView.loadUrl("javascript:window.payErrorCallback(" + payEvent.result + ");");
                            }
                        }
                    }
                }));
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            return view;
        } catch (Exception e) {
            Timber.e(e, "web_fragment");
            return new View(getContext());
        }
    }

    public void onLoadedView() {

    }

    public void onWebFinish() {

    }

    public void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.md_nav_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mTitle.setText("");
        if (hideToolbar) commonToolbar.setVisibility(View.GONE);
    }

    private void initWebSetting() {
        WebStorage webStorage = WebStorage.getInstance();
        WebSettings webSetting = mWebviewWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        String s = webSetting.getUserAgentString();
        webSetting.setUserAgentString(
            s + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.context) + " Android  " + "QingchengApp/Staff  " + "OEM:" + getString(
                R.string.oem_tag));
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        Timber.e("uA:" + webSetting.getUserAgentString());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    public void initCookie(String url) {
        sessionid = PreferenceUtils.getPrefString(getContext(), Configs.PREFER_SESSION, "");

        if (sessionid != null) {
            try {
                URI uri = new URI(url);
                setCookie(uri.getHost(), "qc_session_id", sessionid);
                setCookie(uri.getHost(), "sessionid", sessionid);
                setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
                Timber.e("session:" + sessionid);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            setCookie(Configs.Server, "sessionid", sessionid);
        } else {
            //TODO logout
        }
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(key);
        sb.append("=");
        sb.append(value).append(";");
        cookieManager.setCookie(url, sb.toString());
    }

    private void initChromClient() {

        mWebviewWebView.setWebChromeClient(new WebChromeClient() {

            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                mValueCallback = valueCallback;
                choosePictureFragmentDialog.show(getFragmentManager(), "");
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback,
                android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
                mValueCallbackNew = valueCallback;
                choosePictureFragmentDialog.show(getFragmentManager(), "");
                return true;
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
                mValueCallbackNew = valueCallback;
                choosePictureFragmentDialog.show(getFragmentManager(), "");
                return true;
            }

            @Override public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new MaterialDialog.Builder(getActivity()).content(message)
                    .cancelable(false)
                    .positiveText(R.string.common_i_konw)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            result.confirm();
                        }
                    })
                    .show();
                return true;
            }

            @Override public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new MaterialDialog.Builder(getActivity()).content(message)
                    .positiveText("确定")
                    .negativeText("取消")
                    .cancelable(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            result.confirm();
                            dialog.dismiss();
                        }

                        @Override public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            result.cancel();
                            dialog.dismiss();
                        }
                    })
                    .show();
                return true;
            }

            @Override public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mTitle != null) {
                    mTitle.setText(title);
                }
            }
        });
    }

    public void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (TextUtils.equals(Uri.parse(url).getQueryParameter("hide_title"), "1")) {
                    commonToolbar.setVisibility(View.GONE);
                } else {
                    commonToolbar.setVisibility(View.VISIBLE);
                    if (BuildConfig.DEBUG && cookieManager != null) {
                        LogUtils.e(url + "---cookie---" + cookieManager.getCookie(url));
                    }
                }
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mRefreshSwipeRefreshLayout != null) {
                    mRefreshSwipeRefreshLayout.setRefreshing(false);
                }
                mCurUrl = url;
                onWebFinish();
            }

            @Override public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http")) {
                    try {
                        Intent toAction = new Intent();
                        toAction.setAction(Intent.ACTION_VIEW);
                        toAction.setData(Uri.parse(url));
                        startActivity(toAction);
                    } catch (Exception e) {

                    }
                    return true;
                } else {

                    initCookie(url);
                    //view.loadUrl(url);
                    mCurUrl = url;
                    WebView.HitTestResult hit = view.getHitTestResult();
                    if (getActivity() instanceof WebActivity) {
                        if (!isTouchWebView && (hit == null || hit.getExtra() == null)) {
                            return super.shouldOverrideUrlLoading(view, url);
                        } else {
                            ((WebActivity) getActivity()).onNewWeb(url);
                            isTouchWebView = false;
                        }
                    } else {
                        WebActivity.startWeb(url, getContext());
                    }

                    LogUtils.d("shouldOverrideUrlLoading:" + url + " :");
                    return true;
                }
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtils.e("errorCode:" + errorCode);
                mTitle.setText("");
                showNoNet();
            }
        });
    }

    public void showNoNet() {
        mNoNetwork.setVisibility(View.VISIBLE);
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        try {
            mWebviewWebView.removeAllViews();
            mWebviewWebView.destroy();
            if (msgApi != null) msgApi.detach();
        } catch (Exception e) {

        }
        super.onDestroyView();
    }

    @Override public boolean canSwipeRefreshChildScrollUp() {
        try {
            return mWebviewWebView.getWebScrollY() > 0;
        } catch (Exception e) {
            return true;
        }
    }

    private void goLogin() {
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        toLogin.putExtra("web", true);
        startActivityForResult(toLogin, RESULT_LOGIN);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOGIN) {
                RxBus.getBus().post(new EventFreshUnloginAd());
                initCookie(mCurUrl);
                if (mWebviewWebView != null) mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('login');");
            } else if (requestCode == 99) {
                if (mWebviewWebView != null && data != null && data.getStringExtra("web_action") != null) {
                    mWebviewWebView.loadUrl(
                        "javascript:window.nativeLinkWeb.runCallback('" + data.getStringExtra("web_action") + "','" + data.getStringExtra(
                            "json") + "');");
                }
            }
        }
    }

    public boolean canGoBack() {
        if (mWebviewWebView != null) {
            if (mWebviewWebView.canGoBack()) {
                mWebviewWebView.goBack();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface public String getToken() {
            return PreferenceUtils.getPrefString(getContext(), "token", "");
        }

        @JavascriptInterface public void shareInfo(String json) {
            try {
                ShareBean bean = new Gson().fromJson(json, ShareBean.class);
                ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link).show(getFragmentManager(), "");
            } catch (Exception e) {

            }
        }

        @JavascriptInterface public void wechatPay(String info) {

            // 将该app注册到微信
            try {
                JSONObject object = new JSONObject(info);

                PayReq request = new PayReq();
                request.appId = Configs.APP_ID;
                request.partnerId = object.getString("partnerid");
                request.prepayId = object.getString("prepayid");
                request.packageValue = "Sign=WXPay";
                request.nonceStr = object.getString("noncestr");
                request.timeStamp = object.getString("timestamp");
                request.sign = object.getString("sign");
                //                LogUtil.e("xxx:"+request.checkArgs() );
                msgApi.sendReq(request);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e("wechat pay error");
            }
        }

        @JavascriptInterface public void openDrawer() {

        }

        @JavascriptInterface public void onFinishLoad() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        if (mRefreshSwipeRefreshLayout != null) {
                            mRefreshSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }
        }

        @JavascriptInterface public void setAction(String s) {
            final ToolbarAction toolStr = new Gson().fromJson(s, ToolbarAction.class);
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        if (TextUtils.isEmpty(toolStr.name)) {
                            if (mToobarActionTextView != null) {
                                mToobarActionTextView.setText("");
                                mToobarActionTextView.setVisibility(View.GONE);
                            }
                        } else {
                            if (mToobarActionTextView != null) {

                                mToobarActionTextView.setVisibility(View.VISIBLE);
                                mToobarActionTextView.setText(toolStr.name);
                            }
                        }
                    }
                });
            }
        }

        @JavascriptInterface public void completeAction() {
            if (getActivity() != null) {
                Intent intent = new Intent();
                intent.putExtra(IntentUtils.RESULT, 0);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }

        @JavascriptInterface public void setTitle(final String s) {
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    //                    mToolbar.setTitle(s);
                    mToolbar.setTitle(s);
                }
            });
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
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    getActivity().onBackPressed();
                }
            });
        }

        @JavascriptInterface public void sensorsTrack(final String key, final String json) {
            //            getActivity().runOnUiThread(new Runnable() {
            //                @Override
            //                public void run() {
            //                    SensorsUtils.track(key, json);
            //                }
            //            });

        }

        @JavascriptInterface public String getSessionId() {
            return PreferenceUtils.getPrefString(getContext(), "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }

        @JavascriptInterface public void setArea() {// 跳转去设置
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), SettingActivity.class);
                        intent.putExtra(SettingActivity.ACTION, SettingActivity.ACTION_FIX_SELF_INFO);
                        startActivity(intent);
                    }
                });
            }
        }

        @JavascriptInterface public void goNativePath(final String s) {
            goNativePath(s, "");
        }

        @JavascriptInterface public void goNativePath(final String s, String params) {
            if (TextUtils.isEmpty(s)) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    if (s.contains("activities")) {//跳到青橙专享活动列表页
                        getActivity().finish();
                    } else if (s.contains("area")) {// 跳转去设置
                        setArea();
                    } else if (s.contains("login")) {
                        goLogin();
                    } else if (s.contains("qr_code")) {
                        try {
                            Uri qrCodeUri = Uri.parse(s);
                            String url = Uri.decode(qrCodeUri.getQueryParameter("url"));
                            String title = Uri.decode(qrCodeUri.getQueryParameter("title"));
                            String content = Uri.decode(qrCodeUri.getQueryParameter("content"));
                            new WebShowQcCodeDialogBuilder(title, url).content(content).build().show(getFragmentManager(), "");
                        } catch (Exception e) {
                            LogUtils.e(e.getMessage());
                        }
                    } else {
                        try {
                            Uri uri = Uri.parse(s);
                            Intent tosb = new Intent(Intent.ACTION_VIEW, uri);
                            if (uri.getQueryParameterNames() != null) {
                                for (String s1 : uri.getQueryParameterNames()) {
                                    tosb.putExtra(s1, uri.getQueryParameter(s1));
                                }
                            }
                            webAction = uri.getHost() + "/" + uri.getPath();
                            tosb.putExtra("web_action", uri.getHost() + "/" + uri.getPath());
                            startActivityForResult(tosb, 99);
                        } catch (Exception e) {
                            mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback(goNativePathFailed(" + s + "));");
                        }
                    }
                }
            });
        }
    }
}
