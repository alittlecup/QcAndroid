package cn.qingchengfit.student.view.attendance.absent;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import javax.inject.Inject;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceStudentListFilterFragment;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceAbsentBinding;
import cn.qingchengfit.student.viewmodel.attendance.absent.AttendanceAbsentViewModel;

/**
 * Created by huangbaole on 2017/11/16.
 */
@Leaf(module = "student",path = "/attendance/absent")
public class AttendanceAbsentPage extends StudentBaseFragment<PageAttendanceAbsentBinding, AttendanceAbsentViewModel> {

    private AttendanceAbsentView filterFragment;

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(AttendanceAbsentViewModel.class);
        mViewModel.getLiveItems().observe(this, listitem -> {
            mViewModel.items.set(listitem);
        });
        mViewModel.getFilterIndex().observe(this,index->{
            filterFragment.showPage(index);
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAttendanceAbsentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("缺勤统计"));
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.setViewModel(mViewModel);
        initFragment();
        mViewModel.loadSource(new Pair<>("7","30"));
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }

    private void initFragment() {
        filterFragment = new AttendanceAbsentView();
        stuff(R.id.frag_filter, filterFragment);
    }
}
