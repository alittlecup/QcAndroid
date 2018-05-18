package cn.qingchengfit.recruit.item;

import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ExpendedTextviewItem
    extends AbstractFlexibleItem<ExpendedTextviewItem.item_expended_textviewVH> {
  String content;

  public ExpendedTextviewItem(String content) {
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_expended_textview;
  }

  @Override
  public item_expended_textviewVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new item_expended_textviewVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, item_expended_textviewVH holder, int position,
      List payloads) {
    holder.tv.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class item_expended_textviewVH extends FlexibleViewHolder {
	TextView tv;

    public item_expended_textviewVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tv = (TextView) view.findViewById(R.id.tv);
    }
  }
}