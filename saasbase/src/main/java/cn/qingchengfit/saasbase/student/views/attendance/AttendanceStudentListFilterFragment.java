package cn.qingchengfit.saasbase.student.views.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import rx.functions.Action0;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceStudentListFilterFragment extends BaseFilterFragment {
    public AttendanceStudentFilterFragment filterFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaasbaseParamsInjector.inject(this);
        initFragment();
    }

    @Override
    public void dismiss() {
        if (dismissAction != null) dismissAction.call();
    }

    private void initFragment() {
        filterFragment = new AttendanceStudentFilterFragment();
        filterFragment.setListItemString(new String[]{
                getString(R.string.absence_three_seven)
                , getString(R.string.absence_thirty)
                , getString(R.string.absence_sixty)
                , "自定义"
        });
        filterFragment.setSelectDayAction(daySelect);
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

    Action3<String, String, String> daySelect;

    public void setDaySelectAction(Action3<String, String, String> action) {
        this.daySelect = action;
    }

    private Action0 dismissAction;

    public void setDismissAction(Action0 action) {
        this.dismissAction = action;
    }
}

