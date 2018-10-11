package cn.qingchengfit.items;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.views.fragments.BottomPayDialog;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/20.
 */

public class BottomPayItem extends AbstractFlexibleItem<BottomPayItem.BottomPayVH> {
  @BottomPayDialog.PayType int type;
  String enableMsg;
  private String memCardName;
  private String memCardBalance;
  private String itemMsg;

  public BottomPayItem(@BottomPayDialog.PayType int type) {
    this(type, true, "");
  }

  public BottomPayItem(@BottomPayDialog.PayType int type, boolean enable, String enableMsg) {
    this.type = type;
    setEnabled(enable);
    this.enableMsg = enableMsg;
  }

  public static BottomPayItem memberCardItem(String cardName, String cardBalance) {

    return memberCardItem(cardName, cardBalance, true, "");
  }

  public static BottomPayItem memberCardItem(String cardName, String cardBalance, boolean enable,
      String enableMsg) {
    BottomPayItem item =
        new BottomPayItem(BottomPayDialog.PayType.MEMEBER_CARD_PAY, enable, enableMsg);
    item.setMemCardName(cardName);
    item.setMemCardBalance(cardBalance);
    return item;
  }

  private void setMemCardName(String cardName) {
    memCardName = cardName;
  }

  private void setMemCardBalance(String cardBalance) {
    memCardBalance = cardBalance;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bottom_pay;
  }

  public void setMsg(String msg) {
    this.itemMsg = msg;
  }

  @Override public BottomPayVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BottomPayVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, BottomPayVH holder, int position,
      List payloads) {
    holder.subBalance.setVisibility(View.GONE);
    switch (type) {
      case BottomPayDialog.PayType.CASH_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_cash);
        holder.content.setText("现金支付");
        break;
      case BottomPayDialog.PayType.CREDIT_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_bankcard);
        holder.content.setText("刷卡支付");
        break;
      case BottomPayDialog.PayType.TRANSIT_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_transfer);
        holder.content.setText("转账支付");
        break;
      case BottomPayDialog.PayType.OTHER_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_others);
        holder.content.setText("其他");
        break;
      case BottomPayDialog.PayType.WECHAT_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_wechat);
        holder.content.setText("微信支付");
        break;
      case BottomPayDialog.PayType.ALI_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_alipay);
        holder.content.setText("支付宝");
        break;
      case BottomPayDialog.PayType.POS_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_pos);
        holder.content.setText("POS机支付");
        break;
      case BottomPayDialog.PayType.WECHAT_SCAN_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_qrcode);
        holder.content.setText("微信扫码支付");
        break;
      case BottomPayDialog.PayType.MEMEBER_CARD_PAY:
        holder.icon.setImageResource(R.drawable.vd_payment_membership);
        holder.content.setText(memCardName);
        if (!TextUtils.isEmpty(memCardBalance)) {
          holder.subBalance.setText("余额：" + memCardBalance + "元");
          holder.subBalance.setVisibility(View.VISIBLE);
        }
        break;
    }
    holder.content.setEnabled(isEnabled());

    holder.imgSelected.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);

    if (!isEnabled() && !TextUtils.isEmpty(enableMsg)) {
      holder.tvUnEnabled.setVisibility(View.VISIBLE);
      holder.tvUnEnabled.setText(enableMsg);
      holder.tvUnEnabled.setTextColor(
          holder.tvUnEnabled.getContext().getResources().getColor(R.color.text_grey));
    } else {
      holder.tvUnEnabled.setText("");
      holder.tvUnEnabled.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(itemMsg)) {
      holder.tvAction.setText(itemMsg);
      holder.tvAction.setVisibility(View.VISIBLE);
    } else {
      holder.tvAction.setText("");
      holder.tvAction.setVisibility(View.GONE);
    }
  }

  class BottomPayVH extends FlexibleViewHolder {
    ImageView icon, imgSelected;
    TextView content, subBalance, tvUnEnabled, tvAction;

    public BottomPayVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      icon = view.findViewById(R.id.img);
      imgSelected = view.findViewById(R.id.img_selected);
      content = view.findViewById(R.id.tv);
      subBalance = view.findViewById(R.id.subBalance);
      tvUnEnabled = view.findViewById(R.id.enableMsg);
      tvAction = view.findViewById(R.id.tv_action);
      tvAction.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (view.getId() == R.id.tv_action && !mAdapter.isEnabled(getFlexibleAdapterPosition())) {
        if (mAdapter.mItemClickListener != null
            && mActionState == ItemTouchHelper.ACTION_STATE_IDLE) {
          if (mAdapter.mItemClickListener.onItemClick(getFlexibleAdapterPosition())) {
            toggleActivation();
          }
        }
      } else {
        super.onClick(view);
      }
    }
  }
}
