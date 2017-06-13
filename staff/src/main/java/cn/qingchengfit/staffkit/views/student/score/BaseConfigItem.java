package cn.qingchengfit.staffkit.views.student.score;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.responese.StudentScoreBaseConfigBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemStudentScoreBaseConfigBinding;
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
public class BaseConfigItem extends AbstractFlexibleItem<MyBindingFelxibleViewHolder> {

    public StudentScoreBaseConfigBean data;

    public BaseConfigItem(StudentScoreBaseConfigBean data) {
        this.data = data;
    }

    public StudentScoreBaseConfigBean getData() {
        return data;
    }

    public void setData(StudentScoreBaseConfigBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_score_base_config;
    }

    @Override
    public MyBindingFelxibleViewHolder createViewHolder(final FlexibleAdapter adapter, LayoutInflater inflater, final ViewGroup parent) {
        ItemStudentScoreBaseConfigBinding binding = DataBindingUtil.inflate(inflater, getLayoutRes(), parent, false);
        MyBindingFelxibleViewHolder holder = new MyBindingFelxibleViewHolder(binding.getRoot(), adapter);
        holder.setBinding(binding);
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MyBindingFelxibleViewHolder holder, int position, List payloads) {
        ItemStudentScoreBaseConfigBinding binding = (ItemStudentScoreBaseConfigBinding) holder.getBinding();

        binding.tvStudentScoreBaseLable.setText(data.lable);
        binding.tvStudentScoreBaseValue.setText(new StringBuilder().append(data.value).append(" 分/次").toString());
    }

    @Override public boolean equals(Object o) {
        return false;
    }
}