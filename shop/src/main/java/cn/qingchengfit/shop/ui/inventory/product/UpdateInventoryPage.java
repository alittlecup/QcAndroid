package cn.qingchengfit.shop.ui.inventory.product;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageUpdateInventoryBinding;
import cn.qingchengfit.shop.util.SpanUtils;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/update/inventory") public class UpdateInventoryPage
    extends ShopBaseFragment<PageUpdateInventoryBinding, UpdateInventoryViewModel>
    implements SimpleScrollPicker.SelectItemListener {
  public static final int ADD = 1;
  public static final int REDUCE = 2;
  @Need @UpdateAction Integer action;
  @Need Integer productID;
  private Integer good_id;

  @Override public void onSelectItem(int pos) {
    Good good = mViewModel.getGoods().getValue().get(pos);
    setCurGood(good);
  }

  @IntDef({ ADD, REDUCE }) @Retention(RetentionPolicy.SOURCE) public @interface UpdateAction {
  }

  @Override protected void subscribeUI() {
    mViewModel.getGoods().observe(this, goods -> {
      if (goods != null && !goods.isEmpty()) {
        setCurGood(goods.get(0));
      }
    });
    mViewModel.chooseGoodEvent.observe(this, aVoid -> {
      SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
      ArrayList<String> dataList = new ArrayList<>();
      if (mViewModel.getGoods().getValue() != null && !mViewModel.getGoods().getValue().isEmpty()) {
        for (Good good : mViewModel.getGoods().getValue()) {
          dataList.add(good.getName());
        }
        picker.show(dataList, 0);
        picker.setListener(this);
      }
    });
    mViewModel.getUpdateResult().observe(this ,aBoolean -> {
      ToastUtils.show("保存成功");
    });
  }

  private void setCurGood(Good good) {
    mViewModel.curInventory.set(good.getInventory());
    mViewModel.unit.set(good.getProduct().getUnit());
    mViewModel.productName.set(good.getProduct().getName());
    mViewModel.goodName.set(good.getName());
    good_id = good.getId();
  }

  @Override
  public PageUpdateInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageUpdateInventoryBinding.inflate(inflater, container, false);
    initToolbar();
    mBinding.setViewModel(mViewModel);
    mViewModel.setAction(action);
    mViewModel.loadSource(productID);
    initTest();
    return mBinding;
  }

  private void initTest() {
    Good good = new Good();
    good.setName("400ml");
    good.setInventory(30);
    Product product = new Product();
    product.setName("水");
    product.setUnit("瓶");
    good.setProduct(product);
    setCurGood(good);
  }

  private void initToolbar() {
    String title = "";
    if (action == ADD) {
      title = getString(R.string.add_inventory);
    } else if (action == REDUCE) {
      title = getString(R.string.reduce_inventory);
    }
    ToolbarModel toolbarModel = new ToolbarModel(title);
    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(item -> {
      mViewModel.postRecord(good_id);
      return false;
    });
    mBinding.setToolbarModel(new ToolbarModel(title));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    //toolbar.getMenu()
    //    .getItem(0)
    //    .setTitle(new SpanUtils().append(getString(R.string.common_save))
    //        .setForegroundColor(getResources().getColor(R.color.colorPrimary))
    //        .create());
    // TODO: 2018/1/23 右侧按钮的点击状态
    initToolbar(toolbar);
  }
}
