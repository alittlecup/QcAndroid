package cn.qingcheng.gym.pages.brand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.item.GymBrandItem;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymBrandPageBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.bean.Shop;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "gym", path = "/gym/brand") public class GymBrandPage
    extends GymBaseFragment<GyGymBrandPageBinding, GymBrandViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need Brand brand;

  @Override protected void subscribeUI() {
    mViewModel.datas.observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        List<GymBrandItem> items = new ArrayList<>();
        for (Shop data : datas) {
          items.add(new GymBrandItem(data));
        }
        adapter.updateDataSet(items);
      }
    });
  }

  @Override
  public GyGymBrandPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymBrandPageBinding.inflate(inflater, container, false);
    initRecycler();
    initListener();
    initToolbar();
    if (brand.has_add_permission) {
      mBinding.btnNewGym.setVisibility(View.VISIBLE);
    } else {
      mBinding.btnNewGym.setVisibility(View.GONE);
    }
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel.loadShops(brand.id);
  }

  private void initToolbar() {
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel(brand.name));

    Glide.with(getContext())
        .load(PhotoUtils.getSmall(brand.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(mBinding.imgBrandPhoto, getContext()));
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
      Shop data = ((GymBrandItem) item).getData();
      if (position == 1) {
        routeTo("gym", "/gym/edit", null);
      } else {
        routeTo("gym", "/gym/info", null);
      }
    }
    return false;
  }
}
