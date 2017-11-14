package cn.qingchengfit.saasbase.student.items;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2017/10/27.
 */

public class AllotStaffItem extends AbstractFlexibleItem<AllotStaffItem.AllocateVH>{
    public AllotDataResponse data;
    public AllotStaffItem(AllotDataResponse response){
        this.data=response;
    }
    public AllotDataResponse getData(){
        return data;
    }
    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_allot;
    }

    @Override
    public AllocateVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AllocateVH(view,adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, AllocateVH holder, int position, List payloads) {
        if (TextUtils.isEmpty(data.getSeller().username)) {
            holder.itemSalesHeader.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_nosales_normal));
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(PhotoUtils.getSmall(data.getSeller().avatar))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemSalesHeader, holder.itemView.getContext()));
        }
        holder.tvSaleName.setText(TextUtils.isEmpty(data.getSeller().username) ? "未分配" : data.getSeller().username);
        holder.tvSaleStucount.setText(holder.itemView.getContext().getString(R.string.qc_student_allot_student_count, String.valueOf(data.getCount())));

    }

    class AllocateVH extends FlexibleViewHolder {

        ImageView itemSalesHeader;
        TextView tvSaleName;
        TextView tvSaleStucount;

        public AllocateVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            itemSalesHeader=view.findViewById(R.id.item_sales_header);
            tvSaleName=view.findViewById(R.id.tv_sale_name);
            tvSaleStucount=view.findViewById(R.id.tv_sale_stucount);
        }
    }
}
