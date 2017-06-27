package cn.qingchengfit.utils;

import android.util.Log;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.utils.UpYunUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/13 2015.
 */
public class UpYunClient {

    public static final String UPYUNPATH = "http://zoneke-img.b0.upaiyun.com";
    //空间名
    public static String SPACE = "zoneke-img";
    //操作员
    public static String OPERATER = "qcandroid";
    //密码
    public static String PASSWORD = "07279e9e81c661b259f93a457d6491af";

    public static UpYun init() {
        UpYun upyun = new UpYun("zoneke-img", "qcandroid", "07279e9e81c661b259f93a457d6491af");
        upyun.setTimeout(30);
        upyun.setApiDomain(UpYun.ED_AUTO);
        return upyun;
    }

    public static Observable<String> rxUpLoad(final String cloudpath, final String filePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(final Subscriber<? super String> subscriber) {
                File upFile = new File(filePath);
                //TypedFile upFile = new TypedFile("image/jpeg", new File(filePath));
                String name = UUID.randomUUID().toString() + ".png";
                final Map<String, Object> paramsMap = new HashMap<>();
                //上传空间
                paramsMap.put(Params.BUCKET, SPACE);
                //保存路径，任选其中一个
                paramsMap.put(Params.SAVE_KEY, (cloudpath.startsWith("/") ? "" : "/") + cloudpath + name);
                paramsMap.put(Params.CONTENT_MD5, UpYunUtils.md5Hex(upFile));

                //paramsMap.put(Params.RETURN_URL, UPYUNPATH);
                UpCompleteListener completeListener = new UpCompleteListener() {
                    @Override public void onComplete(boolean isSuccess, String result) {
                        if (isSuccess) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                subscriber.onNext(UPYUNPATH + jsonObject.getString("url"));
                                subscriber.onCompleted();
                            } catch (JSONException e) {
                                subscriber.onError(new Throwable(e));
                            }
                        } else {
                            subscriber.onError(new Throwable("上传图片失败"));
                            Log.e("upyun", isSuccess + ":" + result);
                        }
                    }
                };

                UploadEngine.getInstance().formUpload(upFile, paramsMap, OPERATER, UpYunUtils.md5(PASSWORD), completeListener, null);
                //String upImg = UpYunClient.upLoadImg(cloudpath, new File(filePath));

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param path 记得带分隔符 eg /header/
     * @param userid 云上存储的文件名
     */
    public static boolean upLoadImg(String path, String userid, File file) {
        UpYun upYun = init();
        boolean ret = false;
        try {

            upYun.setContentMD5(UpYun.md5(file));

            ret = upYun.writeFile(path + userid + ".png", file, true);
        } catch (IOException e) {
            //            LogUtil.d("upload headerimg err:" + e.getMessage());
            //            RevenUtils.sendException("upLoadImg", "UpYunClient", e);
        } catch (Exception e) {
            //            LogUtil.e("upload headerimg eer:" + e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * @param path 记得带分隔符 eg /header/
     */
    public static String upLoadImg(String path, File file) {
        UpYun upYun = init();
        String name = UUID.randomUUID().toString() + ".png";
        boolean ret = false;
        try {

            upYun.setContentMD5(UpYun.md5(file));
            ret = upYun.writeFile(path + name, file, true);
        } catch (IOException e) {
            //            LogUtil.d("upload headerimg err:" + e.getMessage());
            //            RevenUtils.sendException("upLoadImg", "UpYunClient", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ret) {
            return UPYUNPATH + path + name;
        } else {
            return "";
        }
    }
}
