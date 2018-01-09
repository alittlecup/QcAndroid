package cn.qingchengfit.weex;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

public class WxPageActivity extends AppCompatActivity
    implements IActivityNavBarSetter, IWXRenderListener {

  private WXSDKInstance mWXSDKInstance;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.weex_container);
    String url = getIntent().getStringExtra("url");
    Uri data=Uri.parse(url);
    String path = "file".equals(data.getScheme()) ? assembleFilePath(data) : data.toString();
    createWXSDKInstance();
    render("weex", path);
  }

  private String assembleFilePath(Uri uri) {
    if (uri != null && uri.getPath() != null) {
      return uri.getPath().replaceFirst("/", "");
    }
    return "";
  }

  private void createWXSDKInstance() {
    destoryWXSDKInstance();
    mWXSDKInstance = new WXSDKInstance(this);
    mWXSDKInstance.registerRenderListener(this);
  }

  private void render(String pageName, String path) {
    mWXSDKInstance.render(pageName, WXFileUtils.loadAsset(path, this), null, null,
        WXRenderStrategy.APPEND_ASYNC);
  }

  private void destoryWXSDKInstance() {
    if (mWXSDKInstance != null) {
      mWXSDKInstance.registerRenderListener(null);
      mWXSDKInstance.destroy();
      mWXSDKInstance = null;
    }
  }

  @Override public boolean push(String param) {
    return false;
  }

  @Override public boolean pop(String param) {
    return false;
  }

  @Override public boolean setNavBarRightItem(String param) {
    return false;
  }

  @Override public boolean clearNavBarRightItem(String param) {
    return false;
  }

  @Override public boolean setNavBarLeftItem(String param) {
    return false;
  }

  @Override public boolean clearNavBarLeftItem(String param) {
    return false;
  }

  @Override public boolean setNavBarMoreItem(String param) {
    return false;
  }

  @Override public boolean clearNavBarMoreItem(String param) {
    return false;
  }

  @Override public boolean setNavBarTitle(String param) {
    return false;
  }

  @Override public void onViewCreated(WXSDKInstance instance, View view) {
    setContentView(view);
  }

  @Override public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override public void onException(WXSDKInstance instance, String errCode, String msg) {

  }
}
