package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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
 * Created by Paper on 15/12/28 2015.
 */

public class ChooseSiteAdapter extends RecyclerView.Adapter<ChooseSiteAdapter.ChooseSiteTextVH> implements View.OnClickListener {

    private List<ImageTwoTextBean> datas;
    private OnRecycleItemClickListener listener;

    public ChooseSiteAdapter(List<ImageTwoTextBean> datas) {
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

    @Override public ChooseSiteTextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ChooseSiteTextVH vh = null;
        if (viewType == 0) {
            vh = new ChooseSiteTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_site, parent, false));
        } else {
            vh = new ChooseSiteTextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_site, parent, false));
        }
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(ChooseSiteTextVH holder, int position) {
        holder.itemView.setTag(position);
        ImageTwoTextBean bean = datas.get(position);
        holder.t1.setText(bean.text1);
        holder.t2.setText(bean.text2);
        if (bean.showRight) {
            holder.righticon.setImageResource(bean.rightIcon);
            holder.righticon.setVisibility(View.VISIBLE);
        } else {
            holder.righticon.setVisibility(View.GONE);
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

    public static class ChooseSiteTextVH extends RecyclerView.ViewHolder {
        @BindView(R.id.text1) public TextView t1;
        @BindView(R.id.text2) public TextView t2;
        @BindView(R.id.righticon) public ImageView righticon;

        public ChooseSiteTextVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
