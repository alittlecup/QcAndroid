package cn.qingchengfit.student.view.attendance.rank;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceRankBinding;
import cn.qingchengfit.student.viewmodel.attendance.rank.AttendanceRankViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;

/**
 * Created by huangbaole on 2017/11/16.
 */
@Leaf(module = "student", path = "/attendance/rank")
public class AttendanceRankPage extends StudentBaseFragment<PageAttendanceRankBinding, AttendanceRankViewModel> {
    private AttendanceRankView rankFilterView;

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(AttendanceRankViewModel.class);
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.items.set(items);
        });
        mViewModel.getFilterIndex().observe(this, index -> {
            rankFilterView.showPage(index);
        });

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAttendanceRankBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("出勤统计"));
        mBinding.setViewModel(mViewModel);
        initToolbar(mBinding.includeToolbar.toolbar);
        initFragment();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.loadSource(mViewModel.getLoadData());
        return mBinding.getRoot();
    }

    private void initFragment() {
        rankFilterView = new AttendanceRankView();
        stuff(R.id.frag_filter, rankFilterView);
    }
}
