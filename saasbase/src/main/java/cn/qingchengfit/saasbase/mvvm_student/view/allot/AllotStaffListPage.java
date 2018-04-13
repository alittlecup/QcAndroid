package cn.qingchengfit.saasbase.mvvm_student.view.allot;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.databinding.PageAllotlistBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentActivityViewModel;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotListViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/11/21.
 */
@Leaf(module = "student", path = "/allot/student")
public class AllotStaffListPage extends StudentBaseFragment<PageAllotlistBinding, AllotListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
    @Need
    Integer type ;

    @Override
    protected void subscribeUI() {
        mViewModel.type = type;
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.isLoading.set(false);
            mViewModel.items.set(items);
        });
        mViewModel.loadSource(1);
    }

    @Override
    public PageAllotlistBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageAllotlistBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel(type == 0 ? "销售列表" : "教练列表"));
        mBinding.setViewModel(mViewModel);
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.recyclerview.setAdapter(new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.setItemClickListener(this);
        return mBinding;
    }

    @Override
    public boolean onItemClick(int position) {
        Uri uri = Uri.parse("qcstaff://student/allotstaff/detail");
        StudentActivityViewModel viewModel = getActivityViewModel();
        viewModel.getAllotStaff().setValue(mViewModel.getLiveItems().getValue().get(position).getData().getSeller());
        viewModel.setAllotType(type);
        routeTo(uri, null);
        return false;
    }
}
