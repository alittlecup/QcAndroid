package cn.qingchengfit.saasbase.staff.items;

import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.staff.beans.SalerData;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SalerDataItem extends AbstractFlexibleItem<SalerDataItem.SalerDataVH> {

  SalerData salerData;

  public SalerDataItem(SalerData salerData) {
    this.salerData = salerData;
  }

  public SalerData getSalerData() {
    return salerData;
  }

  @Override public int getLayoutRes() {
    return R.layout.layout_saler_data;
  }

  @Override public SalerDataVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new SalerDataVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, SalerDataVH holder, int position,
    List payloads) {
    holder.tvTimeTag.setText(holder.itemView.getContext().getResources().getStringArray(R.array.analyz_during_list)[position%3]);
    holder.tvTitle.setText(holder.itemView.getContext().getResources().getStringArray(R.array.saler_datas_duiring)[position%3]);
    holder.tvTime.setText(position == 0 ? DateUtils.getYYYYMMDDfromServer(salerData.start) : DateUtils.getDuringFromServer(salerData.start,salerData.end));
    holder.tvMoney.setText(CmStringUtils.getMoneyStr((float)salerData.amount/100) + "元");
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class SalerDataVH extends FlexibleViewHolder {

	TextView tvTimeTag;
	TextView tvTitle;
	TextView tvTime;
	TextView tvMoney;

    public SalerDataVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTimeTag = (TextView) view.findViewById(R.id.tv_time_tag);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvTime = (TextView) view.findViewById(R.id.tv_time);
      tvMoney = (TextView) view.findViewById(R.id.tv_money);
    }
  }
}