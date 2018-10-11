package com.qingchengfit.fitcoach.fragment.statement.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.model.base.QcStudentBean;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseStudentItem extends AbstractFlexibleItem<ChooseStudentItem.ChooseStudentVH> {

    public QcStudentBean mQcStudentBean;

    public ChooseStudentItem(QcStudentBean qcStudentBean) {
        mQcStudentBean = qcStudentBean;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemText.setText(mQcStudentBean.username);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseStudentVH extends FlexibleViewHolder {
	TextView itemText;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemText = (TextView) view.findViewById(R.id.item_text);
        }
    }
}