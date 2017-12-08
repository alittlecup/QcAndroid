package cn.qingchengfit.saasbase.cards.item;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardtplOptionItem
    extends AbstractFlexibleItem<CardtplOptionItem.CardtplStandardVH> {

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

  @Override
  public CardtplStandardVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardtplStandardVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplStandardVH holder, int position,
      List payloads) {
    String unitStr = CardBusinessUtils.getCardTypeCategoryUnit(cardtplType,holder.title.getContext());
    holder.title.setText("价格"+option.price + "元");
    if (TextUtils.isEmpty(option.charge)) {
      holder.realIncome.setVisibility(View.GONE);
    } else {
      holder.realIncome.setText("(面额"+option.charge + unitStr+")");
      holder.realIncome.setVisibility(View.VISIBLE);
    }
    if (option.days <= 0) {
      holder.validDate.setVisibility(View.GONE);
    } else {
      holder.validDate.setVisibility(View.VISIBLE);
      holder.validDate.setText("(有效期"+option.days+"天)");
    }
    //是否支持充卡 和 购卡
    if (!option.can_charge && !option.can_create) {
      holder.supportType.setVisibility(View.GONE);
    } else {
      holder.supportType.setVisibility(View.VISIBLE);
      holder.supportType.setText(CardBusinessUtils.supportChargeAndCreate(option.can_charge,option.can_create));
    }

    if (adapter.isSelected(position) ) {
      holder.chosen.setVisibility( View.VISIBLE);
      holder.chargeLayout.setBackgroundResource(R.drawable.bg_card_option_primary);
    } else {
      holder.chosen.setVisibility(View.GONE);
      holder.chargeLayout.setBackgroundResource(R.drawable.bg_card_option_grey);
    }

    holder.tagOnlyStaff.setVisibility(option.for_staff ? View.VISIBLE : View.GONE);
    holder.supportType.setPressed(adapter.isSelected(position));
    holder.realIncome.setPressed(adapter.isSelected(position));
    holder.validDate.setPressed(adapter.isSelected(position));
    holder.title.setPressed(adapter.isSelected(position));

    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == 1) {

    }else {
      // TODO: 2017/9/26
    }
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CardtplOptionItem && ((CardtplOptionItem) o).option.id != null && option.id
       !=null){
      return ((CardtplOptionItem) o).option.id.equalsIgnoreCase(option.id);
    }else return false;
  }

  @Override public int hashCode() {
    if (option.id != null)
      return option.id.hashCode();
    return 0;
  }

  public class CardtplStandardVH extends FlexibleViewHolder {
    @BindView(R2.id.title) TextView title;
    @BindView(R2.id.real_income) TextView realIncome;
    @BindView(R2.id.valid_date) TextView validDate;
    @BindView(R2.id.support_type) TextView supportType;
    @BindView(R2.id.charge_layout) LinearLayout chargeLayout;
    @BindView(R2.id.chosen) ImageView chosen;
    @BindView(R2.id.tag_only_staff) ImageView tagOnlyStaff;
    public CardtplStandardVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}