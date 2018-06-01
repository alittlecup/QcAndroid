package cn.qingchengfit.saasbase.items;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.constant.ConstantNotification;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class SystemMsgItem extends AbstractFlexibleItem<SystemMsgItem.SystemMsgVH> {

     String type;
    NotificationGlance notificationGlance;

    public SystemMsgItem(String type, NotificationGlance notificationGlance) {
        this.type = type;
        this.notificationGlance = notificationGlance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NotificationGlance getNotificationGlance() {
        return notificationGlance;
    }

    public void setNotificationGlance(NotificationGlance notificationGlance) {
        this.notificationGlance = notificationGlance;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_system_msg;
    }

    @Override public SystemMsgVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new SystemMsgVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, SystemMsgVH holder, int position, List payloads) {
        holder.itemImg.setImageResource(ConstantNotification.getNotiDrawable(type));
        holder.itemName.setText(ConstantNotification.getNotiStr(type));
        holder.itemRedDogt.setVisibility(notificationGlance.unread > 0 ? View.VISIBLE : View.GONE);
        if (notificationGlance.notification != null && notificationGlance.notification.getId() != null) {
            if (TextUtils.isEmpty(notificationGlance.notification.shop_name)) {
                holder.itemMsgTag.setVisibility(View.GONE);
            } else {
                holder.itemMsgTag.setVisibility(View.VISIBLE);
                holder.itemMsgTag.setTextColor(ContextCompat.getColor(holder.itemMsgTag.getContext(),
                    notificationGlance.unread > 0 ? R.color.red : R.color.text_warm));
                holder.itemMsgTag.setText("[" + notificationGlance.notification.shop_name + "]");
            }

            holder.itemMsgContent.setText(notificationGlance.notification != null ? notificationGlance.notification.getTitle() : "");
            holder.itemTime.setText(
                DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(notificationGlance.notification.getCreated_at())));
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class SystemMsgVH extends FlexibleViewHolder {
	ImageView itemImg;
	ImageView itemRedDogt;
	TextView itemName;
	TextView itemTime;
	TextView itemMsgTag;
	TextView itemMsgContent;

        public SystemMsgVH(View view, FlexibleAdapter adapter) {
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