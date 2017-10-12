package cn.qingchengfit.saasbase.cards.item;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasConstant;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.widgets.ConnerTag;
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

  @Override public CardVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
    return new CardVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardVH holder, int position, List payloads) {
    holder.realcardName.setText(realCard.getName());
    holder.realcardBalance.setText(CardBusinessUtils.getCardBlance(realCard));

    if (realCard.is_locked()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_off_day));
      holder.imgStatus.setText("请假中");
    } else if (!realCard.is_active()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_stop));
      holder.imgStatus.setText("已停卡");
    } else if (realCard.isExpired()) {
      holder.imgStatus.setVisibility(View.VISIBLE);
      holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_out_of_day));
      holder.imgStatus.setText("已过期");
      if (realCard.getType() == SaasConstant.CATEGORY_DATE) {
        holder.realcardBalance.setText("已过期" + (-((Float) realCard.getBalance()).intValue()) + "天");
      }
    } else {
      holder.imgStatus.setVisibility(View.GONE);
    }
    holder.bgCard.setBackground(DrawableUtils.generateBg(8,CardBusinessUtils.getDefaultCardbgColor(realCard.getType())));
    holder.realcardStudents.setText(realCard.getUsersStr());
  }

  @Override public boolean equals(Object o) {
    if (o instanceof CardItem){
      return ((CardItem) o).getRealCard().getId().equalsIgnoreCase(realCard.getId());
    }else
      return false;
  }

  public class CardVH extends FlexibleViewHolder implements AnimatedViewHolder {
    @BindView(R2.id.realcard_name) TextView realcardName;
    @BindView(R2.id.realcard_students) TextView realcardStudents;
    @BindView(R2.id.realcard_balance) TextView realcardBalance;
    @BindView(R2.id.img_stutus) ConnerTag imgStatus;
    @BindView(R2.id.bg_card) RelativeLayout bgCard;

    public CardVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
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