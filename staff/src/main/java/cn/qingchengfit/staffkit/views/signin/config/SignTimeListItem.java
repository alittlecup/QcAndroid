package cn.qingchengfit.staffkit.views.signin.config;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemSigninTimeBinding;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeFrameBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SignTimeListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSigninTimeBinding>> {

  public SignInTimeFrameBean getBean() {
    return bean;
  }

  private SignInTimeFrameBean bean;

  public SignTimeListItem(SignInTimeFrameBean bean) {
    this.bean = bean;
  }

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
    StringBuilder week = new StringBuilder("周(");
    StringBuilder day = new StringBuilder();

    if (bean.checkTimeRepeates()) {
      week.append(bean.getTimeFrameWeekWithSplit("/")).append(")");
      day.append(bean.getStartTime()).append("~").append(bean.getEndTime());
    }
    dataBinding.tvTimeDay.setText(day);
    dataBinding.tvTimeWeek.setText(week);
  }
}
