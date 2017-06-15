package cn.qingchengfit.staffkit.views.student.score;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.responese.StudentScoreAwardRuleBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemStudentScoreAwardBinding;
import cn.qingchengfit.staffkit.views.custom.MyBindingFelxibleViewHolder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.StringUtils;
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
public class ScoreAwardItem extends AbstractFlexibleItem<MyBindingFelxibleViewHolder> {

    public StudentScoreAwardRuleBean data;

    public ScoreAwardItem(StudentScoreAwardRuleBean data) {
        this.data = data;
    }

    public StudentScoreAwardRuleBean getData() {
        return data;
    }

    public void setData(StudentScoreAwardRuleBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score_award;
    }

    @Override
    public MyBindingFelxibleViewHolder createViewHolder(final FlexibleAdapter adapter, LayoutInflater inflater, final ViewGroup parent) {
        ItemStudentScoreAwardBinding binding = DataBindingUtil.inflate(inflater, getLayoutRes(), parent, false);
        MyBindingFelxibleViewHolder holder = new MyBindingFelxibleViewHolder(binding.getRoot(), adapter);
        holder.setBinding(binding);
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MyBindingFelxibleViewHolder holder, int position, List payloads) {
        ItemStudentScoreAwardBinding binding = (ItemStudentScoreAwardBinding) holder.getBinding();

        binding.tvScoreAwardTitle.setText(new StringBuilder().append("奖励规则").append(data.id).toString());
        String dateStart = DateUtils.Date2YYYYMMDDV2(DateUtils.formatDateFromServer(data.dateStart));
        String dateEnd = DateUtils.Date2YYYYMMDDV2(DateUtils.formatDateFromServer(data.dateEnd));
        binding.tvScoreAwardDateRange.setText(new StringBuilder().append(dateStart).append("至").append(dateEnd).toString());

        // 团课
        if (StringUtils.isNumZero(data.groupTimes)) {
            binding.llScoreAwardGroup.setVisibility(View.GONE);
        } else {
            binding.llScoreAwardGroup.setVisibility(View.VISIBLE);
            binding.tvScoreAwardTimesGroup.setText(new StringBuilder().append(data.groupTimes).append("倍").toString());
        }

        //私教
        if (StringUtils.isNumZero(data.privateTimes)) {
            binding.llScoreAwardPrivate.setVisibility(View.GONE);
        } else {
            binding.llScoreAwardPrivate.setVisibility(View.VISIBLE);
            binding.tvScoreAwardTimesPrivate.setText(new StringBuilder().append(data.privateTimes).append("倍").toString());
        }

        // 签到
        if (StringUtils.isNumZero(data.signinTimes)) {
            binding.llScoreAwardSignin.setVisibility(View.GONE);
        } else {
            binding.llScoreAwardSignin.setVisibility(View.VISIBLE);
            binding.tvScoreAwardTimesSignin.setText(new StringBuilder().append(data.signinTimes).append("倍").toString());
        }

        // 新购卡
        if (StringUtils.isNumZero(data.buyTimes)) {
            binding.llScoreAwardBuy.setVisibility(View.GONE);
        } else {
            binding.llScoreAwardBuy.setVisibility(View.VISIBLE);
            binding.tvScoreAwardTimesBuy.setText(new StringBuilder().append(data.buyTimes).append("倍").toString());
        }

        // 续费
        if (StringUtils.isNumZero(data.chargeTimes)) {
            binding.llScoreAwardCharge.setVisibility(View.GONE);
        } else {
            binding.llScoreAwardCharge.setVisibility(View.VISIBLE);
            binding.tvScoreAwardTimesCharge.setText(new StringBuilder().append(data.chargeTimes).append("倍").toString());
        }

        // 箭头
        if (data.is_active) {
            binding.imgArrow.setVisibility(View.VISIBLE);
        } else {
            binding.imgArrow.setVisibility(View.GONE);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }
}