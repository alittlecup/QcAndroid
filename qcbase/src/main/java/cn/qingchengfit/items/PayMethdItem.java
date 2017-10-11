package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
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

  @Override public PayMethdVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new PayMethdVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
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

    @BindView(R2.id.img) ImageView img;
    @BindView(R2.id.tv) TextView tv;
    public PayMethdVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}