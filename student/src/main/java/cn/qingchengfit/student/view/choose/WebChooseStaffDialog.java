package cn.qingchengfit.student.view.choose;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.ViewAllotMultiStaffBinding;
import cn.qingchengfit.student.item.AllotMultiStaffItem;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示已选学员列表的fragment
 * Created by hbl on 17/11/18.
 */
public class WebChooseStaffDialog extends BottomSheetDialogFragment
    implements FlexibleAdapter.OnItemClickListener {

  private ViewAllotMultiStaffBinding binding;
  private CommonFlexAdapter adapter;
  ChooseStaffViewModel viewModel;
  List<QcStudentBean> studentBeans;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getTargetFragment()).get(
        ChooseStaffViewModel.class);
    studentBeans = viewModel.selectStudents.getValue();
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
    adapter.addListener(this);
    binding.tvClearAll.setOnClickListener(v -> onClearClick());
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
    new MaterialDialog.Builder(getContext()).content(getString(R.string.qc_allotsale_clear_alert))
        .positiveText(R.string.common_comfirm)
        .negativeText(R.string.common_cancel)
        .autoDismiss(true)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            viewModel.selectStudents.setValue(new ArrayList<>());
            WebChooseStaffDialog.this.dismiss();
          }
        })
        .show();
  }

  @Override public boolean onItemClick(int position) {
    List<QcStudentBean> value = viewModel.selectStudents.getValue();
    value.remove(((AllotMultiStaffItem) adapter.getItem(position)).getData());
    viewModel.selectStudents.setValue(value);

    adapter.updateDataSet(getItems(viewModel.selectStudents.getValue()));
    binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, adapter.getItemCount()));
    if (adapter.getItemCount() == 0) WebChooseStaffDialog.this.dismiss();
    return false;
  }
}
