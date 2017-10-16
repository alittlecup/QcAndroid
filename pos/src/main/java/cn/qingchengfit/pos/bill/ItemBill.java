package cn.qingchengfit.pos.bill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.bill.model.Bill;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/13.
 */

public class ItemBill extends AbstractFlexibleItem<ItemBill.ItemBillVH> {

  public Bill bill;

  public ItemBill(Bill bill) {
    this.bill = bill;
  }

  @Override public ItemBillVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ItemBillVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemBillVH holder, int position,
      List payloads) {
    holder.tvTime.setText(DateUtils.formatDateToServer(bill.created_at));
    holder.tvOperator.setText(bill.created_by.name);

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bill;
  }

  class ItemBillVH extends FlexibleViewHolder{

    @BindView(R.id.tv_time) TextView tvTime;
    @BindView(R.id.tv_operator) TextView tvOperator;
    @BindView(R.id.img_bill) ImageView imgBill;
    @BindView(R.id.tv_item_bill_name) TextView tvItemBillName;
    @BindView(R.id.tv_item_bill_action) TextView tvItemBillAction;
    @BindView(R.id.tv_item_bill_account) TextView tvItemBillAccount;
    @BindView(R.id.tv_item_bill_status) TextView tvItemBillStatus;

    public ItemBillVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

}
