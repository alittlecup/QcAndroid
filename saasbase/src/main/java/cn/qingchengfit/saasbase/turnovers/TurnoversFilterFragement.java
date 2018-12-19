package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;
import cn.qingchengfit.saasbase.turnovers.TurnoversTimeFilterFragment.TimeType;

public class TurnoversFilterFragement extends BaseFilterFragment {
  private TurnoversVM mViewModel;
  TurnoverFilterSimpleChooseFragment paymentChooseFramgnet;
  TurnoverFilterSimpleChooseFragment featureChooseFramgnet;
  TurnoversTimeFilterFragment timeFilterFragment;
  TurnoverGirdSellerFilterFragment sellerFilterFragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mViewModel = ViewModelProviders.of(getParentFragment()).get(TurnoversVM.class);
    initFragment();
  }

  private void initFragment() {
    List<ITurnoverFilterItemData> payments = mViewModel.getPayments().getValue();
    paymentChooseFramgnet = TurnoverFilterSimpleChooseFragment.newInstance(payments,
        new FlexibleAdapter.OnItemClickListener() {
          @Override public boolean onItemClick(int position) {
            mViewModel.filterPayment.setValue(payments.get(position).getText());
            return false;
          }
        });
    List<ITurnoverFilterItemData> features = mViewModel.getFeature().getValue();
    featureChooseFramgnet = TurnoverFilterSimpleChooseFragment.newInstance(features,
        new FlexibleAdapter.OnItemClickListener() {
          @Override public boolean onItemClick(int position) {
            mViewModel.filterFeature.setValue(features.get(position).getText());

            return false;
          }
        });
    timeFilterFragment = new TurnoversTimeFilterFragment();
    timeFilterFragment.setListener((start, end, type) -> {
      switch (type) {
        case TimeType.DAY:
          mViewModel.filterDate.setValue("今日");
          break;
        case TimeType.WEEK:
          mViewModel.filterDate.setValue("本周");
          break;
        case TimeType. MONTH:
          mViewModel.filterDate.setValue("本月");
          break;
        case TimeType.CUSTOMIZE:
          mViewModel.filterDate.setValue("自定义");
          break;
      }
    });
    sellerFilterFragment = new TurnoverGirdSellerFilterFragment();
  }

  @Override protected String[] getTags() {
    return new String[] { "date", "feature", "seller", "payment" };
  }

  @Override public void dismiss() {
    mViewModel.filterVisible.setValue(false);
  }

  @Override protected Fragment getFragmentByTag(String tag) {
    if (tag.equalsIgnoreCase("date")) {
      return timeFilterFragment;
    } else if (tag.equalsIgnoreCase("feature")) {
      return featureChooseFramgnet;
    } else if (tag.equalsIgnoreCase("payment")) {
      return paymentChooseFramgnet;
    } else if (tag.equalsIgnoreCase("seller")) {
      return sellerFilterFragment;
    }
    return new EmptyFragment();
  }
}
