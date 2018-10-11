package cn.qingchengfit.saasbase.cards.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.PayMethod;
import cn.qingchengfit.saasbase.cards.event.PayEvent;
import cn.qingchengfit.saasbase.cards.item.ItemPayMethod;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/25 2016.
 */
//支付方式
public class BottomPayDialog extends BaseDialogFragment
    implements FlexibleAdapter.OnItemClickListener {

  RecyclerView rvPayList;
  private CommonFlexAdapter adapter;
  private boolean hasEditPermission;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private int pos;
  private boolean isPro = true;

  public static BottomPayDialog newInstance(boolean hasEditPermission, int pos) {
    Bundle args = new Bundle();
    args.putBoolean("permission", hasEditPermission);
    args.putInt("pos", pos);
    BottomPayDialog fragment = new BottomPayDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static BottomPayDialog newInstance(boolean hasEditPermission, int pos, boolean isPro) {
    Bundle args = new Bundle();
    args.putBoolean("permission", hasEditPermission);
    args.putInt("pos", pos);
    args.putBoolean("isPro", isPro);
    BottomPayDialog fragment = new BottomPayDialog();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
    if (getArguments() != null) {
      hasEditPermission = getArguments().getBoolean("permission");
      pos = getArguments().getInt("pos");
      isPro = getArguments().getBoolean("isPro", true);
    }
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    //        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    Window window = dialog.getWindow();
    window.getDecorView().setPadding(0, 0, 0, 0);

    WindowManager.LayoutParams wlp = window.getAttributes();
    wlp.gravity = Gravity.BOTTOM;
    wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
    wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(wlp);
    window.setWindowAnimations(R.style.ButtomDialogStyle);
    dialog.setCanceledOnTouchOutside(true);

    return dialog;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_pay_bottom_saasbase, container, false);
    rvPayList = (RecyclerView) view.findViewById(R.id.rv_pay_list);

    if (adapter == null) {
      adapter = new CommonFlexAdapter(itemList, this);
    }
    rvPayList.setLayoutManager(new LinearLayoutManager(getContext()));
    rvPayList.addItemDecoration(new FlexibleItemDecoration(getContext()).withBottomEdge(true)
        .withOffset(1)
        .withDivider(R.drawable.divider_qc_base_line, R.layout.item_simple_text,
            R.layout.item_simple_text));
    rvPayList.setAdapter(adapter);
    initView();
    return view;
  }

  private void initView() {
    itemList.add(new SimpleTextItemItem("在线支付", Gravity.CENTER_VERTICAL));
    itemList.add(new ItemPayMethod(new PayMethod(7, "微信收款", R.drawable.vd_payment_wechat)));
    itemList.add(new ItemPayMethod(new PayMethod(12, "支付宝收款", R.drawable.vd_payment_alipay)));
    if (hasEditPermission) {
      itemList.add(new SimpleTextItemItem("线下支付", Gravity.CENTER_VERTICAL));
      itemList.add(new ItemPayMethod(
          new PayMethod(1, getResources().getString(R.string.cash_pay), R.drawable.ic_cash_pay,
              isPro)));
      itemList.add(new ItemPayMethod(
          new PayMethod(2, getResources().getString(R.string.credit_pay), R.drawable.ic_credit_pay,
              isPro)));
      itemList.add(new ItemPayMethod(
          new PayMethod(3, getResources().getString(R.string.transit_pay),
              R.drawable.ic_transit_pay, isPro)));
      itemList.add(new ItemPayMethod(
          new PayMethod(4, getResources().getString(R.string.other), R.drawable.ic_other_pay,
              isPro)));
    }
    adapter.updateDataSet(itemList);
    adapter.toggleSelection(pos);
    adapter.notifyItemChanged(pos);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.isSelected(position)) {
      dismiss();
      return false;
    }
    adapter.clearSelection();
    adapter.addSelection(position);
    if (adapter.getItem(position) instanceof ItemPayMethod) {
      RxBus.getBus()
          .post(new PayEvent(((ItemPayMethod) adapter.getItem(position)).getPayMethod(), position));
    }
    adapter.notifyDataSetChanged();
    dismiss();
    return false;
  }
}
