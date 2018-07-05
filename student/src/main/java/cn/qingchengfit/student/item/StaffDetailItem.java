package cn.qingchengfit.student.item;

import android.view.View;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

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
