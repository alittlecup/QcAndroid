package cn.qingchengfit.saasbase.coach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.listener.OnRecycleItemClickListener;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
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
 * Created by Paper on 16/5/11 2016.
 */
public class CoachAapter extends RecyclerView.Adapter<CoachAapter.CoachVh> implements View.OnClickListener {

    List<Staff> datas = new ArrayList<>();
    private OnRecycleItemClickListener listener;
    private String trainerId;

    public CoachAapter(List<Staff> datas) {
        this.datas = datas;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    @Override public CoachVh onCreateViewHolder(ViewGroup parent, int viewType) {
        CoachVh vh = new CoachVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saas_staff, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(CoachVh holder, int position) {
        holder.itemView.setTag(position);
        Staff coach = datas.get(position);
        holder.itemStudentName.setText(coach.username);
        holder.itemStudentPhonenum.setText(coach.phone);
        //holder.itemStudentAlpha.setVisibility(View.GONE);
        if (coach.gender == 0) {
            holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
        } else {
            holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
        }
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(coach.avatar))
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemStudentHeader, holder.itemView.getContext()));
        if (!StringUtils.isEmpty(trainerId)) {
            if (coach.getId().equalsIgnoreCase(trainerId)) {
                holder.iconRight.setVisibility(View.VISIBLE);
                holder.iconRight.setImageResource(R.drawable.ic_green_right);
            } else {
                holder.iconRight.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.iconRight.setVisibility(View.VISIBLE);
            holder.iconRight.setImageResource(R.drawable.ic_arrow_right);
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class CoachVh extends RecyclerView.ViewHolder {
        //@BindView(R2.id.item_student_alpha) TextView itemStudentAlpha;
	ImageView itemStudentHeader;
	RelativeLayout itemStudentHeaderLoop;
	TextView itemStudentName;
	ImageView itemStudentGender;
	TextView itemStudentPhonenum;
	TextView itemStudentGymname;
	ImageView iconRight;

        public CoachVh(View itemView) {
            super(itemView);
          itemStudentHeader = (ImageView) itemView.findViewById(R.id.item_student_header);
          itemStudentHeaderLoop =
              (RelativeLayout) itemView.findViewById(R.id.item_student_header_loop);
          itemStudentName = (TextView) itemView.findViewById(R.id.item_student_name);
          itemStudentGender = (ImageView) itemView.findViewById(R.id.item_student_gender);
          itemStudentPhonenum = (TextView) itemView.findViewById(R.id.item_student_phonenum);
          itemStudentGymname = (TextView) itemView.findViewById(R.id.item_student_gymname);
          iconRight = (ImageView) itemView.findViewById(R.id.icon_right);
        }
    }
}
