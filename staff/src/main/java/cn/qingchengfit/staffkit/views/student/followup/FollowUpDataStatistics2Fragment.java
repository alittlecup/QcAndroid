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
public class FollowUpDataStatistics2Fragment extends FollowUpDataStatisticsBaseFragment {
    @Inject public FollowUpDataStatistics2Fragment() {
    }

    @Override public int getLayoutResouseId() {
        return R.layout.fragment_follow_up_data_statistics_2;
    }

    @Override public int getLineColor() {
        return Color.parseColor("#0db14b");
    }

    @Override public int getFillColor() {
        return Color.parseColor("#aa0db14b");
    }

    @Override public int getStatus() {
        return 2;
    }

    public void initDI() {

    }
}
