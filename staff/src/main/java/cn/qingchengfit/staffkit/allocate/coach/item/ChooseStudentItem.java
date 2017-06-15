package cn.qingchengfit.staffkit.allocate.coach.item;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * Created by fb on 2017/5/3.
 */

public class ChooseStudentItem extends CommonAllocateDetailItem<ChooseStudentItem.ChooseStudentVH> {

    private StudentWithCoach data;

    public ChooseStudentItem(StudentWithCoach data) {
        super(data);
        this.data = data;
    }

    public StudentWithCoach getData() {
        return data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_coach_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChooseStudentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        holder.itemCheckbox.setChecked(adapter.isSelected(position));
        if (data.coaches == null || (data.coaches != null && data.coaches.size() == 0)) {
            holder.itemPersonDesc.setVisibility(View.GONE);
        } else {
            holder.itemPersonDesc.setVisibility(View.VISIBLE);
            holder.itemPersonDesc.setText(
                holder.itemView.getContext().getString(R.string.coach_list, StringUtils.coachesNames(data.coaches)));
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {

    }

    class ChooseStudentVH extends CommonAllocateDetailItem.AllocateDetailVH {

        @BindView(R.id.item_student_modify_alpha) TextView itemStudentModifyAlpha;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.item_person_desc) TextView itemPersonDesc;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}


