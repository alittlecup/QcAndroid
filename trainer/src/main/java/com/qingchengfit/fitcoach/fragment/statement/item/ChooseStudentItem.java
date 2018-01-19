package com.qingchengfit.fitcoach.fragment.statement.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
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
        @BindView(R.id.item_text) TextView itemText;

        public ChooseStudentVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}