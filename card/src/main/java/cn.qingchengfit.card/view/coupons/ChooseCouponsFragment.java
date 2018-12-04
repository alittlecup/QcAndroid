package cn.qingchengfit.card.view.coupons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.card.R;
import cn.qingchengfit.card.bean.Coupon;
import cn.qingchengfit.card.bean.UserWithCoupons;
import cn.qingchengfit.card.databinding.CaChooseCouponsFragmentBinding;
import cn.qingchengfit.card.event.ChooseCouponsEvent;
import cn.qingchengfit.card.item.ChooseCouponsItem;
import cn.qingchengfit.card.view.BottomChooseCouponDialog;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "card", path = "/choose/coupons") public class ChooseCouponsFragment
    extends SaasBindingFragment<CaChooseCouponsFragmentBinding, ChooseCouponsVM>
    implements FlexibleAdapter.OnItemClickListener {

  CommonFlexAdapter adapter;
  @Need float prices;
  @Need Coupon chooseCoupon;
  @Need ArrayList<String> user_ids;


  @Override protected void subscribeUI() {
    mViewModel.getDatas().observe(this, userWithCoupons -> {
      if (userWithCoupons != null && !userWithCoupons.isEmpty()) {
        List<ChooseCouponsItem> datas = new ArrayList<>();
        for (UserWithCoupons coupons : userWithCoupons) {
          datas.add(new ChooseCouponsItem(coupons));
        }
        adapter.updateDataSet(datas);
      }
    });
  }

  @Override public CaChooseCouponsFragmentBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    return CaChooseCouponsFragmentBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initRecyclerView();
    initChooseCoupon();
    if (user_ids != null && !user_ids.isEmpty()) {
      mViewModel.loadCoupons(prices, user_ids);
    }
  }

  private void initChooseCoupon() {
    if (chooseCoupon != null) {
      mBinding.tvChooseCouponName.setText(
          String.format(getResources().getString(R.string.choose_coupon_text),
              chooseCoupon.getDescription()));
      mBinding.tvClearSelected.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RxBus.getBus().post(new ChooseCouponsEvent(null));
          mBinding.llBottomSelected.setVisibility(View.GONE);

        }
      });
      mBinding.llBottomSelected.setVisibility(View.VISIBLE);
    }else{
      mBinding.llBottomSelected.setVisibility(View.GONE);

    }
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("优惠券"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    UserWithCoupons data = ((ChooseCouponsItem) adapter.getItem(position)).getData();
    showChooseCouponDialog(data.getCoupons());
    return false;
  }

  private void showChooseCouponDialog(List<Coupon> coupons) {
    BottomChooseCouponDialog dialog = new BottomChooseCouponDialog(getContext(), coupons);
    dialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
      @Override public boolean onItemClick(int position) {
        dialog.dismiss();
        Coupon coupon = coupons.get(position);
        RxBus.getBus().post(new ChooseCouponsEvent(coupon));
        return false;
      }
    });
    dialog.show();
  }
}
