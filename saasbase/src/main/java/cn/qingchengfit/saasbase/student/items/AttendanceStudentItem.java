package cn.qingchengfit.saasbase.student.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.utils.CircleImgWrapper;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

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
    public AttendanceStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AttendanceStudentVH(view, adapter);
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

        ImageView itemPersonHeader;

        RelativeLayout itemPersonHeaderLoop;

        TextView itemPersonName;

        ImageView itemPersonGender;

        TextView tvReferrerCount;

        TextView itemPersonDesc;

        TextView tvStudentContactTa;

        TextView tvStudentTitle;

        TextView tvStudentDate;

        TextView tvAbsenceDay;

        public AttendanceStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemPersonHeader = (ImageView) view.findViewById(R.id.item_person_header);
          itemPersonHeaderLoop = (RelativeLayout) view.findViewById(R.id.item_person_header_loop);
          itemPersonName = (TextView) view.findViewById(R.id.item_person_name);
          itemPersonGender = (ImageView) view.findViewById(R.id.item_person_gender);
          tvReferrerCount = (TextView) view.findViewById(R.id.tv_referrer_count);
          itemPersonDesc = (TextView) view.findViewById(R.id.item_person_desc);
          tvStudentContactTa = (TextView) view.findViewById(R.id.tv_student_contact_ta);
          tvStudentTitle = (TextView) view.findViewById(R.id.tv_student_title);
          tvStudentDate = (TextView) view.findViewById(R.id.tv_student_date);
          tvAbsenceDay = (TextView) view.findViewById(R.id.text_absence_days);

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