package cn.qingchengfit.animator;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;
import eu.davidea.flexibleadapter.common.FlexibleItemAnimator;

public class SlideInUpItemAnimator extends FlexibleItemAnimator {

    public SlideInUpItemAnimator() {
    }

    public SlideInUpItemAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder, final int index) {
        ViewCompat.animate(holder.itemView)
                  .translationY(holder.itemView.getHeight())
                  .alpha(0)
                  .setDuration(getRemoveDuration())
                  .setInterpolator(mInterpolator)
                  .setListener(new DefaultRemoveVpaListener(holder))
          .setStartDelay(getAddDuration()*index)

          .start();
    }

    @Override
    protected boolean preAnimateAddImpl(final RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(holder.itemView.getHeight());
        holder.itemView.setAlpha(0);
        return true;
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder, final int index) {
        ViewCompat.animate(holder.itemView)
                  .translationY(0)
                  .alpha(1)
                  .setDuration(getAddDuration())
                  .setInterpolator(mInterpolator)
                  .setListener(new DefaultAddVpaListener(holder))
          .setStartDelay(getAddDuration()*index)

          .start();
    }

}