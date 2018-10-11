package cn.qingchengfit.items;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractExpandableItem;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/20.
 */

public class BottomPayExpandItem
    extends AbstractExpandableItem<BottomPayExpandItem.BottomPayExpandVH, BottomPayItem> {
  String content;
  String action;

  public BottomPayExpandItem(String content) {
    this(content, null);
  }

  public BottomPayExpandItem(String content, String action) {
    this.content = content;
    this.action = action;
    setExpanded(true);
    setEnabled(!TextUtils.isEmpty(action));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bottom_pay_expand;
  }

  @Override public BottomPayExpandVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BottomPayExpandVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BottomPayExpandVH holder, int position,
      List payloads) {
    holder.textContent.setText(content);
    if (!TextUtils.isEmpty(action)) {
      holder.textAction.setText(action);
      holder.textAction.setVisibility(View.VISIBLE);
    } else {
      holder.textAction.setText("");
      holder.textAction.setVisibility(View.GONE);
    }
  }

  class BottomPayExpandVH extends ExpandableViewHolder {
    TextView textContent, textAction;

    public BottomPayExpandVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textAction = view.findViewById(R.id.tv_action);
      textContent = view.findViewById(R.id.content);
      textAction.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (view.getId() == R.id.tv_action) {
        super.onClick(view);
      }
    }
  }
}
