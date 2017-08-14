package cn.qingchengfit.saasbase.student.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import java.util.List;

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
 * Created by Paper on 2017/8/11.
 */

public class StudentBusinessUtils {
  public static Drawable getStudentStatusDrawable(Context context, int x) {
    Drawable drawable = new ShapeDrawable(new OvalShape());
    switch (x) {
      case 1:
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.red),
            PorterDuff.Mode.CLEAR);
        break;
      case 2:
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.orange),
            PorterDuff.Mode.CLEAR);
        break;
      default:
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
            PorterDuff.Mode.CLEAR);
        break;
    }
    return drawable;
  }


  public static String getIdStrFromStaffs(List<Staff> staffs){
    if (staffs != null) {
      String ret = "";
      int i = 1;
      for (Staff staff : staffs) {
        ret = TextUtils.concat(ret,staff.getId()).toString();
        if (i < staffs.size())
          ret = TextUtils.concat(ret,",").toString();
        i++;
      }
      return ret;
    }else return "";
  }


}
