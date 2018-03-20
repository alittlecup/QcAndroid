package cn.qingchengfit.shop.ui.items.product;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCategoryBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Channel;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/29.
 */

public class GoodProductItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCategoryBinding>> {
  public static final int SHOW_CARD_PRICE = 2;
  private boolean isExpend = false;
  private Good good;

  public GoodProductItem(Good good) {
    this.good = good;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_category;
  }

  @Override public DataBindingViewHolder<ItemCategoryBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    DataBindingViewHolder<ItemCategoryBinding> viewHolder =
        new DataBindingViewHolder<>(view, adapter);

    return viewHolder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCategoryBinding> holder, int position, List payloads) {
    ItemCategoryBinding dataBinding = holder.getDataBinding();

    dataBinding.categoryPrice.getEditText()
        .setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    dataBinding.categoryPriceCard.getEditText()
        .setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    dataBinding.categoryName.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
    dataBinding.imDelete.setVisibility(View.VISIBLE);
    dataBinding.categoryName.setVisibility(View.VISIBLE);

    if (adapter.getItemCount() == 1) {
      dataBinding.imDelete.setVisibility(View.GONE);
      dataBinding.categoryName.setVisibility(View.GONE);
    }

    if (adapter instanceof CommonFlexAdapter) {
      int status = ((CommonFlexAdapter) adapter).getStatus();
      if (status == SHOW_CARD_PRICE) {
        dataBinding.categoryPriceCard.setVisibility(View.VISIBLE);
      } else {
        dataBinding.categoryPriceCard.setVisibility(View.GONE);
      }
    }
    if (isExpend) {
      dataBinding.imDelete.setVisibility(View.VISIBLE);
      dataBinding.categoryName.setVisibility(View.VISIBLE);
    }
    dataBinding.setGood(good);
    dataBinding.imDelete.setOnClickListener((View v) -> {
      if (adapter.getItemCount() == 1) {
        dataBinding.imDelete.setVisibility(View.GONE);
        dataBinding.categoryName.setVisibility(View.GONE);
        ((GoodProductItem) adapter.getItem(0)).setExpend(false);
      } else {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() == 1) {
          ((GoodProductItem) adapter.getItem(0)).setExpend(true);
        }
      }
      adapter.getRecyclerView()
          .post(() -> ViewUtil.resetRecyclerViewHeight(adapter.getRecyclerView()));
    });
    dataBinding.categoryPrice.addTextWatcher(new AfterTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        if (s != null) {
          String price = checkPrice(s.toString());
          good.setPrice(price, Channel.RMB);
          if(price.equals(s.toString()))return;
          dataBinding.categoryPrice.setContent(price);
        }
      }
    });
    dataBinding.categoryPriceCard.addTextWatcher(new AfterTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        if (s != null) {
          String price = checkPrice(s.toString());
          good.setPrice(price, Channel.CARD);
          if(price.equals(s.toString()))return;
          dataBinding.categoryPriceCard.setContent(price);
        }
      }
    });
  }

  public Good getGood() {
    return good;
  }

  public void setExpend(boolean isExpend) {
    this.isExpend = isExpend;
  }

  public boolean isExpend() {
    return isExpend;
  }

  public abstract static class AfterTextWatcher implements TextWatcher {

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
  }

  private String checkPrice(String price) {
    String format = price;
    if (!TextUtils.isEmpty(price)) {
      boolean change = false;
      try {
        Double doubleprice = Double.valueOf(price);
        if ((doubleprice > 999999)) {
          ToastUtils.show("商品价格不能大于999999");
          change = true;
        } else {
          int i = price.lastIndexOf(".");
          if (i != -1 && price.length() - i > 3) {
            ToastUtils.show("商品价格最多支持两位小数");
            change = true;
          }

        }
      } catch (NumberFormatException ex) {
        ToastUtils.show("请输入正确的价格");
        change = true;
      } finally {
        if (change) {
          format = price.subSequence(0, price.length() - 1).toString();
        }
        if(format.endsWith(".0")){
          format.replaceAll("\\.0","");
        }
      }
    }
    return format;
  }
}
