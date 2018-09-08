package cn.qingchengfit.student.view.transfer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageTransferStudentBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/11/17.
 */
@Leaf(module = "student",path = "/transfer/student")
public class TransferStudentPage extends
    StudentBaseFragment<PageTransferStudentBinding, TransferStudentViewModel> {

    private TransferStudentView filterView;
    CommonFlexAdapter adapter;
    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, followUpItems -> {
            mViewModel.items.set(followUpItems);
        });
        mViewModel.getFilterIndex().observe(this,index -> {
            filterView.showPage(index);

        });
        mViewModel.filterVisible.observe(this,aBoolean -> {
            if(aBoolean){
                mBinding.fragFilter.setVisibility(View.VISIBLE);
                mBinding.fragFilter.setAlpha(0f);
                mBinding.fragFilter.animate().alpha(1).setDuration(20).start();
            }else{
                mBinding.fragFilter.animate().alpha(0).setDuration(20).withEndAction(new Runnable() {
                    @Override public void run() {
                        mBinding.fragFilter.setVisibility(View.GONE);
                    }
                }).start();
            }
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
        mBinding.recyclerView.setAdapter(adapter=new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding;
    }

    private void initFragment() {
        filterView = new TransferStudentView();
        stuff(R.id.frag_filter, filterView);
    }
}
