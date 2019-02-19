package cn.qingcheng.gym.pages.brand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.item.GymBrandItem;
import cn.qingcheng.gym.vo.IGymBrandItemData;
import cn.qingchengfit.gym.databinding.GyGymBrandPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "gym", path = "/gym/brand") public class GymBrandPage
    extends GymBaseFragment<GyGymBrandPageBinding, GymBrandViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need String brandId;

  @Override protected void subscribeUI() {
    mViewModel.datas.observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        List<GymBrandItem> items = new ArrayList<>();
        for (IGymBrandItemData data : datas) {
          items.add(new GymBrandItem(data));
        }
        adapter.updateDataSet(items);
      }
    });
  }

  @Override
  public GyGymBrandPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymBrandPageBinding.inflate(inflater, container, false);
    Log.d("TAG", "initDataBinding: " + brandId);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecycler();
    initListener();
    initToolbar();
  }

  private void initToolbar() {
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel("健身房名称"));
  }

  private void initListener() {
    mBinding.btnNewGym.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
    mBinding.imgEditBrand.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
  }

  private void initRecycler() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof GymBrandItem) {
      IGymBrandItemData data = ((GymBrandItem) item).getData();
      if (position == 1) {
        routeTo("gym", "/gym/edit", null);
      } else {
        routeTo("gym", "/gym/info", null);
      }
    }
    return false;
  }
}
