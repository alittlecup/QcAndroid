//package cn.qingchengfit.staffkit;
//
//import android.annotation.TargetApi;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.AssetManager;
//import android.content.res.Resources;
//import android.os.Build;
//import android.support.multidex.MultiDex;
//import android.util.Log;
//
//import com.tencent.tinker.anno.DefaultLifeCycle;
//import com.tencent.tinker.lib.listener.DefaultPatchListener;
//import com.tencent.tinker.lib.patch.UpgradePatch;
//import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
//import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
//import com.tencent.tinker.lib.service.PatchResult;
//import com.tencent.tinker.loader.app.DefaultApplicationLike;
//import com.tencent.tinker.loader.shareutil.ShareConstants;
//import com.tinkerpatch.sdk.TinkerPatch;
//import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
//import com.tinkerpatch.sdk.server.callback.RollbackCallBack;
//import com.tinkerpatch.sdk.server.callback.TinkerPatchRequestCallback;
//import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
//import com.tinkerpatch.sdk.tinker.service.TinkerServerResultService;
//
//import java.util.HashMap;
//
///**
// * power by
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
// * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
// * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
// * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
// * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
// * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
// * MMMMMM'     :           :           :           :           :    `MMMMMM
// * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
// * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
// * Created by Paper on 2016/12/27.
// */
//
//@DefaultLifeCycle(application = "cn.qingchengfit.staffkit.App",
//        flags = ShareConstants.TINKER_ENABLE_ALL,
//        loadVerifyFlag = false)
//public class AppLike extends DefaultApplicationLike {
//    private static final String TAG = "AppLike";
//
//    public AppLike(Application application, int i, boolean b, long l, long l1, Intent intent, Resources[] resources, ClassLoader[] classLoaders, AssetManager[] assetManagers) {
//        super(application, i, b, l, l1, intent, resources, classLoaders, assetManagers);
//    }
//    /**
//     * install multiDex before install tinker
//     * so we don't need to put the tinker lib classes in the main dex
//     */
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        //you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        //开始检查是否有补丁，这里配置的是每隔访问3小时服务器是否有更新。
//        TinkerPatch.init(this)
//                .reflectPatchLibrary()
//                .setPatchRollbackOnScreenOff(true)
//                .setPatchRestartOnSrceenOff(true);
//        TinkerPatch.with().fetchPatchUpdate(false);
//    }
//
//
//    /**
//     * 在这里给出TinkerPatch的所有接口解释
//     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
//     */
//    private void useSample() {
//        TinkerPatch.init(this)
//                //是否自动反射Library路径,无须手动加载补丁中的So文件
//                //注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
//                .reflectPatchLibrary()
//                //向后台获取是否有补丁包更新,默认的访问间隔为3个小时
//                //若参数为true,即每次调用都会真正的访问后台配置
//                .fetchPatchUpdate(false)
//                //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
//                .setFetchPatchIntervalByHours(3)
//                //向后台获得动态配置,默认的访问间隔为3个小时
//                //若参数为true,即每次调用都会真正的访问后台配置
//                .fetchDynamicConfig(new ConfigRequestCallback() {
//                    @Override
//                    public void onSuccess(HashMap<String, String> hashMap) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//
//                    }
//                }, false)
//                //设置访问后台动态配置的时间间隔,默认为3个小时
//                .setFetchDynamicConfigIntervalByHours(3)
//                //设置当前渠道号,对于某些渠道我们可能会想屏蔽补丁功能
//                //设置渠道后,我们就可以使用后台的条件控制渠道更新
//                .setAppChannel("default")
//                //屏蔽部分渠道的补丁功能
//                .addIgnoreAppChannel("googleplay")
//                //设置tinkerpatch平台的条件下发参数
//                .setPatchCondition("test", "1")
//                //设置补丁合成成功后,锁屏重启程序
//                //默认是等应用自然重启
//                .setPatchRestartOnSrceenOff(true)
//                //我们可以通过ResultCallBack设置对合成后的回调
//                //例如弹框什么
//                .setPatchResultCallback(new ResultCallBack() {
//                    @Override
//                    public void onPatchResult(PatchResult patchResult) {
//                        Log.i(TAG, "onPatchResult callback here");
//                    }
//                })
//                //设置收到后台回退要求时,锁屏清除补丁
//                //默认是等主进程重启时自动清除
//                .setPatchRollbackOnScreenOff(true)
//                //我们可以通过RollbackCallBack设置对回退时的回调
//                .setPatchRollBackCallback(new RollbackCallBack() {
//                    @Override
//                    public void onPatchRollback() {
//                        Log.i(TAG, "onPatchRollback callback here");
//                    }
//                });
//    }
//
//    /**
//     * 自定义Tinker类的高级用法,一般不推荐使用
//     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
//     */
//    private void complexSample() {
//        TinkerPatch.Builder builder = new TinkerPatch.Builder(this);
//        //修改tinker的构造函数,自定义类
//        builder.listener(new DefaultPatchListener(getApplication()))
//                .loadReporter(new DefaultLoadReporter(getApplication()))
//                .patchReporter(new DefaultPatchReporter(getApplication()))
//                .resultServiceClass(TinkerServerResultService.class)
//                .upgradePatch(new UpgradePatch())
//                .patchRequestCallback(new TinkerPatchRequestCallback());
//
//        TinkerPatch.init(builder.build());
//    }
//
//}
