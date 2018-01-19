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
import cn.qingchengfit.bean.ImageThreeBean;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import java.util.List;

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
 * Created by Paper on 15/11/23 2015.
 */
public class ImageThreeAdapter extends RecyclerView.Adapter<ImageThreeAdapter.ImageThreeHolder> implements View.OnClickListener {

    private List<ImageThreeBean> datas;
    private OnRecycleItemClickListener listener;

    public ImageThreeAdapter(List<ImageThreeBean> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public ImageThreeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageThreeHolder holder =
            new ImageThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym_course, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override public void onBindViewHolder(ImageThreeHolder holder, int position) {
        holder.itemView.setTag(position);
        if (datas != null && datas.size() > position) {
            ImageThreeBean bean = datas.get(position);
            Glide.with(App.AppContex).load(bean.img).into(holder.img);
            holder.head.setText(bean.title);
            holder.content1.setText(bean.content1);
            holder.content2.setText(bean.content2);
            if (!TextUtils.isEmpty(bean.tag)) {
                holder.tag.setVisibility(View.VISIBLE);
                holder.tag.setText(bean.tag);
                holder.tag.setBackgroundResource(bean.tagBg);
            } else {
                holder.tag.setVisibility(View.GONE);
            }
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class ImageThreeHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView head;
        public TextView content1;
        public TextView content2;
        public TextView tag;

        public ImageThreeHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.gym_course_img);
            head = (TextView) itemView.findViewById(R.id.gym_course_title);
            content1 = (TextView) itemView.findViewById(R.id.gym_course_time);
            content2 = (TextView) itemView.findViewById(R.id.gym_course_tag);
            tag = (TextView) itemView.findViewById(R.id.gym_course_title_tag);
        }
    }
}
