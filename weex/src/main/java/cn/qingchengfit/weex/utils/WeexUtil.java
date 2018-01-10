package cn.qingchengfit.weex.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import cn.qingchengfit.weex.https.WXHttpManager;
import cn.qingchengfit.weex.https.WXHttpResponse;
import cn.qingchengfit.weex.https.WXHttpTask;
import cn.qingchengfit.weex.https.WXRequestListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static android.content.Intent.ACTION_VIEW;

/**
 * Created by huangbaole on 2018/1/10.
 */

public final class WeexUtil {
  public static HashMap<String, String> jsMap = new HashMap<>();

  public static String assembleFilePath(Uri uri) {
    if (uri != null && uri.getPath() != null) {
      return uri.getPath().replaceFirst("/", "");
    }
    return "";
  }
  public static String checkUri(String url) {
    Set<String> keySet = WeexUtil.jsMap.keySet();
    Iterator<String> iterator = keySet.iterator();
    while (iterator.hasNext()){
      String next = iterator.next();
      if(url.contains(next)){
        return WeexUtil.jsMap.get(next);
      }
    }
    return url;
  }

  public static String makeBundleUri(Uri uri) {
    if (uri != null && uri.getPath() != null) {
      if (!uri.getScheme().equals("file")) {
        String path = uri.getPath();
        String[] split = path.split("\\.");
        StringBuilder stringBuilder =
            new StringBuilder().append(uri.getScheme() + "://" + uri.getHost());
        if (split.length == 2) {
          return stringBuilder.append(path).toString();
        }
        for (int i = 0; i < split.length; i++) {
          if (i == split.length - 2) {
            stringBuilder.append(".");
            continue;
          }
          stringBuilder.append(split[i]);
        }
        return stringBuilder.toString();
      }
      return uri.toString();
    }
    return "";
  }

  public static void openWeexActivity(String url) {
    Intent intent = new Intent(ACTION_VIEW);
    intent.addCategory("cn.qingchengfit.android.intent.category.WEEX");
    intent.putExtra("url", url);
    WXEnvironment.getApplication().startActivity(intent);
  }

  public static void loadJsMap() {
    WXHttpTask task = new WXHttpTask();
    task.url = "http://qcfile.b0.upaiyun.com/qc-commodity-weex/version.json";
    task.requestListener = new WXRequestListener() {
      @Override public void onSuccess(WXHttpTask task) {
        WXHttpResponse response = task.response;
        if (response != null) {
          try {
            String data = new String(response.data, "utf-8");
            JSONObject jsonObject = JSON.parseObject(data);
            Set<String> keySet = jsonObject.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
              String next = iterator.next();
              jsMap.put(next, jsonObject.getString(next));
            }
            if (jsMap.containsKey("commodity_list.js")) {
              openWeexActivity(jsMap.get("commodity_list.js"));
            }
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
        }
      }

      @Override public void onError(WXHttpTask task) {
        Log.d("TAG", "onError: " + task);
      }
    };
    WXHttpManager.getInstance().sendRequest(task);
  }
}
