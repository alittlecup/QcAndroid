package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AddBatchCircleItem extends AbstractFlexibleItem<AddBatchCircleItem.AddBatchCircleVH> {

    public String text;

    public AddBatchCircleItem(String text) {
        this.text = text;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_add_course_batch_circle;
    }

    @Override public AddBatchCircleVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AddBatchCircleVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AddBatchCircleVH holder, int position, List payloads) {
        holder.text.setText(text);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AddBatchCircleVH extends FlexibleViewHolder {
        @BindView(R.id.text) TextView text;

        public AddBatchCircleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}