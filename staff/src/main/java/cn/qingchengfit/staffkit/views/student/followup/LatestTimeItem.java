package cn.qingchengfit.staffkit.views.student.followup;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
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

public class LatestTimeItem extends AbstractFlexibleItem<LatestTimeItem.LatestTimeItemVH> {

    private String data;
    private boolean isSelect;

    public LatestTimeItem(String sourceBean) {
        this.data = sourceBean;
    }

    public LatestTimeItem(String data, boolean isSelect) {
        this.isSelect = isSelect;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_source;
    }

    @Override
    public LatestTimeItem.LatestTimeItemVH createViewHolder(final View view, FlexibleAdapter adapter) {
        return new LatestTimeItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, LatestTimeItem.LatestTimeItemVH holder, int position, List payloads) {
        holder.imgHook.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
        holder.tvSourceName.setText(data);
        holder.tvSourceName.setTextColor(
            ContextCompat.getColor(holder.imgHook.getContext(), adapter.isSelected(position) ? R.color.colorPrimary : R.color.text_black));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class LatestTimeItemVH extends FlexibleViewHolder {
	TextView tvSourceName;
	ImageView imgHook;

        public LatestTimeItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tvSourceName = (TextView) view.findViewById(R.id.tv_source_name);
          imgHook = (ImageView) view.findViewById(R.id.img_hook);
        }
    }
}
