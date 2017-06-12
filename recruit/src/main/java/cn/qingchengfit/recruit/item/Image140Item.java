package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class Image140Item extends AbstractFlexibleItem<Image140Item.Image140VH> {

    String url;

    public Image140Item(String url) {
        this.url = url;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_resume_imgs_show_item;
    }

    @Override public Image140VH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new Image140VH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, Image140VH holder, int position, List payloads) {
        PhotoUtils.middle(holder.img, url);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class Image140VH extends FlexibleViewHolder {
        @BindView(R2.id.img) ImageView img;

        public Image140VH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}