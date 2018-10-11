package cn.qingchengfit.items;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import cn.qingchengfit.widgets.LoadingPointerView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/21.
 */

public class TextProgressItem extends AbstractFlexibleItem<TextProgressItem.TextProgressVH> {

  private Animation rotate;

  public TextProgressItem(Context context) {
    //rotate = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
  }

  @Override public boolean equals(Object o) {
    return  o instanceof TextProgressItem;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_text_progress;
  }

  @Override public TextProgressVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new TextProgressVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, TextProgressVH holder, int position,
      List payloads) {
    //holder.loadingPointerView.startAnimation(rotate);
  }

  class TextProgressVH extends FlexibleViewHolder {
    LoadingPointerView loadingPointerView;

    public TextProgressVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      loadingPointerView = view.findViewById(R.id.pointer);
    }
  }
}
