package cn.qingchengfit.saasbase.cards.views.offday;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.cards.bean.OffDay;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.utils.DateUtils;
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
 * Created by Paper on 16/3/24 2016.
 */
public class OffDayListAdapter extends RecyclerView.Adapter<OffDayListAdapter.OffDayListVH> implements View.OnClickListener {

    List<OffDay> datas;

    private OnRecycleItemClickListener listener;

    public OffDayListAdapter(List<OffDay> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public OffDayListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        OffDayListVH vh = new OffDayListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offday, parent, false));
        vh.cancel_offday.setOnClickListener(this);
        vh.btnAheadOffday.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(OffDayListVH holder, int position) {
        holder.cancel_offday.setTag(position);
        holder.btnAheadOffday.setTag(position);
        OffDay offDay = datas.get(position);
        holder.title.setText(offDay.title);
        holder.period.setText("时间:" + offDay.start + "至" + offDay.end);
        holder.price.setText("收费:" + offDay.price + "元");
        if (TextUtils.isEmpty(offDay.marks)) {
            holder.mark.setVisibility(View.GONE);
        } else {
            holder.mark.setText(offDay.marks);
            holder.mark.setVisibility(View.VISIBLE);
        }
        holder.controler.setText(DateUtils.formatToMMFromServer(offDay.time) + "    操作人:" + offDay.controler);
        if (offDay.status > 1) {
            holder.cancel.setVisibility(View.VISIBLE);
            holder.layoutBtn.setVisibility(View.GONE);
        } else {
            holder.cancel.setVisibility(View.GONE);
            holder.layoutBtn.setVisibility(View.VISIBLE);
        }
        switch (offDay.status) {
            case 1:
                holder.cancel.setText("");
                break;
            case 2:
                holder.cancel.setText("已删除");
                break;
            case 3:
                holder.cancel.setText("已提前销假");
                break;
            case 4:
                holder.cancel.setText("请假已结束");
                break;
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

    public class OffDayListVH extends RecyclerView.ViewHolder {

	TextView title;
	TextView cancel;
	TextView period;
	TextView price;
	TextView mark;
	TextView controler;
	TextView cancel_offday;
	TextView btnAheadOffday;
	LinearLayout layoutBtn;

        public OffDayListVH(View itemView) {
            super(itemView);
          title = (TextView) itemView.findViewById(R.id.title);
          cancel = (TextView) itemView.findViewById(R.id.cancel);
          period = (TextView) itemView.findViewById(R.id.period);
          price = (TextView) itemView.findViewById(R.id.price);
          mark = (TextView) itemView.findViewById(R.id.mark);
          controler = (TextView) itemView.findViewById(R.id.controler);
          cancel_offday = (TextView) itemView.findViewById(R.id.cancel_offday);
          btnAheadOffday = (TextView) itemView.findViewById(R.id.btn_ahead_offday);
          layoutBtn = (LinearLayout) itemView.findViewById(R.id.layout_btn);
        }
    }
}
