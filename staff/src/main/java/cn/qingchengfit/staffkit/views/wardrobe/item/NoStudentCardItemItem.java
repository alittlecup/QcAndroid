package cn.qingchengfit.staffkit.views.wardrobe.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class NoStudentCardItemItem extends AbstractFlexibleItem<NoStudentCardItemItem.NoStudentCardItemVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_no_student_card;
    }

    @Override public NoStudentCardItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new NoStudentCardItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, NoStudentCardItemVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class NoStudentCardItemVH extends FlexibleViewHolder {

        public NoStudentCardItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}