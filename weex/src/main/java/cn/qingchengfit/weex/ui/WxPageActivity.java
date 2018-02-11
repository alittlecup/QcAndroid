package cn.qingchengfit.weex.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexLoadView;
import com.taobao.weex.WXSDKInstance;

/**
 * Activity展示页，同时也是新打开的js文件默认页
 */
public class WxPageActivity extends WxBaseActivity {
  WeexLoadView mWeexLoadView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.weex_container);
    String url = getIntent().getStringExtra("url");
    Uri data = Uri.parse(url);
    mWeexLoadView = new WeexLoadView();
    mWeexLoadView.loadUri(data, this, (ViewGroup) findViewById(R.id.container));
  }

  @Override public WXSDKInstance getWXSDKInstance() {
    if (mWeexLoadView != null) {
      return mWeexLoadView.getmWXSDKInstance();
    }
    return null;
  }

}
