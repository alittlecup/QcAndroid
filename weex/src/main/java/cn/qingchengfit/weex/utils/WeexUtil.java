package cn.qingchengfit.weex.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.weex.https.WXHttpManager;
import cn.qingchengfit.weex.https.WXHttpResponse;
import cn.qingchengfit.weex.https.WXHttpTask;
import cn.qingchengfit.weex.https.WXRequestListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.Intent.ACTION_VIEW;

/**
 * weex工具类
 * Created by huangbaole on 2018/1/10.
 */

public final class WeexUtil {
  public static HashMap<String, String> jsMap = new HashMap<>();

  /**
   * 获取本地asset下uri对应的文件名
   */
  public static String assembleFilePath(Uri uri) {
    if (uri != null && uri.getPath() != null && "file".equals(uri.getScheme())) {
      return uri.getPath().replaceFirst("/", "");
    }
    return "";
  }

  /**
   * 获取传入的url是否有对应的配置文件中的路径
   */
  public static String checkUri(String url) {
    Set<String> keySet = WeexUtil.jsMap.keySet();
    Iterator<String> iterator = keySet.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      if (url.contains(next)) {
        return WeexUtil.jsMap.get(next);
      }
    }
    return url;
  }

  /**
   * 根据加载的地址获取需要给JS端的BundleURL,主要目的是去除远端js路径的hash值
   *
   * @param uri https://qcfile.b0.upaiyun.com/qc-commodity-weex/commodity_list.a5edc2d0468d4df8541811b307654dd2.js
   * @return https://qcfile.b0.upaiyun.com/qc-commodity-weex/commodity_list.js
   */

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

  /**
   * 开启新的界面
   */

  public static void openWeexActivity(String url) {
    Intent intent = new Intent(ACTION_VIEW);
    intent.addCategory("cn.qingchengfit.android.intent.category.WEEX");
    intent.putExtra("url", url);
    WXEnvironment.getApplication().startActivity(intent);
  }

  /**
   * 加载远程的js配置文件并保存至{@link WeexUtil {@link #jsMap}}
   */
  public static void loadJsMap() {
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
              String string = jsonObject.getString("proxy_commodity.js");
              openWeexActivity(string + "/?weex_enable=false");
              return;
            }
            Set<String> keySet = jsonObject.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
              String next = iterator.next();
              jsMap.put(next, jsonObject.getString(next));
            }
            if (jsMap.containsKey("proxy_commodity.js")) {
              openWeexActivity(jsMap.get("proxy_commodity.js"));
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

  public static void loadAndSaveData() {
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
              return;
            }
            task.url = jsonObject.getString("proxy_commodity.js");
            task.requestListener = new WXRequestListener() {

              @Override public void onSuccess(final WXHttpTask task) {
                LogUtil.d("load weex  json  end");
                Observable.just("weex-js-json")
                    .map(new Func1<String, Boolean>() {
                      @Override public Boolean call(String s) {
                        try {
                          //FileUtils.saveCache("weex-js-json",
                          //    new String(task.response.data, "utf-8"));
                          return writeFileFromString("weex-js-json",
                              new String(task.response.data, "utf-8"), true);
                        } catch (UnsupportedEncodingException e) {
                          e.printStackTrace();
                        }
                        return false;
                      }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                      @Override public void call(Boolean aBoolean) {
                        LogUtil.d("weex js -save" + aBoolean);
                      }
                    });
              }

              @Override public void onError(WXHttpTask task) {
              }
            };

            WXHttpManager.getInstance().sendRequest(task);
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

  public static boolean isExistsCache(String key) {
    String path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
            + "/"
            + key;
    File f1 = new File(path);
    return f1.exists();
  }

  public static boolean writeFileFromString(final String key, final String content,
      final boolean append) {
    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) return false;
    String path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
            + "/"
            + key;
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    file = new File(path);
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(file, append));
      bw.write(content);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static final String LINE_SEP = System.getProperty("line.separator");

  public static String readFile2String(final String key, final String charsetName) {
    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(charsetName)) return "";
    String path =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
            + "/"
            + key;
    File file = new File(path);
    if (!file.exists()) return "";
    BufferedReader reader = null;
    try {
      StringBuilder sb = new StringBuilder();

      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));

      String line;
      if ((line = reader.readLine()) != null) {
        sb.append(line);
        while ((line = reader.readLine()) != null) {
          sb.append(LINE_SEP).append(line);
        }
      }
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
