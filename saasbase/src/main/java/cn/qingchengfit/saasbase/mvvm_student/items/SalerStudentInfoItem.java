package cn.qingchengfit.saasbase.mvvm_student.items;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.common.flexble.DataBindingViewHolder;
import cn.qingchengfit.saasbase.databinding.ItemSalerStudentInfoBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SalerStudentInfoItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSalerStudentInfoBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saler_student_info;
  }

  @Override public DataBindingViewHolder<ItemSalerStudentInfoBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemSalerStudentInfoBinding> holder, int position, List payloads) {

  }
}
