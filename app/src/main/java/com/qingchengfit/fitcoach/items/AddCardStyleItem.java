package com.qingchengfit.fitcoach.items;

import android.support.v4.content.ContextCompat;
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

public class AddCardStyleItem extends AbstractFlexibleItem<AddCardStyleItem.AddCardStyleVH> {
    String txt;

    public AddCardStyleItem(String txt) {
        this.txt = txt;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_card_add;
    }

    @Override public AddCardStyleVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AddCardStyleVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AddCardStyleVH holder, int position, List payloads) {
        holder.tv.setText(txt);
        holder.tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(holder.tv.getContext(),R.drawable.ic_add),null,null,null);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AddCardStyleVH extends FlexibleViewHolder {
        @BindView(R.id.tv) TextView tv;

        public AddCardStyleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}