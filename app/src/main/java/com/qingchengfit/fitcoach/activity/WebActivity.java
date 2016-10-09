package com.qingchengfit.fitcoach.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.BitmapUtils;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.ShareDialogFragment;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.PayEvent;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.qingchengfit.fitcoach.bean.ShareBean;
import com.qingchengfit.fitcoach.bean.ToolbarAction;
import com.qingchengfit.fitcoach.component.CustomSwipeRefreshLayout;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.if
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/12 2015.
 */
public class WebActivity extends BaseAcitivity implements WebActivityInterface, CustomSwipeRefreshLayout.CanChildScrollUpCallback {
    //    OriginWebFragment originWebFragment;
    private TextView mToobarActionTextView;
    private Toolbar mToolbar;
    private WebView mWebviewWebView;
    //    private LinearLayout mWebviewRootLinearLayout;
    private CookieManager cookieManager;
    private LinearLayout mNoNetwork;
    private Button mRefresh;

    private List<Integer> mlastPosition = new ArrayList<>(); //记录深度
    private List<String> mTitleStack = new ArrayList<>(); //记录标题
    private MaterialDialog loadingDialog;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallbackNew;
    private PicChooseDialog dialog;
    private List<String> hostArray = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    private String sessionid;
    private IWXAPI msgApi;
    private String test = "{\'appId\': \'wx81e378c8fd03319d\',\'nonceStr\': \'IvGxLujqa73veSM\',\'package\': \'Sign=WXPay\',\'partnerId \': \'1316532101\',\'paySign\': \'205F1707DD8379C0DA1782F7F9BEA2F8\',\'prepayId\': \'wx20160226124520229c7c8edf0039065235\',\'timeStamp\': \'1456462707\'}";
    private Subscription paySp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mToobarActionTextView = (TextView) findViewById(R.id.toobar_action);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebviewWebView = (WebView) findViewById(R.id.webview);
        mRefreshSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh);
//        mWebviewRootLinearLayout = (LinearLayout) findViewById(R.id.webview_root);
        mNoNetwork = (LinearLayout) findViewById(R.id.no_newwork);
        mRefresh = (Button) findViewById(R.id.refresh_network);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoNetwork.setVisibility(View.GONE);
                mWebviewWebView.reload();
            }
        });
        mRefreshSwipeRefreshLayout.setCanChildScrollUpCallback(this);
        initWebSetting();
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
//            mWebviewWebView.loadUrl("javascript:NativeMethod.wechatPay(\"" + test + "\");");
//                msgApi.openWXApp();
        });
        mToolbar.setTitle("");
        initWebClient();
        initChromClient();
        mWebviewWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebviewWebView.addJavascriptInterface(new JsInterface(), "NativeMethod");
        mToobarActionTextView.setOnClickListener(v -> {
            if (mWebviewWebView != null)
                mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
        });

        String hosts = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "hostarray", "");
        if (!TextUtils.isEmpty(hosts)) {
            hostArray = new Gson().fromJson(hosts, new TypeToken<ArrayList<String>>() {
            }.getType());
        }

        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            if (!url.startsWith("http"))
                url = "http://"+url;
            CookieSyncManager.createInstance(this);
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);

            initCookie(url);
            mWebviewWebView.loadUrl(url);
