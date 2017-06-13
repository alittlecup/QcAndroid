package cn.qingchengfit.staffkit.views.student.score.item;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.StudentScoreHistoryBean;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class HistoryItem extends AbstractFlexibleItem<HistoryItem.HistoryVH> {

    private StudentScoreHistoryBean data;

    public HistoryItem(StudentScoreHistoryBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score_history;
    }

    @Override public HistoryVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new HistoryVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, HistoryVH holder, int position, List payloads) {

        Drawable drawableInfo = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.vector_student_score_item_info);
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        holder.info.setCompoundDrawables(drawableInfo, null, null, null);

        Drawable drawableAward = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.vector_student_score_item_award);
        drawableAward.setBounds(0, 0, drawableAward.getMinimumWidth(), drawableAward.getMinimumHeight());
        holder.award.setCompoundDrawables(drawableAward, null, null, null);

        Drawable drawableGym = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.vector_student_score_item_gym);
        drawableGym.setBounds(0, 0, drawableGym.getMinimumWidth(), drawableGym.getMinimumHeight());
        holder.gym.setCompoundDrawables(drawableGym, null, null, null);

        Drawable drawableCommont = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.vector_student_score_item_remark);
        drawableCommont.setBounds(0, 0, drawableCommont.getMinimumWidth(), drawableCommont.getMinimumHeight());
        holder.comment.setCompoundDrawables(drawableCommont, null, null, null);

        holder.title.setText(TextUtils.isEmpty(data.title) ? "" : data.title);
        holder.info.setText(TextUtils.isEmpty(data.info) ? "无" : data.info);
        holder.award.setText(TextUtils.isEmpty(data.award) ? "无" : data.award);
        holder.gym.setText(TextUtils.isEmpty(data.gym) ? "无" : data.gym);
        holder.comment.setText(TextUtils.isEmpty(data.commont) ? "无" : data.commont);
        holder.score.setText(data.score);

        if ("dec".equals(data.type) || "teamarrange_cancel".equals(data.type) || "priarrange_cancel".equals(data.type) || "撤销签到".equals(
            data.type)) {
            holder.score.setTextColor(Color.parseColor("#EA6161"));// 红色
        } else {
            holder.score.setTextColor(Color.parseColor("#0DB14B"));// 绿色
        }

        holder.curScore.setText(new StringBuilder().append("当前积分 ").append(data.curScore).toString());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class HistoryVH extends FlexibleViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.info) TextView info;
        @BindView(R.id.award) TextView award;
        @BindView(R.id.gym) TextView gym;
        @BindView(R.id.comment) TextView comment;
        @BindView(R.id.score) TextView score;
        @BindView(R.id.cur_score) TextView curScore;

        public HistoryVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}