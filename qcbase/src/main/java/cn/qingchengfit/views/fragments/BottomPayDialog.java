package cn.qingchengfit.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.BottomPayExpandItem;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.databinding.ActionBottomChooseDialogBinding;
import com.bigkoo.pickerview.lib.DensityUtil;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/20.
 */

public class BottomPayDialog extends BottomSheetDialog
    implements FlexibleAdapter.OnItemClickListener {

  ActionBottomChooseDialogBinding mBinding;
  CommonFlexAdapter adapter;

  public BottomPayDialog(@NonNull Context context, CharSequence title,List<BottomPayExpandItem> expandItems) {
    super(context);
    initView(context);
    setContentView(mBinding.getRoot());
    mBinding.title.setText(title);

    adapter.updateDataSet(expandItems);
    adapter.addListener(this);
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    mBinding.btnConfirm.setVisibility(View.GONE);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
        DensityUtil.dip2px(getContext(), 600));
    getWindow().setGravity(Gravity.BOTTOM);
  }

  private void initView(Context context) {
    mBinding = ActionBottomChooseDialogBinding.inflate(LayoutInflater.from(context));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(context));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerview.setPadding(0, 0, 0, 0);
    mBinding.imageCancel.setOnClickListener(v -> cancel());
  }

  BottomPayDialog.onItemClickListener listener;

  public void setOnItemClickListener(BottomPayDialog.onItemClickListener listener) {
    this.listener = listener;
  }

  public interface onItemClickListener {
    boolean onItemClick(int position);
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.isEnabled(position) && adapter.isSelectable(position)) {
      adapter.toggleSelection(position);
      adapter.notifyDataSetChanged();
    }
    if (listener != null) {
      listener.onItemClick(position);
    }
    dismiss();
    return false;
  }

  @Retention(RetentionPolicy.RUNTIME) @IntDef({
      PayType.CASH_PAY, PayType.CREDIT_PAY, PayType.TRANSIT_PAY, PayType.OTHER_PAY,
      PayType.WECHAT_PAY, PayType.WECHAT_SCAN_PAY, PayType.POS_PAY, PayType.ALI_PAY,
      PayType.MEMEBER_CARD_PAY
  }) public @interface PayType {
    int CASH_PAY = 1;
    int CREDIT_PAY = 2;
    int TRANSIT_PAY = 3;
    int OTHER_PAY = 4;
    int WECHAT_PAY = 6;
    int WECHAT_SCAN_PAY = 7;
    int POS_PAY = 8;
    int ALI_PAY = 9;
    int MEMEBER_CARD_PAY = 10;
  }
}
