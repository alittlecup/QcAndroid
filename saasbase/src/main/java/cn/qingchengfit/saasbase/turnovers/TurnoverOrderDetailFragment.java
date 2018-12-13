package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.databinding.TurnoverOrderDetailBinding;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;

@Leaf(module = "staff", path = "/turnover/order") public class TurnoverOrderDetailFragment
    extends SaasBindingFragment<TurnoverOrderDetailBinding, TurnoverOrderDetailVM> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

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
  }
}
