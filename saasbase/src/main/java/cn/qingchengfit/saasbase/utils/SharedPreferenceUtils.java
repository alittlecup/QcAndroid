package cn.qingchengfit.saasbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bob Du on 2018/9/11 18:44
 */
public class SharedPreferenceUtils {
    private String name = "QcSP";
    private Context context;

    public SharedPreferenceUtils(Context context) {
        this.context = context;
    }

    public void saveFlag(String flag, boolean isFirst) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(flag, isFirst);
        editor.commit();
    }

    public boolean IsFirst(String flag) {
        boolean isFirst;
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        isFirst = sp.getBoolean(flag, true);
        return isFirst;
    }
}
