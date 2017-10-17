package cn.qingchengfit.saasbase.cards.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
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
  public CardtplStandardVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new CardtplStandardVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplStandardVH holder, int position,
      List payloads) {
    String unitStr = CardBusinessUtils.getCardTypeCategoryUnit(cardtplType,holder.title.getContext());
    holder.title.setText("售价"+option.price + unitStr);
    if (TextUtils.isEmpty(option.charge)) {
      holder.realIncome.setVisibility(View.GONE);
    } else {
      holder.realIncome.setText("（储值"+option.charge + unitStr+"）");
      holder.realIncome.setVisibility(View.VISIBLE);
    }
    if (option.days == 0) {
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
      int colorPrimary = CompatUtils.getColor(holder.title.getContext(), R.color.colorPrimary);
      holder.supportType.setTextColor(colorPrimary);
      holder.realIncome.setTextColor(colorPrimary);
      holder.validDate.setTextColor(colorPrimary);
      holder.title.setTextColor(colorPrimary);
      holder.chargeLayout.setBackgroundResource(R.drawable.bg_rect_corner_primary);
    } else {
      holder.chosen.setVisibility(View.GONE);
      int colorgrey = CompatUtils.getColor(holder.title.getContext(), R.color.text_grey);
      holder.supportType.setTextColor(colorgrey);
      holder.realIncome.setTextColor(colorgrey);
      holder.validDate.setTextColor(colorgrey);
      holder.title.setTextColor(colorgrey);
      holder.chargeLayout.setBackgroundResource(R.drawable.shape_bg_rect_grey_corner4);
    }

    holder.tagOnlyStaff.setVisibility(option.for_staff ? View.VISIBLE : View.GONE);




    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == 1) {

    }else {
      // TODO: 2017/9/26
    }
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CardtplOptionItem){
      return ((CardtplOptionItem) o).option.id.equalsIgnoreCase(option.id);
    }else return false;
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