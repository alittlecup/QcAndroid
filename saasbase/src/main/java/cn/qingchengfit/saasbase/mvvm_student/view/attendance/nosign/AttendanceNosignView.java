package cn.qingchengfit.saasbase.mvvm_student.view.attendance.nosign;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;

import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.nosign.AttendanceNosignViewModel;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceNosingTimeFilterFragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceNosignView extends BaseFilterFragment {
    AttendanceNosingTimeFilterFragment timeFilterFragment;
    FilterListStringFragment listStringFragment;

    AttendanceNosignViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getParentFragment()).get(AttendanceNosignViewModel.class);
        initFragment();

    }

    @Override
    public void dismiss() {
        viewModel.filterVisible.set(false);
    }

    private void initFragment() {
        timeFilterFragment = new AttendanceNosingTimeFilterFragment();
        timeFilterFragment.setListItemString(new String[]{
                "未签课7天内", "未签课30天内", "自定义"
        });
        timeFilterFragment.setSelectDayAction((start, end, title) -> {
            viewModel.topDayText.set(title);
            viewModel.filterVisible.set(false);
            viewModel.getDays().setValue(new Pair<>(start,end));
        });

        listStringFragment = new FilterListStringFragment();
        String[] strings = new String[31];
        for (int i = 0; i <= 30; i++) {
            strings[i] = i + "节以上";
        }
        listStringFragment.setStrings(strings);
        listStringFragment.setOnSelectListener(position -> {
            viewModel.getCount().setValue(position);
            viewModel.topCount.set(position);
            viewModel.filterVisible.set(false);
        });
    }

    @Override
    protected String[] getTags() {
        return new String[]{"days", "count"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return timeFilterFragment;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return listStringFragment;
        }
        return new EmptyFragment();
    }
}
