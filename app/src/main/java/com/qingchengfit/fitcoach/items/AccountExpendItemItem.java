package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AccountExpendItemItem extends AbstractFlexibleItem<AccountExpendItemItem.AccountExpendItemVH> {

    @Override public int getLayoutRes() {
        return R.layout.layout_account_expend;
    }

    @Override public AccountExpendItemVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AccountExpendItemVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AccountExpendItemVH holder, int position, List payloads) {

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AccountExpendItemVH extends FlexibleViewHolder {

        public AccountExpendItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}