package cn.qingchengfit.items;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CmBottomListChosenItem
    extends AbstractFlexibleItem<CmBottomListChosenItem.CmBottomListVH> {

  String content, hint;
  boolean enable = true;
  public CmBottomListChosenItem(String content, String hint) {
    this.content = content;
    this.hint = hint;
  }

  public CmBottomListChosenItem(String content, String hint, boolean enable) {
    this.content = content;
    this.hint = hint;
    this.enable = enable;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cm_bottom_list_choose;
  }

  @Override public CmBottomListVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CmBottomListVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CmBottomListVH holder, int position,
      List payloads) {
    if (TextUtils.isEmpty(hint)) {
      holder.tvHint.setVisibility(View.GONE);
    } else {
      holder.tvHint.setVisibility(View.VISIBLE);
      holder.tvHint.setText(hint);
    }
    holder.tvTitle.setText(content);
    holder.imgChosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    if (!adapter.isEnabled(position)){
      holder.tvTitle.setAlpha(0.5f);
    }else holder.tvTitle.setAlpha(1f);
  }

  @Override public boolean isEnabled() {
    return enable;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CmBottomListVH extends FlexibleViewHolder {
	TextView tvTitle;
	TextView tvHint;
	ImageView imgChosen;

    public CmBottomListVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvTitle = (TextView) view.findViewById(R.id.tv_title);
      tvHint = (TextView) view.findViewById(R.id.tv_hint);
      imgChosen = (ImageView) view.findViewById(R.id.img_chosen);
    }
  }
}