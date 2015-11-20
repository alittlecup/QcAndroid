package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.StatementBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;

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
public class StudentClassRecordAdapter extends RecyclerView.Adapter<ClassRecordViewHolder> {

    private List<StatementBean> datas;

    public StudentClassRecordAdapter(List<StatementBean> datas) {
        this.datas = datas;
    }

    @Override
    public ClassRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ClassRecordViewHolder holder = new ClassRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_record, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ClassRecordViewHolder holder, int position) {
        if (datas != null && datas.size() > 0) {
            StatementBean bean = datas.get(position);
            String now = DateUtils.getOnlyDay(bean.date);
            if (bean.year) {
                holder.yearTextView.setVisibility(View.VISIBLE);
                holder.yearTextView.setText(String.format("%d年上课记录", DateUtils.getYear(bean.date)));
            } else holder.yearTextView.setVisibility(View.GONE);

            if (position == 0 || !now.equalsIgnoreCase(DateUtils.getOnlyDay(datas.get(position - 1).date))) {
                holder.itemStatementDetailHeaderdivierView.setVisibility(View.VISIBLE);
                holder.itemStatementDetailDayTextView.setVisibility(View.VISIBLE);
                holder.itemStatementDetailMonthTextView.setVisibility(View.VISIBLE);
            } else {
                holder.itemStatementDetailHeaderdivierView.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailDayTextView.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailMonthTextView.setVisibility(View.INVISIBLE);
            }

            if (position == getItemCount() - 1) {
                holder.itemStatementDetailBottomdivierView.setVisibility(View.VISIBLE);
            } else
                holder.itemStatementDetailBottomdivierView.setVisibility(View.GONE);


            holder.itemStatementDetailNameTextView.setText(bean.title);
            holder.itemStatementDetailContentTextView.setText(bean.content);
            holder.itemStatementDetailDayTextView.setText(now.substring(3, 5));
            holder.itemStatementDetailMonthTextView.setText(now.substring(0, 2) + App.AppContex.getResources().getString(R.string.pickerview_month));

            Glide.with(App.AppContex).load(bean.picture).asBitmap().into(new CircleImgWrapper(holder.itemStatementDetailPicImageView, App.AppContex));


        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
