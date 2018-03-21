package cn.qingchengfit.weex.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.weex.https.WXHttpManager;
import cn.qingchengfit.weex.https.WXHttpResponse;
import cn.qingchengfit.weex.https.WXHttpTask;
import cn.qingchengfit.weex.https.WXRequestListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * Created by huangbaole on 2018/3/21.
 */

public class LoadJSFileService extends Service {
  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   */
  @Override public void onCreate() {
    super.onCreate();
    LogUtil.d("weex_services_start");
  }

  @Override public void onDestroy() {
    super.onDestroy();
    LogUtil.d("weex_services_destroy");
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    loadAndSaveData();
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private void loadAndSaveData() {
    LogUtil.d("load weex  json");
    WXHttpTask task = new WXHttpTask();
    task.url = "http://qcfile.b0.upaiyun.com/qc-commodity-weex/version_test.json";
    task.requestListener = new WXRequestListener() {
      @Override public void onSuccess(WXHttpTask task) {
        WXHttpResponse response = task.response;
        if (response != null) {
          try {
            String data = new String(response.data, "utf-8");
            JSONObject jsonObject = JSON.parseObject(data);
            Boolean weex_enabled = jsonObject.getBoolean("weex_enabled");
            if (!weex_enabled) {
              stopSelf();
              return;
            }
            task.url = jsonObject.getString("proxy_commodity.js");
            task.requestListener = new WXRequestListener() {

              @Override public void onSuccess(WXHttpTask task) {
                try {
                  LogUtil.d("load weex  json  end");
                  FileUtils.saveCache("weex-js-json", new String(task.response.data, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
                } finally {
                  stopSelf();
                }
              }

              @Override public void onError(WXHttpTask task) {
                stopSelf();
              }
            };

            WXHttpManager.getInstance().sendRequest(task);
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            stopSelf();
          }
        }
      }

      @Override public void onError(WXHttpTask task) {
        Log.d("TAG", "onError: " + task);
        stopSelf();
      }
    };
    WXHttpManager.getInstance().sendRequest(task);
  }
}
