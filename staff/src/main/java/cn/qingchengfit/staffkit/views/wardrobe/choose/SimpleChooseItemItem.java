package cn.qingchengfit.staffkit.views.wardrobe.choose;

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

public class SimpleChooseItemItem extends AbstractFlexibleItem<SimpleChooseItemItem.SimpleChooseItemVH> {

    private LockerRegion lockerRegion;

    public SimpleChooseItemItem(LockerRegion lockerRegion) {
        this.lockerRegion = lockerRegion;
    }

    public LockerRegion getLockerRegion() {
        return lockerRegion;
    }

    public void setLockerRegion(LockerRegion lockerRegion) {
        this.lockerRegion = lockerRegion;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_simple_text;
    }

    @Override public SimpleChooseItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SimpleChooseItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SimpleChooseItemVH holder, int position, List payloads) {
        holder.itemText.setText(lockerRegion.name);
        //        holder.itemText.setTextColor();
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SimpleChooseItemVH extends FlexibleViewHolder {
        @BindView(R.id.item_text) TextView itemText;

        public SimpleChooseItemVH(View view, FlexibleAdapter adapter) {

            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}