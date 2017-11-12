package cn.qingchengfit.saasbase.student.items;

import android.view.View;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.utils.StudentBusinessUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class SalesDetailItem extends StudentItem {
    public SalesDetailItem(QcStudentBean qcStudentBean) {
        super(qcStudentBean);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        if(qcStudentBean.sellers==null||qcStudentBean.sellers.size()==0){
            holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_saleids, " ")
            );
        }else{
            holder.itemStudentGymname.setText(
                    holder.itemView.getContext().getString(R.string.qc_student_saleids, StudentBusinessUtils.getNamesStrFromStaffs(qcStudentBean.sellers))
            );
        }
        holder.itemStudentGymname.setVisibility(View.VISIBLE);
    }
}
