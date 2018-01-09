package cn.qingchengfit.weex;

import android.app.Application;
import android.content.Intent;
import cn.qingchengfit.weex.adapter.ImageAdapter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by huangbaole on 2018/1/8.
 */

public class WeexDelegate {
  private static final String ACTION = "cn.qingchengfit.weex.page";
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
      Intent intent = new Intent(ACTION_VIEW);
      intent.addCategory("cn.qingchengfit.android.intent.category.WEEX");
      intent.putExtra("url",url);;
      intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
      WXEnvironment.getApplication().startActivity(intent);
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


