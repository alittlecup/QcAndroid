package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
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
 * Created by Paper on 16/6/29 2016.
 */
public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringVH> implements View.OnClickListener {

    public List<String> datas;
    public OnRecycleItemClickListener listener;

    public StringAdapter(List<String> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public StringVH onCreateViewHolder(ViewGroup parent, int viewType) {
        StringVH vh = new StringVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(StringVH holder, int position) {
        holder.itemView.setTag(position);
        holder.text.setText(datas.get(position));
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class StringVH extends RecyclerView.ViewHolder {
	TextView text;

        public StringVH(View itemView) {
            super(itemView);
          text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
