package cn.qingchengfit.saasbase.common.flexble;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class DataBindingViewHolder<T extends ViewDataBinding> extends FlexibleViewHolder {
  T mBinding;

  public DataBindingViewHolder(View view, FlexibleAdapter adapter) {
    super(view, adapter);
    mBinding = DataBindingUtil.bind(view);
  }

  public T getDataBinding() {
    return mBinding;
  }
}
