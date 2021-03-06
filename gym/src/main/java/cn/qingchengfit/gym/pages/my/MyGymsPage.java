package cn.qingchengfit.gym.pages.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.gym.GymBaseFragment;
import cn.qingchengfit.gym.item.MyGymsItem;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.gym.databinding.GyPageMyGymsBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "gym", path = "/my/gyms") public class MyGymsPage
    extends GymBaseFragment<GyPageMyGymsBinding, MyGymsViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  private CommonFlexAdapter adapter;
  @Inject LoginStatus loginStatus;

  @Override protected void subscribeUI() {
    mViewModel.datas.observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        List<MyGymsItem> items = new ArrayList<>();
        for (Brand data : datas) {
          if (loginStatus.getUserId().equals(data.created_by.getId())) {
            items.add(new MyGymsItem(data, true));
          } else {
            items.add(new MyGymsItem(data, false));
          }
        }
        adapter.updateDataSet(items);
      }else{
        adapter.clear();
      }
    });
  }

  @Override public GyPageMyGymsBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyPageMyGymsBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    mBinding.tvCreateGym.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("/gym/choose/create", null);
      }
    });
    return mBinding;
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("我的健身房"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof MyGymsItem) {
      Brand data = ((MyGymsItem) item).getData();
      routeTo("/gym/brand", new BundleBuilder().withParcelable("brand", data).build());
    }
    return false;
  }
}
