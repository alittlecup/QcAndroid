package cn.qingchengfit.saasbase.student.views.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import cn.qingchengfit.views.fragments.FilterListFragment;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class AttendanceFilterView extends BaseFilterFragment {
    FilterListStringFragment dayListFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaasbaseParamsInjector.inject(this);
        initFragment();
    }

    private void initFragment() {
        dayListFragment = new FilterListStringFragment();
        String latest7 = "最近7天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -6)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")";
        String latest30 = "最近30天(" + DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), -29)) + "至" + DateUtils.Date2YYYYMMDD(new Date()) + ")";
        dayListFragment.setStrings(new String[]{latest7, latest30});
        dayListFragment.setOnSelectListener(position -> {
            if (position == 0) {
                daysCallback.call(6, "最近7天");
            } else {
                daysCallback.call(29, "最近30天");
            }
        });
    }

    @Override
    public void dismiss() {
        if(dismissAction!=null)dismissAction.call();
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

    private Action2<Integer, String> daysCallback;

    public void setDaysCallback(Action2<Integer, String> action2) {
        this.daysCallback = action2;
    }
    private Action0 dismissAction ;
    public void setDismissAction(Action0 action){
        this.dismissAction=action;
    }
}
