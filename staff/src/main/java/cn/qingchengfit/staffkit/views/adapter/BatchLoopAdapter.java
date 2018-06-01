package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
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
 * Created by Paper on 16/5/4 2016.
 */
public class BatchLoopAdapter extends RecyclerView.Adapter<BatchLoopAdapter.BatchLoopVH> implements View.OnClickListener {

    List<Time_repeat> datas;
    OnRecycleItemClickListener listener;

    public BatchLoopAdapter(List<Time_repeat> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public BatchLoopVH onCreateViewHolder(ViewGroup parent, int viewType) {
        BatchLoopVH vh = new BatchLoopVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_batchloop, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(BatchLoopVH holder, int position) {
        holder.itemView.setTag(position);
        Time_repeat time_repeat = datas.get(position);
        holder.tv_date.setText(time_repeat.getStart() + "\n" + time_repeat.getEnd());
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class BatchLoopVH extends RecyclerView.ViewHolder {
	TextView tv_date;

        public BatchLoopVH(View itemView) {
            super(itemView);
          tv_date = (TextView) itemView.findViewById(R.id.item_date);
        }
    }
}
