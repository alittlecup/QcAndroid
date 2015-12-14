package com.qingchengfit.fitcoach.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.BitmapUtils;
import com.paper.paperbaselibrary.utils.ChoosePicUtils;
import com.paper.paperbaselibrary.utils.FileUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.PicChooseDialog;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymDetailFragment extends Fragment {
    public static final String TAG = GymDetailFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gymdetail_webview)
    WebView webview;
    @Bind(R.id.webview_root)
    LinearLayout webviewRoot;


    private int id;
    private String host;
    private List<Integer> mlastPosition = new ArrayList<>();        //记录webview位置
    private List<String> mTitleStack = new ArrayList<>();           //记录标题
    private CookieManager cookieManager;
    private MaterialDialog alertDialog;
    private boolean isPrivate;
    private MaterialDialog delDialog;
    private ValueCallback<Uri> mValueCallback;
    private PicChooseDialog dialog;
    private List<String> hostArray = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private String sessionid;

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
                    .content("请检查您的网络")
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
        if (dialog == null && getActivity() != null) {
            dialog = new PicChooseDialog(getActivity());
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {


                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mValueCallback.onReceiveValue(null);
                }
            });
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
        }
        String hosts = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "hostarray", "");
        if (!TextUtils.isEmpty(hosts)) {
            hostArray = new Gson().fromJson(hosts, new TypeToken<ArrayList<String>>() {
            }.getType());
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
                            .content("无权编辑该健身房信息")
                            .autoDismiss(true)
                            .positiveText(R.string.common_i_konw)
                            .build();
                }
                alertDialog.show();
            }
            return true;
        });
        initWebSetting();
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
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

                                         LogUtil.d("shouldOverrideUrlLoading" + url);
                                         if (!TextUtils.isEmpty(toolbar.getTitle().toString())) {
                                             toolbar.getMenu().clear();
                                             URI uri = null;
                                             try {
                                                 uri = new URI(url);
                                                 LogUtil.e("host contains" + hostArray.toString());
                                                 if (!hostArray.contains(uri.getHost())) {
                                                     hostArray.add(uri.getHost());

                                                 }
                                                 setCookie(uri.getHost(), "qc_session_id", sessionid);


                                             } catch (URISyntaxException e) {

                                             }
                                             mTitleStack.add(toolbar.getTitle().toString());

                                             WebBackForwardList webBackForwardList = webview.copyBackForwardList();
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

                                             if (canGoBack()) {
                                                 toolbar.getMenu().clear();
                                             }
//                    mTitleStack.add(toolbar.getTitle().toString());
//                    WebBackForwardList webBackForwardList = webview.copyBackForwardList();
//                    mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
//                    LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                                         }
                                         return super.shouldOverrideUrlLoading(view, url);
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


        webview.setWebChromeClient(new WebChromeClient() {
                                       @Override
                                       public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
//                super.openFileChooser(valueCallback, s, s1);
                                           mValueCallback = valueCallback;

                                           dialog.show();
                                       }

                                       @Override
                                       public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                           new MaterialDialog.Builder(getContext())
                                                   .content(message)
                                                   .cancelable(false)
                                                   .positiveText(R.string.common_i_konw)
                                                   .positiveColorRes(R.color.primary)
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
                                           new MaterialDialog.Builder(getContext())
                                                   .content(message)
                                                   .positiveText("确定")
                                                   .positiveColorRes(R.color.primary)
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
                                           toolbar.setTitle(title);
                                       }

                                       @Override
                                       public void getVisitedHistory(ValueCallback<String[]> callback) {
                                           super.getVisitedHistory(callback);

                                       }
                                   }

        );


//        String s = webview.getSettings().getUserAgentString();
//        webview.getSettings().
//                setUserAgentString(s + " FitnessTrainerAssistant/0.2.5" + " Android");
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        webview.loadUrl("");

        initCookie(host);
        webview.loadUrl(host);
        return view;
    }

    private void initWebSetting() {
        WebStorage webStorage = WebStorage.getInstance();
        // TODO Auto-generated constructor stub
        WebSettings webSetting = webview.getSettings();
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

    @Override
    public void onDestroyView() {

        removeCookie();
        if (webview != null) {
            webviewRoot.removeView(webview);
            webview.removeAllViews();
            webview.destroy();
        }
        ButterKnife.unbind(this);
        super.onDestroyView();
    }


    public void removeCookie() {
        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "hostarray", new Gson().toJson(hostArray));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = "";
        if (webview == null)
            return;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChoosePicUtils.CHOOSE_GALLERY) {
                filepath = FileUtils.getPath(getContext(), data.getData());
                mValueCallback.onReceiveValue(data.getData());
                return;
            } else filepath = Configs.CameraPic;
            LogUtil.d(filepath);
//            ShowLoading("正在上传");
            Observable.just(filepath)
                    .subscribeOn(Schedulers.io())
                    .subscribe(s -> {
                        String filename = UUID.randomUUID().toString();
                        BitmapUtils.compressPic(s, Configs.ExternalCache + filename);
                        File upFile = new File(Configs.ExternalCache + filename);

//                        boolean reslut = UpYunClient.upLoadImg("/webup/" + App.coachid + "/", filename, upFile);
                        getActivity().runOnUiThread(() -> {
//                            loadingDialog.dismiss();
                            if (upFile.exists()) {
//                                ToastUtils.show("上传图片成功");
                                mValueCallback.onReceiveValue(Uri.fromFile(upFile));
                                mValueCallback = null;
                            } else {
                                mValueCallback.onReceiveValue(null);
//                                ToastUtils.show(R.drawable.ic_share_fail, "上传图片失败");
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
        }
    }


    private void initCookie(String url) {
        sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
        setCookie(Configs.ServerIp, "sessionid", sessionid);

        if (sessionid != null) {
            try {
                URI uri = new URI(url);
                if (!hostArray.contains(uri.getHost())) {
                    LogUtil.e("uri:" + uri.getHost() + " session:" + cookieManager.getCookie(uri.getHost()));

                    hostArray.add(uri.getHost());
                }
                setCookie(uri.getHost(), "qc_session_id", sessionid);

                LogUtil.e("uri:" + uri.getHost() + " session:" + cookieManager.getCookie(uri.getHost()));

//                setCookie(uri.getHost(), "sessionid", sessionid);
            } catch (URISyntaxException e) {
                //e.printStackTrace();
            }


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
        LogUtil.e("goback:" + (mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1));
        webview.goBackOrForward(mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
        toolbar.setTitle(mTitleStack.get(mTitleStack.size() - 1));
        mTitleStack.remove(mTitleStack.size() - 1);
        mlastPosition.remove(mlastPosition.size() - 1);
        urls.remove(urls.size() - 1);
        if (!canGoBack()) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_edit);
        }
    }

}
