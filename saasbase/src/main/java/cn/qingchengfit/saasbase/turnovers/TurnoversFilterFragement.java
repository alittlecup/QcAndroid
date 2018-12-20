package cn.qingchengfit.saasbase.turnovers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
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
    mViewModel.getPayments()
        .observe(paymentChooseFramgnet, datas -> paymentChooseFramgnet.setDatas(datas));
    mViewModel.getFeature()
        .observe(featureChooseFramgnet, datas -> featureChooseFramgnet.setDatas(datas));
    mViewModel.getSellers()
        .observe(sellerFilterFragment, datas -> sellerFilterFragment.setDatas(datas));
  }

  private void initFragment() {
    List<? extends ITurnoverFilterItemData> payments = mViewModel.getPayments().getValue();
    paymentChooseFramgnet = TurnoverFilterSimpleChooseFragment.newInstance(payments,
        new FlexibleAdapter.OnItemClickListener() {
          @Override public boolean onItemClick(int position) {
            List<? extends ITurnoverFilterItemData> value = mViewModel.getPayments().getValue();
            mViewModel.filterPayment.setValue(value.get(position).getText());
            mViewModel.filterPaymentChannel.setValue(value.get(position).getSignature());
            dismiss();
            return false;
          }
        });
    List<? extends ITurnoverFilterItemData> features = mViewModel.getFeature().getValue();
    featureChooseFramgnet = TurnoverFilterSimpleChooseFragment.newInstance(features,
        new FlexibleAdapter.OnItemClickListener() {
          @Override public boolean onItemClick(int position) {
            List<? extends ITurnoverFilterItemData> value = mViewModel.getFeature().getValue();
            mViewModel.filterFeature.setValue(value.get(position).getText());
            mViewModel.filterFeatureType.setValue(value.get(position).getSignature());
            dismiss();
            return false;
          }
        });
    timeFilterFragment = new TurnoversTimeFilterFragment();
    timeFilterFragment.setListener((start, end, type) -> {
      mViewModel.dateType.setValue(type);
      mViewModel.date.setValue(new Pair<>(start, end));
      dismiss();
    });
    sellerFilterFragment = new TurnoverGirdSellerFilterFragment();
    sellerFilterFragment.setRecycleItemClickListener(new OnRecycleItemClickListener() {
      @Override public void onItemClick(View v, int pos) {
        List<ICommonUser> value = mViewModel.getSellers().getValue();
        mViewModel.filterSeller.setValue(value.get(pos).getTitle());
        mViewModel.filterSellerId.setValue(value.get(pos).getId());
        dismiss();
      }
    });
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
