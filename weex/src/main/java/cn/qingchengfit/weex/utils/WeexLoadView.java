package cn.qingchengfit.weex.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.qingchengfit.weex.https.WXHttpManager;
import cn.qingchengfit.weex.https.WXHttpTask;
import cn.qingchengfit.weex.https.WXRequestListener;
import com.taobao.weex.RenderContainer;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载js文件的工具类
 * Created by huangbaole on 2018/1/10.
 */

public final class WeexLoadView {
  public WXSDKInstance getmWXSDKInstance() {
    return mWXSDKInstance;
  }

  private WXSDKInstance mWXSDKInstance;
  private Map<String, Object> mConfigMap = new HashMap<>();
  private ViewGroup rootView;

  /**
   * 从本地加载
   *
   * @param url 地址
   * @param context 上下文
   * @param rootView 需要展示的view
   */
  private void loadWXfromLocal(String url, final Context context, ViewGroup rootView) {
    this.rootView = rootView;
    createWXSDKInstance(context, rootView);
    final Uri mUri = Uri.parse(url);
    rootView.post(new Runnable() {
      @Override public void run() {
        mConfigMap.put("bundleUrl", WeexUtil.makeBundleUri(mUri));
        String path =
            "file".equals(mUri.getScheme()) ? WeexUtil.assembleFilePath(mUri) : mUri.toString();
        mWXSDKInstance.render("TAG", WXFileUtils.loadAsset(path, context), mConfigMap, null,
            WXRenderStrategy.APPEND_ASYNC);
      }
    });
  }

  /**
   * 加载url
   *
   * @param mUri 地址
   * @param context 上下文
   * @param viewGroup 需要展示的view
   */
  public void loadUri(Uri mUri, Context context, ViewGroup viewGroup) {

    if (TextUtils.equals("http", mUri.getScheme()) || TextUtils.equals("https", mUri.getScheme())) {
      loadWXfromService(mUri.toString(), context, viewGroup);
    } else {
      loadWXfromLocal(mUri.toString(), context, viewGroup);
    }
  }

  /**
   * 从远程加载
   *
   * @param url 地址
   * @param context 上下文
   * @param rootView 需要展示的view
   */
  private void loadWXfromService(final String url, final Context context, ViewGroup rootView) {

    this.rootView = rootView;
    createWXSDKInstance(context, rootView);
    WXHttpTask httpTask = new WXHttpTask();
    httpTask.url = url;
    httpTask.requestListener = new WXRequestListener() {

      @Override public void onSuccess(WXHttpTask task) {
        try {
          mConfigMap.put("bundleUrl", WeexUtil.makeBundleUri(Uri.parse(url)));
          mWXSDKInstance.render("TAG", new String(task.response.data, "utf-8"), mConfigMap, null,
              WXRenderStrategy.APPEND_ASYNC);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

      @Override public void onError(WXHttpTask task) {
        Toast.makeText(context, "network error!", Toast.LENGTH_SHORT).show();
      }
    };

    WXHttpManager.getInstance().sendRequest(httpTask);
  }

  private void destoryWXSDKInstance() {
    if (mWXSDKInstance != null) {
      mWXSDKInstance.registerRenderListener(null);
      mWXSDKInstance.destroy();
      mWXSDKInstance.setRenderContainer(null);
      mWXSDKInstance = null;
    }
  }

  private void createWXSDKInstance(Context context, ViewGroup view) {
    destoryWXSDKInstance();
    RenderContainer renderContainer = new RenderContainer(context);
    rootView.addView(renderContainer,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    mWXSDKInstance = new WXSDKInstance(context);
    mWXSDKInstance.setRenderContainer(renderContainer);
    //mWXSDKInstance.registerRenderListener(this);
    //mWXSDKInstance.setNestedInstanceInterceptor(this);
    mWXSDKInstance.setTrackComponent(true);
  }
}
