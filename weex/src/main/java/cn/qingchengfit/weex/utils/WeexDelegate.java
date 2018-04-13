package cn.qingchengfit.weex.utils;

import android.app.Application;
import cn.qingchengfit.weex.adapter.ImageAdapter;
import cn.qingchengfit.weex.component.QcQrCode;
import cn.qingchengfit.weex.component.QcRichText;
import cn.qingchengfit.weex.module.QcAnimationModule;
import cn.qingchengfit.weex.module.QcNavigatorModule;
import cn.qingchengfit.weex.module.QcShareModule;
import cn.qingchengfit.weex.module.QcTelModule;
import cn.qingchengfit.weex.module.QcTrackModule;
import cn.qingchengfit.weex.module.QcWxModalUIModule;
import cn.qingchengfit.weex.ui.WxPageActivity;
import com.alibaba.android.bindingx.plugin.weex.BindingX;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.common.WXException;

/**
 * Weex初始化代理类
 * Created by huangbaole on 2018/1/8.
 */

public class WeexDelegate {
  private static InitConfig defaultInitConfig =
      new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
  private static IActivityNavBarSetter activityNavBarSetter = new WxActivityNavBarSetter();

  /**
   * 初始化方法
   *
   * @param application 当前的Application
   */
  public static void initWXSDKEngine(Application application) {
    initWXSDKEngine(application, defaultInitConfig);
  }

  /**
   * 初始化方法
   *
   * @param application 当前的Application
   * @param initConfig 当前初始化需要的配置
   */
  public static void initWXSDKEngine(Application application, InitConfig initConfig) {
    WXSDKEngine.initialize(application, initConfig);
    //设置WXNavigatorModule的拦截方法，主要是为了拦截push方法
    WXSDKEngine.setActivityNavBarSetter(activityNavBarSetter);
    initModules();
    initComponents();

    try {
      BindingX.register();
    } catch (WXException e) {
      e.printStackTrace();
    }
  }

  /**
   * 注册components
   */
  private static void initComponents() {
    try {
      WXSDKEngine.registerComponent("qc-rich-text", QcRichText.class);
      WXSDKEngine.registerComponent("qc-qr-code", QcQrCode.class);
    } catch (WXException e) {
      e.printStackTrace();
    }
  }

  /**
   * 注册modules
   */
  private static void initModules() {
    try {
      WXSDKEngine.registerModule("qcModal", QcWxModalUIModule.class);
      WXSDKEngine.registerModule("qcAnimation", QcAnimationModule.class);
      WXSDKEngine.registerModule("qcNavigator", QcNavigatorModule.class);
      WXSDKEngine.registerModule("qcShare", QcShareModule.class);
      WXSDKEngine.registerModule("qcTrack", QcTrackModule.class);
      WXSDKEngine.registerModule("qcTel", QcTelModule.class);
    } catch (WXException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   */
  private static class WxActivityNavBarSetter implements IActivityNavBarSetter {
    /**
     * 拦截push()方法，用于跳转至{@link WxPageActivity}
     */
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


