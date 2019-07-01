package cn.qingchengfit.saasbase.course.detail;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemCircleImageBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class CircleImageItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCircleImageBinding>> {
  String url;
  String text;

  public CircleImageItem(String url) {
    this.url = url;
  }

  public CircleImageItem(String url, String text) {
    this.url = url;
    this.text = text;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_circle_image;
  }

  @Override public DataBindingViewHolder<ItemCircleImageBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCircleImageBinding> holder, int position, List payloads) {
    ItemCircleImageBinding dataBinding = holder.getDataBinding();
    Glide.with(dataBinding.imageView.getContext())
        .load(PhotoUtils.getSmall(url))
        .asBitmap()
        .placeholder(ContextCompat.getDrawable(dataBinding.imageView.getContext(),
            R.drawable.default_manage_male))
        .error(R.drawable.default_manage_male)
        .into(new BitmapImageViewTarget(dataBinding.imageView) {
          @Override protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(dataBinding.imageView.getResources(), resource);
            roundedBitmapDrawable.setCircular(true);
            if (!TextUtils.isEmpty(text)) {
              dataBinding.imageView.setText(text);
              dataBinding.imageView.setBackgroundResource(R.drawable.colorprimary_circle);
              int i = DensityUtil.dip2px(dataBinding.imageView.getContext(), 2);
              dataBinding.imageView.setPadding(i, i, i, i);
            }
            dataBinding.imageView.setImageDrawable(roundedBitmapDrawable);
          }
        });
  }
}
