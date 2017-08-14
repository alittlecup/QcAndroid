package cn.qingchengfit.saasbase.student.views;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StudentOperationItem extends AbstractFlexibleItem<StudentOperationItem.StudentOperationVH> {

    @DrawableRes int iconRes;
    @StringRes int strRes;
    boolean proGym;
    boolean done;

    public StudentOperationItem(int iconRes, int strRes, boolean proGym, boolean done) {
        this.iconRes = iconRes;
        this.strRes = strRes;
        this.proGym = proGym;
        this.done = done;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_operation;
    }

    @Override public StudentOperationVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new StudentOperationVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, StudentOperationVH holder, int position, List payloads) {
        holder.imgFunction.setImageResource(iconRes);
        holder.tvTitle.setText(strRes);
        holder.imgPro.setVisibility(proGym ? View.GONE : View.VISIBLE);
        holder.itemView.setAlpha(done ? 1f : 0.4f);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class StudentOperationVH extends FlexibleViewHolder {
        @BindView(R2.id.img_function) ImageView imgFunction;
        @BindView(R2.id.img_pro) ImageView imgPro;
        @BindView(R2.id.tv_title) TextView tvTitle;

        public StudentOperationVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}