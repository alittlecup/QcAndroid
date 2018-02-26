package cn.qingchengfit.student.view.attendance.absent;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceAbsentBinding;
import cn.qingchengfit.student.viewmodel.attendance.absent.AttendanceAbsentViewModel;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/11/16.
 */
@Leaf(module = "student", path = "/attendance/absent")
public class AttendanceAbsentPage extends StudentBaseFragment<PageAttendanceAbsentBinding, AttendanceAbsentViewModel> {

    private AttendanceAbsentView filterFragment;

    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, listitem -> {
            mViewModel.items.set(listitem);
        });
        mViewModel.getFilterIndex().observe(this, index -> {
            filterFragment.showPage(index);
        });
    }

    @Override
    public PageAttendanceAbsentBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAttendanceAbsentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("缺勤统计"));
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.setViewModel(mViewModel);
        initFragment();
        mViewModel.loadSource(new Pair<>("7", "30"));
        mBinding.recyclerview.setAdapter(new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding;
    }

    private void initFragment() {
        filterFragment = new AttendanceAbsentView();
        stuff(R.id.frag_filter, filterFragment);
    }
}
