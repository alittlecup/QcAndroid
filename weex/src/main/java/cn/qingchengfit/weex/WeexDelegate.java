package cn.qingchengfit.weex;

import android.app.Application;
import cn.qingchengfit.weex.adapter.ImageAdapter;
import cn.qingchengfit.weex.utils.WeexUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;

/**
 * Created by huangbaole on 2018/1/8.
 */

public class WeexDelegate {
  private static InitConfig defaultInitConfig =
      new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
  private static IActivityNavBarSetter activityNavBarSetter = new WxActivityNavBarSetter();

  public static void initWXSDKEngine(Application application) {
    initWXSDKEngine(application, defaultInitConfig);
  }

  public static void initWXSDKEngine(Application application, InitConfig initConfig) {
    WXSDKEngine.initialize(application, initConfig);
    WXSDKEngine.setActivityNavBarSetter(activityNavBarSetter);
    initModules();
    initComponents();
  }

  private static void initComponents() {

  }

  private static void initModules() {

  }

  private static class WxActivityNavBarSetter implements IActivityNavBarSetter {

    @Override public boolean push(String param) {
      JSONObject jsonObject = JSON.parseObject(param);
      String url = jsonObject.getString("url");
      WeexUtil.openWeexActivity(WeexUtil.checkUri(url));
      return true;
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
  }
}


