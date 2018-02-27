package cn.qingchengfit.staffkit.views.wardrobe.item;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class LockerHeaderItem extends AbstractFlexibleItem<LockerHeaderItem.LockerHeaderVH> {

    LockerRegion lockerRegion;

    public LockerHeaderItem(LockerRegion lockerRegion) {
        this.lockerRegion = lockerRegion;
    }

    public LockerRegion getLockerRegion() {
        return lockerRegion;
    }

    public void setLockerRegion(LockerRegion lockerRegion) {
        this.lockerRegion = lockerRegion;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_locker_header;
    }

    @Override public LockerHeaderVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new LockerHeaderVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, LockerHeaderVH holder, int position, List payloads) {
        holder.text.setText(lockerRegion.name);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class LockerHeaderVH extends FlexibleViewHolder {
        @BindView(R.id.text) TextView text;

        public LockerHeaderVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}