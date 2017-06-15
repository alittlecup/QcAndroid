package cn.qingchengfit.staffkit.views.student.followup;

import android.graphics.Color;
import cn.qingchengfit.staffkit.R;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
public class FollowUpDataStatistics1Fragment extends FollowUpDataStatisticsBaseFragment {
    @Inject public FollowUpDataStatistics1Fragment() {
    }

    @Override public int getLayoutResouseId() {
        return R.layout.fragment_follow_up_data_statistics_1;
    }

    @Override public int getLineColor() {
        return Color.parseColor("#f9944e");
    }

    @Override public int getFillColor() {
        return Color.parseColor("#aaf9944e");
    }

    @Override public int getStatus() {
        return 1;
    }

    public void initDI() {

    }
}
