package cn.qingchengfit.items;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/8.
 */

public class FormDeleteItem extends AbstractFlexibleItem<FormDeleteItem.FormDeleteVH> {
  public FormDeleteItem(String data) {
    this(data, false);
  }

  private String data;

  public void setLeft(boolean left) {
    isLeft = left;
  }

  public boolean isLeft() {
    return isLeft;
  }

  private boolean isLeft;

  public FormDeleteItem(String data, boolean isLeft) {
    this.data = data;
    this.isLeft = isLeft;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_form_delete;
  }

  @Override public FormDeleteVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FormDeleteVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FormDeleteVH holder, int position,
      List payloads) {
    holder.textView.setText(data);
    if (isLeft) {
      ViewGroup parent = (ViewGroup) holder.chooseImage.getParent();
      parent.removeView(holder.chooseImage);
      parent.addView(holder.chooseImage, 0);
    } else {
      ViewGroup parent = (ViewGroup) holder.chooseImage.getParent();
      parent.removeView(holder.chooseImage);
      parent.addView(holder.chooseImage, 1);
    }
    holder.chooseImage.setEnabled(isEnabled());
    holder.textView.setEnabled(isEnabled());
  }

  class FormDeleteVH extends FlexibleViewHolder {
    TextView textView;
    CheckBox chooseImage;

    public FormDeleteVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textView = view.findViewById(R.id.text);
      chooseImage = view.findViewById(R.id.choose_img);
      chooseImage.setClickable(false);
    }
  }
}
