package cn.qingchengfit.saasbase.course.course.items;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JackTitleItem extends AbstractFlexibleItem<JackTitleItem.JacketTitleVH> {

    public JackTitleItem() {
        setDraggable(false);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public JacketTitleVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new JacketTitleVH(view, adapter);
    }

    @Override public int getLayoutRes() {
        return R.layout.item_jacket_manage_title;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, JacketTitleVH holder, int position, List payloads) {

    }

    public static class JacketTitleVH extends FlexibleViewHolder {

        public JacketTitleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
        }
    }
}