package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.StudentCardBean;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

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
 * Created by Paper on 15/11/20 2015.
 */
public class StudentCardAdapter extends RecyclerView.Adapter<StudentCardAdapter.CardViewHolder>
        implements View.OnClickListener {
    private List<StudentCardBean> datas;
    private OnRecycleItemClickListener listener;

    public StudentCardAdapter(List<StudentCardBean> datas) {
        this.datas = datas;
    }


    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardViewHolder holder = new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_cart, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (datas != null && datas.size() > position) {
            StudentCardBean bean = datas.get(position);
            holder.mItemCardnameTextView.setText(bean.cardname);
            holder.mItemBalanceTextView.setText(bean.balance);
            holder.mItemGymNameTextView.setText(bean.gymName);
            holder.mItemStudentNamesTextView.setText(bean.students);
            holder.mItemCardnameTextView.setText(bean.timelimit);
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

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView mItemCardnameTextView;
        TextView mItemBalanceTextView;
        TextView mItemGymNameTextView;
        TextView mItemStudentNamesTextView;
        TextView mItemTimeLimitTextView;
        CardView mCardviewCardView;

        public CardViewHolder(View itemView) {
            super(itemView);
            mItemCardnameTextView = (TextView) itemView.findViewById(R.id.item_cardname);
            mItemBalanceTextView = (TextView) itemView.findViewById(R.id.item_balance);
            mItemGymNameTextView = (TextView) itemView.findViewById(R.id.item_gym_name);
            mItemStudentNamesTextView = (TextView) itemView.findViewById(R.id.item_student_names);
            mItemTimeLimitTextView = (TextView) itemView.findViewById(R.id.item_time_limit);
            mCardviewCardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
