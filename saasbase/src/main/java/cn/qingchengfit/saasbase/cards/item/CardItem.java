package cn.qingchengfit.saasbase.cards.item;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasConstant;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.AnimatedViewHolder;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;


public class CardItem extends AbstractFlexibleItem<CardItem.CardVH> {

  Card realCard;

  public CardItem(Card realCard) {
    this.realCard = realCard;
  }

  public Card getRealCard() {
    return realCard;
  }

  public void setRealCard(Card realCard) {
    this.realCard = realCard;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_realcard;
  }

  @Override public CardVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardVH holder, int position, List payloads) {
    holder.realcardName.setText(realCard.getName()+"("+realCard.getId()+")");
    holder.realcardBalance.setText(CardBusinessUtils.getCardBlance(realCard));

    if (realCard.is_locked()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_off_day , holder.imgStatus.getContext()));
      holder.imgStatus.setText("请假中");
    } else if (!realCard.is_active()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_stop , holder.imgStatus.getContext()));
      holder.imgStatus.setText("已停卡");
    } else if (realCard.isExpired()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.bg_card_out_of_day , holder.imgStatus.getContext()));
      holder.imgStatus.setText("已过期");
      if (realCard.getType() == SaasConstant.CATEGORY_DATE) {
        holder.realcardBalance.setText("已过期" + (-((Float) realCard.getBalance()).intValue()) + "天");
      }
    } else {
      holder.imgStatus.setVisibility(View.GONE);
    }
    holder.bgCard.setBackground(DrawableUtils.generateBg(16,CardBusinessUtils.getDefaultCardbgColor(realCard.getType())));
    holder.realcardStudents.setText(realCard.getUsersStr());
    holder.tvCardCate.setText(CardBusinessUtils.getCardTypeCategoryStrHead(realCard.getType(),holder.itemView.getContext()));
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CardItem){
      return ((CardItem) o).getRealCard().getId().equalsIgnoreCase(realCard.getId());
    }else
      return false;
  }

  public class CardVH extends FlexibleViewHolder implements AnimatedViewHolder {
	TextView realcardName;
	TextView realcardStudents;
	TextView realcardBalance;
	TextView tvCardCate;
	TextView imgStatus;
	RelativeLayout bgCard;

    public CardVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      realcardName = (TextView) view.findViewById(R.id.realcard_name);
      realcardStudents = (TextView) view.findViewById(R.id.realcard_students);
      realcardBalance = (TextView) view.findViewById(R.id.realcard_balance);
      tvCardCate = (TextView) view.findViewById(R.id.tv_card_type);
      imgStatus = (TextView) view.findViewById(R.id.img_stutus);
      bgCard = (RelativeLayout) view.findViewById(R.id.bg_card);
    }

    @Override public void scrollAnimators(@NonNull List<Animator> animators, int position,
        boolean isForward) {
      AnimatorHelper.slideInFromRightAnimator(animators,itemView,mAdapter.getRecyclerView(),0.7f);
    }

    @Override public boolean preAnimateAddImpl() {
      ViewCompat.setTranslationX(itemView,-100);
      return false;
    }

    @Override public boolean preAnimateRemoveImpl() {
      return false;
    }

    @Override public boolean animateAddImpl(ViewPropertyAnimatorListener listener, long addDuration,
        int index) {
      ViewCompat.animate(itemView)
          .translationX(100)
          .setDuration(addDuration)
          .setInterpolator(new DecelerateInterpolator())
          .setListener(listener)
          .setStartDelay(index * 150L)
          .start();
      return true;
    }

    @Override
    public boolean animateRemoveImpl(ViewPropertyAnimatorListener listener, long removeDuration,
        int index) {
      return false;
    }
  }
}