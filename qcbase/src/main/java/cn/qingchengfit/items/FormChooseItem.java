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

public class FormChooseItem extends AbstractFlexibleItem<FormChooseItem.FormChooseVH> {
  public FormChooseItem(String data) {
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

  public FormChooseItem(String data, boolean isLeft) {
    this.data = data;
    this.isLeft = isLeft;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_form_choose;
  }

  @Override public FormChooseVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FormChooseVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FormChooseVH holder, int position,
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
    holder.chooseImage.setChecked(adapter.isSelected(position));
    holder.chooseImage.setEnabled(isEnabled());
    holder.textView.setEnabled(isEnabled());

  }

  class FormChooseVH extends FlexibleViewHolder {
    TextView textView;
    CheckBox chooseImage;

    public FormChooseVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textView = view.findViewById(R.id.text);
      chooseImage = view.findViewById(R.id.choose_img);
      chooseImage.setClickable(false);
    }
  }
}
