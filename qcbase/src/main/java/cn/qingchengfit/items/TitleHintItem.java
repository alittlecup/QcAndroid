package cn.qingchengfit.items;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class TitleHintItem extends AbstractFlexibleItem<TitleHintItem.TitleHintVH> {

  protected String content;

  public TitleHintItem(String content) {
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_title_hint;
  }

  @Override public TitleHintVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new TitleHintVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, TitleHintVH holder, int position,
      List payloads) {
    holder.tv.setText(holder.tv.getContext().getString(R.string.title_hint_wrap, content));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class TitleHintVH extends FlexibleViewHolder {
	TextView tv;

    public TitleHintVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tv = (TextView) view.findViewById(R.id.tv);
    }
  }
}