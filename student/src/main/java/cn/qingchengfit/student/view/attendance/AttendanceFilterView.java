package cn.qingchengfit.student.view.attendance;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Date;

import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class AttendanceFilterView extends BaseFilterFragment {
    FilterListStringFragment dayListFragment;
    private AttendanceStudentViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(AttendanceStudentViewModel.class);
        initFragment();
    }

    private void initFragment() {
        dayListFragment = new FilterListStringFragment();
        String latest7 = "最近7天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -6)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")";
        String latest30 = "最近30天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -29)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")";
        dayListFragment.setStrings(new String[]{latest7, latest30});
        dayListFragment.setOnSelectListener(position -> {
            mViewModel.getOffSetDay().setValue(position == 0 ? 6 : 29);
        });
    }

    @Override
    public void dismiss() {
        mViewModel.qcFBChecked.set(false);
    }

    @Override
    protected String[] getTags() {
        return new String[]{"day"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return dayListFragment;
        }
        return new EmptyFragment();
    }
}
