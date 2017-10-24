package cn.qingchengfit.saasbase.bill.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/13.
 */

public class ItemBill extends AbstractFlexibleItem<ItemBill.ItemBillVH> {

  public BusinessBill bill;

  public ItemBill(BusinessBill bill) {
    this.bill = bill;
  }

  @Override public ItemBillVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ItemBillVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemBillVH holder, int position,
      List payloads) {
    holder.tvTime.setText(DateUtils.formatDateToServer(bill.pay_time));
    holder.tvOperator.setText(holder.itemView.getContext()
        .getResources()
        .getString(R.string.bill_operator, bill.created_by.username));

    holder.tvItemBillName.setText(holder.itemView.getContext()
        .getResources()
        .getString(R.string.bill_detail_item, bill.getPayType(holder.itemView.getContext(), bill.pay_type),
            bill.bank_no));
    holder.tvItemBillAction.setText(holder.itemView.getContext()
        .getResources()
        .getString(R.string.bill_detail_item_trade_type, bill.getTradeType(bill.type)));
    holder.tvItemBillAccount.setText(bill.getPrice(bill.price, bill.type));
    holder.tvItemBillStatus.setText(bill.getStatus(holder.itemView.getContext(), bill.status, bill.type));
  }


  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bill;
  }

  class ItemBillVH extends FlexibleViewHolder{

    @BindView(R2.id.tv_time) TextView tvTime;
    @BindView(R2.id.tv_operator) TextView tvOperator;
    @BindView(R2.id.img_bill) ImageView imgBill;
    @BindView(R2.id.tv_item_bill_name) TextView tvItemBillName;
    @BindView(R2.id.tv_item_bill_action) TextView tvItemBillAction;
    @BindView(R2.id.tv_item_bill_account) TextView tvItemBillAccount;
    @BindView(R2.id.tv_item_bill_status) TextView tvItemBillStatus;

    public ItemBillVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

}
