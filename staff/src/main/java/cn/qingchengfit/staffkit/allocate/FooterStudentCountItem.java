package cn.qingchengfit.staffkit.allocate;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/5/2.
 */

public class FooterStudentCountItem extends AbstractFlexibleItem<FooterStudentCountItem.FooterStudentCountVH> {

    int count;

    public FooterStudentCountItem(int count) {
        this.count = count;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public FooterStudentCountVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new FooterStudentCountVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FooterStudentCountVH holder, int position, List payloads) {
        holder.tvTotalCount.setText(String.valueOf(count));
    }

    @Override public int getLayoutRes() {
        return R.layout.item_total_count_footer;
    }

    class FooterStudentCountVH extends FlexibleViewHolder {
	TextView tvTotalCount;

        public FooterStudentCountVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tvTotalCount = (TextView) view.findViewById(R.id.tv_total_count);
        }
    }
}