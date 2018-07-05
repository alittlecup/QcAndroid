package cn.qingchengfit.saasbase.student.views.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterCustomFragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class AttendanceStudentFilterFragment extends SaasBaseFragment {
    FilterListStringFragment dayFilterFragment;
    FilterCustomFragment filterCustomFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListFragment();
        initCustomFragment();
    }

    protected void initCustomFragment() {
        filterCustomFragment = FilterCustomFragment.newInstance("缺勤天数（天）");
        filterCustomFragment.setSelectTime(false);
        filterCustomFragment.setOnBackFilterDataListener(new FilterCustomFragment.OnBackFilterDataListener() {
            @Override
            public void onSettingData(String start, String end) {
                daySelect.call(start, end, "缺勤"+start + "-" + end+"天");
            }

            @Override
            public void onBack() {
                getChildFragmentManager().popBackStackImmediate();
            }
        });
    }

    protected void initListFragment() {
        dayFilterFragment = new FilterListStringFragment();
        dayFilterFragment.setItemList(getListItems(items));
        dayFilterFragment.setOnSelectListener(position -> {
            if (position == dayFilterFragment.getItemCount() - 1) {
                getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out)
                        .replace(R.id.frag_filter_times, filterCustomFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                switch (position) {
                    case 0:
                        daySelect.call("7", "30", items[position]);
                        break;
                    case 1:
                        daySelect.call("30", "60", items[position]);
                        break;
                    case 2:
                        daySelect.call("60", "-1", items[position]);
                        break;

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_times_choose, container, false);
        return view;
    }


    public List<FilterCommonLinearItem> getListItems(String[] strings) {
        List<FilterCommonLinearItem> listLatestDay = new ArrayList<>();
        for (String msg : strings) {
            listLatestDay.add(new FilterCommonLinearItem(msg));
        }
        return listLatestDay;
    }

    private String[] items;

    public void setListItemString(String[] args) {
        this.items = args;
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


    private Action3<String, String, String> daySelect;

    public void setSelectDayAction(Action3<String, String, String> daySelected) {
        this.daySelect = daySelected;
    }

}
