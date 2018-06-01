package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.List;

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
 * Created by Paper on 16/7/14.
 */
public class BrandGymsAdapter extends RecyclerView.Adapter<BrandGymsAdapter.BrandGymsVH> implements View.OnClickListener {

    private List<Shop> datas;
    private OnRecycleItemClickListener listener;

    public BrandGymsAdapter(List<Shop> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public BrandGymsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        BrandGymsVH vh;
        if (viewType == 0) {
            vh = new BrandGymsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_gyms, parent, false));
            vh.del.setOnClickListener(this);
        } else {

            vh = new BrandGymsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_gyms_nodata, parent, false));
            vh.itemView.setOnClickListener(this);
        }

        return vh;
    }

    @Override public void onBindViewHolder(BrandGymsVH holder, int position) {
        holder.del.setTag(position);
        holder.itemView.setTag(position);
        if (getItemViewType(position) == 0) {
            Shop brandShop = datas.get(position - 1);
            holder.gymName.setText(brandShop.name);
            holder.imgPro.setImageResource(
                DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(brandShop.system_end)) >= 0 ? R.drawable.ic_pro_green
                    : R.drawable.ic_pro_free);
            //            holder.gymName.setCompoundDrawables(null,null,ContextCompat.getDrawable(holder.gymName.getContext(),R.drawable.ic_pro_green),null);
            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(PhotoUtils.getSmall(brandShop.photo)))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(holder.gymImg, holder.gymImg.getContext()));
            holder.gymContact.setText("联系方式: ".concat(brandShop.contact));
            holder.gymDate.setText("超级管理员：".concat(
                brandShop.superuser == null ? "" : (brandShop.superuser.getUsername() == null ? "" : brandShop.superuser.getUsername())));
            holder.gymPermission.setText("我的职位: ".concat(brandShop.position == null ? "" : brandShop.position));
        }
    }

    @Override public int getItemViewType(int position) {
        return position == 0 ? 1 : 0;
    }

    @Override public int getItemCount() {
        return datas.size() + 1;
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class BrandGymsVH extends RecyclerView.ViewHolder {

	ImageView gymImg;
	TextView gymName;
	TextView gymDate;
	TextView gymContact;
	TextView gymPermission;
	TextView del;
	ImageView imgPro;

        public BrandGymsVH(View itemView) {
            super(itemView);
          gymImg = (ImageView) itemView.findViewById(R.id.gym_img);
          gymName = (TextView) itemView.findViewById(R.id.gym_name);
          gymDate = (TextView) itemView.findViewById(R.id.gym_date);
          gymContact = (TextView) itemView.findViewById(R.id.gym_contact);
          gymPermission = (TextView) itemView.findViewById(R.id.gym_permission);
          del = (TextView) itemView.findViewById(R.id.del);
          imgPro = (ImageView) itemView.findViewById(R.id.img_pro);
        }
    }
}