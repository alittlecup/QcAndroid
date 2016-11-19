package com.qingchengfit.fitcoach.fragment.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;



public class SimpleTextItemItem extends AbstractFlexibleItem<SimpleTextItemItem.SimpleTextItemVH> {

    private String text;

    public SimpleTextItemItem(String text) {
        this.text = text;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_simple_text;
    }

    @Override
    public SimpleTextItemVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleTextItemVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SimpleTextItemVH holder, int position, List payloads) {
        holder.itemText.setText(text);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class SimpleTextItemVH extends FlexibleViewHolder {

        @BindView(R.id.item_text)
        TextView itemText;

        public SimpleTextItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}