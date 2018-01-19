package cn.qingchengfit.items;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CmBottomListChosenItem
    extends AbstractFlexibleItem<CmBottomListChosenItem.CmBottomListVH> {

  String content, hint;

  public CmBottomListChosenItem(String content, String hint) {
    this.content = content;
    this.hint = hint;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cm_bottom_list_choose;
  }

  @Override public CmBottomListVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CmBottomListVH(view, adapter);
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
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CmBottomListVH extends FlexibleViewHolder {
    @BindView(R2.id.tv_title) TextView tvTitle;
    @BindView(R2.id.tv_hint) TextView tvHint;
    @BindView(R2.id.img_chosen) ImageView imgChosen;

    public CmBottomListVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}