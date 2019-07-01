package cn.qingchengfit.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import cn.qingchengfit.items.BottomChooseItem;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.widgets.databinding.ActionBottomChooseDialogBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class BottomChooseDialog extends BottomSheetDialog
    implements FlexibleAdapter.OnItemClickListener {
  ActionBottomChooseDialogBinding mBinding;
  CommonFlexAdapter adapter;

  private int type = SelectableAdapter.Mode.SINGLE;

  public BottomChooseDialog(@NonNull Context context, CharSequence title,
      List<BottomChooseData> contents) {
    this(context, title, contents, SelectableAdapter.Mode.SINGLE);
  }

  public BottomChooseDialog(@NonNull Context context, CharSequence title,
      List<BottomChooseData> contents, @SelectableAdapter.Mode int type) {
    super(context);
    initView(context);
    setContentView(mBinding.getRoot());
    mBinding.title.setText(title);
    if (contents != null && !contents.isEmpty()) {
      List<BottomChooseItem> items = new ArrayList<>();
      for (BottomChooseData data : contents) {
        items.add(new BottomChooseItem(data));
      }
      adapter.updateDataSet(items);
    }
    adapter.addListener(this);
    adapter.setMode(type);
    if (type == SelectableAdapter.Mode.SINGLE) {
      mBinding.btnConfirm.setVisibility(View.GONE);
    } else {
      mBinding.btnConfirm.setVisibility(View.VISIBLE);
      mBinding.btnConfirm.setOnClickListener(v -> {
        dismiss();
        if (confirmClickListener != null) {
          confirmClickListener.onItemClick(adapter.getSelectedPositions());
        }
      });
    }
  }

  private void initView(Context context) {
    mBinding = ActionBottomChooseDialogBinding.inflate(LayoutInflater.from(context));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(context));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerview.addItemDecoration(
        new cn.qingchengfit.utils.DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    mBinding.imageCancel.setOnClickListener(v -> cancel());
  }

  BottomChooseDialog.onItemClickListener listener;

  public void setConfirmClickListener(onConfirmClickListener confirmClickListener) {
    this.confirmClickListener = confirmClickListener;
  }

  public void addSeleced(int position) {
    if (adapter != null) {
      adapter.addSelection(position);
    }
  }

  public void setEnabled(int position, boolean isEnable) {
    if (adapter != null) {
      IFlexible item = adapter.getItem(position);
      if (item != null) {
        item.setEnabled(isEnable);
      }
    }
  }

  BottomChooseDialog.onConfirmClickListener confirmClickListener;

  public void setOnItemClickListener(BottomChooseDialog.onItemClickListener listener) {
    this.listener = listener;
  }

  public interface onItemClickListener {
    /**
     * 返回值是因为通过选中的item 进行业务判断，来反向控制是否选中
     *
     * @return true-表示可以修改选择，false-表示不可以选中新的点击
     */
    boolean onItemClick(int position);
  }

  public interface onConfirmClickListener {
    void onItemClick(List<Integer> positions);
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.getMode() == SelectableAdapter.Mode.SINGLE) {
      if (listener != null) {
        boolean b = listener.onItemClick(position);
        if (b) {
          adapter.toggleSelection(position);
          adapter.notifyDataSetChanged();
        }
      }
      dismiss();
    } else {
      adapter.toggleSelection(position);
      adapter.notifyDataSetChanged();
    }
    return false;
  }
}
