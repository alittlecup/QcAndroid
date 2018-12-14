package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

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
    sellerFilterFragment = new TurnoverGirdSellerFilterFragment();
  }

  @Override protected String[] getTags() {
    return new String[] { "date", "feature", "seller", "payment" };
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
