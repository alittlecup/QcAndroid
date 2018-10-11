package cn.qingchengfit.saasbase.student.items;

import android.view.View;

import cn.qingchengfit.saascommon.item.StudentItem;
import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class CoachDetailItem extends StudentItem {
    public CoachDetailItem(QcStudentBean qcStudentBean) {
        super(qcStudentBean);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        //if (qcStudentBean instanceof QcStudentWithCoach) {
        //    if (((QcStudentWithCoach) qcStudentBean).coaches == null || ((QcStudentWithCoach) qcStudentBean).coaches.size() == 0) {
        //        holder.itemStudentGymname.setText(
        //                holder.itemView.getContext().getString(R.string.qc_student_coachids, " ")
        //        );
        //    } else {
        //        holder.itemStudentGymname.setText(
        //                holder.itemView.getContext().getString(R.string.qc_student_coachids, StudentBusinessUtils.getNamesStrFromStaffs(((QcStudentWithCoach) qcStudentBean).coaches))
        //        );
        //    }
        //    holder.itemStudentGymname.setVisibility(View.VISIBLE);
        //}
    }
}
