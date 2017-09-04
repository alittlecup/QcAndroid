package cn.qingchengfit.items;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class TagItem extends AbstractFlexibleItem<TagItem.TagVH> {

    String content;
    int textSize;
    int color;


    public TagItem(String content, int color) {
        this.content = content;
        this.color = color;
    }

    public TagItem(String content, int textSize, int color) {
        this.content = content;
        this.textSize = textSize;
        this.color = color;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_tag;
    }

    @Override public TagVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new TagVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, TagVH holder, int position, List payloads) {
        holder.tv.setText(content);
        holder.tv.setTextColor(color);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(1,color);
        drawable.setCornerRadius(4);
        holder.tv.setBackground(drawable);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class TagVH extends FlexibleViewHolder {
        TextView tv;
        public TagVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            tv = (TextView)view.findViewById(R.id.item_txt);
        }
    }
}