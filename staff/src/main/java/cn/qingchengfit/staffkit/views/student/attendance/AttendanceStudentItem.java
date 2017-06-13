package cn.qingchengfit.staffkit.views.student.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Absentce;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.EventContactUser;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AttendanceStudentItem extends AbstractFlexibleItem<AttendanceStudentItem.AttendanceStudentVH> {

    private Absentce data;

    public AttendanceStudentItem(Absentce data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_student_attendance;
    }

    @Override public AttendanceStudentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AttendanceStudentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AttendanceStudentVH holder, int position, List payloads) {
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

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AttendanceStudentVH extends FlexibleViewHolder {
        @BindView(R.id.item_person_header) ImageView itemPersonHeader;
        @BindView(R.id.item_person_header_loop) RelativeLayout itemPersonHeaderLoop;
        @BindView(R.id.item_person_name) TextView itemPersonName;
        @BindView(R.id.item_person_gender) ImageView itemPersonGender;
        @BindView(R.id.tv_referrer_count) TextView tvReferrerCount;
        @BindView(R.id.item_person_desc) TextView itemPersonDesc;
        @BindView(R.id.tv_student_contact_ta) TextView tvStudentContactTa;
        @BindView(R.id.tv_student_title) TextView tvStudentTitle;
        @BindView(R.id.tv_student_date) TextView tvStudentDate;
        @BindView(R.id.text_absence_days) TextView tvAbsenceDay;

        public AttendanceStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            tvStudentContactTa.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (data != null) RxBus.getBus().post(new EventContactUser(data.user));
                }
            });
        }
    }
}