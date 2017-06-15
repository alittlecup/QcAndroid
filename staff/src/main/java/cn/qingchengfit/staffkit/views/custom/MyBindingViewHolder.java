package cn.qingchengfit.staffkit.views.custom;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;

/**
 * Created by yangming on 16/10/11.
 */

public class MyBindingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_sales_header) public ImageView itemSalesHeader;
    @BindView(R.id.tv_sale_name) public TextView tvSaleName;
    @BindView(R.id.tv_sale_stucount) public TextView tvSaleStucount;

    public MyBindingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}