package cn.qingchengfit.staffkit.views.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangming on 16/8/25.
 */
public class SignInLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<SignInTasks.SignInTask> data = new ArrayList<>();

    private OnRecycleItemClickListener listener;

    public SignInLogAdapter(List<SignInTasks.SignInTask> data) {
        this.data = data;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        holder = new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signin_log, parent, false));
        return holder;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SignInTasks.SignInTask bean = data.get(position);
        holder.itemView.setTag(bean.getId());
        StudentsHolder studentsHolder = (StudentsHolder) holder;
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(bean.getCheckinAvatar()))
            .asBitmap()
            .placeholder(R.drawable.img_default_checkinphoto_noadd)
            .into(studentsHolder.iv_signin_item_student_face);
        studentsHolder.tv_signin_item_student_name.setText(bean.getUserName());

        studentsHolder.iv_signin_item_student_gender.setImageResource(
            bean.getUserGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);

        studentsHolder.tv_signin_item_in_time.setText(
            holder.itemView.getContext().getString(R.string.sign_in_log_in, bean.getCreatedAt().replace("T", " ")));
        if (TextUtils.isEmpty(bean.getCheckoutAt())) {
            studentsHolder.tv_signin_item_out_time.setText("暂未签出");
            Drawable drawableOut = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_sign_out_nomarl);
            drawableOut.setBounds(0, 0, drawableOut.getMinimumWidth(), drawableOut.getMinimumHeight());
            studentsHolder.tv_signin_item_out_time.setCompoundDrawables(drawableOut, null, null, null);
        } else {
            studentsHolder.tv_signin_item_out_time.setText(
                App.context.getString(R.string.sign_in_log_out, bean.getCheckoutAt().replace("T", " ")));
            Drawable drawableOut = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_sign_out_confirm);
            drawableOut.setBounds(0, 0, drawableOut.getMinimumWidth(), drawableOut.getMinimumHeight());
            studentsHolder.tv_signin_item_out_time.setCompoundDrawables(drawableOut, null, null, null);
        }
        studentsHolder.itemView.setOnClickListener(this);

        if (bean.getStatus() == 1) {
            studentsHolder.tvSigninLogCancle.setVisibility(View.VISIBLE);
        } else {
            studentsHolder.tvSigninLogCancle.setVisibility(View.GONE);
        }

        Drawable drawableIn = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_sign_in_selected);
        drawableIn.setBounds(0, 0, drawableIn.getMinimumWidth(), drawableIn.getMinimumHeight());
        ((StudentsHolder) holder).tv_signin_item_in_time.setCompoundDrawables(drawableIn, null, null, null);

        Drawable drawableOut = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_sign_out_confirm);
        drawableOut.setBounds(0, 0, drawableOut.getMinimumWidth(), drawableOut.getMinimumHeight());
        ((StudentsHolder) holder).tv_signin_item_out_time.setCompoundDrawables(drawableOut, null, null, null);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    @Override public void onClick(View view) {
        if (listener != null) {
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
        @BindView(R.id.iv_signin_item_student_face) ImageView iv_signin_item_student_face;
        @BindView(R.id.tv_signin_item_student_name) TextView tv_signin_item_student_name;
        @BindView(R.id.tv_signin_out_time) TextView tv_signin_item_in_time;
        @BindView(R.id.tv_signin_item_out_time) TextView tv_signin_item_out_time;
        @BindView(R.id.iv_signin_item_student_gender) ImageView iv_signin_item_student_gender;
        @BindView(R.id.tv_signin_log_cancle) TextView tvSigninLogCancle;

        public StudentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
