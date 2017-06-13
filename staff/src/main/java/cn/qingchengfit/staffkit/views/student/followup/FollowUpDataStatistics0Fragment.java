package cn.qingchengfit.staffkit.views.student.followup;

import android.graphics.Color;
import cn.qingchengfit.staffkit.R;

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
public class FollowUpDataStatistics0Fragment extends FollowUpDataStatisticsBaseFragment {

    @Override public int getLayoutResouseId() {
        return R.layout.fragment_follow_up_data_statistics_0;
    }

    @Override public int getLineColor() {
        return Color.parseColor("#6eb8f1");
    }

    @Override public int getFillColor() {
        return Color.parseColor("#aa6eb8f1");
    }

    @Override public int getStatus() {
        return 0;
    }

    public void initDI() {
    }
}
