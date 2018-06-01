package cn.qingchengfit.staffkit.views.custom;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;

/**
 * Created by yangming on 16/10/11.
 */

public class MyBindingViewHolder extends RecyclerView.ViewHolder {
  public ImageView itemSalesHeader;
  public TextView tvSaleName;
  public TextView tvSaleStucount;

  public MyBindingViewHolder(View itemView) {
    super(itemView);
    itemSalesHeader = itemView.findViewById(R.id.item_sales_header);
    tvSaleName = itemView.findViewById(R.id.tv_sale_name);
    tvSaleStucount = itemView.findViewById(R.id.tv_sale_stucount);
  }
}