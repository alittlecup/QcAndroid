package cn.qingchengfit.saasbase.bill.items;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/29.
 */

public class ItemBillAccount extends AbstractFlexibleItem<ItemBillAccount.ItemBillAccountVH> implements
  IHeader<ItemBillAccount.ItemBillAccountVH>{

  String time;
  float expenses;
  float income;

  public ItemBillAccount(String time, float expenses, float income) {
    this.time = time;
    this.expenses = expenses;
    this.income = income;
  }

  public String getTime() {
    return time;
  }

  @Override public boolean equals(Object o) {
    if (o instanceof ItemBillAccount){
      return time != null && ((ItemBillAccount) o).getTime().equalsIgnoreCase(this.time);
    }
    return false;
  }

  @Override
  public ItemBillAccountVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ItemBillAccountVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ItemBillAccountVH holder, int position,
      List payloads) {
    holder.tvBillTime.setText(DateUtils.getYYMMfromServer(time));
    holder.tvBillIncome.setText(
        holder.itemView.getResources().getString(R.string.bill_count_indeed,income));

    holder.tvBillReduce.setText(
        holder.itemView.getResources().getString(R.string.bill_count_reduce,expenses));
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bill_count;
  }

  class ItemBillAccountVH extends FlexibleViewHolder {

    @BindView(R2.id.tv_bill_time) TextView tvBillTime;
    @BindView(R2.id.tv_bill_income) TextView tvBillIncome;
    @BindView(R2.id.tv_bill_reduce) TextView tvBillReduce;

    public ItemBillAccountVH(View view, FlexibleAdapter adapter) {
      super(view, adapter,true);
      ButterKnife.bind(this, view);
    }
  }
}
