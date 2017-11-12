package cn.qingchengfit.saasbase.student.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class CoachChooseItem extends CoachDetailItem {
    public CoachChooseItem(QcStudentBean qcStudentBean) {
        super(qcStudentBean);
    }
    @Override public StudentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                ViewGroup parent) {
        StudentVH vh = super.createViewHolder(adapter, inflater, parent);
        vh.cb.setVisibility(View.VISIBLE);
        return vh;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position,
                                         List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        holder.cb.setChecked(adapter.isSelected(position));
    }
}
