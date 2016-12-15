package com.qingchengfit.fitcoach.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.CompatUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.Utils.SensorsUitls;
import com.qingchengfit.fitcoach.Utils.ShareDialogFragment;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.bean.Contact;
import com.qingchengfit.fitcoach.bean.PayEvent;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.qingchengfit.fitcoach.bean.ShareBean;
import com.qingchengfit.fitcoach.bean.ToolbarAction;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.ChoosePictureFragmentDialog;
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

import static com.qingchengfit.fitcoach.R.id.webview;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2016/11/29.
 */

public class WebFragment extends BaseFragment implements CustomSwipeRefreshLayout.CanChildScrollUpCallback {

    @BindView(R.id.toobar_action) public TextView mToobarActionTextView;
    @BindView(R.id.toolbar) public Toolbar mToolbar;
    @BindView(R.id.toolbar_titile) public TextView mTitle;
    @BindView(webview) public WebView mWebviewWebView;
    @BindView(R.id.refresh) protected CustomSwipeRefreshLayout mRefreshSwipeRefreshLayout;
    @BindView(R.id.refresh_network) Button mRefresh;
    @BindView(R.id.no_newwork) LinearLayout mNoNetwork;
    @BindView(R.id.webview_root) RelativeLayout webviewRoot;
    @BindView(R.id.copy_link_to_wechat) TextView copyLinkToWechat;
    @BindView(R.id.go_to_how) Button goToHow;
    @BindView(R.id.guide_to_wechat_layout) public RelativeLayout guideToWechatLayout;
    @BindView(R.id.close_guide) ImageView closeGuide;
    private CookieManager cookieManager;
    private IWXAPI msgApi;
    private ChoosePictureFragmentDialog choosePictureFragmentDialog;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallbackNew;
    private String sessionid;
    public String mCurUrl;

    public static WebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mCurUrl = getArguments().getString("url");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                mWebviewWebView.addJavascriptInterface(new JsInterface(), "NativeMethod");
                mToobarActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (mWebviewWebView != null) mWebviewWebView.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
                    }
                });

                if (getArguments() != null) {
                    String url = getArguments().getString("url");

                    CookieSyncManager.createInstance(getActivity());
                    cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);

                    initCookie(url);
                    mWebviewWebView.loadUrl(url);
                    try{
                        LogUtil.e("session:"+cookieManager.getCookie(url));
                    }catch (Exception e){

                    }
                }

                mRefreshSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
                mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override public void onRefresh() {
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
                            if (mValueCallback != null)
                                mValueCallback.onReceiveValue(Uri.fromFile(new File(filePath)));
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

        RxRegiste(RxBus.getBus().register(PayEvent.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<PayEvent>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(PayEvent payEvent) {
                    if (payEvent.result == 0) {
                        if (mWebviewWebView != null) mWebviewWebView.loadUrl("javascript:window.paySuccessCallback();");
                    } else {
                        if (mWebviewWebView != null)
                            mWebviewWebView.loadUrl("javascript:window.payErrorCallback(" + payEvent.result + ");");
                    }
                }
            }));

        return view;
    }

    public void onLoadedView(){

    }
    public void onWebFinish(){

    }


    public void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.md_nav_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mTitle.setText("");
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
        webSetting.setUserAgentString(s
            + " FitnessTrainerAssistant/"
            + AppUtils.getAppVer(App.AppContex)
            + " Android  "
            + "QingchengApp/Trainer  "
            + "OEM:"
            + getString(R.string.oem_tag));
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        LogUtil.e("uA:" + webSetting.getUserAgentString());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    private void initCookie(String url) {
        sessionid = PreferenceUtils.getPrefString(getContext(), "session_id", "");

        if (sessionid != null) {
            try {
                URI uri = new URI(url);
                setCookie(uri.getHost(), "qc_session_id", sessionid);
                setCookie(uri.getHost(), "sessionid", sessionid);
                setCookie(uri.getHost(), "oem", getString(R.string.oem_tag));
                LogUtil.e("session:"+sessionid);
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

            @Override public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
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
                if (mTitle != null){
                    mTitle.setText(title);
                }
            }
        });
    }

    public void initWebClient() {
        mWebviewWebView.setWebViewClient(new WebViewClient() {

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.e(" start url:" + url);
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

                initCookie(url);
                LogUtil.d("shouldOverrideUrlLoading:" + url + " :");
                return false;
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.e("errorCode:" + errorCode);
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
        super.onDestroyView();
    }

    @Override public boolean canSwipeRefreshChildScrollUp() {
        try {
            return mWebviewWebView.getWebScrollY() > 0;
        } catch (Exception e) {
            return true;
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
                //                LogUtil.e("xxx:"+request.checkArgs() );
                msgApi.sendReq(request);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.e("wechat pay error");
            }
        }

        @JavascriptInterface public void openDrawer() {

        }
         @JavascriptInterface public void onFinishLoad() {
            if (mRefreshSwipeRefreshLayout != null){
                mRefreshSwipeRefreshLayout.setRefreshing(false);
            }
        }


        @JavascriptInterface public void setAction(String s) {
            final ToolbarAction toolStr = new Gson().fromJson(s, ToolbarAction.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                    if (TextUtils.isEmpty(toolStr.name)) {
                        mToobarActionTextView.setText("");
                        mToobarActionTextView.setVisibility(View.GONE);
                    } else {
                        mToobarActionTextView.setVisibility(View.VISIBLE);
                        mToobarActionTextView.setText(toolStr.name);
                    }
                }
            });
        }

        @JavascriptInterface public void completeAction() {
            getActivity().finish();
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

        @JavascriptInterface public void sensorsTrack(String key,String json){
            SensorsUitls.track(key,json);
        }

        @JavascriptInterface public String getSessionId() {
            return PreferenceUtils.getPrefString(getContext(), "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }

        @JavascriptInterface
        public void setArea() {// 跳转去设置
            Intent intent = new Intent();
            intent.setClass(getActivity(), SettingActivity.class);
            intent.putExtra("to", 1);
            startActivity(intent);
        }

        @JavascriptInterface
        public void goNativePath(String s) {

            if ("activities".equals(s)) {//跳到青橙专享活动列表页
                getActivity().finish();
            } else if ("area".equals(s)) {// 跳转去设置
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class);
                intent.putExtra("to", 1);
                startActivity(intent);
            }

        }


    }
}