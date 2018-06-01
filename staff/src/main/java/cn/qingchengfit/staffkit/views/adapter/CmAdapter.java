package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.responese.CmBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.DateUtils;
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
 * Created by Paper on 16/5/5 2016.
 */
public class CmAdapter extends RecyclerView.Adapter<CmAdapter.CmVh> implements View.OnClickListener {

    private final int type;
    List<CmBean> datas;
    private String[] weeks = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
    private OnRecycleItemClickListener listener;

    public CmAdapter(List<CmBean> datas, int type) {
        this.datas = datas;
        this.type = type;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public CmVh onCreateViewHolder(ViewGroup parent, int viewType) {
        CmVh vh = new CmVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manage, parent, false));
        vh.delete.setOnClickListener(this);
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(CmVh holder, int position) {
        if (position >= datas.size()) return;
        CmBean bean = datas.get(position);
        holder.delete.setTag(position);
        holder.itemView.setTag(position);
        StringBuffer sb = new StringBuffer(holder.itemView.getContext().getString(R.string.every));
        for (int i = 0; i < bean.week.size(); i++) {
            sb.append(weeks[bean.week.get(i) - 1]).append("、");
        }
        holder.week.setText(sb.substring(0, sb.length() - 1));
        if (type == Configs.TYPE_PRIVATE) {
            holder.time.setText(DateUtils.getTimeHHMM(bean.dateStart) + "-" + DateUtils.getTimeHHMM(bean.dateEnd));
        } else {
            holder.time.setText(DateUtils.getTimeHHMM(bean.dateStart));
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null && v.getTag() != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class CmVh extends RecyclerView.ViewHolder {
	TextView time;
	TextView week;

	ImageView delete;

        public CmVh(View itemView) {
            super(itemView);
          time = (TextView) itemView.findViewById(R.id.time);
          week = (TextView) itemView.findViewById(R.id.week);
          delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
}
