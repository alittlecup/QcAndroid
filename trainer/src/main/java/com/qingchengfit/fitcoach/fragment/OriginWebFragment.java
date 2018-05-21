package com.qingchengfit.fitcoach.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.common.Contact;
import cn.qingchengfit.model.common.PlatformInfo;
import cn.qingchengfit.model.common.ShareBean;
import cn.qingchengfit.model.common.ToolbarAction;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PhoneFuncUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.WebActivityInterface;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class OriginWebFragment extends WebFragment {
  public static final String TAG = OriginWebFragment.class.getName();
  private static final int REQUEST_UPLOAD_FILE_CODE = 12343;
	WebView webview;
  CookieManager cookieManager;
	Toolbar toolbar;
	TextView toobarAction;
	TextView toolbarTitle;
  private String base_url;
  private Gson gson;
  //    private Observable<NewPushMsg> mObservable;
  private List<Integer> mlastPosition = new ArrayList<>();
  private List<String> mTitleStack = new ArrayList<>();
  private WebActivityInterface mActivityCallback;
  //    private ValueCallback<Uri> mUploadFile;
  private boolean isTitle;
  private TextView mToobarActionTextView;
  private Toolbar mToolbarToolbar;
  private WebView mWebviewWebView;
  private LinearLayout mWebviewRootLinearLayout;
  private List<String> hostArray = new ArrayList<>();
  private String sessionid;
  private List<String> urls = new ArrayList<>();


  public OriginWebFragment() {
  }

  private void showDialog() {

    DialogUtils.showAlert(getContext(), "请检查您的网络",
        (materialDialog, dialogAction) -> materialDialog.dismiss());
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mActivityCallback = (WebActivityInterface) context;
  }

  @Override public void onDetach() {
    mActivityCallback = null;
    super.onDetach();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gson = new Gson();
    if (getArguments() != null) {
      base_url = getArguments().getString(BASE_URL);
      isTitle = getArguments().getBoolean("isTitle");
    }
  }

  @SuppressLint("SetJavaScriptEnabled") @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_origin_web, container, false);
    webview = (WebView) view.findViewById(R.id.webview);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toobarAction = (TextView) view.findViewById(R.id.toobar_action);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

    mWebviewRootLinearLayout = (LinearLayout) view.findViewById(R.id.webview_root);



    toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    toolbarTitle.setText("");
    if (isTitle) toolbar.setVisibility(View.GONE);

    webview.addJavascriptInterface(new JsInterface(), "NativeMethod");
    String hosts = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "hostarray", "");
    if (!TextUtils.isEmpty(hosts)) {
      hostArray = new Gson().fromJson(hosts, new TypeToken<ArrayList<String>>() {
      }.getType());
    }
    webview.setWebViewClient(new WebViewClient() {

      @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtil.e("url:" + url);
      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      }

      @Override public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
      }

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.d("shouldOverrideUrlLoading" + url);
        if (!TextUtils.isEmpty(toolbarTitle.getText().toString())) {
          URI uri = null;
          try {
            uri = new URI(url);

            if (!hostArray.contains(uri.getHost())) {
              hostArray.add(uri.getHost());
            }
            LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
            setCookie(uri.getHost(), "qc_session_id", sessionid);
            LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
          } catch (URISyntaxException e) {

          }
          mTitleStack.add(toolbarTitle.getText().toString());
          WebBackForwardList webBackForwardList = mWebviewWebView.copyBackForwardList();
          if (uri != null) {
            String path = uri.getHost() + uri.getPath();
            LogUtil.e("urlpath:" + path);
            if (urls.contains(path)) {
              int step = urls.size() - urls.indexOf(path);
              LogUtil.e("step:" + step);
              mlastPosition.add(webBackForwardList.getCurrentIndex() + step);
              urls = urls.subList(0, urls.indexOf(path));
            } else {
              urls.add(path);
              mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
            }
          } else {
            mlastPosition.add(webBackForwardList.getCurrentIndex() + 1);
          }
        }
        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override public void onReceivedError(WebView view, int errorCode, String description,
          String failingUrl) {
        toolbarTitle.setText("");
        webview.loadUrl("");
        showDialog();
      }
    });

    webview.setWebChromeClient(new WebChromeClient() {
      @Override public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        super.openFileChooser(valueCallback, s, s1);
        ToastUtils.show("open file");
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
        DialogUtils.showAlert(getContext(), message, (dialog, action) -> {
          dialog.dismiss();
          result.confirm();
        });
        return true;
      }

      @Override
      public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        DialogUtils.shwoConfirm(getContext(), message, new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            switch (dialogAction) {
              case POSITIVE:
                result.confirm();
                break;
              case NEGATIVE:
                result.cancel();
                break;
            }
          }
        });
        return true;
      }

      @Override public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);

        toolbarTitle.setText(title);
      }

      @Override public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
      }
    });
    webview.getSettings().setJavaScriptEnabled(true);
    //        webview.setInitialScale(getScale());
    String s = webview.getSettings().getUserAgentString();
    webview.getSettings()
        .setUserAgentString(s
            + " FitnessTrainerAssistant/"
            + AppUtils.getAppVer(App.AppContex)
            + " Android  "
            + "QingchengApp/Trainer  "
            + "OEM:"
            + getString(R.string.oem_tag));
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

    //        initCookie();
    //toolbar action callback
    toobarAction.setOnClickListener(v -> {
      if (webview != null) {
        webview.loadUrl("javascript:window.nativeLinkWeb.runCallback('setAction');");
      }
    });
    //        mObservable = RxBus.getBus().register(NewPushMsg.class);
    //        mObservable.subscribe(newPushMsg -> webview.loadUrl("javascript:window.nativeLinkWeb.updateNotifications();"));
    webview.loadUrl(base_url);
    CookieSyncManager.createInstance(getContext());
    cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    initCookie(base_url);

    return view;
  }

  //最后在OnActivityResult中接受返回的结果
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
    //            if (null == mUploadFile) {
    //                return;
    //            }
    //            Uri result = (null == data) ? null : data.getData();
    //            if (null != result) {
    //                ContentResolver resolver = getActivity().getContentResolver();
    //                String[] columns = {MediaStore.Images.Media.DATA};
    //                Cursor cursor = resolver.query(result, columns, null, null, null);
    //                cursor.moveToFirst();
    //                int columnIndex = cursor.getColumnIndex(columns[0]);
    //                String imgPath = cursor.getString(columnIndex);
    //                System.out.println("imgPath = " + imgPath);
    //                if (null == imgPath) {
    //                    return;
    //                }
    //                File file = new File(imgPath);
    //                //将图片处理成大小符合要求的文件
    //                result = Uri.fromFile(handleFile(file));
    //                mUploadFile.onReceiveValue(result);
    //                mUploadFile = null;
    //            }
    //        }
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

  @Override public Boolean canGoBack() {

    if (webview != null) {
      return mlastPosition.size() > 0;
    } else {
      return false;
    }
  }

  public void goBack() {
    WebBackForwardList webBackForwardList = webview.copyBackForwardList();
    LogUtil.e("goback:" + (mlastPosition.get(mlastPosition.size() - 1)
        - webBackForwardList.getCurrentIndex()
        - 1));
    webview.goBackOrForward(
        mlastPosition.get(mlastPosition.size() - 1) - webBackForwardList.getCurrentIndex() - 1);
    toolbarTitle.setText(mTitleStack.get(mTitleStack.size() - 1));
    mTitleStack.remove(mTitleStack.size() - 1);
    mlastPosition.remove(mlastPosition.size() - 1);
    //        if (mlastPosition.size()>0){
    //            webview.goBackOrForward(-mlastPosition.get(mlastPosition.size()-1)+webBackForwardList.getCurrentIndex());
    //            mlastPosition.remove(mlastPosition.size()-1);
    //        }else
    //            webview.goBack();
  }

  public void startLoadUrl(String url) {
    if (webview != null) webview.loadUrl(url);
  }

  private void initCookie(String url) {
    sessionid = PreferenceUtils.getPrefString(App.AppContex, "session_id", "");
    if (sessionid != null) {
      setCookie(Configs.Server, "sessionid", sessionid);
      try {
        URI uri = new URI(url);
        if (!hostArray.contains(uri.getHost())) {
          hostArray.add(uri.getHost());
          LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
          setCookie(uri.getHost(), "qc_session_id", sessionid);
          LogUtil.e(uri.getHost() + "  " + cookieManager.getCookie(uri.getHost()));
        }
      } catch (URISyntaxException e) {
        //e.printStackTrace();
      }
      //            setCookie("http://192.168.31.108", "qc_session_id", sessionid);
      //            setCookie(Configs.HOST_NAMESPACE_0, "qc_session_id", sessionid);
      //            setCookie(Configs.HOST_NAMESPACE_1, "qc_session_id", sessionid);
    } else {
      //TODO logout
    }
  }

  @Override public void onDestroyView() {
    PreferenceUtils.setPrefString(App.AppContex, App.coachid + "hostarray",
        new Gson().toJson(hostArray));
    //必须先移除webview
    if (webview != null) {
      mWebviewRootLinearLayout.removeView(webview);
      //            webview.removeAllViews();
      webview.destroy();
    }

    super.onDestroyView();
  }

  @Override public void removeCookie() {
    //        if (cookieManager != null) {
    //            cookieManager.removeAllCookie();
    //        }
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

    @JavascriptInterface public String getToken() {
      return PreferenceUtils.getPrefString(App.AppContex, "token", "");
    }

    @JavascriptInterface public void shareInfo(String json) {
      ShareBean bean = gson.fromJson(json, ShareBean.class);
      //            ShareUtils.oneKeyShared(getContext(), bean.title, bean.imgUrl, bean.desc, bean.title);
      ShareDialogFragment.newInstance(bean.title, bean.desc, bean.imgUrl, bean.link)
          .show(getFragmentManager(), "");
    }

    @JavascriptInterface public void openDrawer() {
      openmainDrawer();
    }

    @JavascriptInterface public void setAction(String s) {
      LogUtil.e("setAction:" + s);
      ToolbarAction toolStr = gson.fromJson(s, ToolbarAction.class);

      getActivity().runOnUiThread(() -> {
        if (TextUtils.isEmpty(toolStr.name)) {
          toobarAction.setVisibility(View.GONE);
        } else {
          toobarAction.setVisibility(View.VISIBLE);
          toobarAction.setText(toolStr.name);
        }
      });
    }

    @JavascriptInterface public void completeAction() {
      mActivityCallback.onfinish();
    }

    @JavascriptInterface public void setTitle(String s) {
      if (getActivity() != null) {
        getActivity().runOnUiThread(new Runnable() {
          @Override public void run() {
            toolbarTitle.setText(s);
          }
        });
      }
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
      if (getActivity() != null) {
        getActivity().runOnUiThread(() -> {
          getActivity().onBackPressed();
        });
      }
    }

    @JavascriptInterface public String getSessionId() {
      return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
    }

    @JavascriptInterface
    public void shareTimeline(String title, String link, String imgurl, String successCallback,
        String failedCallback) {

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
