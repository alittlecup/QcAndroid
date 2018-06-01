package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by peggy on 16/5/29.
 */

public class PermissionGymAdapter extends RecyclerView.Adapter<PermissionGymAdapter.ImageTwoTextVH> {

    List<Shop> shops;

    private OnRecycleItemClickListener listener;
    private Brand brand;

    public PermissionGymAdapter(List<Shop> shops, Brand brand) {
        this.shops = shops;
        this.brand = brand;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public PermissionGymAdapter.ImageTwoTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        PermissionGymAdapter.ImageTwoTextVH vh =
            new PermissionGymAdapter.ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
        return vh;
    }

    @Override public void onBindViewHolder(ImageTwoTextVH holder, int position) {
        holder.itemView.setTag(position);
        Shop shop = shops.get(position);
        holder.itemGymName.setText(shop.name + " | " + brand.getName());
        holder.itemBrand.setText(shop.address);
        Glide.with(holder.itemView.getContext()).load(PhotoUtils.getSmall(shop.image)).into(holder.itemGymHeader);
        holder.view.setVisibility(shop.has_permission ? View.GONE : View.VISIBLE);
    }

    @Override public int getItemCount() {
        return shops.size();
    }

    public static class ImageTwoTextVH extends RecyclerView.ViewHolder {
	ImageView itemGymHeader;
	TextView itemGymName;
	TextView itemIsPersonal;
	ImageView qcIdentify;
	TextView itemGymPhonenum;
	ImageView itemRight;
	TextView itemBrand;
	View view;

        public ImageTwoTextVH(View itemView) {
            super(itemView);
          itemGymHeader = (ImageView) itemView.findViewById(R.id.item_gym_header);
          itemGymName = (TextView) itemView.findViewById(R.id.item_gym_name);
          itemIsPersonal = (TextView) itemView.findViewById(R.id.item_is_personal);
          qcIdentify = (ImageView) itemView.findViewById(R.id.qc_identify);
          itemGymPhonenum = (TextView) itemView.findViewById(R.id.item_gym_phonenum);
          itemRight = (ImageView) itemView.findViewById(R.id.item_right);
          itemBrand = (TextView) itemView.findViewById(R.id.item_gym_brand);
          view = (View) itemView.findViewById(R.id.hiden);
        }
    }
}
