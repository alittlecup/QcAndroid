package cn.qingchengfit.saasbase.cards.item;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardtplOptionItem extends AbstractFlexibleItem<CardtplOptionItem.CardtplStandardVH> {

  CardTplOption option;
  int cardtplType;

  public CardtplOptionItem(CardTplOption option, int cardtplType) {
    this.option = option;
    this.cardtplType = cardtplType;
  }

  public CardTplOption getOption() {
    return option;
  }

  public void setOption(CardTplOption option) {
    this.option = option;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cardtpl_standard;
  }

  @Override public CardtplStandardVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardtplStandardVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplStandardVH holder, int position,
      List payloads) {
    holder.layoutCardOption.setEnabled(mEnabled);
    String unitStr =
        CardBusinessUtils.getCardTypeCategoryUnit(cardtplType, holder.title.getContext());
    holder.title.setText("¥" + option.price);
    if (TextUtils.isEmpty(option.charge)) {
      holder.realIncome.setVisibility(View.GONE);
    } else {
      holder.realIncome.setText("面额" + option.charge + unitStr);
      holder.realIncome.setVisibility(View.VISIBLE);
    }
    if (option.days <= 0 || !option.isLimit_days()) {
      holder.validDate.setVisibility(View.VISIBLE);
      holder.validDate.setText("有效期:不限");
    } else {
      holder.validDate.setVisibility(View.VISIBLE);
      holder.validDate.setText("有效期" + option.days + "天");
    }
    if (cardtplType == Configs.CATEGORY_DATE){
      holder.validDate.setVisibility(View.GONE);
    }
    //是否支持充卡 和 购卡
    if (!option.can_charge && !option.can_create) {
      holder.supportType.setVisibility(View.GONE);
    } else {
      holder.supportType.setVisibility(View.VISIBLE);
      holder.supportType.setText(
          CardBusinessUtils.supportChargeAndCreate(option.can_charge, option.can_create));
    }

    holder.imgCardOption.setImageDrawable(
        holder.itemView.getResources().getDrawable(R.drawable.vd_chosen_hook));
    if (adapter.isSelected(position)) {
      holder.imgCardOption.setVisibility(View.VISIBLE);
      holder.shadow.setVisibility(View.VISIBLE);
    } else {
      holder.imgCardOption.setVisibility(View.GONE);
      holder.shadow.setVisibility(View.GONE);
    }

    holder.tagOnlyStaff.setVisibility(option.for_staff ? View.VISIBLE : View.GONE);
    holder.supportType.setPressed(adapter.isSelected(position));
    holder.realIncome.setPressed(adapter.isSelected(position));
    holder.validDate.setPressed(adapter.isSelected(position));
    holder.title.setPressed(adapter.isSelected(position));

    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == 1) {

    } else {
      // TODO: 2017/9/26
    }
  }

  @Override public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CardtplOptionItem
        && ((CardtplOptionItem) o).option.id != null
        && option.id != null) {
      return ((CardtplOptionItem) o).option.id.equalsIgnoreCase(option.id);
    } else {
      return false;
    }
  }

  @Override public int hashCode() {
    if (option.id != null) return option.id.hashCode();
    return 0;
  }

  public class CardtplStandardVH extends FlexibleViewHolder {
	TextView title;
	TextView realIncome;
	TextView validDate;
	TextView supportType;
	LinearLayout chargeLayout;
	ImageView imgCardOption;
	ImageView tagOnlyStaff;
	View shadow;
	FrameLayout layoutCardOption;

    public CardtplStandardVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      title = (TextView) view.findViewById(R.id.title);
      realIncome = (TextView) view.findViewById(R.id.real_income);
      validDate = (TextView) view.findViewById(R.id.valid_date);
      supportType = (TextView) view.findViewById(R.id.support_type);
      chargeLayout = (LinearLayout) view.findViewById(R.id.charge_layout);
      imgCardOption = (ImageView) view.findViewById(R.id.img_custom_card_option);
      tagOnlyStaff = (ImageView) view.findViewById(R.id.tag_only_staff);
      shadow = (View) view.findViewById(R.id.shadow_card_option);
      layoutCardOption = (FrameLayout) view.findViewById(R.id.layout_card_option);
    }
  }
}