package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateDetail;
import java.util.Date;
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
 * Created by Paper on 16/4/30 2016.
 */
public class PrivateBatchAdapter extends RecyclerView.Adapter<PrivateBatchAdapter.BatchViewHolder> implements View.OnClickListener {

    private List<QcResponsePrivateDetail.PrivateBatch> data;
    private OnRecycleItemClickListener listener;
    public PrivateBatchAdapter(List<QcResponsePrivateDetail.PrivateBatch> data) {
        this.data = data;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public PrivateBatchAdapter.BatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BatchViewHolder holder = new BatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_batch, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override public void onBindViewHolder(PrivateBatchAdapter.BatchViewHolder holder, int position) {
        holder.itemView.setTag(position);
        QcResponsePrivateDetail.PrivateBatch batch = data.get(position);
        holder.courseName.setText(batch.course.getName());
        Glide.with(holder.itemView.getContext()).load(PhotoUtils.getSmall(batch.course.getPhoto())).into(holder.img);
        holder.title.setText(batch.from_date + "至" + batch.to_date);
        if (DateUtils.getDayMidnight(DateUtils.formatDateFromYYYYMMDD(batch.to_date)) < DateUtils.getDayMidnight(new Date())) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
	TextView title;
	TextView courseName;
	ImageView img;
	View view;

        public BatchViewHolder(View itemView) {
            super(itemView);
          title = (TextView) itemView.findViewById(R.id.title);
          courseName = (TextView) itemView.findViewById(R.id.course_name);
          img = (ImageView) itemView.findViewById(R.id.img);
          view = (View) itemView.findViewById(R.id.outofdate);
        }
    }
}
