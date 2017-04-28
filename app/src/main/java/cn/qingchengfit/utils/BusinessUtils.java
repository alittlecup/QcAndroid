package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import com.qingchengfit.fitcoach.R;
import java.util.ArrayList;
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
 * Created by Paper on 2017/4/15.
 */

public class BusinessUtils {
    /**
     * 变更销售-会员状态
     *
     * @param view view
     * @param status status
     * 0 # 新注册
     * 1 # 跟进中
     * 2 # 会员
     * 3 # 非会员
     */
    public static void studentStatus(TextView view, int status) {
        String statuStr = "";
        Drawable drawable = new ColorDrawable();
        //switch (Integer.valueOf(status)) {
            //case 0:
            //    statuStr = "新注册";
            //    drawable = new CircleView(R.color.qc_student_status_0);
            //    break;
            //case 1:
            //    statuStr = "跟进中";
            //    drawable = new CircleView(R.color.qc_student_status_1);
            //    break;
            //case 2:
            //    statuStr = "会员";
            //    drawable = new CircleView(R.color.qc_student_status_2);
            //    break;
            //default:
            //    statuStr = "未知";
            //    drawable = new CircleView(R.color.qc_student_status_0);
            //    break;
        //}
        view.setText(statuStr);
        drawable.setBounds(0, 0, 26, 26);
        view.setCompoundDrawablePadding(10);
        view.setCompoundDrawables(drawable, null, null, null);
    }


    public static List<String> qcstudentIds2List(List<QcStudentBean> students,Context context) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i) instanceof Personage)
                ret.add(context.getString(R.string.chat_user_id_header,students.get(i).getId()));
        }

        return ret;
    }

    public static List<String> qcstudentIds2ListChatExcepteSb(List<QcStudentBean> students, String id,Context context) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            if (!TextUtils.equals(students.get(i).getId(),id))
                ret.add(context.getString(R.string.chat_user_id_header,students.get(i).getId()));
        }

        return ret;
    }
}
