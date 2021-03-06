package com.qingchengfit.fitcoach.items;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.FunctionBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class DailyWorkItem extends AbstractFlexibleItem<DailyWorkItem.DailyWorkVH> {

  public FunctionBean bean;

  public DailyWorkItem(FunctionBean bean) {
    this.bean = bean;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_daily_work;
  }

  @Override public DailyWorkVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new DailyWorkVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, DailyWorkVH holder, int position,
      List payloads) {
    if (bean != null) {
      holder.text.setText(bean.text);
      holder.img.setImageResource(bean.resImg);
      if (TextUtils.isEmpty(rightText)) {
        holder.tvNotSet.setVisibility(View.GONE);
      } else {
        holder.tvNotSet.setVisibility(View.VISIBLE);
        holder.tvNotSet.setText(rightText);
      }
    }
    //        Glide.with(holder.itemView.getContext()).load(bean.resImg).into(holder.img);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  String rightText;

  public void setRightTopText(String text) {
    this.rightText = text;
  }

  public class DailyWorkVH extends FlexibleViewHolder {
    ImageView img;
    TextView text;
    TextView tvNotSet;

    public DailyWorkVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      img = (ImageView) view.findViewById(R.id.img);
      text = (TextView) view.findViewById(R.id.text);
      tvNotSet = view.findViewById(R.id.tv_not_set);
    }
  }
}