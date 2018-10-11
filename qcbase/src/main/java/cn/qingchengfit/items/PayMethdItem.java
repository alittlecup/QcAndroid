package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PayMethdItem extends AbstractFlexibleItem<PayMethdItem.PayMethdVH> {

  @DrawableRes int icon;
  String txt;

  public PayMethdItem(int icon, String txt) {
    this.icon = icon;
    this.txt = txt;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_pay_method;
  }

  @Override public PayMethdVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new PayMethdVH(view,adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, PayMethdVH holder, int position,
      List payloads) {
    holder.img.setImageResource(icon);
    holder.tv.setText(txt);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class PayMethdVH extends FlexibleViewHolder {

	ImageView img;
	TextView tv;
    public PayMethdVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = (ImageView) view.findViewById(R.id.img);
      tv = (TextView) view.findViewById(R.id.tv);
    }
  }
}