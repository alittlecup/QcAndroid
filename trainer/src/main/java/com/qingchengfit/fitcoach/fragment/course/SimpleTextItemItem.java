package com.qingchengfit.fitcoach.fragment.course;

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

public class SimpleTextItemItem extends AbstractFlexibleItem<SimpleTextItemItem.SimpleTextItemVH> {

    private String text;

    public SimpleTextItemItem(String text) {
        this.text = text;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_simple_text;
    }

    @Override public SimpleTextItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SimpleTextItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SimpleTextItemVH holder, int position, List payloads) {
        holder.itemText.setText(text);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SimpleTextItemVH extends FlexibleViewHolder {

        @BindView(R.id.item_text) TextView itemText;

        public SimpleTextItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}