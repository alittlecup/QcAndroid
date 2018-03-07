package cn.qingchengfit.items;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ButtonItem extends AbstractFlexibleItem<ButtonItem.ButtonVH> {

  int id;
  String txt;
  @DrawableRes int bg;
  @ColorRes int txtColor;
  @DimenRes int textSize;

  private ButtonItem(Builder builder) {
    id = builder.id;
    txt = builder.txt;
    bg = builder.bg;
    txtColor = builder.txtColor;
    textSize = builder.textSize;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override public int getLayoutRes() {
    return R.layout.item_button;
  }

  @Override public ButtonVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new ButtonVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ButtonVH holder, int position,
    List payloads) {
    holder.btn.setText(txt);
    if (bg != 0) holder.btn.setBackgroundResource(bg);
    if (txtColor != 0) {
      holder.btn.setTextColor(CompatUtils.getColor(holder.btn.getContext(), txtColor));
    }
    if (textSize != 0) {
      holder.btn.setTextSize(holder.btn.getContext().getResources().getDimension(textSize));
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class ButtonVH extends FlexibleViewHolder {
    TextView btn;

    public ButtonVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      btn = view.findViewById(R.id.btn);
    }
  }

  public static final class Builder {
    private int id;
    private String txt;
    private int bg;
    private int txtColor;
    private int textSize;

    private Builder() {
    }

    public Builder id(int val) {
      id = val;
      return this;
    }

    public Builder txt(String val) {
      txt = val;
      return this;
    }

    public Builder bg(int val) {
      bg = val;
      return this;
    }

    public Builder txtColor(int val) {
      txtColor = val;
      return this;
    }

    public Builder textSize(int val) {
      textSize = val;
      return this;
    }

    public ButtonItem build() {
      return new ButtonItem(this);
    }
  }
}