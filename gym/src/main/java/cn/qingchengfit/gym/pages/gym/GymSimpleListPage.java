package cn.qingchengfit.gym.pages.gym;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.gym.GymBaseFragment;
import cn.qingchengfit.gym.bean.BrandWithGyms;
import cn.qingchengfit.gym.item.GymSimpleListItem;
import cn.qingchengfit.gym.databinding.GyGymSimpleListPageBinding;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "gym", path = "/gym/simple/list") public class GymSimpleListPage
    extends GymBaseFragment<GyGymSimpleListPageBinding, GymSimpleListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need BrandWithGyms brand;

  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymSimpleListPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymSimpleListPageBinding.inflate(inflater, container, false);
    initRecyclerView();
    mBinding.setToolbarModel(new ToolbarModel(brand.brand.name));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
  }


  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    List< ? extends Gym> gyms = brand.gyms;
    List<IFlexible> items = new ArrayList<>();
    if (gyms != null && !gyms.isEmpty()) {
      for (Gym gym : gyms) {
        items.add(new GymSimpleListItem(brand.brand.name, gym));
      }
    }
    adapter.updateDataSet(items);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof GymSimpleListItem) {
      Gym gym = ((GymSimpleListItem) item).getGym();

      routeTo("/gym/apply", new BundleBuilder().withParcelable("gym", gym).withString("brandName",brand.brand.name).build());
    }
    return false;
  }
}
