package cn.qingchengfit.staffkit.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/8/25.
 */
public class SignInStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<StudentBean> data = new ArrayList<>();
    private OnRecycleItemClickListener listener;

    public SignInStudentAdapter(List<StudentBean> data) {
        this.data = data;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        holder = new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signin_student, parent, false));
        return holder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StudentsHolder studentsHolder = (StudentsHolder) holder;
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        StudentBean bean = data.get(position);
        studentsHolder.tv_signin_item_student_name.setText(bean.getUsername());
        studentsHolder.tv_signin_item_student_phone.setText(bean.getPhone());
        if (bean.gender) {//男
            studentsHolder.iv_signin_item_student_gender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            studentsHolder.iv_signin_item_student_gender.setImageResource(R.drawable.ic_gender_signal_female);
        }
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(bean.checkin_avatar))
            .asBitmap()
            .placeholder(R.drawable.img_default_checkinphoto)
            .error(R.drawable.img_default_checkinphoto)
            .into(studentsHolder.iv_signin_item_student_face);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    @Override public void onClick(View view) {
        if (listener != null && (int) view.getTag() < data.size()) {
            listener.onItemClick(view, (int) view.getTag());
        }
    }

    @Override public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public static class StudentsHolder extends RecyclerView.ViewHolder {
	ImageView iv_signin_item_student_face;
	TextView tv_signin_item_student_name;
	TextView tv_signin_item_student_phone;
	ImageView iv_signin_item_student_gender;

        public StudentsHolder(View itemView) {
            super(itemView);
          iv_signin_item_student_face =
              (ImageView) itemView.findViewById(R.id.iv_signin_item_student_face);
          tv_signin_item_student_name =
              (TextView) itemView.findViewById(R.id.tv_signin_item_student_name);
          tv_signin_item_student_phone =
              (TextView) itemView.findViewById(R.id.tv_signin_item_student_phone);
          iv_signin_item_student_gender =
              (ImageView) itemView.findViewById(R.id.iv_signin_item_student_gender);
        }
    }
}
