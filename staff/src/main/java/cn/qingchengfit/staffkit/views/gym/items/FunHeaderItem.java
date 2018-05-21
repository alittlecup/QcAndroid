package cn.qingchengfit.staffkit.views.gym.items;

import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FunHeaderItem extends AbstractHeaderItem<FunHeaderItem.FunHeaderVH> implements
  IHeader<FunHeaderItem.FunHeaderVH>{

    public String txt;

    public FunHeaderItem(String txt) {
        setHidden(false);
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_fun_header;
    }

    @Override public FunHeaderVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new FunHeaderVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FunHeaderVH holder, int position, List payloads) {
        holder.title.setText(txt);
    }

    @Override public boolean equals(Object o) {
        return o instanceof FunHeaderItem && ((FunHeaderItem) o).getTxt().equalsIgnoreCase(txt);
    }

    public class FunHeaderVH extends FlexibleViewHolder {
	TextView title;

        public FunHeaderVH(View view, FlexibleAdapter adapter) {
            super(view, adapter ,true);
          title = (TextView) view.findViewById(R.id.title);
        }
    }
}