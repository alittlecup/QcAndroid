package cn.qingchengfit.saasbase.cards.item;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.MeasureUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.AnimatedViewHolder;
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

  @Override public CardTplVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardTplVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardTplVH holder, int position,
      List payloads) {
    holder.tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type,holder.tvCardtplName.getContext()).substring(0,1));
    holder.cardview.setBackground(DrawableUtils.generateBg(MeasureUtils.dpToPx(8f,holder.cardview.getResources()),CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
    holder.tvCardtplName.setText(cardTpl.getName());
    holder.tvCardId.setText(cardTpl.getId());
    holder.tvGymName.setText(cardTpl.getShopNames());
    holder.imgStatus.setVisibility(cardTpl.is_enable?View.GONE:View.VISIBLE);
    holder.imgStatus.setText("已停卡");
    holder.imgStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.red,holder.imgStatus.getContext()));
  }


  @Override public boolean equals(Object o) {
    if (o instanceof CardTplItem){
      return ((CardTplItem) o).getCardTpl().getId().equalsIgnoreCase(cardTpl.getId());
    }else return false;
  }


  public class CardTplVH extends FlexibleViewHolder implements AnimatedViewHolder {
    @BindView(R2.id.tv_card_tpl_type) TextView tvCardTplType;
    @BindView(R2.id.tv_cardtpl_name) TextView tvCardtplName;
    @BindView(R2.id.tv_gym_name) TextView tvGymName;
    @BindView(R2.id.tv_card_id) TextView tvCardId;
    @BindView(R2.id.cardview) RelativeLayout cardview;
    @BindView(R2.id.img_stutus) TextView imgStatus;
    public CardTplVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }

    @Override public void scrollAnimators(@NonNull List<Animator> animators, int position,
        boolean isForward) {
      if (isForward)
        AnimatorHelper.slideInFromBottomAnimator(animators,itemView,mAdapter.getRecyclerView());
      else AnimatorHelper.slideInFromTopAnimator(animators,itemView,mAdapter.getRecyclerView());
    }

    @Override public boolean preAnimateAddImpl() {
      //itemView.setTranslationY(500);
      return false;
    }

    @Override public boolean preAnimateRemoveImpl() {
      return false;
    }

    @Override public boolean animateAddImpl(ViewPropertyAnimatorListener listener, long addDuration,
      int index) {
      //ViewCompat.animate(itemView).setDuration(addDuration).translationY(-500)
      //  .setInterpolator(new DecelerateInterpolator())
      //  .setListener(listener)
      //  .setStartDelay(index * 250L)
      //  .start();
      return false;
    }

    @Override
    public boolean animateRemoveImpl(ViewPropertyAnimatorListener listener, long removeDuration,
      int index) {
      return false;
    }
  }
}