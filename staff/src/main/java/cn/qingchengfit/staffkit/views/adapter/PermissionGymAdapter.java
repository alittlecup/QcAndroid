package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
        @BindView(R.id.item_gym_header) ImageView itemGymHeader;
        @BindView(R.id.item_gym_name) TextView itemGymName;
        @BindView(R.id.item_is_personal) TextView itemIsPersonal;
        @BindView(R.id.qc_identify) ImageView qcIdentify;
        @BindView(R.id.item_gym_phonenum) TextView itemGymPhonenum;
        @BindView(R.id.item_right) ImageView itemRight;
        @BindView(R.id.item_gym_brand) TextView itemBrand;
        @BindView(R.id.hiden) View view;

        public ImageTwoTextVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
