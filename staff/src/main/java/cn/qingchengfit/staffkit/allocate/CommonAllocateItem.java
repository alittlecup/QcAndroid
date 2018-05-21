package cn.qingchengfit.staffkit.allocate;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/5/2.
 */

public class CommonAllocateItem extends AbstractFlexibleItem<CommonAllocateItem.AllocateVH> {

    public Coach data;

    public CommonAllocateItem(Coach data) {
        this.data = data;
    }

    public Coach getData() {
        return data;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_allotsale_sale;
    }

    @Override public AllocateVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AllocateVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AllocateVH holder, int position, List payloads) {
        if (TextUtils.isEmpty(data.coach.username)) {
            holder.itemSalesHeader.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_nosales_normal));
        } else {
            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(data.coach.avatar))
                .asBitmap()
                .into(new CircleImgWrapper(holder.itemSalesHeader, holder.itemView.getContext()));
        }
        holder.tvSaleName.setText(TextUtils.isEmpty(data.coach.username) ? "未分配" : data.coach.username);
        holder.tvSaleStucount.setText(holder.itemView.getContext().getString(R.string.qc_allotsale_stucount, String.valueOf(data.count)));
    }

    class AllocateVH extends FlexibleViewHolder {

	ImageView itemSalesHeader;
	TextView tvSaleName;
	TextView tvSaleStucount;

        public AllocateVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemSalesHeader = (ImageView) view.findViewById(R.id.item_sales_header);
          tvSaleName = (TextView) view.findViewById(R.id.tv_sale_name);
          tvSaleStucount = (TextView) view.findViewById(R.id.tv_sale_stucount);
        }
    }
}
