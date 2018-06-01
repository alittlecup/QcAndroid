package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/12/28 2015.
 */

public class ImageTwoTextAdapter extends RecyclerView.Adapter<ImageTwoTextAdapter.ImageTwoTextVH> implements View.OnClickListener {

    private List<ImageTwoTextBean> datas;
    private OnRecycleItemClickListener listener;

    public ImageTwoTextAdapter(List<ImageTwoTextBean> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public List<ImageTwoTextBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ImageTwoTextBean> datas) {
        this.datas = datas;
    }

    public void freshData(List<ImageTwoTextBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override public ImageTwoTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageTwoTextVH vh = null;
        if (viewType == 0) {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
        } else if (viewType == 1) {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false));
        } else {
            vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
        }
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(ImageTwoTextVH holder, int position) {
        holder.itemView.setTag(position);
        ImageTwoTextBean bean = datas.get(position);
        holder.itemGymName.setText(bean.text1);
        holder.itemBrand.setVisibility(View.GONE);
        if (bean.type != 0) {
            holder.itemGymPhonenum.setVisibility(View.GONE);
            holder.qcIdentify.setVisibility(View.GONE);
            holder.itemGymHeader.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(bean.text2)) {
                holder.itemGymPhonenum.setVisibility(View.GONE);
            } else {
                holder.itemGymPhonenum.setVisibility(View.VISIBLE);
                holder.itemGymPhonenum.setText(bean.text2);
            }

            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(bean.imgUrl))
                .asBitmap()
                .placeholder(R.drawable.ic_default_header)
                .into(new CircleImgWrapper(holder.itemGymHeader, holder.itemView.getContext()));
            if (bean.showIcon) {
                holder.qcIdentify.setVisibility(View.VISIBLE);
            } else {
                holder.qcIdentify.setVisibility(View.GONE);
            }
            if (bean.showRight) {
                holder.itemRight.setVisibility(View.VISIBLE);
                if (bean.rightIcon != 0) {
                    holder.itemRight.setImageResource(bean.rightIcon);
                    holder.itemGymName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                }
            } else {
                holder.itemRight.setVisibility(View.GONE);
            }
            holder.view.setVisibility(bean.hiden ? View.VISIBLE : View.GONE);
        }
    }

    @Override public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    @Override public int getItemCount() {
        if (datas != null) {
            return datas.size();
        } else {
            return 0;
        }
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
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
