package cn.qingchengfit.student.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import java.util.List;

public class FlexiableSwipeAdapter extends CommonFlexAdapter{

  public FlexiableSwipeAdapter(@NonNull List items) {
    super(items);
  }

  public FlexiableSwipeAdapter(@NonNull List items, @Nullable Object listeners) {
    super(items, listeners);
  }

  @Override public boolean onItemMove(int fromPosition, int toPosition) {
    Boolean ret = super.onItemMove(fromPosition, toPosition);
    return ret;
  }
}
