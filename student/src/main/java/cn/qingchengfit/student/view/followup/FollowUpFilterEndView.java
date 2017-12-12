package cn.qingchengfit.student.view.followup;

import android.databinding.OnRebindCallback;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.common.MyLinearLayoutManager;
import cn.qingchengfit.student.databinding.ViewStudentFilterBinding;
import cn.qingchengfit.student.viewmodel.followup.FollowUpFilterViewModel;
import cn.qingchengfit.student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

/**
 * Created by huangbaole on 2017/12/11.
 */

public class FollowUpFilterEndView extends StudentBaseFragment<ViewStudentFilterBinding, FollowUpFilterViewModel> {
    CommonFlexAdapter adapter;

    @Override
    protected void subscribeUI() {
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
        mBinding.addOnRebindCallback(new OnRebindCallback<ViewStudentFilterBinding>() {
            @Override
            public void onBound(ViewStudentFilterBinding binding) {
                if (binding.recyclerBillFilter.getAdapter() != adapter) {
                    adapter = (CommonFlexAdapter) binding.recyclerBillFilter.getAdapter();
                }
            }
        });
        return mBinding;
    }

    private void initRecyclerView() {
        mBinding.recyclerBillFilter.setLayoutManager(new MyLinearLayoutManager(getContext()));
        mBinding.recyclerBillFilter.addItemDecoration(
                new FlexibleItemDecoration(getContext()).withDivider(cn.qingchengfit.saasbase.R.drawable.divider_grey_left_margin)
                        .withBottomEdge(true));
    }

}
