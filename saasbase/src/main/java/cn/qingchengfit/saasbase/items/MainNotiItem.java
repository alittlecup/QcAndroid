package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

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
	ImageView itemImg;
	ImageView itemRedDogt;
	TextView itemName;
	TextView itemTime;
	TextView itemMsgTag;
	TextView itemMsgContent;

        public MainNotiVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemImg = (ImageView) view.findViewById(R.id.item_img);
          itemRedDogt = (ImageView) view.findViewById(R.id.item_red_dogt);
          itemName = (TextView) view.findViewById(R.id.item_name);
          itemTime = (TextView) view.findViewById(R.id.item_time);
          itemMsgTag = (TextView) view.findViewById(R.id.item_msg_tag);
          itemMsgContent = (TextView) view.findViewById(R.id.item_msg_content);
        }
    }
}