package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardDetailItem extends AbstractFlexibleItem<CardDetailItem.CardDetailVH> {

  Card mCard;

  public CardDetailItem(Card mCard) {
    this.mCard = mCard;
  }

  public Card getCard() {
    return mCard;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_detail;
  }

  @Override public CardDetailVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardDetailVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardDetailVH holder, int position,
      List payloads) {
    holder.tvCardtplName.setText(mCard.getName());
    holder.tvCardId.setText(mCard.getId());
    holder.tvGymName.setText(mCard.getSupportGyms());
    holder.tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(mCard.getType(),
        holder.tvCardTplType.getContext()));
    holder.cardview.setBackground(
        DrawableUtils.generateBg(8f, CardBusinessUtils.getDefaultCardbgColor(mCard.getType())));
    if (mCard.isCheck_valid() && mCard.getType() != Configs.CATEGORY_DATE) {
      holder.tvCardAppend.setText("有效期："
          + DateUtils.getYYMMfromServer(mCard.getValid_from())
          + "至"
          + DateUtils.getYYMMfromServer(mCard.getValid_to()));
    } else if (mCard.getType() == Configs.CATEGORY_DATE) {
      holder.tvCardAppend.setText("有效期："
          + DateUtils.getYYMMfromServer(mCard.getStart())
          + "至"
          + DateUtils.getYYMMfromServer(mCard.getEnd()));
    } else {
      holder.tvCardAppend.setText("");
    }

    holder.imgStutus.setVisibility(mCard.getCard_tpl().is_enable?View.GONE:View.VISIBLE);
    holder.imgStutus.setText("已停卡");
    holder.imgStutus.setBackground(DrawableUtils.generateCardStatusBg(R.color.red,holder.imgStutus.getContext()));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CardDetailVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_card_tpl_type) TextView tvCardTplType;
    @BindView(R2.id.tv_cardtpl_name) TextView tvCardtplName;
    @BindView(R2.id.tv_gym_name) TextView tvGymName;
    @BindView(R2.id.tv_card_id) TextView tvCardId;
    @BindView(R2.id.cardview) RelativeLayout cardview;
    @BindView(R2.id.tv_card_append) TextView tvCardAppend;
    @BindView(R2.id.img_stutus) TextView imgStutus;

    public CardDetailVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}