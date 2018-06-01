package cn.qingchengfit.saasbase.course.course.items;

import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JacketAddItem extends AbstractFlexibleItem<JacketAddItem.JacketAddVH> {
    public JacketAddItem() {
        setDraggable(false);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public JacketAddVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new JacketAddVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, JacketAddVH holder, int position, List payloads) {

    }

    @Override public int getLayoutRes() {
        return R.layout.item_jacket_add;
    }

    public static class JacketAddVH extends FlexibleViewHolder {
	TextView addImg;

        public JacketAddVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          addImg = (TextView) view.findViewById(R.id.add_img);
        }
    }
}