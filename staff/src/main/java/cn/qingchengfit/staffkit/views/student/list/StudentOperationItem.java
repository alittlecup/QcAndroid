package cn.qingchengfit.staffkit.views.student.list;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StudentOperationItem extends AbstractFlexibleItem<StudentOperationItem.StudentOperationVH> {

    @DrawableRes int iconRes;
    @StringRes int strRes;
    boolean proGym;
    boolean done;
    private int width;

    public StudentOperationItem(int iconRes, int strRes, boolean proGym, boolean done) {
        this.iconRes = iconRes;
        this.strRes = strRes;
        this.proGym = proGym;
        this.done = done;
    }

    public int getStrRes(){
        return strRes;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_operation;
    }

    @Override public StudentOperationVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        StudentOperationVH holder = new StudentOperationVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
        this.width = parent.getWidth();
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(parent.getWidth() / 4, parent.getHeight() / 2));
        return holder;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, StudentOperationVH holder, int position, List payloads) {
        //if (position == adapter.getItemCount() - 1){
        //    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //}
        holder.imgFunction.setImageResource(iconRes);
        holder.tvTitle.setText(strRes);
        holder.imgPro.setVisibility(proGym ? View.GONE : View.VISIBLE);
        holder.itemView.setAlpha(done ? 1f : 0.4f);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class StudentOperationVH extends FlexibleViewHolder {
        @BindView(R.id.img_function) ImageView imgFunction;
        @BindView(R.id.img_pro) ImageView imgPro;
        @BindView(R.id.tv_title) TextView tvTitle;

        public StudentOperationVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}