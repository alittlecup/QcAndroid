package cn.qingchengfit.staffkit.views.course;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SimpleTextItemItem extends AbstractFlexibleItem<SimpleTextItemItem.SimpleTextItemVH> {

    float textSize = 0;
    int gravity = Gravity.CENTER_VERTICAL;
    private String text;

    public SimpleTextItemItem(String text) {
        this.text = text;
    }

    public SimpleTextItemItem(String text, float textSize) {
        this.textSize = textSize;
        this.text = text;
    }

    public SimpleTextItemItem(String text, int g) {
        this.text = text;
        gravity = g;
    }

    public String getText() {
        return text;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_simple_text;
    }

    @Override public SimpleTextItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SimpleTextItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SimpleTextItemVH holder, int position, List payloads) {
        holder.itemText.setText(text);
        if (textSize > 0) holder.itemText.setTextSize(textSize);
        holder.itemText.setGravity(gravity);
    }

    @Override public boolean equals(Object o) {
        return o instanceof SimpleTextItemItem && ((SimpleTextItemItem) o).getText().equalsIgnoreCase(text);
    }

    public class SimpleTextItemVH extends FlexibleViewHolder {

        @BindView(cn.qingchengfit.staffkit.R.id.item_text) TextView itemText;

        public SimpleTextItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}