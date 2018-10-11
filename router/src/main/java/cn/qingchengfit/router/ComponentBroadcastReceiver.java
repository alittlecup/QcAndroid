package cn.qingchengfit.router;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * app之间的组件调用通信方式：广播
 * @author billy.qi
 */
public class ComponentBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        QC.log("onReceive, packageName=" + context.getPackageName() + "， action=" + action);
        if (!QC.RESPONSE_FOR_REMOTE_CC) {
            QC.log("receive cc, but QC.enableRemoteCC() is set to false in this app");
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        String componentName = extras.getString(RemoteQCInterceptor.KEY_COMPONENT_NAME);
        if (TextUtils.isEmpty(componentName) ||
                !ComponentManager.hasComponent(componentName)) {
            //当前app中不包含此组件，直接返回（其它app会响应该调用）
            return;
        }
        if (QC.VERBOSE_LOG) {
            QC.verboseLog(extras.getString(RemoteQCInterceptor.KEY_CALL_ID)
                , "receive remote cc, start service to perform it.");
        }
        Intent serviceIntent = new Intent(context, ComponentService.class);
        serviceIntent.putExtras(extras);
        context.startService(serviceIntent);
    }
}
