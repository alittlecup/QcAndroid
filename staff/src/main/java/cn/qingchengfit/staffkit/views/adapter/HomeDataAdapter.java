package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.HomeInfo;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import java.util.List;

/**
 * Created by peggy on 16/6/2.
 */

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.HomeDataVH> implements View.OnClickListener {

    private List<HomeInfo.Stat> datas;
    private OnRecycleItemClickListener listener;

    public HomeDataAdapter(List<HomeInfo.Stat> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public HomeDataVH onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeDataVH vh = new HomeDataVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_data, parent, false));
        vh.orderToday.setOnClickListener(this);
        vh.weekOrder.setOnClickListener(this);
        vh.monthOrder.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(HomeDataVH holder, int position) {
        holder.orderToday.setTag(position);
        holder.weekOrder.setTag(position);
        holder.monthOrder.setTag(position);
        if (position * 3 < datas.size()) {
            HomeInfo.Stat s = datas.get(position * 3);
            holder.infoTitile1.setText(s.getName());
            holder.infoCount1.setText(s.getValue());
        }
        if (position * 3 + 1 < datas.size()) {
            HomeInfo.Stat s = datas.get(position * 3 + 1);
            holder.infoTitile2.setText(s.getName());
            holder.infoCount2.setText(s.getValue());
        }
        if (position * 3 + 2 < datas.size()) {
            HomeInfo.Stat s = datas.get(position * 3 + 2);
            holder.infoTitile3.setText(s.getName());
            holder.infoCount3.setText(s.getValue());
        }
    }

    @Override public int getItemCount() {
        return (datas.size() + 2) / 3;
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class HomeDataVH extends RecyclerView.ViewHolder {
        @BindView(R.id.info_titile_1) TextView infoTitile1;
        @BindView(R.id.info_count_1) TextView infoCount1;
        @BindView(R.id.order_today) LinearLayout orderToday;
        @BindView(R.id.info_titile_2) TextView infoTitile2;
        @BindView(R.id.info_count_2) TextView infoCount2;
        @BindView(R.id.week_order) LinearLayout weekOrder;
        @BindView(R.id.info_titile_3) TextView infoTitile3;
        @BindView(R.id.info_count_3) TextView infoCount3;
        @BindView(R.id.month_order) LinearLayout monthOrder;

        public HomeDataVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
