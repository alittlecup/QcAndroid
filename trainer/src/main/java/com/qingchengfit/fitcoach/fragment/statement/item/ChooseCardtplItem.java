package com.qingchengfit.fitcoach.fragment.statement.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override public ChooseStudentVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChooseStudentVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseStudentVH holder, int position, List payloads) {
        holder.itemText.setText(mCard_tpl.getName());
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