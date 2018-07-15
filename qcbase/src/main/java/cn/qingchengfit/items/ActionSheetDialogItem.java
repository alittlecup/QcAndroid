package cn.qingchengfit.items;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class ActionSheetDialogItem
    extends AbstractFlexibleItem<ActionSheetDialogItem.ActionSheetDialogViewHolder> {
  CharSequence charSequence;

  public ActionSheetDialogItem(CharSequence charSequence) {
    this.charSequence = charSequence;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.action_sheet_dialog_item;
  }

  @Override
  public ActionSheetDialogViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    return new ActionSheetDialogViewHolder(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ActionSheetDialogViewHolder holder,
      int position, List payloads) {
    holder.content.setText(charSequence);
  }

  class ActionSheetDialogViewHolder extends FlexibleViewHolder {
    TextView content;

    public ActionSheetDialogViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      content = view.findViewById(R.id.content);
    }
  }
}