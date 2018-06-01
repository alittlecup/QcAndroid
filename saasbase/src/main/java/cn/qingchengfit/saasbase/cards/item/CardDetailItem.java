package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.widgets.ExpandTextView;
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
          + DateUtils.getYYYYMMDDfromServer(mCard.getValid_from())
          + "至"
          + DateUtils.getYYYYMMDDfromServer(mCard.getValid_to()));
    } else if (mCard.getType() == Configs.CATEGORY_DATE) {
      holder.tvCardAppend.setText("有效期："
          + DateUtils.getYYYYMMDDfromServer(mCard.getStart())
          + "至"
          + DateUtils.getYYYYMMDDfromServer(mCard.getEnd()));
    } else {
      holder.tvCardAppend.setText("");
    }

    if (mCard.is_locked()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_off_day , holder.imgStatus.getContext()));
      holder.imgStatus.setText("请假中");
    } else if (!mCard.is_active()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_stop , holder.imgStatus.getContext()));
      holder.imgStatus.setText("已停卡");
    } else if (mCard.isExpired()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_out_of_day , holder.imgStatus.getContext()));
      holder.imgStatus.setText("已过期");
    } else {
      holder.imgStatus.setVisibility(View.GONE);
    }
    holder.tvCardExpandDesc.setVisibility(View.GONE);
    //holder.imgStutus.setVisibility(mCard.is_active() ? View.GONE:View.VISIBLE);
    //holder.imgStutus.setText("已停卡");
    //holder.imgStutus.setBackground(DrawableUtils.generateCardStatusBg(R.color.red,holder.imgStutus.getContext()));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CardDetailVH extends FlexibleViewHolder {
	TextView tvCardTplType;
	TextView tvCardtplName;
	TextView tvGymName;
	TextView tvCardId;
	RelativeLayout cardview;
	TextView tvCardAppend;
	TextView imgStatus;
	ExpandTextView tvCardExpandDesc;

    public CardDetailVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvCardTplType = (TextView) view.findViewById(R.id.tv_card_tpl_type);
      tvCardtplName = (TextView) view.findViewById(R.id.tv_cardtpl_name);
      tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
      tvCardId = (TextView) view.findViewById(R.id.tv_card_id);
      cardview = (RelativeLayout) view.findViewById(R.id.cardview);
      tvCardAppend = (TextView) view.findViewById(R.id.tv_card_append);
      imgStatus = (TextView) view.findViewById(R.id.img_stutus);
      tvCardExpandDesc = (ExpandTextView) view.findViewById(R.id.tv_card_expand_desc);
    }
  }
}