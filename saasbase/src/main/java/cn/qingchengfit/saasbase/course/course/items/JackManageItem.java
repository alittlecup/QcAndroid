package cn.qingchengfit.saasbase.course.course.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class JackManageItem extends AbstractFlexibleItem<JackManageItem.JackManageVH> {

    String img;

    public JackManageItem(String img) {
        this.img = img;
        setDraggable(true);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_jacket_manage;
    }

    @Override public JackManageVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new JackManageVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, JackManageVH holder, int position, List payloads) {
        Glide.with(holder.getContentView().getContext()).load(PhotoUtils.getSmall(img)).into(holder.img);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public static class JackManageVH extends FlexibleViewHolder {
        @BindView(R2.id.delete) ImageView delete;
        @BindView(R2.id.img) ImageView img;
        @BindView(R2.id.drag_tag) ImageView dragTag;
        @BindView(R2.id.container) LinearLayout container;
        @BindView(R2.id.text) TextView textView;

        public JackManageVH(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            if (adapter.isHandleDragEnabled()) {
                setDragHandleView(dragTag);
            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    adapter.removeItem(getAdapterPosition());
                }
            });
        }
    }
}