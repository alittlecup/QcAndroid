package cn.qingchengfit.saasbase.mvvm_student.view.transfer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageTransferStudentBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.transfer.TransferStudentViewModel;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2017/11/17.
 */
@Leaf(module = "student",path = "/transfer/student")
public class TransferStudentPage extends
    StudentBaseFragment<PageTransferStudentBinding, TransferStudentViewModel> {

    private TransferStudentView filterView;

    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, followUpItems -> {
            mViewModel.items.set(followUpItems);
        });
        mViewModel.getFilterIndex().observe(this,index -> {
            filterView.showPage(index);
        });

    }

    @Override
    public PageTransferStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageTransferStudentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("会员转化"));
        initToolbar(mBinding.includeToolbar.toolbar);
        initFragment();
        mBinding.setViewModel(mViewModel);
        mViewModel.loadSource(mViewModel.getDataHolder());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding;
    }

    private void initFragment() {
        filterView = new TransferStudentView();
        stuff(R.id.frag_filter, filterView);
    }
}
