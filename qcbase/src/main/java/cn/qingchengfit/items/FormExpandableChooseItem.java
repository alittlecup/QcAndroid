package cn.qingchengfit.items;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractExpandableItem;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.flexibleadapter.utils.Log;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/8.
 */

public class FormExpandableChooseItem extends
    AbstractExpandableItem<FormExpandableChooseItem.FormExpandableChooseVN, FormChooseItem> {
  private String data;
  private List<FormChooseItem> subDatas;

  public boolean isLeft() {
    return isLeft;
  }

  public void setLeft(boolean left) {
    isLeft = left;
  }

  private boolean isLeft;

  public FormExpandableChooseItem(String data, List<FormChooseItem> subDatas) {
    this(data, subDatas, false);
  }

  public FormExpandableChooseItem(String data, List<FormChooseItem> subDatas, boolean isLeft) {
    this.data = data;
    this.subDatas = subDatas;
    this.isLeft = isLeft;
    setExpanded(true);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_form_choose_expand;
  }

  @Override public FormExpandableChooseVN createViewHolder(View view, FlexibleAdapter adapter) {
    return new FormExpandableChooseVN(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, FormExpandableChooseVN holder, int position,
      List payloads) {
    holder.textView.setText(data);
    if (isLeft) {
      ViewGroup parent = (ViewGroup) holder.checkBox.getParent();
      parent.removeView(holder.checkBox);
      parent.addView(holder.checkBox, 0);
    } else {
      ViewGroup parent = (ViewGroup) holder.checkBox.getParent();
      parent.removeView(holder.checkBox);
      parent.addView(holder.checkBox, 1);
    }

    holder.checkBox.setChecked(adapter.isSelected(position));
    if (adapter.isSelected(position)) {
      holder.checkBox.setChecked(true);
    }
    if (hasSubSelected(adapter, position)) {
      holder.checkBox.setChecked(false);
      holder.checkBox.setActivated(true);
    } else {
      holder.checkBox.setActivated(false);
      holder.checkBox.setChecked(false);
    }
    if (isAllSubSelected(adapter, position)) {
      holder.checkBox.setChecked(true);
      adapter.addSelection(position);
    }
    holder.checkBox.setEnabled(isEnabled());
    holder.textView.setEnabled(isEnabled());
  }

  private boolean isAllSubSelected(FlexibleAdapter adapter, int curPos) {
    for (int i = 1; i <= getSubItemsCount(); i++) {
      if (adapter.isSelectable(curPos + i)) {
        if (!adapter.isSelected(curPos + i)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean hasSubSelected(FlexibleAdapter adapter, int curPos) {
    for (int i = 1; i <= getSubItemsCount(); i++) {
      if (adapter.isSelectable(curPos + i) && adapter.isEnabled(curPos + i)) {
        if (adapter.isSelected(curPos + i)) {
          return true;
        }
      }
    }
    return false;
  }

  class FormExpandableChooseVN extends ExpandableViewHolder {
    TextView textView;
    CheckBox checkBox;

    public FormExpandableChooseVN(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      textView = view.findViewById(R.id.text);
      checkBox = view.findViewById(R.id.choose_img);
      checkBox.setClickable(false);
    }

    @Override public void onClick(View view) {
      super.onClick(view);
      int position = getFlexibleAdapterPosition();
      if (!mAdapter.isEnabled(position)) return;
      // Experimented that, if LongClick is not consumed, onClick is fired. We skip the
      // call to the listener in this case, which is allowed only in ACTION_STATE_IDLE.
      if (mAdapter.mItemClickListener != null && mActionState == ItemTouchHelper.ACTION_STATE_IDLE) {
        Log.v("onClick on position %s mode=%s", position, FlexibleUtils.getModeName(mAdapter.getMode()));
        // Get the permission to activate the View from user
        if (mAdapter.mItemClickListener.onItemClick(position)) {
          // Now toggle the activation
          toggleActivation();
        }
      }
    }

    @Override protected boolean isViewExpandableOnClick() {
      return false;
    }
  }

}