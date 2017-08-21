package cn.qingchengfit.saasbase.staff.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/12 2016.
 */
public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffVh> implements View.OnClickListener {

    List<StaffShip> datas;
    private OnRecycleItemClickListener listener;

    public StaffAdapter(List<StaffShip> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public StaffVh onCreateViewHolder(ViewGroup parent, int viewType) {
        StaffVh vh = new StaffVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(StaffVh holder, int position) {
        holder.itemView.setTag(position);
        Staff staff = datas.get(position).user;
        holder.itemStudentAlpha.setVisibility(View.GONE);
        holder.itemStudentName.setText(staff.getUsername());
        holder.itemStudentPhonenum.setText(staff.getPhone());
        holder.itemStudentGymname.setText(datas.get(position).position.name);

        holder.itemStudentGender.setImageResource(
            staff.getGender() == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(staff.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemStudentHeader, holder.itemView.getContext()));
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) listener.onItemClick(v, (int) v.getTag());
    }

    public class StaffVh extends RecyclerView.ViewHolder {

        @BindView(R2.id.item_student_alpha) TextView itemStudentAlpha;
        @BindView(R2.id.item_student_header) ImageView itemStudentHeader;
        @BindView(R2.id.item_student_header_loop) RelativeLayout itemStudentHeaderLoop;
        @BindView(R2.id.item_student_name) TextView itemStudentName;
        @BindView(R2.id.item_student_gender) ImageView itemStudentGender;
        @BindView(R2.id.item_student_phonenum) TextView itemStudentPhonenum;
        @BindView(R2.id.item_student_gymname) TextView itemStudentGymname;

        public StaffVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
