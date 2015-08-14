package com.paper.paperbaselibrary.utils;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
 * Created by Paper on 15/8/15 2015.
 * <p>
 * 用于获取手机硬件以及系统信息
 */
public class PhoneInfoUtils {

    public static void getSDCardInfo() {
        // 需要判断手机上面SD卡是否插好，如果有SD卡的情况下，我们才可以访问得到并获取到它的相关信息，当然以下这个语句需要用if做判断
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            // 获取block的SIZE
            long blocSize = statfs.getBlockSize();
            // 获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            // 空闲的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            // 计算总空间大小和空闲的空间大小
            // 存储空间大小跟空闲的存储空间大小就被计算出来了。
            long availableSize = blocSize * availaBlock;
            // (availableBlocks * blockSize)/1024 KIB 单位
            // (availableBlocks * blockSize)/1024 /1024 MIB单位
            long allSize = blocSize * totalBlocks;
        }

    }

// 首先设置用户权限
// <uses-permission
// android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
// <uses-permission
// android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
// <uses-permission
// android:name="android.permission.WAKE_LOCK"></uses-permission>

    /**
     * 获取wifi地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress2(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    /**
     * 转换ip为 255.255.255.255
     *
     * @param i
     * @return
     */
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    // 查看本机外网IP
/*
 * 该方法需要设备支持上网 查看
 * System.out.println((GetNetIp("http://fw.qq.com/ipaddress"))); 加权限
 * <uses-permission
 * android:name="android.permission.INTERNET"></uses-permission>
 * 通过获取http://fw.qq.com/ipaddress网页取得外网IP 这里有几个查看IP的网址然后提取IP试试。
 * http://ip168.com/ http://www.cmyip.com/ http://city.ip138.com/ip2city.asp
 */

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getSIMinfo(Context context) {
         /*
     * TelephonyManager类主要提供了一系列用于访问与手机通讯相关的状态和信息的get方法。其中包括手机SIM的状态和信息
     * 、电信网络的状态及手机用户的信息。
     * 在应用程序中可以使用这些get方法获取相关数据。TelephonyManager类的对象可以通过Context
     * .getSystemService(Context.TELEPHONY_SERVICE)
     * 方法来获得，需要注意的是有些通讯信息的获取对应用程序的权限有一定的限制
     * ，在开发的时候需要为其添加相应的权限。以下列出TelephonyManager类所有方法及说明：
     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家
     * 以及某些类型的用户访问信息。 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
     * 使用Context.getSystemService (Context.TELEPHONY_SERVICE)来获取这个类的实例。
     */

        // 解释：
        // IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
        // IMSI共有15位，其结构如下：
        // MCC+MNC+MIN
        // MCC：Mobile Country Code，移动国家码，共3位，中国为460;
        // MNC:Mobile NetworkCode，移动网络码，共2位
        // 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
        // 合起来就是（也是Android手机中APN配置文件中的代码）：
        // 中国移动：46000 46002
        // 中国联通：46001
        // 中国电信：46003
        // 举例，一个典型的IMSI号码为460030912121001

        // IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
        // IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的
        // 其组成为：
        // 1. 前6位数(TAC)是”型号核准号码”，一般代表机型
        // 2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
        // 3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
        // 4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    /*
     * 电话状态： 1.tm.CALL_STATE_IDLE=0 无活动，无任何状态时 2.tm.CALL_STATE_RINGING=1
     * 响铃，电话进来时 3.tm.CALL_STATE_OFFHOOK=2 摘机
     */
        tm.getCallState();// int
/*
     * 电话方位：
     */
        // 返回当前移动终端的位置
        CellLocation location = tm.getCellLocation();
        // 请求位置更新，如果更新将产生广播，接收对象为注册LISTEN_CELL_LOCATION的对象，需要的permission名称为
        // ACCESS_COARSE_LOCATION。
        location.requestLocationUpdate();

        /**
         * 获取数据活动状态
         *
         * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据 DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据
         * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据 DATA_ACTIVITY_NONE
         * 数据连接状态：活动，但无数据发送和接受
         */
        tm.getDataActivity();

        /**
         * 获取数据连接状态
         *
         * DATA_CONNECTED 数据连接状态：已连接 DATA_CONNECTING 数据连接状态：正在连接
         * DATA_DISCONNECTED 数据连接状态：断开 DATA_SUSPENDED 数据连接状态：暂停
         */
        tm.getDataState();

        /**
         * 返回当前移动终端的唯一标识，设备ID
         *
         * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID Return null if device ID is not
         * available.
         */
        String Imei = tm.getDeviceId();// String

    /*
     * 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。 设备的软件版本号： 例如：the IMEI/SV(software
     * version) for GSM phones. Return null if the software version is not
     * available.
     */
        tm.getDeviceSoftwareVersion();// String

    /*
     * 手机号： GSM手机的 MSISDN. Return null if it is unavailable.
     */
        String phoneNum = tm.getLine1Number();// String

    /*
     * 获取ISO标准的国家码，即国际长途区号。 注意：仅当用户已在网络注册后有效。 在CDMA网络中结果也许不可靠。
     */
        tm.getNetworkCountryIso();// String

    /*
     * MCC+MNC(mobile country code + mobile network code) 注意：仅当用户已在网络注册时有效。
     * 在CDMA网络中结果也许不可靠。
     */
        tm.getNetworkOperator();// String

    /*
     * 按照字母次序的current registered operator(当前已注册的用户)的名字 注意：仅当用户已在网络注册时有效。
     * 在CDMA网络中结果也许不可靠。
     */

        tm.getNetworkOperatorName();// String

    /*
     * 当前使用的网络类型： 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0 NETWORK_TYPE_GPRS GPRS网络
     * 1 NETWORK_TYPE_EDGE EDGE网络 2 NETWORK_TYPE_UMTS UMTS网络 3
     * NETWORK_TYPE_HSDPA HSDPA网络 8 NETWORK_TYPE_HSUPA HSUPA网络 9
     * NETWORK_TYPE_HSPA HSPA网络 10 NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5 NETWORK_TYPE_EVDO_A EVDO网络,
     * revision A. 6 NETWORK_TYPE_1xRTT 1xRTT网络 7
     */
        tm.getNetworkType();// int

    /*
     * 手机类型： 例如： PHONE_TYPE_NONE 无信号 PHONE_TYPE_GSM GSM信号 PHONE_TYPE_CDMA
     * CDMA信号
     */
        tm.getPhoneType();// int

    /*
     * Returns the ISO country code equivalent for the SIM provider's
     * country code. 获取ISO国家码，相当于提供SIM卡的国家码。
     */
        tm.getSimCountryIso();// String

    /*
     * Returns the MCC+MNC (mobile country code + mobile network code) of
     * the provider of the SIM. 5 or 6 decimal digits.
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字. SIM卡的状态必须是
     * SIM_STATE_READY(使用getSimState()判断).
     */
        tm.getSimOperator();// String

    /*
     * 服务商名称： 例如：中国移动、联通 SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     */
        tm.getSimOperatorName();// String

    /*
     * SIM卡的序列号： 需要权限：READ_PHONE_STATE
     */
        tm.getSimSerialNumber();// String

    /*
     * SIM的状态信息： SIM_STATE_UNKNOWN 未知状态 0 SIM_STATE_ABSENT 没插卡 1
     * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2 SIM_STATE_PUK_REQUIRED
     * 锁定状态，需要用户的PUK码解锁 3 SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
     * SIM_STATE_READY 就绪状态 5
     */
        tm.getSimState();// int

    /*
     * 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone. 需要权限：READ_PHONE_STATE
     */
        tm.getSubscriberId();// String

    /*
     * 取得和语音邮件相关的标签，即为识别符 需要权限：READ_PHONE_STATE
     */

        tm.getVoiceMailAlphaTag();// String

    /*
     * 获取语音邮件号码： 需要权限：READ_PHONE_STATE
     */
        tm.getVoiceMailNumber();// String

    /*
     * ICC卡是否存在
     */
        tm.hasIccCard();// boolean

    /*
     * 是否漫游: (在GSM用途下)
     */
        tm.isNetworkRoaming();//

        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = tm.getSubscriberId(); // 国际移动用户识别码
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {

            ProvidersName = "中国联通";

        } else if (IMSI.startsWith("46003")) {

            ProvidersName = "中国电信";

        }
        // 返回当前移动终端附近移动终端的信息
    /*
     * 附近的电话的信息: 类型：List<NeighboringCellInfo>
     * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
     */
        List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
        for (NeighboringCellInfo info : infos) {
            // 获取邻居小区号
            int cid = info.getCid();
            // 获取邻居小区LAC，LAC:
            // 位置区域码。为了确定移动台的位置，每个GSM/PLMN的覆盖区都被划分成许多位置区，LAC则用于标识不同的位置区。
            info.getLac();
            info.getNetworkType();
            info.getPsc();
            // 获取邻居小区信号强度
            info.getRssi();
        }
        String mtyb = android.os.Build.BRAND;// 手机品牌
        String mtype = android.os.Build.MODEL; // 手机型号
        return "手机号码:" + phoneNum + "\n" + "服务商：" + ProvidersName + "\n" + "IMEI：" + Imei;

    }

    /**
     * 获取外网ip
     *
     * @param ipaddr
     * @return
     */

    public String GetNetIp(String ipaddr) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(ipaddr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                return strber.toString();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机cpu信息
     *
     * @return
     */

    private String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""}; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        // Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return "1-cpu型号:" + cpuInfo[0] + "2-cpu频率:" + cpuInfo[1];
    }// 和内存信息同理，cpu信息可通过读取/proc/cpuinfo文件来得到，其中第一行为cpu型号，第二行为cpu频率。

    private String getTimes(Context mContext) {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        int m = (int) ((ut / 60) % 60);
        int h = (int) ((ut / 3600));
        return h + " 小时" + +m + " 分钟";
    }


    private String getHandSetInfo() {
        String handSetInfo = "手机型号:" + android.os.Build.MODEL
                + "\n系统版本:" + android.os.Build.VERSION.RELEASE
                + "\n产品型号:" + android.os.Build.PRODUCT
                + "\n版本显示:" + android.os.Build.DISPLAY
                + "\n系统定制商:" + android.os.Build.BRAND
                + "\n设备参数:" + android.os.Build.DEVICE
                + "\n开发代号:" + android.os.Build.VERSION.CODENAME
                + "\nSDK版本号:" + android.os.Build.VERSION.SDK_INT
                + "\nCPU类型:" + android.os.Build.CPU_ABI
                + "\n硬件类型:" + android.os.Build.HARDWARE
                + "\n主机:" + android.os.Build.HOST
                + "\n生产ID:" + android.os.Build.ID
                + "\nROM制造商:" + android.os.Build.MANUFACTURER // 这行返回的是rom定制商的名称
                ;
        return handSetInfo;
    }


}
