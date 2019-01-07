package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.TurnoverOrderDetailBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.staff.views.ChooseSalerFragment;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "staff", path = "/turnover/order") public class TurnoverOrderDetailFragment
    extends SaasBindingFragment<TurnoverOrderDetailBinding, TurnoverOrderDetailVM> {
  CommonFlexAdapter adapter;
  @Need String turId;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.getTurDetail().observe(this, detail -> {
      if (detail != null) {
        mBinding.tvType.setText(
            TurnoversHomePage.trade_types.get(detail.getTrade_type()).getDesc());
        mBinding.tvCheckoutName.setText(detail.getCheckoutName());
        mBinding.tvPayment.setText(
            TurnoversHomePage.paymentChannels.get(detail.getChannel()).getDesc());
        mBinding.tvDate.setText(detail.getDate());
        mBinding.tvOrderId.setText(detail.getID());
        mBinding.tvSeller.setText(detail.getSellerName());
        mBinding.tvMark.setText(detail.getRemarks());
        mBinding.tvMoney.setText("￥" + detail.getAmount());
      }
    });
    mViewModel.staffMutableLiveData.observe(this, staff -> {
      if (staff != null) {
        mBinding.tvSeller.setText(staff.getUsername());
      }
    });
    mViewModel.getOrderList().observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        List<TurnoverOrderHistoryItem> items = new ArrayList<>();
        for (TurOrderSellerHistory history : datas) {
          items.add(new TurnoverOrderHistoryItem(history));
        }
        adapter.updateDataSet(items);
      }
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override
  public TurnoverOrderDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return TurnoverOrderDetailBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("账单详情"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mViewModel.loadTurDetail(turId);
    mBinding.tvSellerChange.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.SHOP_TURNOVER_CHANGE)) {
        ChooseSalerFragment fragment = new ChooseSalerFragment();
        fragment.setArguments(new BundleBuilder().withBoolean("hasNoStaff", false).build());
        routeTo(fragment);
      } else {
        DialogUtils.showAlert(getContext(), R.string.alert_permission_forbid);
      }
    });
  }
}
