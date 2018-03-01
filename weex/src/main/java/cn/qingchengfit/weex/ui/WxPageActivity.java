package cn.qingchengfit.weex.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexLoadView;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 * Activity展示页，同时也是新打开的js文件默认页
 */
public class WxPageActivity extends WxBaseActivity {
  WeexLoadView mWeexLoadView;
  @Inject GymWrapper gymWrapper;
  boolean mWeexEnable = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.weex_container);
    String url = getIntent().getStringExtra("url");
    Uri data = Uri.parse(url);
    String weex_enable = data.getQueryParameter("weex_enable");
    if ("false".equals(weex_enable)) {
      mWeexEnable = false;
      String host = gymWrapper.getCoachService().getHost();
      String uri = host
          + "/shop/"
          + gymWrapper.getCoachService().getShop_id()
          + "/mobile/staff/commodity/?hide_title=1";
      Log.d("TAG", "onCreate: " + uri);
      WebActivity.startWeb(uri, this);
      finish();
      return;
    }
    mWeexEnable = true;
    mWeexLoadView = WeexLoadView.getInstance();
    prepareUserInfo(mWeexLoadView);
    mWeexLoadView.loadUri(data, this, (ViewGroup) findViewById(R.id.container));
  }



  private void prepareUserInfo(WeexLoadView loadView) {
    String g_shop_id = gymWrapper.getCoachService().getShop_id();
    String g_shop_name = gymWrapper.getCoachService().name();
    String g_shop_phone = gymWrapper.getCoachService().getPhone();
    String g_shop_logo = gymWrapper.getCoachService().getPhoto();
    String g_brand_id = gymWrapper.getBrand().getId();
    String g_brand_name = gymWrapper.getBrand().getName();
    Map<String, Object> member = new HashMap<>();
    member.put("g_shop_id", g_shop_id);
    member.put("g_shop_name", g_shop_name);
    member.put("g_shop_phone", g_shop_phone);
    member.put("g_shop_logo", g_shop_logo);
    member.put("g_brand_id", g_brand_id);
    member.put("g_brand_name", g_brand_name);
    Map<String, Object> g_user = new HashMap<>();
    g_user.put("g_user", member);
    JSONObject jsonObject = new JSONObject(g_user);
    loadView.mConfigMap.put("custom", jsonObject);
  }

  @Override public WXSDKInstance getWXSDKInstance() {

    if (mWeexLoadView != null && mWeexEnable) {
      return mWeexLoadView.getmWXSDKInstance();
    }
    return null;
  }
}
