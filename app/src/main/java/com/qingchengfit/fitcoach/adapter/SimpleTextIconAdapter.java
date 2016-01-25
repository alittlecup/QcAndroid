package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
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
 * Created by Paper on 16/1/8 2016.
 */
public class SimpleTextIconAdapter extends RecyclerView.Adapter<SimpleTextIconAdapter.SimpleTextVH>
        implements View.OnClickListener {

    private List<ImageIconBean> datas;
    private OnRecycleItemClickListener listener;

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public SimpleTextIconAdapter(List<ImageIconBean> datas) {
        this.datas = datas;
    }


    @Override
    public SimpleTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleTextVH vh = new SimpleTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simpleicon, parent, false));
        vh.itemView.setOnClickListener(this);
        vh.icon.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(SimpleTextVH holder, int position) {
        ImageIconBean bean = datas.get(position);
        holder.itemView.setTag(position);
        holder.icon.setTag(position);
        holder.imageiconText.setText(bean.content);
        if (bean.showIcon){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(bean.icon);
        }else {
            holder.icon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onItemClick(v, (int) v.getTag());
    }

    public class SimpleTextVH extends RecyclerView.ViewHolder {

        @Bind(R.id.imageicon_text)
        TextView imageiconText;
        @Bind(R.id.icon)
        ImageView icon;
        public SimpleTextVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
