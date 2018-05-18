package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.recruit.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PositionDescItem extends AbstractFlexibleItem<PositionDescItem.PositionDescVH> {

  String title;
  String content;

  public PositionDescItem(String title, String content) {
    this.title = title;
    this.content = content;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_position_desc;
  }

  @Override public PositionDescVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PositionDescVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PositionDescVH holder, int position, List payloads) {
    holder.tvTitle.setText(title);
    holder.tvContent.setText(content);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PositionDescVH extends FlexibleViewHolder {
	TextView tvTitle;
	TextView tvContent;

    public PositionDescVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvContent = (TextView) view.findViewById(R.id.tv_content);
    }
  }
}