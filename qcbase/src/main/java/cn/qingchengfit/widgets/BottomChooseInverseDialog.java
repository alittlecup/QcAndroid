package cn.qingchengfit.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import cn.qingchengfit.model.common.BottomChooseData;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.util.List;

/**
 * Created by huangbaole on 2018/4/11.
 */

public class BottomChooseInverseDialog extends BottomChooseDialog {

  public BottomChooseInverseDialog(@NonNull Context context, CharSequence title,
      List<BottomChooseData> contents) {
    super(context, title, contents);
  }

  public BottomChooseInverseDialog(@NonNull Context context, CharSequence title,
      List<BottomChooseData> contents, int type) {
    super(context, title, contents, type);
  }

  @Override public boolean onItemClick(int position) {
      if (adapter.getMode() == SelectableAdapter.Mode.SINGLE) {
        if (listener != null) {
          boolean b = listener.onItemClick(position);
          if (b) {
            if(adapter.isSelected(position)){
              adapter.clearSelection();
            }else{
              adapter.clearSelection();
              adapter.addSelection(position);
            }
            adapter.notifyDataSetChanged();
          }
        }
        dismiss();
      } else {
        adapter.toggleSelection(position);
        adapter.notifyDataSetChanged();
      }
      return false;
  }
}
