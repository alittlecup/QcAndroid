package cn.qingchengfit.router;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * @author billy.qi
 * @since 17/7/9 18:37
 */
@SuppressLint("PrivateApi")
class QCUtil {

    static Map<String, Object> convertToMap(JSONObject json) {
        Map<String, Object> params = null;
        try{
            if (json != null) {
                params = new HashMap<>(json.length());
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        Object value = json.get(key);
                        if (value == JSONObject.NULL) {
                            value = null;
                        }
                        params.put(key, value);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    static JSONObject convertToJson(Map<String, Object> map) {
        if (map != null) {
            try{
                JSONObject json = new JSONObject();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null) {
                        json.put(entry.getKey(), null);
                    } else {
                        json.put(entry.getKey(), value.toString());
                    }
                }
                return json;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Boolean isRunningMainProcess = null;
    /**
     * 进程是否以包名在运行（当前进程是否为主进程）
     */
    static boolean isMainProcess(){
        if (isRunningMainProcess == null) {
            Application application = QC.getApplication();
            if (application == null) {
                return false;
            }
            boolean main = false;
            try {
                ActivityManager manager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
                if (manager == null) {
                    return true;
                }
                List<RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
                for (RunningAppProcessInfo appProcess : processes) {
                    if (appProcess.pid == android.os.Process.myPid()
                            && application.getPackageName().equals(appProcess.processName)) {
                        main = true;
                        break;
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();

            }
            isRunningMainProcess = main;
        }
        return isRunningMainProcess;
    }

    /**
     * 反射获取application对象
     * @return application
     */
    static Application initApplication() {
        try {
            //通过反射的方式来获取当前进程的application对象
            Application app = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
            if (app != null) {
                return app;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            Application app = (Application) Class.forName("android.app.AppGlobals")
                    .getMethod("getInitialApplication").invoke(null);
            if (app != null) {
                return app;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
