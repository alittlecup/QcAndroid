package cn.qingchengfit.saasbase.turnovers;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import com.bigkoo.pickerview.lib.DensityUtil;
import eu.davidea.flexibleadapter.FlexibleAdapter;

public class TurNoDataItem extends CommonNoDataItem {
  public TurNoDataItem(int imgRes, String hintStr, String titleStr) {
    super(imgRes, hintStr, titleStr);
  }

  public TurNoDataItem(Drawable imgRes, String hintStr, String titleStr) {
    super(imgRes, hintStr, titleStr);
  }

  public TurNoDataItem(int imgUrl, String hintStr) {
    super(imgUrl, hintStr);
  }

  public TurNoDataItem(int imgRes, String hintStr, String titleStr, String btnStr,
      OnEmptyBtnClickListener onEmptyBtnClickListener) {
    super(imgRes, hintStr, titleStr, btnStr, onEmptyBtnClickListener);
  }

  @Override public CommonNodataVH createViewHolder(View view, FlexibleAdapter adapter) {
    View img = view.findViewById(R.id.img);
    ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
    layoutParams.height= DensityUtil.dip2px(view.getContext(),108);
    layoutParams.width= DensityUtil.dip2px(view.getContext(),80);
    img.setLayoutParams(layoutParams);
    return super.createViewHolder(view, adapter);
  }
}
