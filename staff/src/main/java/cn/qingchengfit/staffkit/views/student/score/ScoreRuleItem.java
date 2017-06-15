package cn.qingchengfit.staffkit.views.student.score;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.responese.StudentScoreRuleBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemStudentScoreRuleBinding;
import cn.qingchengfit.staffkit.views.custom.MyBindingFelxibleViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/26.
 */
public class ScoreRuleItem extends AbstractFlexibleItem<MyBindingFelxibleViewHolder> {

    public StudentScoreRuleBean data;
    public View.OnClickListener listener;

    public ScoreRuleItem(StudentScoreRuleBean data, View.OnClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public StudentScoreRuleBean getData() {
        return data;
    }

    public void setData(StudentScoreRuleBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score_rule;
    }

    @Override
    public MyBindingFelxibleViewHolder createViewHolder(final FlexibleAdapter adapter, LayoutInflater inflater, final ViewGroup parent) {
        ItemStudentScoreRuleBinding binding = DataBindingUtil.inflate(inflater, getLayoutRes(), parent, false);
        MyBindingFelxibleViewHolder holder = new MyBindingFelxibleViewHolder(binding.getRoot(), adapter);
        holder.setBinding(binding);
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MyBindingFelxibleViewHolder holder, int position, List payloads) {
        ItemStudentScoreRuleBinding binding = (ItemStudentScoreRuleBinding) holder.getBinding();
        binding.itemDelete.setTag(position);
        binding.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                listener.onClick(view);
            }
        });

        binding.tvAmountRange.setText(
            new StringBuilder().append("实收金额").append(data.amountStart).append("至").append(data.amountEnd).append("元").toString());

        binding.tvPerScore.setText(new StringBuilder().append("每1元得").append(data.perScore).append("积分").toString());
    }

    @Override public boolean equals(Object o) {
        return false;
    }
}