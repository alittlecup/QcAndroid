package cn.qingchengfit.saasbase.student.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.utils.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class AttendanceStudentItem extends AbstractFlexibleItem<AttendanceStudentItem.AttendanceStudentVH> {

    private Absentce data;

    public AttendanceStudentItem(Absentce data) {
        this.data = data;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_student_attendance;
    }

    @Override
    public AttendanceStudentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AttendanceStudentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, AttendanceStudentVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
                .load(data.user.getAvatar())
                .asBitmap()
                .into(new CircleImgWrapper(holder.itemPersonHeader, holder.itemPersonHeader.getContext()));
        holder.itemPersonName.setText(data.user.getUsername());
        holder.itemPersonGender.setImageResource(
                data.user.gender == 1 ? R.drawable.ic_gender_signal_female : R.drawable.ic_gender_signal_male);
        holder.itemPersonDesc.setText(data.user.getPhone());
        holder.tvAbsenceDay.setText(data.absence + "");
        holder.tvStudentTitle.setText(data.title);
        holder.tvStudentDate.setText(data.date_and_time);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class AttendanceStudentVH extends FlexibleViewHolder {
        @BindView(R2.id.item_person_header)
        ImageView itemPersonHeader;
        @BindView(R2.id.item_person_header_loop)
        RelativeLayout itemPersonHeaderLoop;
        @BindView(R2.id.item_person_name)
        TextView itemPersonName;
        @BindView(R2.id.item_person_gender)
        ImageView itemPersonGender;
        @BindView(R2.id.tv_referrer_count)
        TextView tvReferrerCount;
        @BindView(R2.id.item_person_desc)
        TextView itemPersonDesc;
        @BindView(R2.id.tv_student_contact_ta)
        TextView tvStudentContactTa;
        @BindView(R2.id.tv_student_title)
        TextView tvStudentTitle;
        @BindView(R2.id.tv_student_date)
        TextView tvStudentDate;
        @BindView(R2.id.text_absence_days)
        TextView tvAbsenceDay;

        public AttendanceStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            // REFACTOR: 2017/11/14 拨打电话
//            tvStudentContactTa.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (data != null) RxBus.getBus().post(new EventContactUser(data.user));
//                }
//            });
        }
    }
}