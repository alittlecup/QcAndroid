package cn.qingchengfit.weex.ui;

import android.support.v7.app.AppCompatActivity;
import com.taobao.weex.WXSDKInstance;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbaole on 2018/2/6.
 */

public abstract class WxBaseActivity extends AppCompatActivity {
  @Override public void onBackPressed() {
    Map<String, Object> params = new HashMap<>();
    params.put("key", "value");
    if (getWXSDKInstance() != null) {
      getWXSDKInstance().fireGlobalEventCallback("beforeNavigatorChange", params);
    }
  }

  public abstract WXSDKInstance getWXSDKInstance();
}
