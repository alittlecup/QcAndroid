package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.ViewAllotMultiStaffBinding;
import cn.qingchengfit.student.item.AllotMultiStaffItem;
import cn.qingchengfit.student.view.home.StudentListViewModel;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示已选学员列表的fragment
 * Created by hbl on 17/11/18.
 */
public class AllotSaleShowSelectDialogView extends BottomSheetDialogFragment
    implements FlexibleAdapter.OnItemClickListener {

  private ViewAllotMultiStaffBinding binding;
  private CommonFlexAdapter adapter;
  StudentListViewModel viewModel;
  List<QcStudentBean> studentBeans;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getTargetFragment()).get(StudentListViewModel.class);
    studentBeans = viewModel.getSelectedDatas().getValue();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.view_allot_multi_staff, container, false);
    binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, studentBeans.size()));

    Drawable drawableClear = getResources().getDrawable(R.drawable.ic_allotsale_clear);
    drawableClear.setBounds(0, 0, drawableClear.getMinimumWidth(),
        drawableClear.getMinimumHeight());
    binding.tvClearAll.setCompoundDrawables(drawableClear, null, null, null);

    adapter = new CommonFlexAdapter(getItems(studentBeans));
    binding.recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.recycleview.addItemDecoration(
        new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    binding.recycleview.setAdapter(adapter);
    binding.tvClearAll.setOnClickListener((view) -> onClearClick());
    adapter.addListener(this);
    return binding.getRoot();
  }

  private List<AllotMultiStaffItem> getItems(List<QcStudentBean> datas) {
    List<AllotMultiStaffItem> items = new ArrayList<>(datas.size());
    for (QcStudentBean studentBean : datas) {
      items.add(new AllotMultiStaffItem(studentBean, 1));
    }
    return items;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public void onClearClick() {

    DialogUtils.showConfirm(getContext(), "", getString(R.string.qc_allotsale_clear_alert),
        (dialog, action) -> {
          dialog.dismiss();
          if (action == DialogAction.POSITIVE) {
            viewModel.getSelectedDatas().setValue(new ArrayList<>());
            AllotSaleShowSelectDialogView.this.dismiss();
          }
        });
  }

  @Override public boolean onItemClick(int position) {
    viewModel.getSelectedDatas()
        .getValue()
        .remove(((AllotMultiStaffItem) adapter.getItem(position)).getData());
    adapter.updateDataSet(getItems(viewModel.getSelectedDatas().getValue()));
    viewModel.getRemoveSelectPos().setValue(position);
    binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, adapter.getItemCount()));
    if (adapter.getItemCount() == 0) AllotSaleShowSelectDialogView.this.dismiss();
    return false;
  }
}
