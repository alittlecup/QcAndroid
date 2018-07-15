package cn.qingchengfit.items;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class BottomChooseItem extends AbstractFlexibleItem<BottomChooseItem.BottomChooseItemVH> {
  BottomChooseData data;

  public BottomChooseItem(BottomChooseData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.bottom_choose_item;
  }

  @Override public BottomChooseItemVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BottomChooseItemVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BottomChooseItemVH holder, int position,
      List payloads) {
    if (!TextUtils.isEmpty(data.getContent())) {
      holder.content.setText(data.getContent());
    }
    if (!TextUtils.isEmpty(data.getSubContent())) {
      holder.subContent.setText(data.getSubContent());
      holder.subContent.setVisibility(View.VISIBLE);
    } else {
      holder.subContent.setVisibility(View.GONE);
    }
    if (adapter.getMode() == SelectableAdapter.Mode.SINGLE) {
      if (adapter.isSelected(position)) {
        holder.selectImage.setBackgroundResource(R.drawable.vd_hook_primary);
        holder.selectImage.setVisibility(View.VISIBLE);
      } else {
        holder.selectImage.setVisibility(View.GONE);
      }
    } else if (adapter.getMode() == SelectableAdapter.Mode.MULTI) {
      holder.selectImage.setChecked(adapter.isSelected(position));
      holder.selectImage.setEnabled(adapter.isEnabled(position));
    }
    holder.content.setEnabled(adapter.isEnabled(position));
  }

  class BottomChooseItemVH extends FlexibleViewHolder {
    TextView content;
    TextView subContent;
    CheckBox selectImage;

    public BottomChooseItemVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      content = view.findViewById(R.id.content);
      subContent = view.findViewById(R.id.sub_content);
      selectImage = view.findViewById(R.id.select_image);
      selectImage.setClickable(false);
    }
  }
}
