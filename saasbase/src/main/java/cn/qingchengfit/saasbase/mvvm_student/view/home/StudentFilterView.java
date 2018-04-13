package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ViewStudentFilterBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ;
 * Created by huangbaole on 2017/12/6.
 */

public class StudentFilterView extends
    StudentBaseFragment<ViewStudentFilterBinding, StudentFilterViewModel> {
    CommonFlexAdapter adapter;

    @Override
    protected void subscribeUI() {
        // REFACTOR: 2017/12/11
        mViewModel.getRemoteFilters().observe(this, filterModels -> mViewModel.items.set(mViewModel.getItems(filterModels)));
        mViewModel.getmResetEvent().observe(this, aVoid -> {
            showDialog();
        });
        mViewModel.loadfilterModel();
    }

    private void showDialog() {
        new MaterialDialog.Builder(getContext()).content("确认重置所有筛选项么？")
                .positiveText(R.string.common_comfirm)
                .negativeText(R.string.common_cancel)
                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doReset();
                        mViewModel.filterMap.clear();
                        mViewModel.getmFilterMap().setValue(new HashMap<>());
                    }
                })
                .show();
    }

    private void doReset() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.addSelection(i);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public ViewStudentFilterBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ViewStudentFilterBinding.inflate(inflater, container, false);
        initRecyclerView();
        mBinding.setViewModel(mViewModel);
        return mBinding;
    }

    private void initRecyclerView() {
        mBinding.recyclerBillFilter.setAdapter(adapter=new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerBillFilter.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerBillFilter.addItemDecoration(
                new FlexibleItemDecoration(getContext()).withDivider(cn.qingchengfit.saasbase.R.drawable.divider_grey_left_margin)
                        .withBottomEdge(true));
    }


}
