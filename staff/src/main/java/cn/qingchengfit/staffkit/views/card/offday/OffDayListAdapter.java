package cn.qingchengfit.staffkit.views.card.offday;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.OffDay;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
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

        @BindView(R.id.title) TextView title;
        @BindView(R.id.cancel) TextView cancel;
        @BindView(R.id.period) TextView period;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.mark) TextView mark;
        @BindView(R.id.controler) TextView controler;
        @BindView(R.id.cancel_offday) TextView cancel_offday;
        @BindView(R.id.btn_ahead_offday) TextView btnAheadOffday;
        @BindView(R.id.layout_btn) LinearLayout layoutBtn;

        public OffDayListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
