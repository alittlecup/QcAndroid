package cn.qingchengfit.student.view.attendance.absent;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;

import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceStudentFilterFragment;
import cn.qingchengfit.student.viewmodel.attendance.AttendanceStudentViewModel;
import cn.qingchengfit.student.viewmodel.attendance.absent.AttendanceAbsentViewModel;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import rx.functions.Action0;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceAbsentView extends BaseFilterFragment {
    public AttendanceStudentFilterFragment filterFragment;

    AttendanceAbsentViewModel mViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel= ViewModelProviders.of(getParentFragment()).get(AttendanceAbsentViewModel.class);
        initFragment();
    }

    @Override
    public void dismiss() {
        mViewModel.filterChecked.set(false);
    }

    private void initFragment() {
        filterFragment = new AttendanceStudentFilterFragment();
        filterFragment.setListItemString(new String[]{
                getString(R.string.absence_three_seven)
                , getString(R.string.absence_thirty)
                , getString(R.string.absence_sixty)
                , "自定义"
        });
        filterFragment.setSelectDayAction((start,end,title)->{
            mViewModel.filterChecked.set(false);
            mViewModel.filterText.set(title);
            mViewModel.loadSource(new Pair<>(start,end));
        });
    }

    @Override
    protected String[] getTags() {
        return new String[]{"days"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) return filterFragment;
        return new EmptyFragment();
    }


}

