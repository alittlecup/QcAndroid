package cn.qingchengfit.saasbase.course.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Trainer;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.widgets.RatingBarVectorFix;
import com.bumptech.glide.Glide;
import java.util.List;

public class CourseTeacherAdapter extends RecyclerView.Adapter<CourseTeacherAdapter.CourseTeacherVH> implements View.OnClickListener {

    private List<Trainer> datas;
    private OnRecycleItemClickListener listener;

    public CourseTeacherAdapter(List<Trainer> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public CourseTeacherVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseTeacherVH vh =
            new CourseTeacherVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_teacher_horizon, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(CourseTeacherVH holder, int position) {
        holder.itemView.setTag(position);
        Trainer teacher = datas.get(position);
        holder.name.setText(teacher.username);
        Glide.with(holder.itemView.getContext())
            .load(teacher.avatar)
            .asBitmap()
            .placeholder(R.drawable.ic_default_head_nogender)
            .into(new CircleImgWrapper(holder.header, holder.itemView.getContext()));
        holder.rateStar.setRating(teacher.score);
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class CourseTeacherVH extends RecyclerView.ViewHolder {

        @BindView(R2.id.header) ImageView header;
        @BindView(R2.id.name) TextView name;
        @BindView(R2.id.rate_star) RatingBarVectorFix rateStar;

        public CourseTeacherVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}