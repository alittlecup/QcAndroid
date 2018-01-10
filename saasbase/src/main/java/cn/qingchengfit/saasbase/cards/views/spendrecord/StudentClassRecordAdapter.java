package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.StatementBean;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.List;

import static android.view.View.GONE;

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
public class StudentClassRecordAdapter extends RecyclerView.Adapter<ClassRecordViewHolder> implements View.OnClickListener {

    private List<StatementBean> datas;
    private OnRecycleItemClickListener listener;

    public StudentClassRecordAdapter(List<StatementBean> datas) {
        this.datas = datas;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public ClassRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ClassRecordViewHolder holder =
            new ClassRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_record, parent, false));
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override public void onBindViewHolder(ClassRecordViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (datas != null && datas.size() > 0) {
            StatementBean bean = datas.get(position);
            String now = DateUtils.Date2MMDD(bean.date);
            if (bean.year) {
                holder.headerlayout.setVisibility(View.VISIBLE);
                holder.yearTextView.setText(String.format("%d年%d月上课记录", DateUtils.getYear(bean.date), DateUtils.getMonth(bean.date) + 1));
                holder.monthdata.setText(bean.month_data);
            } else {
                holder.headerlayout.setVisibility(GONE);
            }

            if (position == 0 || !now.equalsIgnoreCase(DateUtils.Date2MMDD(datas.get(position - 1).date))) {
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
            } else {
                holder.itemStatementDetailBottomdivierView.setVisibility(GONE);
            }

            holder.itemStatementDetailNameTextView.setText(bean.title);
            holder.itemStatementDetailContentTextView.setText(bean.content);
            holder.itemStatementDetailDayTextView.setText(now.substring(3, 5));
            holder.itemStatementDetailMonthTextView.setText(
                now.substring(0, 2) + holder.itemView.getContext().getResources().getString(R.string.pickerview_month));
            if (TextUtils.isEmpty(bean.account)) {
                holder.spendaccount.setVisibility(GONE);
            } else {
                holder.spendaccount.setVisibility(View.VISIBLE);
                holder.spendaccount.setText(bean.account);
            }

            if (bean.cardtpl == Configs.CATEGORY_DATE){
                holder.spendaccount.setVisibility(GONE);
            }

            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(bean.picture))
                .asBitmap()
                .into(new CircleImgWrapper(holder.itemStatementDetailPicImageView, holder.itemView.getContext()));
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }
}
