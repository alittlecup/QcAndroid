package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AccountSubItem extends AbstractFlexibleItem<AccountSubItem.AccountSubVH>
    implements ISectionable<AccountSubItem.AccountSubVH, IHeader> {
    private static final long serialVersionUID = 2519281529221244210L;

    String id;
    private IHeader mIHeader;

    public AccountSubItem(IHeader IHeader, String i) {
        mIHeader = IHeader;
        this.id = i;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_account_sub;
    }

    @Override public AccountSubVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AccountSubVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AccountSubVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        if (o instanceof AccountSubItem) {
            return ((AccountSubItem) o).id.equalsIgnoreCase(this.id);
        } else {
            return false;
        }
    }

    @Override public IHeader getHeader() {
        return mIHeader;
    }

    @Override public void setHeader(IHeader header) {
        mIHeader = header;
    }

    public class AccountSubVH extends FlexibleViewHolder {

        public AccountSubVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}