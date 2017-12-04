package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpTopSalerView;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStatusViewModel;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2017/11/7.
 */

public class FollowUpFilterView extends BaseFilterFragment {
    FilterListStringFragment studentStatusFragment;
    FilterListStringFragment genderFragment;
    FilterTimesFragment topDayFragment;
    FilterTimesFragment dayFragment;
    FollowUpTopSalerView topSalerView;
    FollowUpTopSalerView salersView;
    FollowUpStatusViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(FollowUpStatusViewModel.class);
        initFragment();
    }

    @Override
    public void dismiss() {
        mViewModel.filterVisible.set(false);
    }

    private void initFragment() {
        studentStatusFragment = new FilterListStringFragment();
        String[] status = new String[]{"全部", "新注册", "已接洽", "会员"};
        studentStatusFragment.setStrings(status);
        studentStatusFragment.setOnSelectListener(position -> {
            mViewModel.studentStatus.set(position == 0 ? "会员状态" : status[position]);
            mViewModel.getStatus().setValue(position == 0 ? null : String.valueOf(position - 1));
            dismiss();
        });

        genderFragment = new FilterListStringFragment();
        String[] genders = new String[]{"全部", "男", "女"};
        genderFragment.setStrings(genders);
        genderFragment.setOnSelectListener(position -> {
            mViewModel.gender.set(position == 0 ? "性别" : genders[position]);
            mViewModel.getGenderM().setValue(position == 0 ? null : String.valueOf(position - 1));
            dismiss();
        });
        dayFragment = FilterTimesFragment.getInstance(0, -1);

        dayFragment.setSelectDayAction((start, end, title) -> {
            mViewModel.getDays().setValue(new Pair<>(start,end));
            mViewModel.today.set(title);
            dismiss();
        });

        salersView = new FollowUpTopSalerView();
        salersView.setOnItemClick(staff -> {
            mViewModel.salerName.set(staff == null ? "销售" : staff.username);
            mViewModel.getStaff().setValue(staff);
            dismiss();
        });


        topDayFragment = FilterTimesFragment.getInstance(1, 30);
        topSalerView = new FollowUpTopSalerView();

        topSalerView.setOnItemClick(staff -> {
            mViewModel.topSalerName.set(staff == null ? "销售" : staff.username);
            mViewModel.getTopStaff().setValue(staff);
            dismiss();
        });
        topDayFragment.setSelectDayAction((start, end, title) -> {
            mViewModel.getTopDays().setValue(new Pair<>(start,end));
            mViewModel.topLatestDay.set(title);
            dismiss();
        });

    }

    @Override
    protected String[] getTags() {
        return new String[]{"salerList", "studentStatus", "today", "latestDay", "gender", "topSalers"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return salersView;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return studentStatusFragment;
        } else if (tag.equalsIgnoreCase(getTags()[2])) {
            return dayFragment;
        } else if (tag.equalsIgnoreCase(getTags()[3])) {
            return topDayFragment;
        } else if (tag.equalsIgnoreCase(getTags()[4])) {
            return genderFragment;
        } else if (tag.equalsIgnoreCase(getTags()[5])) {
            return topSalerView;
        }
        return new EmptyFragment();
    }
}
