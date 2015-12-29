package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/28 2015.
 */

public class ImageTwoTextAdapter extends RecyclerView.Adapter<ImageTwoTextAdapter.ImageTwoTextVH>
    implements View.OnClickListener{


    private List<ImageTwoTextBean> datas;
    private OnRecycleItemClickListener listener;

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

    public ImageTwoTextAdapter(List<ImageTwoTextBean> datas) {
        this.datas = datas;
    }

    @Override
    public ImageTwoTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageTwoTextVH vh = new ImageTwoTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageTwoTextVH holder, int position) {
        holder.itemView.setTag(position);
        ImageTwoTextBean bean = datas.get(position);
        holder.itemGymName.setText(bean.text1);
        if (TextUtils.isEmpty(bean.text2))
            holder.itemGymPhonenum.setVisibility(View.GONE);
        else{
            holder.itemGymPhonenum.setVisibility(View.VISIBLE);
            holder.itemGymPhonenum.setText(bean.text2);
        }
        Glide.with(App.AppContex).load(bean.imgUrl).asBitmap().into(new CircleImgWrapper(holder.itemGymHeader,App.AppContex));
        if (bean.showIcon)
            holder.qcIdentify.setVisibility(View.VISIBLE);
        else holder.qcIdentify.setVisibility(View.GONE);
        if (bean.showRight){
            holder.itemRight.setVisibility(View.VISIBLE);
            if (bean.rightIcon != 0){
                holder.itemRight.setImageResource(bean.rightIcon);
                holder.itemGymName.setTextColor(App.AppContex.getResources().getColor(R.color.primary));
            }
        }
        else holder.itemRight.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        if (datas != null)
            return datas.size();
        else return 0;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null)
            listener.onItemClick(v,(int)v.getTag());
    }

    public static class ImageTwoTextVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_gym_header)
        ImageView itemGymHeader;
        @Bind(R.id.item_gym_name)
        TextView itemGymName;
        @Bind(R.id.item_is_personal)
        TextView itemIsPersonal;
        @Bind(R.id.qc_identify)
        ImageView qcIdentify;
        @Bind(R.id.item_gym_phonenum)
        TextView itemGymPhonenum;
        @Bind(R.id.item_right)
        ImageView itemRight;
        public ImageTwoTextVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
