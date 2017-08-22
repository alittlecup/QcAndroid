package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/8/25.
 */
public class SignInCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<SignInTasks.Schedule> data = new ArrayList<>();

    public SignInCourseAdapter(List<SignInTasks.Schedule> data) {
        this.data = data;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signin_course, parent, false));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseHolder courseHolder = (CourseHolder) holder;
        SignInTasks.Schedule schedule = data.get(position);
        courseHolder.tv_signin_item_time.setText(schedule.getTime().split("T")[1]);
        courseHolder.tv_signin_item_course_name.setText(schedule.getName());
        courseHolder.tv_signin_item_course_teacher.setText(
            holder.itemView.getContext().getString(R.string.sign_in_room, schedule.getTeacher().getUsername()));
        courseHolder.tv_signin_item_course_room.setText(
            holder.itemView.getContext().getString(R.string.sign_in_coach, schedule.getSpace().getName()));
    }

    @Override public int getItemCount() {
        return data.size();
    }

    @Override public void onClick(View view) {

    }

    @Override public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public class CourseHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_signin_item_time) TextView tv_signin_item_time;
        @BindView(R.id.tv_signin_item_course_name) TextView tv_signin_item_course_name;
        @BindView(R.id.tv_signin_item_course_teacher) TextView tv_signin_item_course_teacher;
        @BindView(R.id.tv_signin_item_course_room) TextView tv_signin_item_course_room;

        public CourseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
