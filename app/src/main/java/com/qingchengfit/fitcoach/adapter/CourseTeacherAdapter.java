package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.usecase.bean.CourseDetailTeacher;
import cn.qingchengfit.staffkit.views.custom.CircleImgWrapper;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RatingBarVectorFix;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/5.
 */
public class CourseTeacherAdapter extends RecyclerView.Adapter<CourseTeacherAdapter.CourseTeacherVH>
        implements View.OnClickListener {

    private List<CourseDetailTeacher> datas;
    private OnRecycleItemClickListener listener;

    public CourseTeacherAdapter(List<CourseDetailTeacher> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CourseTeacherVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseTeacherVH vh = new CourseTeacherVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_teacher_horizon, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(CourseTeacherVH holder, int position) {
        holder.itemView.setTag(position);
        CourseDetailTeacher teacher = datas.get(position);
        holder.name.setText(teacher.username);
        Glide.with(holder.itemView.getContext()).load(teacher.avatar).asBitmap().placeholder(R.drawable.ic_default_head_nogender).into(new CircleImgWrapper(holder.header,holder.itemView.getContext()));
        holder.rateStar.setRating(teacher.score);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onItemClick(v, (int) v.getTag());
    }

    public class CourseTeacherVH extends RecyclerView.ViewHolder {

        @Bind(R.id.header)
        ImageView header;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.rate_star)
        RatingBarVectorFix rateStar;

        public CourseTeacherVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}