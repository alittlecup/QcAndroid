package cn.qingchengfit.items;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ActionDescItem extends AbstractFlexibleItem<ActionDescItem.ActionDescVH> {

    int action;
    String title, desc;
    @DrawableRes
    int icon;
    boolean clickable = true;

    private ActionDescItem(Builder builder) {
        setAction(builder.action);
        title = builder.title;
        desc = builder.desc;
        icon = builder.icon;
        clickable = builder.clickable;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_action_desc;
    }

    @Override
    public ActionDescVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ActionDescVH(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ActionDescVH holder, int position,
                               List payloads) {
        holder.imgAction.setImageResource(icon);
        holder.title.setText(title);
        holder.desc.setText(desc);
        holder.imgRight.setVisibility(clickable ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ActionDescItem) {
            return ((ActionDescItem) o).getAction() == action;
        } else return false;
    }

    public class ActionDescVH extends FlexibleViewHolder {
        ImageView imgAction;
        TextView title;
        TextView desc;
        ImageView imgRight;

        public ActionDescVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            imgAction = view.findViewById(R.id.img_action);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.tv_desc);
            imgRight = view.findViewById(R.id.img_right);
        }
    }

    public static final class Builder {
        private int action;
        private String title;
        private String desc;
        private int icon;
        private boolean clickable = true;

        public Builder() {
        }

        public Builder action(int val) {
            action = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder desc(String val) {
            desc = val;
            return this;
        }

        public Builder icon(int val) {
            icon = val;
            return this;
        }

        public Builder clickable(boolean val) {
            clickable = val;
            return this;
        }

        public ActionDescItem build() {
            return new ActionDescItem(this);
        }
    }
}