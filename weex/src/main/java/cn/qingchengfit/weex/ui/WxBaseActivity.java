package cn.qingchengfit.weex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.taobao.weex.WXSDKInstance;
import dagger.android.AndroidInjection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbaole on 2018/2/6.
 */

public abstract class WxBaseActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidInjection.inject(this);
  }

  @Override public void onBackPressed() {
    Map<String, Object> params = new HashMap<>();
    params.put("key", "value");
    if (getWXSDKInstance() != null) {
      getWXSDKInstance().fireGlobalEventCallback("beforeNavigatorChange", params);
    }else{
      super.onBackPressed();
    }
  }

  public abstract WXSDKInstance getWXSDKInstance();
}
