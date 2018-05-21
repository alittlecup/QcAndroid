package cn.qingchengfit.notisetting.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class NotiSettingChargeItem
    extends AbstractFlexibleItem<NotiSettingChargeItem.NotiSettingChargeVH> {
  int money;
  int count;

  public NotiSettingChargeItem(int money, int count) {
    this.money = money;
    this.count = count;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_noti_setting_charge;
  }

  @Override
  public NotiSettingChargeVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new NotiSettingChargeVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, NotiSettingChargeVH holder, int position,
      List payloads) {
    holder.tvMoney.setText(money + "元");
    holder.tvCount.setText(count + "条");
    holder.itemView.setBackgroundResource(
        adapter.isSelected(position) ? R.drawable.bg_rect_corner_primary
            : R.drawable.bg_rect_black);
    holder.imgChosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class NotiSettingChargeVH extends FlexibleViewHolder {
	TextView tvMoney;
	TextView tvCount;
	ImageView imgChosen;

    public NotiSettingChargeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvMoney = (TextView) view.findViewById(R.id.tv_money);
      tvCount = (TextView) view.findViewById(R.id.tv_count);
      imgChosen = (ImageView) view.findViewById(R.id.img_chosen);
    }
  }
}