////            setCookie(".qingchengfit.cn", "qc_session_id", "abcd");
//            cookieManager.setCookie("*.qingchengfit.cn","key = abc");
//            String cookieResult = cookieManager.getCookie("feature3.qingchengfit.cn");
//            LogUtil.e("  1:"+cookieResult);
//            LogUtil.e("  2:"+cookieManager.getCookie(Configs.Server));
//            LogUtil.e("  3:"+cookieManager.getCookie("www.qingchengfit.cn"));
//            LogUtil.e("  4:"+cookieManager.getCookie(".qingchengfit.cn"));
//            Toast.makeText(this,cookieResult,Toast.LENGTH_LONG).show();
        }

        mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebviewWebView.reload();
            }
        });
        mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRefreshSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mRefreshSwipeRefreshLayout.setRefreshing(true);
            }
        });

        msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), getString(R.string.wechat_code));
        msgApi.registerApp(getString(R.string.wechat_code));

        paySp = RxBus.getBus().register(PayEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PayEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PayEvent payEvent) {
                        if (payEvent.result == 0) {
                            if (mWebviewWebView != null)
                                mWebviewWebView.loadUrl("javascript:window.paySuccessCallback();");
                        } else {
                            if (mWebviewWebView != null)
                                mWebviewWebView.loadUrl("javascript:window.payErrorCallback(" + payEvent.result + ");");
                        }
                    }
                })
        ;


        if (dialog == null) {
            dialog = new PicChooseDialog(WebActivity.this);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {


                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mValueCallback != null)
                        mValueCallback.onReceiveValue(null);
                    if (mValueCallbackNew != null)
                        mValueCallbackNew.onReceiveValue(null);
                }
            });
            dialog.setListener(v -> {
                        dialog.dismiss();
                        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.CAMERA)) {
                            Intent intent = new Intent();
                            // 指定开启系统相机的Action
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
                        } else ToastUtils.showDefaultStyle("请开启拍照权限");
                    },
                    v -> {
                        //图片选择
                        dialog.dismiss();
                        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/jpeg");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                            } else {
                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);

                            }
                        } else ToastUtils.showDefaultStyle("请开启外部存储权限");
                    }

            );
        }

    }

    private void initChromClient() {

        mWebviewWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
//                super.openFileChooser(valueCallback, s, s1);
                mValueCallback = valueCallback;

                dialog.show();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
                mValueCallbackNew = valueCallback;
                dialog.show();
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new MaterialDialog.Builder(WebActivity.this)
                        .content(message)
                        .cancelable(false)
                        .positiveText(R.string.common_i_konw)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                result.confirm();
                            }
                        })
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                new MaterialDialog.Builder(WebActivity.this)
                        .content(message)
                        .positiveText("确定")
                        .negativeText("取消")
                        .cancelable(false)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                result.confirm();
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                result.cancel();
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);

            }

        });

    }

    private void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.e("url:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mRefreshSwipeRefreshLayout.setRefreshing(false);
                super.onPageFinished(view, url);

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("shouldOverrideUrlLoading:" + url + " :");

                if (!TextUtils.isEmpty(mToolbar.getTitle().toString())) {
                    mToobarActionTextView.setText("");
                    mToobarActionTextView.setVisibility(View.GONE);
                    URI uri = null;
                    try {
                        uri = new URI(url);
                        LogUtil.e("host contains   " + hostArray.toString());
                        if (!hostArray.contains(uri.getHost())) {
                            hostArray.add(uri.getHost());
                        }
                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
                        setCookie(uri.getHost(), "qc_session_id", sessionid);
                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
                        setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
                    } catch (URISyntaxException e) {

                    }
                    mTitleStack.add(mToolbar.getTitle().toString());
                    WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();

                    if (uri != null) {
                        String path = uri.getHost() + uri.getPath();
                        if (!path.endsWith("/")) {
                            return false;
                        }
                        if (urls.contains(path)) {
                            int step = urls.size() - urls.indexOf(path);
                            mlastPosition = mlastPosition.subList(0, urls.indexOf(path));
                            mlastPosition.add(webBackForwardList.getCurrentIndex() - step + 1);
                            urls = urls.subList(0, urls.indexOf(path));
//                                                     mTitleStack = mTitleStack.subList(0,urls.indexOf(path)+1);
                        } else {
                            mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                        }
                        urls.add(path);

                    } else {
                        mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                    }


                    LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.e("errorCode:" + errorCode);
                mToolbar.setTitle("");
//                mWebviewWebView.loadUrl("");
                showNoNet();

            }


        });

    }

    public Boolean canGoBack() {

        if (mWebviewWebView != null) {
            return mlastPosition.size() > 0;
        } else return false;
    }


    /**
     * 显示无网络状态
     */
    private void showNoNet() {
        mNoNetwork.setVisibility(View.VISIBLE);
    }

    private void initWebSetting() {
        WebStorage webStorage = WebStorage.getInstance();
        // TODO Auto-generated constructor stub
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
        webSetting.setUserAgentString(webSetting.getUserAgentString() + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android  OEM:" + getString(R.string.oem_tag));
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        LogUtil.e("uA:" + webSetting.getUserAgentString());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    }


    private void initCookie(String url) {
        sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
        LogUtil.e("initCookie:" + sessionid);
        if (sessionid != null) {
            try {
                URI uri = new URI(url);
                if (uri.getHost() != null && !hostArray.contains(uri.getHost())) {
                    hostArray.add(uri.getHost());
                    if (cookieManager != null) {
                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
                    }
                }
                setCookie(uri.getHost(), "qc_session_id", sessionid);
                setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
            } catch (URISyntaxException e) {
                //e.printStackTrace();
            }
            setCookie(Configs.ServerIp, "sessionid", sessionid);
//            setCookie("http://192.168.31.108", "qc_session_id", sessionid);
//            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
//            setCookie(".qingchengfit.cn", "qc_session_id", sessionid);
//            setCookie(".cn", "qc_session_id", sessionid);
//            setCookie(".com", "qc_session_id", sessionid);
//            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
        } else {
            //TODO logout
        }
    }

    public void setCookie(String url, String key, String value) {
        if (TextUtils.isEmpty(url))
            return;
        StringBuffer sb = new StringBuffer();
//        String oriCookie = xWalkCookieManager.getCookie(url);
//        if (oriCookie != null){
//        sb.append(xWalkCookieManager.getCookie(url));
//        sb.append(";");}
        sb.append(key);
        sb.append("=");
        sb.append(value).append(";");
        if (cookieManager != null)
            cookieManager.setCookie(url, sb.toString());
//        LogUtil.e(sb.toString());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = "";
        if (mWebviewWebView == null)
            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
                filepath = FileUtils.getPath(this, data.getData());
                if (mValueCallback != null)
                    mValueCallback.onReceiveValue(data.getData());
                if (mValueCallbackNew != null) {
                    Uri[] uris = new Uri[1];
                    uris[0] = data.getData();
                    mValueCallbackNew.onReceiveValue(uris);
                }
                return;
            } else filepath = Configs.CameraPic;
            LogUtil.d(filepath);
            ShowLoading("正在上传");
            Observable.just(filepath)
                    .subscribeOn(Schedulers.io())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
                        File upFile = new File(Configs.ExternalCache + filename);

//                        boolean reslut = UpYunClient.upLoadImg("/webup/" + App.coachid + "/", filename, upFile);
                        runOnUiThread(() -> {
                            loadingDialog.dismiss();
                            if (upFile.exists()) {
                                ToastUtils.show("上传图片成功");

//                                mValueCallback.onReceiveValue(Uri.fromFile(upFile));
                                if (mValueCallback != null)
                                    mValueCallback.onReceiveValue(Uri.fromFile(upFile));
                                if (mValueCallbackNew != null) {
                                    Uri[] uris = new Uri[1];
                                    uris[0] = Uri.fromFile(upFile);
                                    mValueCallbackNew.onReceiveValue(uris);
                                }
                                mValueCallback = null;
                                mValueCallbackNew = null;
                            } else {
                                if (mValueCallback != null)
                                    mValueCallback.onReceiveValue(null);
                                if (mValueCallbackNew != null) {
                                    mValueCallbackNew.onReceiveValue(null);
                                }
                                ToastUtils.show(R.drawable.ic_share_fail, "上传图片失败");
                            }
//                            if (reslut) {
//                                LogUtil.d("success");
//
//
//                            } else {
//                                ToastUtils.show(R.drawable.ic_share_fail,"资源服务器错误");
//                            }
                        });

                    });

        } else {
            if (mValueCallback != null)
                mValueCallback.onReceiveValue(null);
            if (mValueCallbackNew != null)
                mValueCallbackNew.onReceiveValue(null);
        }
    }

    public void ShowLoading(String content) {
        if (loadingDialog == null)
            loadingDialog = new MaterialDialog.Builder(this)
                    .content("请稍后")
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        if (content != null)
            loadingDialog.setContent(content);
        loadingDialog.show();
    }

    public void removeCookies() {

        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "hostarray", new Gson().toJson(hostArray));

        for (String host : hostArray) {
            cookieManager.setCookie(host, "sessionid" + "=" + ";expires=Mon, 03 Jun 0000 07:01:29 GMT;");
        }

    }

    @Override
    protected void onDestroy() {
        dialog = null;
        mValueCallback = null;
        mValueCallbackNew = null;
        if (mWebviewWebView != null) {
            removeCookies();
            mRefreshSwipeRefreshLayout.removeView(mWebviewWebView);
            mWebviewWebView.removeAllViews();
            mWebviewWebView.destroy();
        }
        if (paySp != null)
            paySp.unsubscribe();
        super.onDestroy();

    }

    public void goBack() {
        WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
        LogUtil.e("goback:" + (mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1));
        mWebviewWebView.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
        mToolbar.setTitle(mTitleStack.get(mTitleStack.size() - 1));
        mTitleStack.remove(mTitleStack.size() - 1);
        mlastPosition.remove(mlastPosition.size() - 1);
        mToobarActionTextView.setText("");
    }


    @Override
    public void onBackPressed() {
        if (canGoBack()) {
//            originWebFragment.goBack();
            goBack();
        } else {
            setResult(-1);
            this.finish();
        }

    }

    @Override
    public void onfinish() {

        setResult(1001);
        this.finish();
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        try {
            boolean result = mWebviewWebView.getWebScrollY() > 0;
            return result;
        } catch (Exception e) {
            return true;
        }

    }

    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return PreferenceUtils.getPrefString(App.AppContex, "token", "");
        }

        @JavascriptInterface
        public void shareInfo(String json) {
            LogUtil.e(json);
            try {

                ShareBean bean = new Gson().fromJson(json, ShareBean.class);
//                ShareUtils.oneKeyShared(WebActivity.this, bean.link, bean.imgUrl, bean.desc, ben.title);
                ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link).show(getSupportFragmentManager(), "");
            } catch (Exception e) {

            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });

        }

        @JavascriptInterface
        public void wechatPay(String info) {
            LogUtil.d(info);

            // 将该app注册到微信
            try {
                JSONObject object = new JSONObject(info);

                PayReq request = new PayReq();
//                request.appId = Configs.APP_ID;
                request.appId = getString(R.string.wechat_code);
                request.partnerId = object.getString("partnerid");
//                request.partnerId = "1316532101";
//                request.prepayId = "wx201602261807466f5480e7010494724957";
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr = "4VNwF2PFwgXCbmr";
//                request.timeStamp = "1456481266";//MD5.genTimeStamp() + "";
//                request.sign = "053EB9B1AD8487A3008DFE2035D774A9";//MD5.getSign(request.timeStamp, request.nonceStr);
//                request.partnerId = object.getString("partnerId");

                request.prepayId = object.getString("prepayid");
                request.packageValue = "Sign=WXPay";
                request.nonceStr = object.getString("noncestr");
                request.timeStamp = object.getString("timestamp");
                request.sign = object.getString("sign");
                LogUtil.e("xxx:" + request.checkArgs());
                msgApi.sendReq(request);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.e("wechat pay error");
            }

        }

        @JavascriptInterface
        public void openDrawer() {


        }

        @JavascriptInterface
        public void setAction(String s) {
            LogUtil.e("setAction:" + s);
            ToolbarAction toolStr = new Gson().fromJson(s, ToolbarAction.class);

            runOnUiThread(() -> {
                if (TextUtils.isEmpty(toolStr.name)) {
                    mToobarActionTextView.setText("");
                    mToobarActionTextView.setVisibility(View.GONE);
                } else {
                    mToobarActionTextView.setVisibility(View.VISIBLE);
                    mToobarActionTextView.setText(toolStr.name);

                }

            });
        }

        @JavascriptInterface
        public void completeAction() {
            onfinish();
        }

        @JavascriptInterface
        public void setTitle(String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToolbar.setTitle(s);
                }
            });

        }


        @JavascriptInterface
        public String getContacts() {
            List<Contact> contacts = PhoneFuncUtils.initContactList(WebActivity.this);
            Gson gson = new Gson();
            return gson.toJson(contacts);
        }

        @JavascriptInterface
        public String getPlatform() {
            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(WebActivity.this));
            Gson gson = new Gson();
            return gson.toJson(info);
        }

        @JavascriptInterface
        public void goBack() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            });
        }

        @JavascriptInterface
        public String getSessionId() {
            return PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }

    }


}
