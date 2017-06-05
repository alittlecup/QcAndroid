package cn.qingchengfit.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import java.util.HashMap;

import static android.R.attr.action;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/27.
 */

public class BaseRouter {

    HashMap<String, Class<?>> routers = new HashMap<>();

    public void routerTo(String module, String action, Context context, int request) {
        if (routers.containsKey(module)) {
            Intent it = new Intent(context, routers.get(module));
            it.putExtra("action", action);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(it, request);
            }
        } else {
            //没有指定模块 todo
        }
    }

    public void routerTo(String module, String action, Fragment context, int request) {
        if (routers.containsKey(module)) {
            Intent it = new Intent(context.getContext(), routers.get(module));
            it.putExtra("action", action);
            context.startActivityForResult(it, request);
        } else {
            //没有指定模块 todo
        }
    }

    public void routerTo(String module, Context context) {
        if (routers.containsKey(module)) {
            Intent it = new Intent(context, routers.get(module));
            it.putExtra("action", action);
            context.startActivity(it);
        } else {
            //没有指定模块 todo
        }
    }

    public void registeRouter(String module, Class<?> activitycalss) {
        routers.put(module, activitycalss);
    }
}
