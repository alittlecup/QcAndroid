package cn.qingchengfit.saasbase.bill.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasConstant;
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

  @Override public ItemBillVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ItemBillVH(view, adapter);
  }

  public BusinessBill getBill() {
    return bill;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemBillVH holder, int position,
      List payloads) {

    holder.imgBill.setImageResource(bill.getTradeDrawable(bill.pay_type));
    holder.tvTime.setText(DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(bill.pay_time)));
    holder.tvOperator.setText(holder.itemView.getContext()
        .getResources()
        .getString(R.string.bill_operator, bill.created_by.username));

    //交易类型
    if (bill.bank_no.length() > 0 && bill.pay_type.equals(SaasConstant.PAY_TYPE_UNIONPAY)) {
      holder.tvItemBillName.setText(holder.itemView.getContext()
          .getResources()
          .getString(R.string.bill_detail_item, holder.itemView.getContext()
              .getString(bill.getPayType(bill.type, bill.pay_type)),
                  bill.bank_no.substring(bill.bank_no.length() - 4, bill.bank_no.length())));
    }else{
      holder.tvItemBillName.setText(
          holder.itemView.getContext().getString(bill.getPayType(bill.type, bill.pay_type)));
    }

    //交易行为
    if (bill.type == 6 && bill.pay_type.equals(SaasConstant.PAY_TYPE_UNIONPAY)){
      holder.tvItemBillAction.setText(holder.itemView.getContext()
          .getResources()
          .getString(R.string.back_cash_way_card, bill.bank_name,
              bill.bank_no.substring(bill.bank_no.length() - 4, bill.bank_no.length())));
    }else {
      holder.tvItemBillAction.setText(holder.itemView.getContext()
          .getResources()
          .getString(R.string.bill_detail_item_trade_type,
              bill.getTradeType(holder.itemView.getContext(), bill.type, bill.pay_type),
              bill.getOrigin(holder.itemView.getContext(), bill.origin)));
    }
    if (bill.status == 4){
      holder.tvItemBillAccount.setTextColor(holder.itemView.getResources().getColor(R.color.text_grey));
    }else{
      holder.tvItemBillAccount.setTextColor(holder.itemView.getResources().getColor(R.color.text_dark));
    }
    holder.tvItemBillAccount.setText(bill.getPrice(bill.price, bill.type));
    holder.tvItemBillStatus.setText(bill.getStatus(holder.itemView.getContext(), bill.status));
  }


  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bill;
  }

  class ItemBillVH extends FlexibleViewHolder{

	TextView tvTime;
	TextView tvOperator;
	ImageView imgBill;
	TextView tvItemBillName;
	TextView tvItemBillAction;
	TextView tvItemBillAccount;
	TextView tvItemBillStatus;

    public ItemBillVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTime = (TextView) view.findViewById(R.id.tv_time);
      tvOperator = (TextView) view.findViewById(R.id.tv_operator);
      imgBill = (ImageView) view.findViewById(R.id.img_bill);
      tvItemBillName = (TextView) view.findViewById(R.id.tv_item_bill_name);
      tvItemBillAction = (TextView) view.findViewById(R.id.tv_item_bill_action);
      tvItemBillAccount = (TextView) view.findViewById(R.id.tv_item_bill_account);
      tvItemBillStatus = (TextView) view.findViewById(R.id.tv_item_bill_status);
    }
  }

}
