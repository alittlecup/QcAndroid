package cn.qingchengfit.student.items;

import android.view.View;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/12/1.
 */

public class ChooseStaffItem extends StaffDetailItem {
    public ChooseStaffItem(QcStudentBean qcStudentBean, Integer type) {
        super(qcStudentBean, type);
    }

    @Override
    public StudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        StudentVH vh = super.createViewHolder(view, adapter);
        vh.cb.setVisibility(View.VISIBLE);
        return vh;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position,
                               List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        holder.cb.setChecked(adapter.isSelected(position));
    }
}
