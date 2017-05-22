package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class HintItem extends AbstractFlexibleItem<HintItem.HintVH> {

    String text;
    int resBg = R.color.bg_grey;

    private HintItem(Builder builder) {
        text = builder.text;
        resBg = builder.resBg;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_hint;
    }

    @Override public HintVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new HintVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, HintVH holder, int position, List payloads) {
        holder.hintText.setText(text);
        holder.hintText.setBackgroundResource(resBg);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static final class Builder {
        private String text;
        private int resBg;

        public Builder() {
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder resBg(int val) {
            resBg = val;
            return this;
        }

        public HintItem build() {
            return new HintItem(this);
        }
    }

    public class HintVH extends FlexibleViewHolder {
        @BindView(R.id.hint_text) TextView hintText;

        public HintVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}