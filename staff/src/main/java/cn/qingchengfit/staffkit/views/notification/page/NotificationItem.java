package cn.qingchengfit.staffkit.views.notification.page;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;

import java.util.List;



import cn.qingchengfit.model.responese.NotificationMsg;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class NotificationItem extends AbstractFlexibleItem<NotificationItem.NotificationVH> {

    private NotificationMsg msg;

    public NotificationItem(NotificationMsg msg) {
        this.msg = msg;
    }

    public NotificationMsg getMsg() {
        return msg;
    }

    public void setMsg(NotificationMsg msg) {
        this.msg = msg;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_notifacation;
    }

    @Override public NotificationVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new NotificationVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, NotificationVH holder, int position, List payloads) {
        NotificationMsg entity = msg;
        if (TextUtils.isEmpty(entity.getUrl()) && entity.card_id == 0 && entity.type < 11) {
            holder.iconRight.setVisibility(View.GONE);
        } else {
            holder.iconRight.setVisibility(View.VISIBLE);
        }
        holder.itemNotiTitle.setText(entity.getTitle());
        holder.itemNotiTime.setText(entity.getCreated_at().replace("T", " "));
        holder.itemNotiSender.setText(entity.getSender());
        Glide.with(adapter.getRecyclerView().getContext()).load(PhotoUtils.getSmall(entity.getPhoto())).into(holder.itemNotiIcon);
        holder.itemNotiDesc.setText(entity.getDescription());
        if (!entity.is_read()) {
            holder.itemNotiUnread.setVisibility(View.VISIBLE);
        } else {
            holder.itemNotiUnread.setVisibility(View.INVISIBLE);
        }
    }

    @Override public boolean equals(Object o) {
        return o instanceof NotificationItem && ((NotificationItem) o).getMsg() != null && ((NotificationItem) o).getMsg().getId().equals(getMsg().getId());
    }

    public class NotificationVH extends FlexibleViewHolder {
	ImageView itemNotiUnread;
	ImageView itemNotiIcon;
	TextView itemNotiTitle;
	TextView itemNotiDesc;
	TextView itemNotiTime;
	TextView itemNotiSender;
	ImageView iconRight;

        public NotificationVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemNotiUnread = (ImageView) view.findViewById(R.id.item_noti_unread);
          itemNotiIcon = (ImageView) view.findViewById(R.id.item_noti_icon);
          itemNotiTitle = (TextView) view.findViewById(R.id.item_noti_title);
          itemNotiDesc = (TextView) view.findViewById(R.id.item_noti_desc);
          itemNotiTime = (TextView) view.findViewById(R.id.item_noti_time);
          itemNotiSender = (TextView) view.findViewById(R.id.item_noti_sender);
          iconRight = (ImageView) view.findViewById(R.id.icon_right);
        }
    }
}