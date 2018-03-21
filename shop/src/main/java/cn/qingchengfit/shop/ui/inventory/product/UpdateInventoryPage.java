package cn.qingchengfit.shop.ui.inventory.product;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageUpdateInventoryBinding;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
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
  @Need String productID;
  private String good_id;

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
      // TODO: 2018/3/20  有取消的buttom selector
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
    mViewModel.getUpdateResult().observe(this, aBoolean -> {
      ToastUtils.show("保存成功");
      getActivity().onBackPressed();
    });
  }

  private void setCurGood(Good good) {
    mViewModel.curInventory.set(good.getInventory());
    mViewModel.unit.set(good.getProduct().getUnit());
    mViewModel.productName.set(good.getProduct().getName());
    mViewModel.goodName.set(good.getName());
    good_id = good.getId();
    if (TextUtils.isEmpty(good.getName())) {
      mBinding.goodName.setVisibility(View.GONE);
    }
  }

  @Override
  public PageUpdateInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageUpdateInventoryBinding.inflate(inflater, container, false);
    initToolbar();
    mBinding.setViewModel(mViewModel);
    mViewModel.setAction(action);
    mViewModel.loadSource(productID);
    mBinding.save.setEnabled(false);
    mBinding.productName.setEnable(false);
    mBinding.offsetCount.addTextWatcher(new GoodProductItem.AfterTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        String trim = s.toString().trim();
        try {
          if (!TextUtils.isEmpty(trim)) {
            mViewModel.offSetInventory.set(Integer.valueOf(trim));
            if(action==ADD){
              if(mViewModel.curInventory.get()+mViewModel.offSetInventory.get()>9999){
                ToastUtils.show("商品库存不能大于9999");
                mBinding.offsetCount.setContent(s.subSequence(0, s.length() - 1).toString());
                return;
              }
            }
            mBinding.save.setEnabled(true);
          } else {
            mViewModel.offSetInventory.set(0);
            mBinding.save.setEnabled(false);
          }
        } catch (NumberFormatException exception) {
          ToastUtils.show("请输入正确数字");
          mBinding.save.setEnabled(false);
          mBinding.offsetCount.setContent(s.subSequence(0, s.length() - 1).toString());
        }
      }
    });
    return mBinding;
  }

  private void initToolbar() {
    String title = "";
    if (action == ADD) {
      title = getString(R.string.add_inventory);
    } else if (action == REDUCE) {
      title = getString(R.string.reduce_inventory);
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;

    initToolbar(toolbar);

    if (!CompatUtils.less21()
        && mBinding.save.getParent() instanceof ViewGroup
        && isfitSystemPadding()) {
      mBinding.save.setPadding(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
    }

    mBinding.save.setOnClickListener(v -> {
      // TODO: 2018/3/20 数据检查
      mViewModel.postRecord(good_id);
    });
  }
}
