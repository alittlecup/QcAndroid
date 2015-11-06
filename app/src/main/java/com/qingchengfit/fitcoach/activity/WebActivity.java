package com.qingchengfit.fitcoach.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
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
import com.qingchengfit.fitcoach.Utils.ShareUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.qingchengfit.fitcoach.bean.ShareBean;
import com.qingchengfit.fitcoach.bean.ToolbarAction;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.schedulers.Schedulers;

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
public class WebActivity extends BaseAcitivity implements WebActivityInterface {
    //    OriginWebFragment originWebFragment;
    private TextView mToobarActionTextView;
    private Toolbar mToolbar;
    private WebView mWebviewWebView;
    private LinearLayout mWebviewRootLinearLayout;
    private CookieManager cookieManager;


    private List<Integer> mlastPosition = new ArrayList<>(); //记录深度
    private List<String> mTitleStack = new ArrayList<>(); //记录标题
    private MaterialDialog loadingDialog;
    private ValueCallback<Uri> mValueCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mToobarActionTextView = (TextView) findViewById(R.id.toobar_action);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebviewWebView = (WebView) findViewById(R.id.webview);
        mWebviewRootLinearLayout = (LinearLayout) findViewById(R.id.webview_root);
        initWebSetting();
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mToolbar.setNavigationOnClickListener(v -> this.onBackPressed());
        mToolbar.setTitle("");
        initWebClient();
        initChromClient();
        mWebviewWebView.addJavascriptInterface(new JsInterface(), "NativeMethod");
        mToobarActionTextView.setOnClickListener(v -> {
            if (mWebviewWebView != null)
                mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
        });
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            mWebviewWebView.loadUrl(url);
            CookieSyncManager.createInstance(this);
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            initCookie(url);
////            setCookie(".qingchengfit.cn", "qc_session_id", "abcd");
//            cookieManager.setCookie("*.qingchengfit.cn","key = abc");
//            String cookieResult = cookieManager.getCookie("feature3.qingchengfit.cn");
//            LogUtil.e("  1:"+cookieResult);
//            LogUtil.e("  2:"+cookieManager.getCookie(Configs.Server));
//            LogUtil.e("  3:"+cookieManager.getCookie("www.qingchengfit.cn"));
//            LogUtil.e("  4:"+cookieManager.getCookie(".qingchengfit.cn"));
//            Toast.makeText(this,cookieResult,Toast.LENGTH_LONG).show();
        }


    }

    private void initChromClient() {
        mWebviewWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
//                super.openFileChooser(valueCallback, s, s1);
                mValueCallback = valueCallback;
                PicChooseDialog dialog = new PicChooseDialog(WebActivity.this);
                dialog.setListener(v -> {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            // 指定开启系统相机的Action
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Configs.CameraPic)));
                            startActivityForResult(intent, ChoosePicUtils.CHOOSE_CAMERA);
                        },
                        v -> {
                            //图片选择
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/jpeg");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                            } else {
                                startActivityForResult(intent, ChoosePicUtils.CHOOSE_GALLERY);
                            }
                        }

                );
                dialog.show();
            }

            private Intent createCameraIntent() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
                //=======================================================
                Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);//选择图片文件
                imageIntent.setType("image/*");
                //=======================================================
                return cameraIntent;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new MaterialDialog.Builder(WebActivity.this)
                        .title(message)
                        .cancelable(false)
                        .positiveText("Ok")
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
                        .title(message)
                        .positiveText("Ok")
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

            @Override
            public void getVisitedHistory(ValueCallback<String[]> callback) {
                super.getVisitedHistory(callback);

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
                super.onPageFinished(view, url);

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("shouldOverrideUrlLoading" + url);
                if (!TextUtils.isEmpty(mToolbar.getTitle().toString())) {
                    mTitleStack.add(mToolbar.getTitle().toString());
                    WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
                    mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                    LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.e("errorCode:" + errorCode);
                mToolbar.setTitle("");
                mWebviewWebView.loadUrl("");
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
        webSetting.setUserAgentString(webSetting.getUserAgentString() + " FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android");
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    }


    private void initCookie(String url) {
        String sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");


        if (sessionid != null) {
            try {
                URI uri = new URI(url);
                setCookie(uri.getHost(), "qc_session_id", sessionid);
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
        StringBuffer sb = new StringBuffer();
//        String oriCookie = xWalkCookieManager.getCookie(url);
//        if (oriCookie != null){
//        sb.append(xWalkCookieManager.getCookie(url));
//        sb.append(";");}
        sb.append(key);
        sb.append("=");
        sb.append(value).append(";");
        cookieManager.setCookie(url, sb.toString());
        LogUtil.e(sb.toString());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = "";
        if (mWebviewWebView == null)
            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
                filepath = FileUtils.getPath(this, data.getData());
                mValueCallback.onReceiveValue(data.getData());
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
                                mValueCallback.onReceiveValue(Uri.fromFile(upFile));
                                mValueCallback = null;
                            } else {
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

    @Override
    protected void onDestroy() {
//        originWebFragment.removeCookie();
        super.onDestroy();

    }

    public void goBack() {
        WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
        mWebviewWebView.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
        mToolbar.setTitle(mTitleStack.get(mTitleStack.size() - 1));
        mTitleStack.remove(mTitleStack.size() - 1);
        mlastPosition.remove(mlastPosition.size() - 1);
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
//        super.onBackPressed();

    }

    @Override
    public void onfinish() {

        setResult(1001);
        this.finish();
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
            ShareBean bean = new Gson().fromJson(json, ShareBean.class);
            ShareUtils.oneKeyShared(WebActivity.this, bean.title, bean.imgUrl, bean.desc, bean.title);
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
            mToolbar.setTitle(s);
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
