package cn.qingchengfit.staffkit.views.student.score.item;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


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

    @Override public HistoryVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new HistoryVH(view, adapter);
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
            holder.score.setTextColor(ContextCompat.getColor(holder.score.getContext(),R.color.primary));
        }

        holder.curScore.setText(new StringBuilder().append("当前积分 ").append(data.curScore).toString());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class HistoryVH extends FlexibleViewHolder {
	TextView title;
	TextView info;
	TextView award;
	TextView gym;
	TextView comment;
	TextView score;
	TextView curScore;

        public HistoryVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          title = (TextView) view.findViewById(R.id.title);
          info = (TextView) view.findViewById(R.id.info);
          award = (TextView) view.findViewById(R.id.award);
          gym = (TextView) view.findViewById(R.id.gym);
          comment = (TextView) view.findViewById(R.id.comment);
          score = (TextView) view.findViewById(R.id.score);
          curScore = (TextView) view.findViewById(R.id.cur_score);
        }
    }
}