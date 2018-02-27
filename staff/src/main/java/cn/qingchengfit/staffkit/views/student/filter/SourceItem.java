package cn.qingchengfit.staffkit.views.student.filter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CompatUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/2.
 */

public class SourceItem extends AbstractFlexibleItem<SourceItem.SourceItemVH> {

    private StudentSourceBean data;

    public SourceItem(StudentSourceBean sourceBean) {
        this.data = sourceBean;
    }

    public StudentSourceBean getData() {
        return data;
    }

    public void setData(StudentSourceBean data) {
        this.data = data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_source;
    }

    @Override public SourceItem.SourceItemVH createViewHolder(final View view, FlexibleAdapter adapter) {
        return new SourceItem.SourceItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SourceItem.SourceItemVH holder, int position, List payloads) {
        holder.itemView.setTag(position);

        holder.tvSourceName.setTextColor(CompatUtils.getColor(holder.itemView.getContext(),
            adapter.isSelected(position) ? R.color.qc_theme_green : R.color.qc_text_black));
        holder.tvSourceName.setText(data.name);
        holder.imgHook.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SourceItemVH extends FlexibleViewHolder {
        @BindView(R.id.tv_source_name) TextView tvSourceName;
        @BindView(R.id.img_hook) ImageView imgHook;

        public SourceItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
