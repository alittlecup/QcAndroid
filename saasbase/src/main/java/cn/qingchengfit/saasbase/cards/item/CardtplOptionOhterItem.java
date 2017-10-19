package cn.qingchengfit.saasbase.cards.item;

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

  @Override
  public CardtplOptionOhterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new CardtplOptionOhterVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplOptionOhterVH holder, int position,
      List payloads) {
    holder.chosen.setVisibility(adapter.isSelected(position)?View.VISIBLE:View.GONE);
    int colorPrimary = CompatUtils.getColor(holder.title.getContext(), R.color.text_color_gray);
    holder.realIncome.setTextColor(colorPrimary);
    holder.validDate.setTextColor(colorPrimary);
    holder.title.setTextColor(colorPrimary);
  }

  @Override public boolean equals(Object o) {
    return o instanceof CardtplOptionOhterItem;
  }

  public class CardtplOptionOhterVH extends FlexibleViewHolder {
    @BindView(R2.id.title) TextView title;
    @BindView(R2.id.real_income) TextView realIncome;
    @BindView(R2.id.valid_date) TextView validDate;
    @BindView(R2.id.support_type) TextView supportType;
    @BindView(R2.id.charge_layout) LinearLayout chargeLayout;
    @BindView(R2.id.chosen) ImageView chosen;
    @BindView(R2.id.tag_only_staff) ImageView tagOnlyStaff;
    public CardtplOptionOhterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      title.setText("其他");
      realIncome.setVisibility(View.GONE);
      validDate.setVisibility(View.GONE);
      supportType.setVisibility(View.GONE);
      tagOnlyStaff.setVisibility(View.GONE);
    }
  }
}