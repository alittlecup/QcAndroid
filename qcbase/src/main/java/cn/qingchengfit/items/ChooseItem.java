package cn.qingchengfit.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseItem extends AbstractFlexibleItem<ChooseItem.ChooseVH> {

    public String textStr;

    public ChooseItem(String text) {
        this.textStr = text;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose;
    }

    @Override public ChooseVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseVH(view,adapter);
    }


    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseVH holder, int position, List payloads) {
        holder.text.setText(textStr);
        //        adapter.animateView(holder.itemView, position, adapter.isSelected(position));
        if (adapter.isSelected(position)) {
            holder.chooseImg.setVisibility(View.VISIBLE);
        } else {
            holder.chooseImg.setVisibility(View.GONE);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseVH extends FlexibleViewHolder {
	TextView text;
	ImageView chooseImg;

        public ChooseVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          text = (TextView) view.findViewById(R.id.text);
          chooseImg = (ImageView) view.findViewById(R.id.choose_img);
        }
    }
}