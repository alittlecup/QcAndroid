package cn.qingchengfit.items;

import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CmBottomListItem extends AbstractFlexibleItem<CmBottomListItem.CmBottomListVH> {

    int gravity = Gravity.CENTER;
    @ColorRes int color;
    String content;

    public CmBottomListItem(String content) {
        this.content = content;
    }

    public CmBottomListItem(int gravity, int color, String content) {
        this.gravity = gravity;
        this.color = color;
        this.content = content;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_cm_bottom_list;
    }

    @Override public CmBottomListVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CmBottomListVH(view,adapter);
    }


    @Override public void bindViewHolder(FlexibleAdapter adapter, CmBottomListVH holder, int position, List payloads) {
        holder.tv.setGravity(gravity);
        holder.tv.setText(content);
        if (color != 0) {
            holder.tv.setTextColor(CompatUtils.getColor(holder.tv.getContext(), color));
        } else {
            holder.tv.setTextColor(CompatUtils.getColor(holder.tv.getContext(), R.color.text_dark));
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class CmBottomListVH extends FlexibleViewHolder {
        @BindView(R2.id.tv) TextView tv;

        public CmBottomListVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}