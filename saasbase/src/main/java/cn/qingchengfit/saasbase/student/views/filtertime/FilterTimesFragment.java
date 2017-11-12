package cn.qingchengfit.saasbase.student.views.filtertime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventFilterDate;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpEvent;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.functions.Action0;
import rx.functions.Action1;

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

public class FilterTimesFragment extends SaasBaseFragment {
    public int page = 0;
    //最近7天 最近30天 自定义
    // 1 : + 今日
    int type = 0;
    int limitDay; // 限制自定义选择的范围

    @Inject
    FollowUpFilterModel model;
    @Named("commonFilter")
    @Inject
    StudentFilter filter;

    @Named("topFilter")
    @Inject
    StudentFilter topFilter;

    FilterListStringFragment dayFilterFragment;
    FilterCustomFragment filterCustomFragment;

    public static FilterTimesFragment getInstance(int type, int limitDay) {
        FilterTimesFragment filterTimesFragment = new FilterTimesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("limitDay", limitDay);
        filterTimesFragment.setArguments(bundle);
        return filterTimesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dayFilterFragment = new FilterListStringFragment();
        type = getArguments().getInt("type");
        limitDay = getArguments().getInt("limitDay");
        dayFilterFragment.setItemList(getListItem());
        dayFilterFragment.setOnSelectListener(position -> {
            if (position == dayFilterFragment.getItemCount() - 1) {
                getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out)
                        .replace(R.id.frag_filter_times, filterCustomFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                if (type == 0) {
                    switch (position) {
                        case 0:
                            model.today.set("今日");
                            filter.registerTimeStart = DateUtils.getStringToday();
                            filter.registerTimeEnd = DateUtils.getStringToday();
                            break;
                        case 1:
                            filter.registerTimeEnd = DateUtils.getStringToday();
                            filter.registerTimeStart = DateUtils.minusDay(new Date(), 6);
                            model.today.set("最近7天");
                            break;
                        case 2:
                            model.today.set("最近30天");
                            filter.registerTimeEnd = DateUtils.getStringToday();
                            filter.registerTimeStart = DateUtils.minusDay(new Date(), 29);
                            break;
                    }
                    selectAction.call(0);
                } else {
                    switch (position) {
                        case 0:
                            topFilter.registerTimeEnd = DateUtils.getStringToday();
                            topFilter.registerTimeStart = DateUtils.minusDay(new Date(), 6);
                            model.topLatestDay.set("最近7天");
                            break;
                        case 1:
                            model.topLatestDay.set("最近30天");
                            topFilter.registerTimeEnd = DateUtils.getStringToday();
                            topFilter.registerTimeStart = DateUtils.minusDay(new Date(), 29);
                            break;
                    }
                    selectAction.call(1);
                }
            }
        });
        filterCustomFragment = FilterCustomFragment.newInstance("时间段");
        filterCustomFragment.setSelectTime(true);
        filterCustomFragment.setOnBackFilterDataListener(new FilterCustomFragment.OnBackFilterDataListener() {
            @Override
            public void onSettingData(String start, String end) {
                if (type == 0) {
                    filter.registerTimeEnd = end;
                    filter.registerTimeStart = start;
                    selectAction.call(0);
                }else{
                    topFilter.registerTimeEnd = end;
                    topFilter.registerTimeStart = start;
                    selectAction.call(1);
                }
            }

            @Override
            public void onBack() {
                getChildFragmentManager().popBackStackImmediate();
            }
        });

        filterCustomFragment.limitDay = limitDay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_times_choose, container, false);
        return view;
    }

    private List<FilterCommonLinearItem> getListItem() {
        List<FilterCommonLinearItem> listLatestDay = new ArrayList<>();
        listLatestDay.add(new FilterCommonLinearItem("最近7天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -6)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")"));
        listLatestDay.add(new FilterCommonLinearItem("最近30天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -29)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")"));
        listLatestDay.add(new FilterCommonLinearItem("自定义"));
        if (type == 0) {
            listLatestDay.add(0, new FilterCommonLinearItem("今日"));
        }
        return listLatestDay;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction tr = getChildFragmentManager().beginTransaction();
        tr.replace(R.id.frag_filter_times, dayFilterFragment);
        tr.commitAllowingStateLoss();
    }

    @Override
    public String getFragmentName() {
        return FilterTimesFragment.class.getName();
    }

    private Action1<Integer> selectAction;

    public void setSelectAction(Action1<Integer> action) {
        this.selectAction = action;
    }


}
