package cn.qingchengfit.staffkit.allocate.coach.item;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.CommonAllocateDetailItem;
import cn.qingchengfit.staffkit.allocate.coach.model.StudentWithCoach;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/5/4.
 */

public class CoachStudentDetailItem extends CommonAllocateDetailItem<CoachStudentDetailItem.CoachStudentDetailVH> {

    private StudentWithCoach studentBean;

    public CoachStudentDetailItem(StudentWithCoach studentBean) {
        super(studentBean);
        this.studentBean = studentBean;
    }

    public StudentWithCoach getData() {
        return studentBean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_coach_student_detail;
    }

    @Override public CoachStudentDetailVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CoachStudentDetailVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CoachStudentDetailVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        if (studentBean.coaches == null || (studentBean.coaches != null && studentBean.coaches.size() == 0)) {
            holder.itemPersonDesc.setVisibility(View.GONE);
        } else {
            holder.itemPersonDesc.setVisibility(View.VISIBLE);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.coach_list, StringUtils.coachesNames(studentBean.coaches)));
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {

    }

    class CoachStudentDetailVH extends CommonAllocateDetailItem.AllocateDetailVH {

        @BindView(R.id.item_person_desc) TextView itemPersonDesc;

        public CoachStudentDetailVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
