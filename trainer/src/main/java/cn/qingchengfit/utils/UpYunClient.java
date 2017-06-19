//package cn.qingchengfit.utils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//import main.java.com.UpYun;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 15/8/13 2015.
// */
//public class UpYunClient {
//
//    public static final String UPYUNPATH = "http://zoneke-img.b0.upaiyun.com/";
//
//    public static UpYun init() {
//        UpYun upyun = new UpYun("zoneke-img", "qcandroid", "07279e9e81c661b259f93a457d6491af");
//        upyun.setTimeout(30);
//        upyun.setApiDomain(UpYun.ED_AUTO);
//        return upyun;
//    }
//
//    public static Observable<String> rxUpLoad(final String cloudpath, final String filePath) {
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override public void call(Subscriber<? super String> subscriber) {
//                String upImg = UpYunClient.upLoadImg(cloudpath, new File(filePath));
//                subscriber.onNext(upImg);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }
//
//    /**
//     * @param path 记得带分隔符 eg /header/
//     * @param userid 云上存储的文件名
//     */
//    public static boolean upLoadImg(String path, String userid, File file) {
//        UpYun upYun = init();
//        boolean ret = false;
//        try {
//
//            upYun.setContentMD5(UpYun.md5(file));
//
//            ret = upYun.writeFile(path + userid + ".png", file, true);
//        } catch (IOException e) {
//            //            LogUtil.d("upload headerimg err:" + e.getMessage());
//            //            RevenUtils.sendException("upLoadImg", "UpYunClient", e);
//        } catch (Exception e) {
//            //            LogUtil.e("upload headerimg eer:" + e.getMessage());
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    /**
//     * @param path 记得带分隔符 eg /header/
//     */
//    public static String upLoadImg(String path, File file) {
//        UpYun upYun = init();
//        String name = UUID.randomUUID().toString() + ".png";
//        boolean ret = false;
//        try {
//
//            upYun.setContentMD5(UpYun.md5(file));
//            ret = upYun.writeFile(path + name, file, true);
//        } catch (IOException e) {
//            //            LogUtil.d("upload headerimg err:" + e.getMessage());
//            //            RevenUtils.sendException("upLoadImg", "UpYunClient", e);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (ret) {
//            return UPYUNPATH + path + name;
//        } else {
//            return "";
//        }
//    }
//
//    /**
//     public static final String UPYUNPATH = "http://zoneke-img.b0.upaiyun.com";
//
//     public static String KEY = "9TRMelXZlcUdrm+lrIluDPR4BFc=";
//     public static String SPACE = "zoneke-img";
//     //public static String SPACE = "qcandroid";
//     public static String savePath = "/uploads/{year}{mon}{day}/{random32}{.suffix}";
//
//
//
//     public static Map<String, Object> getUpParams(){
//     Map<String, Object> paramsMap = new HashMap<>();
//     //上传空间
//     paramsMap.put(Params.BUCKET, SPACE);
//     //保存路径，任选其中一个
//     paramsMap.put(Params.SAVE_KEY,savePath);
//     //paramsMap.put(Params.RETURN_URL,UPYUNPATH);
//     return paramsMap;
//     }
//
//     public static Observable<String> rxUpLoad(final String cloudpath, final String filePath) {
//     return Observable.create(new Observable.OnSubscribe<String>() {
//    @Override public void call(Subscriber<? super String> subscriber) {
//    //Map<String, Object> params = getUpParams();
//    //if (cloudpath.startsWith("/"))
//    //else params.put(Params.RETURN_URL,UPYUNPATH+"/"+cloudpath);
//    UploadManager.getInstance().formUpload(new File(filePath), getUpParams(), new SignatureListener() {
//    @Override public String getSignature(String policy) {
//    return UpYunUtils.md5(policy + KEY);
//    }
//    }, new UpCompleteListener() {
//    @Override public void onComplete(boolean isSuccess, String result) {
//    if (isSuccess) {
//    try{
//    JSONObject jsonObject = new JSONObject(result);
//    subscriber.onNext(UPYUNPATH+jsonObject.getString("url"));
//
//    }catch (Exception e){
//    subscriber.onNext("");
//    }
//
//
//    } else {
//    subscriber.onNext("");
//    }
//    subscriber.onCompleted();
//    }
//    }, new UpProgressListener() {
//    @Override public void onRequestProgress(long bytesWrite, long contentLength) {
//
//    }
//    });
//
//
//    }
//    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//
//     }
//
//     */
//
//}
