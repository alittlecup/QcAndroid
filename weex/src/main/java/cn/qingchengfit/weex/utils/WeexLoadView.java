package cn.qingchengfit.weex.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.qingchengfit.weex.https.WXHttpManager;
import cn.qingchengfit.weex.https.WXHttpTask;
import cn.qingchengfit.weex.https.WXRequestListener;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.RenderContainer;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbaole on 2018/1/10.
 */

public final class WeexLoadView implements IWXRenderListener {
  private WXSDKInstance mWXSDKInstance;
  private Map<String, Object> mConfigMap = new HashMap<>();
  private ViewGroup rootView;

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

  public void loadUri(Uri mUri, Context context, ViewGroup viewGroup) {

    if (TextUtils.equals("http", mUri.getScheme()) || TextUtils.equals("https", mUri.getScheme())) {
      loadWXfromService(mUri.toString(), context, viewGroup);
    } else {
      loadWXfromLocal(mUri.toString(), context, viewGroup);
    }
  }

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
    mWXSDKInstance.registerRenderListener(this);
    //mWXSDKInstance.setNestedInstanceInterceptor(this);
    mWXSDKInstance.setTrackComponent(true);
  }

  @Override public void onViewCreated(WXSDKInstance instance, View view) {
  }

  @Override public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override public void onException(WXSDKInstance instance, String errCode, String msg) {

  }
}
