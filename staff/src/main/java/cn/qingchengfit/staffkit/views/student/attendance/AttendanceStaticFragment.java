package cn.qingchengfit.staffkit.views.student.attendance;

import android.graphics.Color;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpDataStatisticsBaseFragment;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

/**
 * Created by fb on 2017/3/8.
 */

@FragmentWithArgs public class AttendanceStaticFragment extends FollowUpDataStatisticsBaseFragment {

    @Override public int getLayoutResouseId() {
        return R.layout.fragment_attendance_chart;
    }

    @Override public int getLineColor() {
        return Color.parseColor("#FF8CB4B9");
    }

    @Override public int getFillColor() {
        return Color.parseColor("#648CB4B9");
    }

    @Override public int getStatus() {
        return 0;
    }

    //public void initDI() {
    //    ((AttendanceComponent)mCallbackActivity.getComponent()).inject(this);
    //}
}
