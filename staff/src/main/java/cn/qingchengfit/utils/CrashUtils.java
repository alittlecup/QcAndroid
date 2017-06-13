//package cn.qingchengfit.utils;
//
//import cn.qingchengfit.staffkit.BuildConfig;
//import timber.log.Timber;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/5/14 2016.
// */
//public class CrashUtils {
//
//    public static void sendCrash(Throwable throwable) {
//        sendCrash(throwable,"");
//    }
//    public static void sendCrash(Throwable throwable,String pulsinfo) {
//        if (!BuildConfig.DEBUG) {
////            FIR.sendCrashManually(throwable);
//        }else {
//            Timber.e(pulsinfo + " -------error!!!");
//            throwable.printStackTrace();
//            Timber.e("------error end!!!");
//
//        }
//    }
//}
