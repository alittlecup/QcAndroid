package cn.qingchengfit.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.student.attendance.FilterCustomFragment;
import cn.qingchengfit.staffkit.views.student.attendance.FilterCustomFragmentBuilder;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpFilterEvent;
import cn.qingchengfit.staffkit.views.student.followup.LatestTimeFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;

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
 * Created by Paper on 2017/5/4.
 */

@FragmentWithArgs public class FilterTimesFragment extends BaseFragment {
    public int page = 0;
    //最近7天 最近30天 自定义
    // 1 : + 今日
    @Arg int type = 0;
    @Arg int limitDay; // 限制自定义选择的范围
    LatestTimeFragment latestTimeFragment;
    FilterCustomFragment filterCustomFragment;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        latestTimeFragment = new LatestTimeFragment();
        latestTimeFragment.type = type;
        latestTimeFragment.setOnItemClickListener(new FlexibleAdapter.OnItemClickListener() {
            @Override public boolean onItemClick(int i) {
                if ((latestTimeFragment.type == 0 && i == 2) || (type == 1 && i == 3)) {
                    getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out)
                        .replace(R.id.frag_filter_times, filterCustomFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                } else {
                    FollowUpFilterEvent e = new FollowUpFilterEvent(FollowUpFilterEvent.EVENT_LATEST_TIME_CLICK);
                    e.page = page;
                    e.position = i;
                    RxBus.getBus().post(e);
                }
                return true;
            }
        });
        filterCustomFragment = new FilterCustomFragmentBuilder("时间段").build();
        filterCustomFragment.setSelectTime(true);
        filterCustomFragment.setOnBackFilterDataListener(new FilterCustomFragment.OnBackFilterDataListener() {
            @Override public void onSettingData(String start, String end) {
                FollowUpFilterEvent e = new FollowUpFilterEvent(FollowUpFilterEvent.EVENT_LATEST_TIME_CUSTOM_DATA);
                e.page = page;
                e.start = start;
                e.end = end;
                RxBus.getBus().post(e);
            }

            @Override public void onBack() {
                getChildFragmentManager().popBackStackImmediate();
            }
        });

        filterCustomFragment.limitDay = limitDay;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_times_choose, container, false);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction tr = getChildFragmentManager().beginTransaction();
        tr.replace(R.id.frag_filter_times, latestTimeFragment);
        tr.commitAllowingStateLoss();
    }

    @Override public String getFragmentName() {
        return FilterTimesFragment.class.getName();
    }
}
