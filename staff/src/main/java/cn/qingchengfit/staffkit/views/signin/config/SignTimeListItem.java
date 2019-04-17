package cn.qingchengfit.staffkit.views.signin.config;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemSigninTimeBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SignTimeListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSigninTimeBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_signin_time;
  }

  @Override public DataBindingViewHolder<ItemSigninTimeBinding> createViewHolder(View view,
      FlexibleAdapter flexibleAdapter) {
    return new DataBindingViewHolder<>(view, flexibleAdapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter flexibleAdapter,
      DataBindingViewHolder<ItemSigninTimeBinding> viewHolder, int i, List list) {
    ItemSigninTimeBinding dataBinding = viewHolder.getDataBinding();
    dataBinding.tvTimeDay.setText("");
    dataBinding.tvTimeWeek.setText("");
  }
}
