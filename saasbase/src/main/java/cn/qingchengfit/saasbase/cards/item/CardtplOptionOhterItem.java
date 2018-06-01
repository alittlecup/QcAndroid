package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.utils.CompatUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardtplOptionOhterItem
    extends AbstractFlexibleItem<CardtplOptionOhterItem.CardtplOptionOhterVH> {

  @Override public int getLayoutRes() {
    return R.layout.item_cardtpl_standard_other;
  }

  @Override public CardtplOptionOhterVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardtplOptionOhterVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplOptionOhterVH holder, int position,
      List payloads) {
    holder.chosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    int colorPrimary = CompatUtils.getColor(holder.title.getContext(), R.color.text_color_gray);
    holder.realIncome.setTextColor(colorPrimary);
    holder.validDate.setTextColor(colorPrimary);
    holder.title.setTextColor(colorPrimary);
    if (adapter.isSelected(position)) {
      holder.chosen.setVisibility(View.VISIBLE);
      holder.shadow.setVisibility(View.VISIBLE);
    } else {
      holder.chosen.setVisibility(View.GONE);
      holder.shadow.setVisibility(View.GONE);
    }
  }

  @Override public boolean equals(Object o) {
    return o instanceof CardtplOptionOhterItem;
  }

  public class CardtplOptionOhterVH extends FlexibleViewHolder {
	TextView title;
	TextView realIncome;
	TextView validDate;
	TextView supportType;
	LinearLayout chargeLayout;
	ImageView chosen;
	ImageView tagOnlyStaff;
	View shadow;

    public CardtplOptionOhterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      title = (TextView) view.findViewById(R.id.title);
      realIncome = (TextView) view.findViewById(R.id.real_income);
      validDate = (TextView) view.findViewById(R.id.valid_date);
      supportType = (TextView) view.findViewById(R.id.support_type);
      chargeLayout = (LinearLayout) view.findViewById(R.id.charge_layout);
      chosen = (ImageView) view.findViewById(R.id.chosen);
      tagOnlyStaff = (ImageView) view.findViewById(R.id.tag_only_staff);
      shadow = (View) view.findViewById(R.id.shadow_card_option);

      title.setText("自定义");
      realIncome.setVisibility(View.GONE);
      validDate.setVisibility(View.GONE);
      supportType.setVisibility(View.GONE);
      tagOnlyStaff.setVisibility(View.GONE);
    }
  }
}