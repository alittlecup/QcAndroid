package cn.qingchengfit.weex.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexLoadView;
import com.alibaba.fastjson.JSONObject;
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
          + "/mobile/staff/commodity/?"
          + data.getQuery()
          + "&hide_nav=1";
      LogUtil.d("TAG", "onCreate: " + uri);
      WebActivity.startWeb(uri, this);
      finish();
      return;
    }
    mWeexEnable = true;
    mWeexLoadView = WeexLoadView.getInstance();
    prepareUserInfo(mWeexLoadView);
    mWeexLoadView.loadUri(data, this, (ViewGroup) findViewById(R.id.container));
    Log.d("TAG", "onCreate: ");
  }

  private void prepareUserInfo(WeexLoadView loadView) {
    String g_shop_id = gymWrapper.getCoachService().getShop_id();
    String g_shop_name = gymWrapper.getCoachService().name();
    String g_shop_phone = gymWrapper.getCoachService().getPhone();
    String g_shop_logo = gymWrapper.getCoachService().getPhoto();
    String g_brand_id = gymWrapper.getCoachService().getBrand_id();
    String g_brand_name = gymWrapper.getCoachService().getBrand_name();
    Map<String, Object> member = new HashMap<>();
    member.put("g_shop_id", g_shop_id);
    member.put("g_shop_name", g_shop_name);
    member.put("g_shop_phone", g_shop_phone);
    member.put("g_shop_logo", g_shop_logo);
    member.put("g_cloud_brand_id", g_brand_id);
    member.put("g_brand_name", g_brand_name);
    member.put("g_fitness_host", subString(gymWrapper.getCoachService().getHost()));
    member.put("g_session_value", PreferenceUtils.getPrefString(this, "session_id", ""));
    member.put("g_session_key", PreferenceUtils.getPrefString(this, "session_key", ""));
    Map<String, Object> g_user = new HashMap<>();
    g_user.put("g_user", member);
    JSONObject jsonObject = new JSONObject(g_user);
    loadView.mConfigMap.put("custom", jsonObject);
  }

  private String subString(String uri) {
    if (uri.lastIndexOf("/") == uri.length() - 1) {
      return uri.substring(0, uri.length() - 1);
    }
    return uri;
  }

  @Override public void onBackPressed() {
    Map<String, Object> params = new HashMap<>();
    params.put("key", "value");
    if (!isNativeBack()) {
      mWeexLoadView.getmWXSDKInstance().fireGlobalEventCallback("beforeNavigatorChange", params);
    } else {
      super.onBackPressed();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mWeexLoadView != null) {
      mWeexLoadView.destoryWXSDKInstance();
      mWeexLoadView = null;
    }
  }

  public boolean isNativeBack() {
    if (mWeexLoadView != null && mWeexEnable && !mWeexLoadView.isRenderError()) {
      return false;
    }
    return true;
  }
}
