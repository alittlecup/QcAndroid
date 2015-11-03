package com.qingchengfit.fitcoach.fragment;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymDetailFragment extends Fragment {
    public static final String TAG = GymDetailFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gymdetail_webview)
    WebView webview;


    private int id;
    private String host;
    private List<Integer> mlastPosition = new ArrayList<>();        //记录webview位置
    private List<String> mTitleStack = new ArrayList<>();           //记录标题
    private CookieManager cookieManager;
    private MaterialDialog alertDialog;
    private boolean isPrivate;
    private MaterialDialog delDialog;
    public GymDetailFragment() {
    }

    public static GymDetailFragment newInstance(int id, String host, boolean isTag) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("host", host + "/mobile/coach/shop/welcome/");
        args.putBoolean("isPrivate", isTag);
        GymDetailFragment fragment = new GymDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void showDialog() {
        if (delDialog == null) {
            delDialog = new MaterialDialog.Builder(getContext())
                    .autoDismiss(true)
                    .title("请检查您的网络")
                    .positiveText("确定")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);

                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();
        }
        delDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            host = getArguments().getString("host");
            isPrivate = getArguments().getBoolean("isPrivate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gym_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(item -> {
            if (isPrivate) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.web_frag_layout, AddSelfGymFragment.newInstance(App.coachid))
                        .addToBackStack(null)
                        .commit();
            } else {
                if (alertDialog == null) {
                    alertDialog = new MaterialDialog.Builder(getContext())
                            .content("您不能直接编辑所属健身房信息，请联系健身房管理员在青橙后台修改")
                            .autoDismiss(true)
                            .positiveText("我知道了")
                            .build();
                }
                alertDialog.show();
            }
            return true;
        });
        webview.setWebViewClient(new WebViewClient() {

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         LogUtil.e("url:" + url);

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         LogUtil.e("url:" + url);
                                     }

                                     @Override
                                     public void onLoadResource(WebView view, String url) {
                                         super.onLoadResource(view, url);
                                     }


                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         LogUtil.e("url:" + url);
                                         if (!TextUtils.isEmpty(toolbar.getTitle().toString())) {
                                             mTitleStack.add(toolbar.getTitle().toString());
                                             WebBackForwardList webBackForwardList = webview.copyBackForwardList();
                                             mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                                             toolbar.getMenu().clear();
                                             LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                                         }
                                         return super.shouldOverrideUrlLoading(view, url);
                                     }

                                     @Override
                                     public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                                         super.onReceivedHttpError(view, request, errorResponse);
                                         toolbar.getMenu().clear();

                                     }

                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                                         super.onReceivedError(view, request, error);
                                         LogUtil.e("onReceivedError 1");
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         LogUtil.e("onReceivedError");
                                         webview.loadUrl("");
                                         showDialog();
//                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                     }
                                 }


        );


        webview.setWebChromeClient(new

                                           WebChromeClient() {


                                               @Override
                                               public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                                                   LogUtil.e("showfilechooser");
                                                   return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                                               }

                                               @Override
                                               public void onReceivedTitle(WebView view, String title) {
                                                   super.onReceivedTitle(view, title);
                                                   toolbar.setTitle(title);
                                               }

                                               @Override
                                               public void getVisitedHistory(ValueCallback<String[]> callback) {
                                                   super.getVisitedHistory(callback);

                                               }
                                           }

        );
        webview.getSettings().
                setJavaScriptEnabled(true);

        String s = webview.getSettings().getUserAgentString();
        webview.getSettings().

                setUserAgentString(s + " FitnessTrainerAssistant/0.2.5" + " Android");
//        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
//         开启DOM storage API 功能
//        webview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
//        webview.getSettings().setDatabaseEnabled(true);
//        String cacheDirPath = Configs.ExternalCache;
//        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
//        webview.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
//        webview.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
//        webview.getSettings().setAppCacheEnabled(true);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        initCookie();

        webview.loadUrl(host);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeCookie();
        webview.destroy();
        ButterKnife.unbind(this);
    }


    public void removeCookie() {
        if (cookieManager != null) {
            if (Build.VERSION.SDK_INT < 21) {
                cookieManager.removeAllCookie();
            } else {
                cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean value) {

                    }
                });
            }
        }
    }

    private void initCookie() {
        String sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
        if (sessionid != null) {
            setCookie(Configs.ServerIp, "sessionid", sessionid);
            setCookie("http://192.168.31.108", "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
        } else {
            //TODO logout
        }
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(key);
        sb.append("=");
        sb.append(value);
        cookieManager.setCookie(url, sb.toString());
    }

    public Boolean canGoBack() {
        if (webview != null) {
            return mlastPosition.size() > 0;
        } else return false;
    }

    public void goBack() {
        WebBackForwardList webBackForwardList = webview.copyBackForwardList();
        webview.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
        toolbar.setTitle(mTitleStack.get(mTitleStack.size() - 1));
        mTitleStack.remove(mTitleStack.size() - 1);
        mlastPosition.remove(mlastPosition.size() - 1);
        if (!canGoBack()) {
            toolbar.inflateMenu(R.menu.menu_edit);
        }
    }

}
