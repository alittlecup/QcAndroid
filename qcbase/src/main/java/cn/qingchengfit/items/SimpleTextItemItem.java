package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SimpleTextItemItem extends AbstractFlexibleItem<SimpleTextItemItem.SimpleTextItemVH> {

  float textSize = 0;
  int gravity = Gravity.CENTER_VERTICAL;
  private String text;
  @DrawableRes private int bg;



  public SimpleTextItemItem(String text) {
    this.text = text;
  }

  public SimpleTextItemItem(String text, float textSize) {
    this.textSize = textSize;
    this.text = text;
  }

  public SimpleTextItemItem(String text, Integer g) {
    this.text = text;
    gravity = g;
  }

  private SimpleTextItemItem(Builder builder) {
    textSize = builder.textSize;
    gravity = builder.gravity;
    text = builder.text;
    bg = builder.bg;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getData() {
    return text;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_simple_text;
  }

  @Override public SimpleTextItemVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new SimpleTextItemVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, SimpleTextItemVH holder, int position,
    List payloads) {
    holder.itemText.setText(text);
    if (textSize > 0) holder.itemText.setTextSize(textSize);
    holder.itemText.setGravity(gravity);
    if (bg >0) holder.itemView.setBackgroundResource(bg);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class SimpleTextItemVH extends FlexibleViewHolder {

	TextView itemText;

    public SimpleTextItemVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      itemText = (TextView) view.findViewById(R.id.item_text);
    }
  }

  public static final class Builder {
    private float textSize;
    private int gravity;
    private String text;
    private int bg;

    private Builder() {
    }

    public Builder textSize(float val) {
      textSize = val;
      return this;
    }

    public Builder gravity(int val) {
      gravity = val;
      return this;
    }

    public Builder text(String val) {
      text = val;
      return this;
    }

    public Builder bg(int val) {
      bg = val;
      return this;
    }

    public SimpleTextItemItem build() {
      return new SimpleTextItemItem(this);
    }
  }
}