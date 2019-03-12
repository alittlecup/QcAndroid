package cn.qingcheng.gym.pages.brand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.BransShopsPremission;
import cn.qingcheng.gym.item.GymBrandItem;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymBrandPageBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.utils.BundleBuilder;
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
import javax.inject.Inject;

@Leaf(module = "gym", path = "/gym/brand") public class GymBrandPage
    extends GymBaseFragment<GyGymBrandPageBinding, GymBrandViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need Brand brand;
  @Inject LoginStatus loginStatus;

  @Override protected void subscribeUI() {
    mViewModel.datas.observe(this, datas -> {
      if (datas != null && !datas.isEmpty()) {
        List<GymBrandItem> items = new ArrayList<>();
        if (isCreateUser()) {
          for (Shop data : datas) {
            GymBrandItem gymBrandItem = new GymBrandItem(data);
            gymBrandItem.setEditAble(true);
            items.add(gymBrandItem);
          }
        } else {
          if (premissions != null && !premissions.isEmpty()) {
            A:
            for (Shop shop : datas) {
              for (BransShopsPremission shopsPremission : premissions) {
                if (shop.id.equals(String.valueOf(shopsPremission.getGym_id()))) {
                  GymBrandItem gymBrandItem = new GymBrandItem(shop);
                  gymBrandItem.setEditAble(shopsPremission.isHas_permission());
                  items.add(gymBrandItem);
                  continue A;
                }
              }
            }
          }
        }
        adapter.updateDataSet(items);
      }
    });
  }

  private List<BransShopsPremission> premissions;

  @Override
  public GyGymBrandPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymBrandPageBinding.inflate(inflater, container, false);
    mBinding.setBrand(brand);
    initRecycler();
    initListener();
    initToolbar();
    if (isCreateUser()) {
      mBinding.imgEditBrand.setVisibility(View.VISIBLE);
      mBinding.btnNewGym.setVisibility(View.VISIBLE);
    } else {
      mBinding.imgEditBrand.setVisibility(View.GONE);
      mBinding.btnNewGym.setVisibility(View.GONE);
      mViewModel.loadShopPermissions(brand.id, PermissionServerUtils.STUDIO_LIST_CAN_CHANGE)
          .observe(this, bransShopsPremissions -> {
            if (bransShopsPremissions != null) {
              List<BransShopsPremission> shops = bransShopsPremissions.shops;
              if (adapter != null && adapter.getItemCount() > 0) {
                A:
                for (int i = 0; i < adapter.getItemCount(); i++) {
                  IFlexible item = adapter.getItem(i);
                  if (item instanceof GymBrandItem && shops != null && !shops.isEmpty()) {
                    for (BransShopsPremission shopsPremission : shops) {
                      if (String.valueOf(shopsPremission.getGym_id())
                          .equals(((GymBrandItem) item).getData().id)) {
                        ((GymBrandItem) item).setEditAble(shopsPremission.isHas_permission());
                        continue A;
                      }
                    }
                  }
                }
                adapter.notifyDataSetChanged();
              } else {
                premissions = shops;
              }
            }
          });
    }
    mViewModel.loadShops(brand.id);
    return mBinding;
  }

  private boolean isCreateUser() {
    return loginStatus.getUserId().equals(brand.created_by.getId());
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
        routeTo("/gym/create", new BundleBuilder().withParcelable("brand", brand).build());
      }
    });
    mBinding.imgEditBrand.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent("cn.qingchengfit.staffkit.views.setting.BrandManageActivity");
        try {
          intent = new Intent(getContext(),
              Class.forName("cn.qingchengfit.staffkit.views.setting.BrandManageActivity"));
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
        intent.putExtra(Configs.EXTRA_BRAND, brand);
        intent.putExtra("to", "edit");
        startActivityForResult(intent, 1001);
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
      getActivity().onBackPressed();
    }
  }

  private void initRecycler() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof GymBrandItem) {
      Shop data = ((GymBrandItem) item).getData();
      boolean editAble = ((GymBrandItem) item).isEditAble();
      if (editAble) {
        routeTo("/gym/edit",
            new BundleBuilder()
                .withParcelable("shop", data)
                .withParcelable("brand", brand)
                .build());
      }else{
        routeTo("/gym/edit",
            new BundleBuilder()
                .withParcelable("shop", data)
                .withParcelable("brand", brand)
                .build());
      }
    }
    return false;
  }
}
