package cn.qingchengfit.saasbase.student.views.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import rx.functions.Action0;
import rx.functions.Action2;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceNosignFilterView extends BaseFilterFragment {
    AttendanceNosingTimeFilterFragment timeFilterFragment;
    FilterListStringFragment listStringFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    @Override
    public void dismiss() {
        if(dismissAction!=null)dismissAction.call();
    }

    private void initFragment() {
        timeFilterFragment = new AttendanceNosingTimeFilterFragment();
        timeFilterFragment.setListItemString(new String[]{
                "未签课7天内", "未签课30天内", "自定义"
        });
        timeFilterFragment.setSelectDayAction(daySelect);

        listStringFragment=new FilterListStringFragment();
        String[] strings=new String[31];
        for (int i=0;i<=30;i++){
            strings[i]=i+"节以上";
        }
        listStringFragment.setStrings(strings);
        listStringFragment.setOnSelectListener(position -> {
            countSelectAction.call(strings[position],position);
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

    Action3<String, String, String> daySelect;

    public void setDaySelectAction(Action3<String, String, String> action) {
        this.daySelect = action;
    }

    public void setCountSelectAction(Action2<String,Integer> countSelectAction) {
        this.countSelectAction = countSelectAction;
    }

    Action2<String,Integer> countSelectAction;

    private Action0 dismissAction;

    public void setDismissAction(Action0 action) {
        this.dismissAction = action;
    }
}
