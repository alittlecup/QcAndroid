# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/peggy/Applications/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/peggy/Applications/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.ipt.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.support.v4.app.Fragment
    -keep public class * extends android.support.v7.app.AppCompatActivity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment



    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######


    #####混淆保护自己项目的部分代码以及引用的第三方jar包library#######

    #-libraryjars libs/umeng-analytics-v5.2.4.jar

    #三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
    #-libraryjars libs/sdk-v1.0.0.jar
    #-libraryjars libs/look-v1.0.1.jar

    #如果不想混淆 keep 掉
    -keep class com.lippi.recorder.iirfilterdesigner.** {*; }
    #友盟
    -keep class com.umeng.**{*;}
    -keep class android.support.**{*;}

    #项目特殊处理代码

# --------------------------------------------------------------------------
# Addidional for x5.sdk classes for apps

-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
	*;
}

-keep class com.tencent.smtt.sdk.CacheManager {
	public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
	public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
	public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
	public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
	public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
	public static final <fields>;
	public java.lang.String getExtra();
	public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
	public <fields>;
	public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public enum com.tencent.smtt.sdk.QbSdk$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keepattributes Signature
-keep public class com.tencent.smtt.sdk.ValueCallback {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
	public <fields>;
	public <methods>;
}

-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
	public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
	public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebIconDatabase {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebStorage {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
	public <fields>;
	public <methods>;
}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

# Added for game demos
-keep public class com.tencent.smtt.sdk.TBSGamePlayer {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.Apn {
	public <fields>;
	public <methods>;
}
# end


-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
	public <fields>;
	public <methods>;
}

-keep class MTT.ThirdAppInfoNew {
	*;
}

-keep class com.tencent.mtt.MttTraceEvent {
	*;
}

# Game related
-keep public class com.tencent.smtt.gamesdk.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBooter {
        public <fields>;
        public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
	public protected *;
}

-keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
	public *;
}
#---------------------------------------------------------------------------



#butterkinfe
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class rx.**{*;}
-keep class cn.qingchengfit.staffkit.model.**{*;}
-keep class cn.qingchengfit.staffkit.usecase.**{*;}
-keep class okhttp3.**{*;}
-keep class cn.qingchengfit.**{*;}
-keep class cn.qingchengfit.constant.**{*;}
-keep class com.tencent.**{*;}
-keep class com.tinkerpatch.**{*;}
-keep class com.timehop.**{*;}
-keep class io.github.**{*;}
-keep class com.hannesdorfmann.**{*;}
-keep class se.emilsjolander.**{*;}
-keep class com.sensorsdata.**{*;}
-keep class com.h6ah4i.android.**{*;}
-keep class eu.davidea.**{*;}
-keep class com.dlazaro66.**{*;}
-keep class com.github.bumptech.**{*;}
-keep class com.tbruyelle.rxpermissions.**{*;}
-keep class com.getbase.**{*;}
-keep class com.google.**{*;}
-keep class com.hkm.**{*;}
-keep class rg.glassfish.**{*;}
-keep class io.reactivex.**{*;}
-keep class com.github.afollestad.**{*;}
-keep class com.umeng.analytics.**{*;}
-keep class com.jakewharton.**{*;}
-keep class com.baidu.android.pushservice.**{*;}
-keep class com.xiaomi.mipush.**{*;}


-dontwarn  rx.**
-dontwarn  cn.qingchengfit.staffkit.model.**
-dontwarn  cn.qingchengfit.staffkit.usecase.**
-dontwarn  okhttp3.**
-dontwarn  cn.qingchengfit.**
-dontwarn  cn.qingchengfit.constant.**
-dontwarn  com.tencent.**
-dontwarn  com.tinkerpatch.**
-dontwarn  com.timehop.**
-dontwarn  io.github.**
-dontwarn  com.hannesdorfmann.**
-dontwarn  se.emilsjolander.**
-dontwarn  com.sensorsdata.**
-dontwarn  com.h6ah4i.android.**
-dontwarn  eu.davidea.**
-dontwarn  com.dlazaro66.**
-dontwarn  com.github.bumptech.**
-dontwarn  com.tbruyelle.rxpermissions.**
-dontwarn  com.getbase.**
-dontwarn  com.google.**
-dontwarn  com.hkm.**
-dontwarn  rg.glassfish.**
-dontwarn  io.reactivex.**
-dontwarn  com.github.afollestad.**
-dontwarn  com.umeng.analytics.**
-dontwarn  com.jakewharton.**
-dontwarn  com.baidu.android.pushservice.**
-dontwarn  com.xiaomi.mipush.**


-keep class com.commonsware.cwac.** { *; }


#---------------------------------------------------------------------------
#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}






    #忽略警告
    -dontwarn com.lippi.recorder.utils**
    #保留一个完整的包
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}


    #如果引用了v4或者v7包
    -dontwarn android.support.**

    ####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
    #-keepclassmembers enum * {
    #  public static **[] values();
    #  public static ** valueOf(java.lang.String);
    #}

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #避免混淆泛型 如果混淆报错建议关掉
    #–keepattributes Signature

    #移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
    #-assumenosideeffects class android.util.Log {
    #    public static boolean isLoggable(java.lang.String, int);
    #    public static int v(...);
    #    public static int i(...);
    #    public static int w(...);
    #    public static int d(...);
    #    public static int e(...);
    #}

    #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
    #gson
    #-libraryjars libs/gson-2.2.2.jar
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class android.support.**{*;}
    -keep class com.google.**{*;}
    -keep class in.srain.**{*;}
    -keep class com.afollestad.**{*;}
    -keep class me.gujun.**{*;}
    -keep class com.squareup.**{*;}
    -keep class me.relex.**{*;}
    -keep class com.squareup.**{*;}
    -keep class io.reactivex.**{*;}
    -keep class org.glassfish.**{*;}
    -keep class com.google.**{*;}
    -keep class com.qingchengfit.fitcoach.bean.**{*;}
    -keep class com.qingchengfit.fitcoach.http.bean.**{*;}
        -keep class net.kencochrane.raven.**{*;}
        -keep public class * implements com.bumptech.glide.module.GlideModule
        -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
        }
        -keep class butterknife.** { *; }
        -dontwarn butterknife.internal.**
        -keep class **$$ViewBinder { *; }

        -keepclasseswithmembernames class * {
            @butterknife.* <fields>;
        }

        -keepclasseswithmembernames class * {
            @butterknife.* <methods>;
        }

#//push


-keep class com.tencent.qcloud.timchat.chatutils.** {*;}
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**
-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}
-keep class com.baidu.mapapi.**{*;}
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**