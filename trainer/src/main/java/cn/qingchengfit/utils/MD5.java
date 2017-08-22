//package cn.qingchengfit.utils;
//
//import com.qingchengfit.fitcoach.Configs;
//import java.security.MessageDigest;
//import java.util.Random;
//
//public class MD5 {
//
//    private MD5() {
//    }
//
//    public final static String getMessageDigest(byte[] buffer) {
//        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//        try {
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
//            mdTemp.update(buffer);
//            byte[] md = mdTemp.digest();
//            int j = md.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(str);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String getSign(String timestamp, String ran) {
//        String x = "appid="
//            + Configs.APP_ID
//            + "&prepayid=wx20160226124520229c7c8edf0039065235&partnerid=1316532101&package=Sign=WXPay&noncestr="
//            + ran
//            + "&timestamp="
//            + timestamp
//            + "&key=dbd657ae473c16bf3a9fff88d666dc55";
//        return MD5.getMessageDigest(x.getBytes()).toUpperCase();
//    }
//
//    public static long genTimeStamp() {
//        return System.currentTimeMillis() / 1000;
//    }
//
//    public static String genNonceStr() {
//        Random random = new Random();
//        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }
//}
