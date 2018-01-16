package cn.qingchengfit.shop.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCategoryBinding;

/**
 * Created by huangbaole on 2017/12/27.
 */

public class CategoryItemView extends LinearLayout {
  ItemCategoryBinding binding;

  public CategoryItemView(Context context) {
    this(context, null, 0);
  }

  public CategoryItemView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CategoryItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    binding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_category, this, true);
    binding.imDelete.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        ViewGroup parent = (ViewGroup) CategoryItemView.this.getParent();
        if (parent.getChildCount() == 1) {
          setCategoryVisible(false);
          setDeleteVisible(false);
        } else {
          parent.removeView(CategoryItemView.this);
        }
      }
    });
  }

  public void setCategoryVisible(boolean visible) {
    binding.categoryName.setVisibility(visible ? VISIBLE : GONE);
  }

  public boolean isCategoryVisible() {
    return binding.categoryName.getVisibility() == VISIBLE;
  }

  public void setDeleteVisible(boolean visible) {
    // TODO: 2017/12/28 要不要加动画
    if (visible) {
      binding.imDelete.setVisibility(VISIBLE);
    } else {
      binding.imDelete.setVisibility(GONE);
    }
  }
}
