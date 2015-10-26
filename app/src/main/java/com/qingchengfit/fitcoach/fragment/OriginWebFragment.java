package com.qingchengfit.fitcoach.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.WebActivityInterface;
import com.qingchengfit.fitcoach.bean.PlatformInfo;
import com.qingchengfit.fitcoach.bean.TitleBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OriginWebFragment extends WebFragment {
    public static final String TAG = OriginWebFragment.class.getName();
    private static final int REQUEST_UPLOAD_FILE_CODE = 12343;
    @Bind(R.id.webview)
    WebView webview;
    CookieManager cookieManager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private String base_url;
    private Gson gson;
    //    private Observable<NewPushMsg> mObservable;
    private List<Integer> mlastPosition = new ArrayList<>();
    private List<String> mTitleStack = new ArrayList<>();
    private WebActivityInterface mActivityCallback;
    private ValueCallback<Uri> mUploadFile;


    public OriginWebFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (WebActivityInterface) context;
    }

    @Override
    public void onDetach() {
        mActivityCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        if (getArguments() != null) {
            base_url = getArguments().getString(BASE_URL);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_web, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle("");
        webview.addJavascriptInterface(new JsInterface(), "NativeMethod");

        webview.setWebViewClient(new WebViewClient() {

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
                if (!TextUtils.isEmpty(toolbar.getTitle().toString())) {
                    mTitleStack.add(toolbar.getTitle().toString());
                    WebBackForwardList webBackForwardList = webview.copyBackForwardList();
                    mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
                    LogUtil.e("webCount:" + webBackForwardList.getCurrentIndex());
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


        });


        webview.setWebChromeClient(new WebChromeClient() {

            // Andorid 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                openFileChooser(uploadFile);
            }

            // Andorid 3.0 +
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                openFileChooser(uploadFile);
            }

            // Android 3.0
            public void openFileChooser(ValueCallback<Uri> uploadFile) {
                // Toast.makeText(WebviewActivity.this, "上传文件/图片",Toast.LENGTH_SHORT).show();
                mUploadFile = uploadFile;
                startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), REQUEST_UPLOAD_FILE_CODE);
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
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("clientJsonBegin")) {
                    String[] strings = title.split("clientJsonBegin");
                    toolbar.setTitle(strings[0]);
                    String jsonStr = strings[1].replace("clientJsonBegin", "");
                    Gson gson = new Gson();
                    TitleBean titleBean = gson.fromJson(jsonStr, TitleBean.class);
                    toolbar.getMenu().clear();
                    switch (titleBean.navIcon) {
                        case 0:
                            break;
                        case 1:
                            toolbar.setNavigationIcon(R.drawable.ic_cross_white);
                            break;
                        case 2:
                            toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                            break;
                        default:
                            break;
                    }
                    switch (titleBean.actionIcon) {
                        case 0:
                            break;
                        case 1:
                            toolbar.inflateMenu(R.menu.add);
                            break;
                        case 2:
                            break;

                        default:
                            break;
                    }

                } else {
                    toolbar.setTitle(title);
                }

            }

            @Override
            public void getVisitedHistory(ValueCallback<String[]> callback) {
                super.getVisitedHistory(callback);

            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
//        webview.setInitialScale(getScale());
        String s = webview.getSettings().getUserAgentString();
        webview.getSettings().setUserAgentString(s + " FitnessTrainerAssistant/0.2.5" + " Android");
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        // 开启DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = Configs.ExternalCache;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        webview.getSettings().setAppCacheEnabled(true);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        initCookie();
//        mObservable = RxBus.getBus().register(NewPushMsg.class);
//        mObservable.subscribe(newPushMsg -> webview.loadUrl("javascript:window.nativeLinkWeb.updateNotifications();"));
        webview.loadUrl(base_url);
        return view;
    }

    //最后在OnActivityResult中接受返回的结果

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (null == mUploadFile) {
                return;
            }
            Uri result = (null == data) ? null : data.getData();
            if (null != result) {
                ContentResolver resolver = getActivity().getContentResolver();
                String[] columns = {MediaStore.Images.Media.DATA};
                Cursor cursor = resolver.query(result, columns, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(columns[0]);
                String imgPath = cursor.getString(columnIndex);
                System.out.println("imgPath = " + imgPath);
                if (null == imgPath) {
                    return;
                }
                File file = new File(imgPath);
                //将图片处理成大小符合要求的文件
                result = Uri.fromFile(handleFile(file));
                mUploadFile.onReceiveValue(result);
                mUploadFile = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 处理拍照/选择的文件
     */
    private File handleFile(File file) {
        DisplayMetrics dMetrics = getResources().getDisplayMetrics();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        System.out.println("  imageWidth = " + imageWidth + " imageHeight = " + imageHeight);
        int widthSample = (int) (imageWidth / (dMetrics.density * 90));
        int heightSample = (int) (imageHeight / (dMetrics.density * 90));
        System.out.println("widthSample = " + widthSample + " heightSample = " + heightSample);
        options.inSampleSize = widthSample < heightSample ? heightSample : widthSample;
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        System.out.println("newBitmap.size = " + newBitmap.getRowBytes() * newBitmap.getHeight());
        File handleFile = new File(file.getParentFile(), "upload.png");
        try {
            if (newBitmap.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(handleFile))) {
                System.out.println("保存图片成功");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return handleFile;

    }


    @Override
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
//        if (mlastPosition.size()>0){
//            webview.goBackOrForward(-mlastPosition.get(mlastPosition.size()-1)+webBackForwardList.getCurrentIndex());
//            mlastPosition.remove(mlastPosition.size()-1);
//        }else
//            webview.goBack();
    }


    public void startLoadUrl(String url) {
        if (webview != null)
            webview.loadUrl(url);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webview != null)
            webview.destroy();
        ButterKnife.unbind(this);
//        RxBus.getBus().unregister(NewPushMsg.class.getSimpleName(), mObservable);

    }


    @Override
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

    public void openmainDrawer() {
        getActivity().runOnUiThread(() -> RxBus.getBus().post(RxBus.OPEN_DRAWER));
    }

    public void setCookie(String url, String key, String value) {
        StringBuffer sb = new StringBuffer();
//        String oriCookie = xWalkCookieManager.getCookie(url);
//        if (oriCookie != null){
//        sb.append(xWalkCookieManager.getCookie(url));
//        sb.append(";");}
        sb.append(key);
        sb.append("=");
        sb.append(value);
        cookieManager.setCookie(url, sb.toString());
    }


    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return PreferenceUtils.getPrefString(App.AppContex, "token", "");
        }


        @JavascriptInterface
        public void openDrawer() {
            openmainDrawer();

        }

        @JavascriptInterface
        public void completeAction() {
            mActivityCallback.onfinish();
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
        public void goBack() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getActivity().onBackPressed();
                });
            }
        }

        @JavascriptInterface
        public String getSessionId() {
            return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
        }

        @JavascriptInterface
        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {

        }

//        @JavascriptInterface
//        public void resize(final float height) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    webview.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
//                }
//            });
//        }

    }
}
