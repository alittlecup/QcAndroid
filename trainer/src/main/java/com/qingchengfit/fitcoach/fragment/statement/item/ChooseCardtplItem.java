package com.qingchengfit.fitcoach.fragment.statement.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.model.base.Card_tpl;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseCardtplItem extends AbstractFlexibleItem<ChooseCardtplItem.ChooseStudentVH> {

    public Card_tpl mCard_tpl;

    public ChooseCardtplItem(Card_tpl card_tpl) {
        mCard_tpl = card_tpl;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_student;
    }

    @Override public ChooseStudentVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStudentVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemText.setText(mCard_tpl.getName());
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