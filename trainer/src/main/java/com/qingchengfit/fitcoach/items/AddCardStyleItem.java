package com.qingchengfit.fitcoach.items;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
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

    @Override public AddCardStyleVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AddCardStyleVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AddCardStyleVH holder, int position, List payloads) {
        holder.tv.setText(txt);
      holder.tv.setCompoundDrawablesWithIntrinsicBounds(
          ContextCompat.getDrawable(holder.tv.getContext(), R.drawable.vd_add), null, null,
            null);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AddCardStyleVH extends FlexibleViewHolder {
	TextView tv;

        public AddCardStyleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}