package cn.qingchengfit.saasbase.mvvm_student.items;

import android.view.View;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.saasbase.student.utils.StudentBusinessUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class StaffDetailItem extends StudentItem {
    private Integer type;

    public StaffDetailItem(QcStudentBean qcStudentBean, Integer type) {
        super(qcStudentBean);
        this.type = type;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        int stringRes = type == 0 ? R.string.qc_student_saleids : R.string.qc_student_coachids;
        if (getQcStudentBean().sellers == null || getQcStudentBean().sellers.size() == 0) {
            holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(stringRes, " ")
            );
        } else {
            holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(stringRes, StudentBusinessUtils.getNamesStrFromStaffs(getQcStudentBean().sellers))
            );
        }
        holder.itemStudentGymname.setVisibility(View.VISIBLE);
    }
}
