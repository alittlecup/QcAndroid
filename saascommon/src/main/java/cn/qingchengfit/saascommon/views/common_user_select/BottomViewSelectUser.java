package cn.qingchengfit.saascommon.views.common_user_select;

import android.support.design.widget.BottomSheetDialogFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;

public class BottomViewSelectUser extends BottomSheetDialogFragment
  implements FlexibleAdapter.OnItemClickListener {
  @Override public boolean onItemClick(int position) {
    return false;
  }
}
