package cn.qingchengfit.student.view.transfer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageTransferStudentBinding;
import cn.qingchengfit.student.viewmodel.transfer.TransferStudentViewModel;


/**
 * Created by huangbaole on 2017/11/17.
 */
@Leaf(module = "student",path = "/transfer/student")
public class TransferStudentPage extends StudentBaseFragment<PageTransferStudentBinding, TransferStudentViewModel> {

    private TransferStudentView filterView;

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(TransferStudentViewModel.class);
        mViewModel.getLiveItems().observe(this, followUpItems -> {
            mViewModel.items.set(followUpItems);
        });
        mViewModel.getFilterIndex().observe(this,index -> {
            filterView.showPage(index);
        });

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageTransferStudentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("会员转化"));
        initToolbar(mBinding.includeToolbar.toolbar);
        initFragment();
        mBinding.setViewModel(mViewModel);
        mViewModel.loadSource(mViewModel.getDataHolder());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }

    private void initFragment() {
        filterView = new TransferStudentView();
        stuff(R.id.frag_filter, filterView);
    }
}
