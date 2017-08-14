package cn.qingchengfit.saasbase.cards.cardtypes.item;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTplStandard;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardtplStandardItem
    extends AbstractFlexibleItem<CardtplStandardItem.CardtplStandardVH> {

  CardTplStandard standard;

  public CardtplStandardItem(CardTplStandard standard) {
    this.standard = standard;
  }

  public CardTplStandard getStandard() {
    return standard;
  }

  public void setStandard(CardTplStandard standard) {
    this.standard = standard;
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
    holder.title.setText(standard.getContent() + CardBusinessUtils.getCardTypeCategoryUnit(standard.cardtype,holder.title.getContext()));
    if (TextUtils.isEmpty(standard.getReal_income())) {
      holder.realIncome.setVisibility(View.GONE);
    } else {
      holder.realIncome.setText(standard.getReal_income());
      holder.realIncome.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(standard.getValid_date())) {
      holder.validDate.setVisibility(View.GONE);
    } else {
      holder.validDate.setVisibility(View.VISIBLE);
      holder.validDate.setText(standard.getValid_date());
    }
    if (TextUtils.isEmpty(standard.getSupport_type())) {
      holder.supportType.setVisibility(View.GONE);
    } else {
      holder.supportType.setVisibility(View.VISIBLE);
      holder.supportType.setText(standard.getSupport_type());
    }
    if (adapter.isSelected(position) ) {
      holder.chosen.setVisibility( View.VISIBLE);
    } else {
      holder.chosen.setVisibility(View.GONE);
    }

    holder.tagOnlyStaff.setVisibility(standard.for_staff ? View.VISIBLE : View.GONE);
    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == 1) {
      int colorgrey = CompatUtils.getColor(holder.title.getContext(), R.color.text_grey);
      holder.supportType.setTextColor(colorgrey);
      holder.realIncome.setTextColor(colorgrey);
      holder.validDate.setTextColor(colorgrey);
      holder.title.setTextColor(colorgrey);
      holder.chargeLayout.setBackgroundResource(R.drawable.shape_bg_rect_grey_corner4);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
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