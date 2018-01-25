package cn.qingchengfit.student.view.transfer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpTopSalerView;
import cn.qingchengfit.student.viewmodel.transfer.TransferStudentViewModel;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class TransferStudentView extends BaseFilterFragment {
    FollowUpTopSalerView salerView;
    FilterTimesFragment dayFragment;
    TransferStudentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel= ViewModelProviders.of(getParentFragment()).get(TransferStudentViewModel.class);
        initFragment();
    }

    @Override
    public void dismiss() {
        viewModel.filterVisible.set(false);
    }

    private void initFragment() {
        dayFragment = FilterTimesFragment.getInstance(1, 30);
        dayFragment.setSelectDayAction((start,end,title)->{
            viewModel.getMutabkleDays().setValue(new Pair<>(start,end));
            viewModel.days.set(title);
            viewModel.filterVisible.set(false);
        });
        salerView = new FollowUpTopSalerView();
        salerView.setOnItemClick(staff -> {
            viewModel.getStaff().setValue(staff);
            viewModel.staffName.set(staff==null?"销售":staff.username);
            viewModel.filterVisible.set(false);
        });
    }

    @Override
    protected String[] getTags() {
        return new String[]{"saler", "day"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return salerView;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return dayFragment;
        }
        return new EmptyFragment();
    }



}