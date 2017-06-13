package cn.qingchengfit.staffkit.views.gym;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.ImageTextBean;
import cn.qingchengfit.staffkit.views.adapter.ImageTextVerticalVH;
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
 * Created by Paper on 16/2/1 2016.
 */
@SuppressWarnings("unused") public class GridImageTextAdapter extends RecyclerView.Adapter<ImageTextVerticalVH>
    implements View.OnClickListener {

    OnRecycleItemClickListener listener;
    List<ImageTextBean> datas;

    public GridImageTextAdapter(List<ImageTextBean> d) {
        this.datas = d;
    }

    public void freshData(List<ImageTextBean> d) {
        this.datas = d;
        notifyDataSetChanged();
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public ImageTextVerticalVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageTextVerticalVH vh =
            new ImageTextVerticalVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagetext_vertical, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(ImageTextVerticalVH holder, int position) {
        holder.itemView.setTag(position);
        if (position < datas.size()) {
            holder.tv.setText(datas.get(position).text);
            holder.img.setVisibility(View.VISIBLE);
            //            Glide.with(App.context).load(datas.get(position).img).into(holder.img);
            holder.img.setImageResource(datas.get(position).img);
        } else {
            holder.tv.setText("");
            holder.img.setVisibility(View.GONE);
        }
    }

    @Override public int getItemCount() {
        if (datas.size() % 3 == 0) {
            return datas.size();
        } else {
            return datas.size() + (3 - datas.size() % 3);
        }
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }
}
