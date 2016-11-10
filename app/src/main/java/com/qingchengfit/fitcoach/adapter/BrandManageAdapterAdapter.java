package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/13.
 */
public class BrandManageAdapterAdapter extends RecyclerView.Adapter<BrandManageAdapterAdapter.BrandManageAdapterVH>
        implements View.OnClickListener {

    private List<Brand> datas;
    private OnRecycleItemClickListener listener;

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public BrandManageAdapterAdapter(List<Brand> datas) {
        this.datas = datas;
    }

    @Override
    public BrandManageAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {

        BrandManageAdapterVH vh = null;
        if (viewType == 0)
            vh = new BrandManageAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_manage, parent, false));
        else
            vh = new BrandManageAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_add, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(BrandManageAdapterVH holder, int position) {
        holder.itemView.setTag(position);

        if (Long.parseLong(datas.get(position).getId()) > 0) {
            Brand brand = datas.get(position);
            Glide.with(holder.image.getContext()).load(PhotoUtils.getSmall(brand.getPhoto())).asBitmap().placeholder(R.drawable.ic_default_header).into(new CircleImgWrapper(holder.image, holder.image.getContext()));
            if (!TextUtils.isEmpty(brand.getPhoto()))
                Glide.with(holder.image.getContext()).load(PhotoUtils.getGauss(brand.getPhoto())).placeholder(R.drawable.bg_brand).into(holder.bg);
            else                 Glide.with(holder.image.getContext()).load(R.drawable.bg_brand).placeholder(R.drawable.bg_brand).into(holder.bg);

            holder.name.setText(brand.getName());
            String user = "";
            if (brand.getCreated_by() != null && brand.getCreated_by().getUsername() != null)
                user = brand.getCreated_by().getUsername();
            else user = "";
            holder.brand.setText(String.format(Locale.CHINA, holder.image.getContext().getString(R.string.brand_id_creator), brand.getCname(), user));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (Long.parseLong(datas.get(position).getId()) > 0)
            return 0;
        else return 1;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onItemClick(v, (int) v.getTag());
    }

    public class BrandManageAdapterVH extends RecyclerView.ViewHolder {
        @Bind(R.id.background)
        ImageView bg;
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.brand)
        TextView brand;

        public BrandManageAdapterVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}