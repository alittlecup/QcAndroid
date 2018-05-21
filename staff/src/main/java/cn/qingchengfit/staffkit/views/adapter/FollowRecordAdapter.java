package cn.qingchengfit.staffkit.views.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.common.FollowRecord;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import java.util.ArrayList;
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
 * Created by Paper on 16/3/23 2016.
 */
public class FollowRecordAdapter extends RecyclerView.Adapter<FollowRecordAdapter.FollowRecordVH> implements View.OnClickListener {

    List<FollowRecord> datas = new ArrayList<>();
    Context context;

    OnRecycleItemClickListener recycleItemClickListener;

    public FollowRecordAdapter(List<FollowRecord> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public OnRecycleItemClickListener getRecycleItemClickListener() {
        return recycleItemClickListener;
    }

    public void setRecycleItemClickListener(OnRecycleItemClickListener recycleItemClickListener) {
        this.recycleItemClickListener = recycleItemClickListener;
    }

    @Override public FollowRecordVH onCreateViewHolder(ViewGroup parent, int viewType) {
        FollowRecordVH vh = new FollowRecordVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followrecord, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onBindViewHolder(FollowRecordVH holder, int position) {
        holder.itemView.setTag(position);
        FollowRecord followRecord = datas.get(position);
        holder.time.setText(followRecord.time);
        holder.follower.setText(followRecord.follower);
        holder.content.setText(followRecord.content);
        holder.recycleviewImgs.setLayoutManager(new GridLayoutManager(context, 4));
        List<BodyTestBody.Photo> photos = new ArrayList<>();
        for (String s : followRecord.imgs) {
            photos.add(new BodyTestBody.Photo(s, false));
        }
        holder.recycleviewImgs.setAdapter(new ImageGridAdapter(photos));
    }

    @Override public void onClick(View v) {
        if (recycleItemClickListener != null) {
            recycleItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public class FollowRecordVH extends RecyclerView.ViewHolder {

	TextView time;
	TextView follower;
	TextView content;
	RecyclerView recycleviewImgs;

        public FollowRecordVH(View itemView) {
            super(itemView);
          time = (TextView) itemView.findViewById(R.id.time);
          follower = (TextView) itemView.findViewById(R.id.follower);
          content = (TextView) itemView.findViewById(R.id.content);
          recycleviewImgs = (RecyclerView) itemView.findViewById(R.id.recycleview_imgs);
        }
    }
}
