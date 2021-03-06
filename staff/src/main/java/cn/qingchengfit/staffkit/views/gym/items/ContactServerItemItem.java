package cn.qingchengfit.staffkit.views.gym.items;

import android.view.View;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ContactServerItemItem extends AbstractFlexibleItem<ContactServerItemItem.ContactServerItemVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_contact_qingcheng;
    }

    @Override public ContactServerItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ContactServerItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ContactServerItemVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ContactServerItemVH extends FlexibleViewHolder {

        public ContactServerItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);

        }
    }
}