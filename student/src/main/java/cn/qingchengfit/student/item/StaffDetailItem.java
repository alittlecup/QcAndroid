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
        if(type==0){
            if (getQcStudentBean().sellers == null || getQcStudentBean().sellers.size() == 0) {
                holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_saleids , " ")
                );
            } else {
                holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_saleids , StudentBusinessUtils.getNamesStrFromStaffs(getQcStudentBean().sellers))
                );
            }
        }else if(type==1){
            if (getQcStudentBean().coaches == null || getQcStudentBean().coaches.size() == 0) {
                holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_coachids , " ")
                );
            } else {
                holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_coachids , StudentBusinessUtils.getNamesStrFromStaffs(getQcStudentBean().coaches))
                );
            }
        }
        holder.itemStudentGymname.setVisibility(View.VISIBLE);
    }
}
