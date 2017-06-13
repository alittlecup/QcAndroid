package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class MainNotiItem extends AbstractFlexibleItem<MainNotiItem.MainNotiVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_system_msg;
    }

    @Override public MainNotiVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new MainNotiVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, MainNotiVH holder, int position, List payloads) {

    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class MainNotiVH extends FlexibleViewHolder {
        @BindView(R.id.item_img) ImageView itemImg;
        @BindView(R.id.item_red_dogt) ImageView itemRedDogt;
        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.item_time) TextView itemTime;
        @BindView(R.id.item_msg_tag) TextView itemMsgTag;
        @BindView(R.id.item_msg_content) TextView itemMsgContent;

        public MainNotiVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}