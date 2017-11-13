package com.qingchengfit.fitcoach.items;

import android.support.v7.widget.SwitchCompat;
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
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.ArrayList;
import java.util.List;

public class AccountExpendItemItem extends AbstractFlexibleItem<AccountExpendItemItem.AccountExpendItemVH>
    implements IExpandable<AccountExpendItemItem.AccountExpendItemVH, AccountSubItem>, IHeader<AccountExpendItemItem.AccountExpendItemVH> {

    Card_tpl mCard_tpl;

    private List<AccountSubItem> mSubItems = new ArrayList<>();
    private boolean isExpended = false;

    public AccountExpendItemItem(Card_tpl card_tpl, int x) {
        mCard_tpl = card_tpl;
        for (int i = 0; i < x; i++) {
            addItem(new AccountSubItem(this, card_tpl.getId() + "_" + x));
        }
    }

    public Card_tpl getCard_tpl() {
        return mCard_tpl;
    }

    public void setCard_tpl(Card_tpl card_tpl) {
        mCard_tpl = card_tpl;
    }

    @Override public int getLayoutRes() {
        return R.layout.layout_account_expend;
    }

    @Override public AccountExpendItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AccountExpendItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AccountExpendItemVH holder, int position, List payloads) {
        holder.label.setText(mCard_tpl.getName());
        holder.switcher.setChecked(isExpended);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public boolean isExpanded() {
        return isExpended;
    }

    @Override public void setExpanded(boolean expanded) {
        this.isExpended = expanded;
    }

    @Override public int getExpansionLevel() {
        return 0;
    }

    public void addItem(AccountSubItem item) {
        mSubItems.add(item);
    }

    @Override public List<AccountSubItem> getSubItems() {
        return mSubItems;
    }

    public class AccountExpendItemVH extends ExpandableViewHolder {
        @BindView(R.id.label) TextView label;
        @BindView(R.id.switcher) SwitchCompat switcher;

        public AccountExpendItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override protected boolean isViewExpandableOnClick() {
            return true;
        }

        @Override protected void expandView(int position) {
            super.expandView(position);
            //Let's notify the item has been expanded
            if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

        @Override protected void collapseView(int position) {
            super.collapseView(position);
            //Let's notify the item has been collapsed
            if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }
    }
}