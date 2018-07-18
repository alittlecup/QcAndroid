package cn.qingchengfit.saascommon.views.common_user_select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.databinding.CmViewUserSelectBinding;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

public class CommonUserSelectView extends BaseFragment{
  protected CmViewUserSelectBinding binding;
  protected List<IFlexible> datas = new ArrayList<>();
  protected CommonFlexAdapter adapter = new CommonFlexAdapter(datas,this);
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    binding = CmViewUserSelectBinding.inflate(inflater);
    initRv();
    initListener();
    return binding.getRoot();
  }

  private void initListener() {
    binding.llShowSelect.setOnClickListener(view -> {
      //mViewModel.getSelectedDatas().setValue(getSelectDataBeans());
      AllotSaleShowSelectDialogView f = new AllotSaleShowSelectDialogView();
      f.setTargetFragment(this, 0);
      f.show(getFragmentManager(), "");
    });
  }

  private void initRv() {
    binding.rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    binding.rv.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withBottomEdge(true).withOffset(1)
    );
    binding.rv.setAdapter(adapter);
  }

  public void filter(String filter){
    adapter.setSearchText(filter);
    adapter.filterItems();
  }

  public void setDatas(List<IFlexible> d){
    this.datas.clear();
    this.datas.addAll(d);
    this.adapter.updateDataSet(this.datas);
  }




}
