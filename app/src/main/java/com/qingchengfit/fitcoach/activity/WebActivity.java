package com.qingchengfit.fitcoach.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.WebFragment;

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
public class WebActivity extends BaseAcitivity {

    private WebFragment webfrag;

    public static void startWeb(String url, Context context) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
            SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.
            SOFT_INPUT_STATE_HIDDEN);

        //if (!CompatUtils.less21()) {
        //    Window window = getWindow();
        //    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        //        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //        window.setStatusBarColor(Color.TRANSPARENT);
        //        window.setNavigationBarColor(Color.BLACK);
        //    }
        //}
        setContentView(R.layout.activity_frag);
        webfrag = WebFragment.newInstance(getIntent().getStringExtra("url"));
        getSupportFragmentManager().beginTransaction().replace(R.id.web_frag_layout, webfrag).commit();
    }

    public void onNewWeb(String url) {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            .add(R.id.web_frag_layout, WebFragment.newInstance(url))
            .addToBackStack("")
            .commit();
    }

    @Override public void onBackPressed() {
        if (webfrag != null && webfrag.canGoBack()) {

        } else {
            super.onBackPressed();
        }
    }

    //
    //    private TextView mToobarActionTextView;
    //    private Toolbar mToolbar;
    //    private WebView mWebviewWebView;
    //    private CookieManager cookieManager;
    //    private LinearLayout mNoNetwork;
    //    private Button mRefresh;
    //
    //    private List<Integer> mlastPosition = new ArrayList<>(); //记录深度
    //    private List<String> mTitleStack = new ArrayList<>(); //记录标题
    //    private MaterialDialog loadingDialog;
    //    private ValueCallback<Uri> mValueCallback;
    //    private ValueCallback<Uri[]> mValueCallbackNew;
    //    private PicChooseDialog dialog;
    //    private List<String> hostArray = new ArrayList<>();
    //    private List<String> urls = new ArrayList<>();
    //    private CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    //    private String sessionid;
    //    private IWXAPI msgApi;
    //    private String test = "{\'appId\': \'wx81e378c8fd03319d\',\'nonceStr\': \'IvGxLujqa73veSM\',\'package\': \'Sign=WXPay\',\'partnerId \': \'1316532101\',\'paySign\': \'205F1707DD8379C0DA1782F7F9BEA2F8\',\'prepayId\': \'wx20160226124520229c7c8edf0039065235\',\'timeStamp\': \'1456462707\'}";
    //    private Subscription paySp;
    //
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_web);
    //        mToobarActionTextView = (TextView) findViewById(R.id.toobar_action);
    //        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    //        mWebviewWebView = (WebView) findViewById(R.id.webview);
    //        mRefreshSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.refresh);
    //        mNoNetwork = (LinearLayout) findViewById(R.id.no_newwork);
    //        mRefresh = (Button) findViewById(R.id.refresh_network);
    //        mRefresh.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                mNoNetwork.setVisibility(View.GONE);
    //                mWebviewWebView.reload();
    //            }
    //        });
    //        mRefreshSwipeRefreshLayout.setCanChildScrollUpCallback(this);
    //        initWebSetting();
    //        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    //        mToolbar.setNavigationOnClickListener(v -> {
    //            onBackPressed();
    //        });
    //        mToolbar.setTitle("");
    //        initWebClient();
    //        initChromClient();
    //        mWebviewWebView.setOnLongClickListener(new View.OnLongClickListener() {
    //            @Override
    //            public boolean onLongClick(View v) {
    //                return true;
    //            }
    //        });
    //        mWebviewWebView.addJavascriptInterface(new JsInterface(), "NativeMethod");
    //        mToobarActionTextView.setOnClickListener(v -> {
    //            if (mWebviewWebView != null)
    //                mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
    //        });
    //
    //        String hosts = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "hostarray", "");
    //        if (!TextUtils.isEmpty(hosts)) {
    //            hostArray = new Gson().fromJson(hosts, new TypeToken<ArrayList<String>>() {
    //            }.getType());
    //        }
    //
    //        if (getIntent() != null && getIntent().getStringExtra("url") != null) {
    //            String url = getIntent().getStringExtra("url");
    //            if (!url.startsWith("http"))
    //                url = "http://"+url;
    //            CookieSyncManager.createInstance(this);
    //            cookieManager = CookieManager.getInstance();
    //            cookieManager.setAcceptCookie(true);
    //
    //            initCookie(url);
    //            mWebviewWebView.loadUrl(url);
    //        }
    //
    //        mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
    //        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    //            @Override
    //            public void onRefresh() {
    //                mWebviewWebView.reload();
    //            }
    //        });
    //        mRefreshSwipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    //            @Override
    //            public void onGlobalLayout() {
    //                mRefreshSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    //                mRefreshSwipeRefreshLayout.setRefreshing(true);
    //            }
    //        });
    //
    //        msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), getString(R.string.wechat_code));
    //        msgApi.registerApp(getString(R.string.wechat_code));
    //
    //        paySp = RxBus.getBus().register(PayEvent.class)
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
    //                            if (mWebviewWebView != null)
    //                                mWebviewWebView.loadUrl("javascript:window.paySuccessCallback();");
    //                        } else {
    //                            if (mWebviewWebView != null)
    //                                mWebviewWebView.loadUrl("javascript:window.payErrorCallback(" + payEvent.result + ");");
    //                        }
    //                    }
    //                })
    //        ;
    //
    //
    //        if (dialog == null) {
    //            dialog = new PicChooseDialog(WebActivity.this);
    //            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    //                @Override
    //                public void onDismiss(DialogInterface dialog) {
    //
    //
    //                }
    //            });
    //            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
    //                @Override
    //                public void onCancel(DialogInterface dialog) {
    //                    if (mValueCallback != null)
    //                        mValueCallback.onReceiveValue(null);
    //                    if (mValueCallbackNew != null)
    //                        mValueCallbackNew.onReceiveValue(null);
    //                }
    //            });
    //            dialog.setListener(v -> {
    //                        dialog.dismiss();
    //                        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.CAMERA)) {
    //                            Intent intent = new Intent();
    //                            // 指定开启系统相机的Action
    //                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    //                            intent.addCategory(Intent.CATEGORY_DEFAULT);
    //                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
    //                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
    //                        } else ToastUtils.showDefaultStyle("请开启拍照权限");
    //                    },
    //                    v -> {
    //                        //图片选择
    //                        dialog.dismiss();
    //                        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    //                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
    //                            intent.addCategory(Intent.CATEGORY_OPENABLE);
    //                            intent.setType("image/jpeg");
    //                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
    //                            } else {
    //                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
    //
    //                            }
    //                        } else ToastUtils.showDefaultStyle("请开启外部存储权限");
    //                    }
    //
    //            );
    //        }
    //
    //    }
    //
    //    private void initChromClient() {
    //
    //        mWebviewWebView.setWebChromeClient(new WebChromeClient() {
    //            @Override
    //            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
    //                mValueCallback = valueCallback;
    //
    //                dialog.show();
    //            }
    //
    //            @Override
    //            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
    //                mValueCallbackNew = valueCallback;
    //                dialog.show();
    //                return true;
    //            }
    //
    //            @Override
    //            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
    //                new MaterialDialog.Builder(WebActivity.this)
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
    //            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
    //                new MaterialDialog.Builder(WebActivity.this)
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
    //                mToolbar.setTitle(title);
    //
    //            }
    //
    //        });
    //
    //    }
    //
    //    private void initWebClient() {
    //        mWebviewWebView.setWebViewClient(new WebViewClient() {
    //
    //            @Override
    //            public void onPageStarted(WebView view, String url, Bitmap favicon) {
    //                super.onPageStarted(view, url, favicon);
    //                LogUtil.e("url:" + url);
    //            }
    //
    //            @Override
    //            public void onPageFinished(WebView view, String url) {
    //                mRefreshSwipeRefreshLayout.setRefreshing(false);
    //                super.onPageFinished(view, url);
    //
    //            }
    //
    //            @Override
    //            public void onLoadResource(WebView view, String url) {
    //                super.onLoadResource(view, url);
    //            }
    //
    //
    //            @Override
    //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //                LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
    //
    //                if (!TextUtils.isEmpty(mToolbar.getTitle().toString())) {
    //                    mToobarActionTextView.setText("");
    //                    mToobarActionTextView.setVisibility(View.GONE);
    //                    URI uri = null;
    //                    try {
    //                        uri = new URI(url);
    //                        LogUtil.e("host contains   " + hostArray.toString());
    //                        if (!hostArray.contains(uri.getHost())) {
    //                            hostArray.add(uri.getHost());
    //                        }
    //                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
    //                        setCookie(uri.getHost(), "qc_session_id", sessionid);
    //                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
    //                        setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
    //                    } catch (URISyntaxException e) {
    //
    //                    }
    //                    mTitleStack.add(mToolbar.getTitle().toString());
    //                    WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
    //
    //                    if (uri != null) {
    //                        String path = uri.getHost() + uri.getPath();
    //                        if (!path.endsWith("/")) {
    //                            return false;
    //                        }
    //                        if (urls.contains(path)) {
    //                            int step = urls.size() - urls.indexOf(path);
    //                            mlastPosition = mlastPosition.subList(0, urls.indexOf(path));
    //                            mlastPosition.add(webBackForwardList.getCurrentIndex() - step + 1);
    //                            urls = urls.subList(0, urls.indexOf(path));
    ////                                                     mTitleStack = mTitleStack.subList(0,urls.indexOf(path)+1);
    //                        } else {
    //                            mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
    //                        }
    //                        urls.add(path);
    //
    //                    } else {
    //                        mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
    //                    }
    //
    //
    //                    LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
    //                }
    //                return super.shouldOverrideUrlLoading(view, url);
    //            }
    //
    //            @Override
    //            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    //                LogUtil.e("errorCode:" + errorCode);
    //                mToolbar.setTitle("");
    //                showNoNet();
    //
    //            }
    //
    //
    //        });
    //
    //    }
    //
    //    public Boolean canGoBack() {
    //
    //        if (mWebviewWebView != null) {
    //            return mlastPosition.size() > 0;
    //        } else return false;
    //    }
    //
    //
    //    /**
    //     * 显示无网络状态
    //     */
    //    private void showNoNet() {
    //        mNoNetwork.setVisibility(View.VISIBLE);
    //    }
    //
    //    private void initWebSetting() {
    //        WebStorage webStorage = WebStorage.getInstance();
    //        WebSettings webSetting = mWebviewWebView.getSettings();
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
    //        webSetting.setUserAgentString(webSetting.getUserAgentString() + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android  OEM:" + getString(R.string.oem_tag));
    //        LogUtil.e("uA:" + webSetting.getUserAgentString());
    //        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
    //        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    //        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    //    }
    //
    //
    //    private void initCookie(String url) {
    //        sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
    //        LogUtil.e("initCookie:" + sessionid);
    //        if (sessionid != null) {
    //            try {
    //                URI uri = new URI(url);
    //                if (uri.getHost() != null ) {
    //                    //hostArray.add(uri.getHost());
    //                    if (cookieManager != null) {
    //                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
    //                        LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
    //                    }
    //                }
    //                setCookie(uri.getHost(), "qc_session_id", sessionid);
    //                setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
    //            } catch (URISyntaxException e) {
    //                //e.printStackTrace();
    //            }
    //            setCookie(Configs.ServerIp, "sessionid", sessionid);
    //        } else {
    //            //TODO logout
    //        }
    //    }
    //
    //    public void setCookie(String url, String key, String value) {
    //        if (TextUtils.isEmpty(url))
    //            return;
    //        StringBuffer sb = new StringBuffer();
    //        sb.append(key);
    //        sb.append("=");
    //        sb.append(value).append(";");
    //        if (cookieManager != null)
    //            cookieManager.setCookie(url, sb.toString());
    //    }
    //
    //
    //    @Override
    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        String filepath = "";
    //        if (mWebviewWebView == null)
    //            return;
    //        if (resultCode == Activity.RESULT_OK) {
    //            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
    //                filepath = FileUtils.getPath(this, data.getData());
    //                if (mValueCallback != null)
    //                    mValueCallback.onReceiveValue(data.getData());
    //                if (mValueCallbackNew != null) {
    //                    Uri[] uris = new Uri[1];
    //                    uris[0] = data.getData();
    //                    mValueCallbackNew.onReceiveValue(uris);
    //                }
    //                return;
    //            } else filepath = Configs.CameraPic;
    //            LogUtil.d(filepath);
    //            ShowLoading("正在上传");
    //            Observable.just(filepath)
    //                    .subscribeOn(Schedulers.io())
    //                    .subscribe(s -> {
    //                        String filename = UUID.randomUUID().toString();
    //                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
    //                        File upFile = new File(Configs.ExternalCache + filename);
    //                        runOnUiThread(() -> {
    //                            loadingDialog.dismiss();
    //                            if (upFile.exists()) {
    //                                ToastUtils.show("上传图片成功");
    //                                if (mValueCallback != null)
    //                                    mValueCallback.onReceiveValue(Uri.fromFile(upFile));
    //                                if (mValueCallbackNew != null) {
    //                                    Uri[] uris = new Uri[1];
    //                                    uris[0] = Uri.fromFile(upFile);
    //                                    mValueCallbackNew.onReceiveValue(uris);
    //                                }
    //                                mValueCallback = null;
    //                                mValueCallbackNew = null;
    //                            } else {
    //                                if (mValueCallback != null)
    //                                    mValueCallback.onReceiveValue(null);
    //                                if (mValueCallbackNew != null) {
    //                                    mValueCallbackNew.onReceiveValue(null);
    //                                }
    //                                ToastUtils.show(R.drawable.ic_share_fail, "上传图片失败");
    //                            }
    //                        });
    //
    //                    });
    //
    //        } else {
    //            if (mValueCallback != null)
    //                mValueCallback.onReceiveValue(null);
    //            if (mValueCallbackNew != null)
    //                mValueCallbackNew.onReceiveValue(null);
    //        }
    //    }
    //
    //    public void ShowLoading(String content) {
    //        if (loadingDialog == null)
    //            loadingDialog = new MaterialDialog.Builder(this)
    //                    .content("请稍后")
    //                    .progress(true, 0)
    //                    .cancelable(false)
    //                    .build();
    //        if (content != null)
    //            loadingDialog.setContent(content);
    //        loadingDialog.show();
    //    }
    //
    //    public void removeCookies() {
    //
    //        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "hostarray", new Gson().toJson(hostArray));
    //
    //        for (String host : hostArray) {
    //            cookieManager.setCookie(host, "sessionid" + "=" + ";expires=Mon, 03 Jun 0000 07:01:29 GMT;");
    //        }
    //
    //    }
    //
    //    @Override
    //    protected void onDestroy() {
    //        dialog = null;
    //        mValueCallback = null;
    //        mValueCallbackNew = null;
    //        if (mWebviewWebView != null) {
    //            removeCookies();
    //            mRefreshSwipeRefreshLayout.removeView(mWebviewWebView);
    //            mWebviewWebView.removeAllViews();
    //            mWebviewWebView.destroy();
    //        }
    //        if (paySp != null)
    //            paySp.unsubscribe();
    //        super.onDestroy();
    //
    //    }
    //
    //    public void goBack() {
    //        WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
    //        LogUtil.e("goback:" + (mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1));
    //        mWebviewWebView.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
    //        mToolbar.setTitle(mTitleStack.get(mTitleStack.size() - 1));
    //        mTitleStack.remove(mTitleStack.size() - 1);
    //        mlastPosition.remove(mlastPosition.size() - 1);
    //        mToobarActionTextView.setText("");
    //    }
    //
    //
    //    @Override
    //    public void onBackPressed() {
    //        if (canGoBack()) {
    ////            originWebFragment.goBack();
    //            goBack();
    //        } else {
    //            setResult(-1);
    //            this.finish();
    //        }
    //
    //    }
    //
    //    @Override
    //    public void onfinish() {
    //
    //        setResult(1001);
    //        this.finish();
    //    }
    //
    //    @Override
    //    public boolean canSwipeRefreshChildScrollUp() {
    //        try {
    //            boolean result = mWebviewWebView.getWebScrollY() > 0;
    //            return result;
    //        } catch (Exception e) {
    //            return true;
    //        }
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
    //            return PreferenceUtils.getPrefString(App.AppContex, "token", "");
    //        }
    //
    //        @JavascriptInterface
    //        public void shareInfo(String json) {
    //            LogUtil.e(json);
    //            try {
    //
    //                ShareBean bean = new Gson().fromJson(json, ShareBean.class);
    //                ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link).show(getSupportFragmentManager(), "");
    //            } catch (Exception e) {
    //
    //            }
    //
    //        }
    //
    //        @JavascriptInterface
    //        public void wechatPay(String info) {
    //            LogUtil.d(info);
    //            // 将该app注册到微信
    //            try {
    //                JSONObject object = new JSONObject(info);
    //
    //                PayReq request = new PayReq();
    //                request.appId = getString(R.string.wechat_code);
    //                request.partnerId = object.getString("partnerid");
    //
    //                request.prepayId = object.getString("prepayid");
    //                request.packageValue = "Sign=WXPay";
    //                request.nonceStr = object.getString("noncestr");
    //                request.timeStamp = object.getString("timestamp");
    //                request.sign = object.getString("sign");
    //                LogUtil.e("xxx:" + request.checkArgs());
    //                msgApi.sendReq(request);
    //            } catch (JSONException e) {
    //                e.printStackTrace();
    //                LogUtil.e("wechat pay error");
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
    //            LogUtil.e("setAction:" + s);
    //            ToolbarAction toolStr = new Gson().fromJson(s, ToolbarAction.class);
    //
    //            runOnUiThread(() -> {
    //                if (TextUtils.isEmpty(toolStr.name)) {
    //                    mToobarActionTextView.setText("");
    //                    mToobarActionTextView.setVisibility(View.GONE);
    //                } else {
    //                    mToobarActionTextView.setVisibility(View.VISIBLE);
    //                    mToobarActionTextView.setText(toolStr.name);
    //
    //                }
    //
    //            });
    //        }
    //
    //        @JavascriptInterface
    //        public void completeAction() {
    //            onfinish();
    //        }
    //
    //        @JavascriptInterface
    //        public void setTitle(String s) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    mToolbar.setTitle(s);
    //                }
    //            });
    //
    //        }
    //
    //
    //        @JavascriptInterface
    //        public String getContacts() {
    //            List<Contact> contacts = PhoneFuncUtils.initContactList(WebActivity.this);
    //            Gson gson = new Gson();
    //            return gson.toJson(contacts);
    //        }
    //
    //        @JavascriptInterface
    //        public String getPlatform() {
    //            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(WebActivity.this));
    //            Gson gson = new Gson();
    //            return gson.toJson(info);
    //        }
    //
    //        @JavascriptInterface
    //        public void goBack() {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    onBackPressed();
    //                }
    //            });
    //        }
    //
    //        @JavascriptInterface
    //        public String getSessionId() {
    //            return PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
    //        }
    //
    //        @JavascriptInterface
    //        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {
    //
    //        }
    //
    //    }
    //
}
