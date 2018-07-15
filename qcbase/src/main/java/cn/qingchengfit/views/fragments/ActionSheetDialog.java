package cn.qingchengfit.views.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.items.ActionSheetDialogItem;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.databinding.ActionSheetDialogBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/3/27.
 */

public class ActionSheetDialog extends BottomSheetDialog
    implements FlexibleAdapter.OnItemClickListener {
  ActionSheetDialogBinding mBinding;
  CommonFlexAdapter adapter;

  public ActionSheetDialog(@NonNull Context context, CharSequence title,
      List<? extends CharSequence> contents) {
    super(context);
    initView(context);
    setContentView(mBinding.getRoot());
    mBinding.title.setText(title);
    if (contents != null && !contents.isEmpty()) {
      List<ActionSheetDialogItem> items = new ArrayList<>();
      for (CharSequence charSequence : contents) {
        items.add(new ActionSheetDialogItem(charSequence));
      }
      adapter.updateDataSet(items);
    }
    adapter.addListener(this);
  }

  private void initView(Context context) {
    mBinding = ActionSheetDialogBinding.inflate(LayoutInflater.from(context));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(context));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerview.addItemDecoration(
        new cn.qingchengfit.utils.DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    mBinding.bottomCancel.setOnClickListener(v -> cancel());
  }

  onItemClickListener listener;

  public void setOnItemClickListener(onItemClickListener listener) {
    this.listener = listener;
  }

  public interface onItemClickListener {
    boolean onItemClick(int position);
  }

  @Override public boolean onItemClick(int position) {
    if (listener != null) {
      listener.onItemClick(position);
    }
    dismiss();
    return false;
  }
}
