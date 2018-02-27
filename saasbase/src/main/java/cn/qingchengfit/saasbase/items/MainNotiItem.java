package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class MainNotiItem extends AbstractFlexibleItem<MainNotiItem.MainNotiVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_system_msg;
    }

    @Override public MainNotiVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new MainNotiVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MainNotiVH holder, int position, List payloads) {

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class MainNotiVH extends FlexibleViewHolder {
        @BindView(R2.id.item_img) ImageView itemImg;
        @BindView(R2.id.item_red_dogt) ImageView itemRedDogt;
        @BindView(R2.id.item_name) TextView itemName;
        @BindView(R2.id.item_time) TextView itemTime;
        @BindView(R2.id.item_msg_tag) TextView itemMsgTag;
        @BindView(R2.id.item_msg_content) TextView itemMsgContent;

        public MainNotiVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}