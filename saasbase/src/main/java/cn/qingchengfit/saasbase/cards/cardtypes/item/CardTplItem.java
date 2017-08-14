package cn.qingchengfit.saasbase.cards.cardtypes.item;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.widgets.ConnerTag;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardTplItem extends AbstractFlexibleItem<CardTplItem.CardTplVH> {

  CardTpl cardTpl;

  public CardTplItem(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  public CardTpl getCardTpl() {
    return cardTpl;
  }

  public void setCardTpl(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_type;
  }

  @Override public CardTplVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new CardTplVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardTplVH holder, int position,
      List payloads) {
    holder.cardname.setText(cardTpl.getName());
    holder.cardid.setText("ID: " + cardTpl.getId());
    holder.intro.setText(cardTpl.getDescription());
    holder.limit.setText(cardTpl.getLimit());
    holder.cardview.setCardBackgroundColor(
        cn.qingchengfit.utils.ColorUtils.parseColor(cardTpl.getColor(), 200).getColor());
    holder.supportGyms.setText("适用场馆: " + cardTpl.getShopNames());
    holder.imgStutus.setVisibility(cardTpl.is_enable?View.GONE:View.VISIBLE);
    holder.type.setText(CardBusinessUtils.getCardTypeCategoryStr(cardTpl.type,holder.type.getContext()));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CardTplVH extends FlexibleViewHolder {
    @BindView(R2.id.cardname) TextView cardname;
    @BindView(R2.id.cardid) TextView cardid;
    @BindView(R2.id.support_gyms) TextView supportGyms;
    @BindView(R2.id.limit) TextView limit;
    @BindView(R2.id.intro) TextView intro;
    @BindView(R2.id.type) TextView type;
    @BindView(R2.id.card_bg) LinearLayout cardBg;
    @BindView(R2.id.img_stutus) ConnerTag imgStutus;
    @BindView(R2.id.cardview) CardView cardview;

    public CardTplVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}