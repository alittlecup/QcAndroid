//package cn.qingchengfit.items;
//
//import android.support.annotation.DrawableRes;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.qingchengfit.bean.NotificationGlance;
//import cn.qingchengfit.constant.ConstantNotification;
//import cn.qingchengfit.utils.DateUtils;
//import com.qingchengfit.fitcoach.R;
//import eu.davidea.flexibleadapter.FlexibleAdapter;
//import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
//import eu.davidea.viewholders.FlexibleViewHolder;
//import java.util.List;
//
//public class SystemMsgItem extends AbstractFlexibleItem<SystemMsgItem.SystemMsgVH> {
//
//    @DrawableRes int type;
//    NotificationGlance notificationGlance;
//    public SystemMsgItem(int type, NotificationGlance notificationGlance) {
//        this.type = type;
//        this.notificationGlance = notificationGlance;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public NotificationGlance getNotificationGlance() {
//        return notificationGlance;
//    }
//
//    public void setNotificationGlance(NotificationGlance notificationGlance) {
//        this.notificationGlance = notificationGlance;
//    }
//
//    @Override public int getLayoutRes() {
//        return R.layout.item_system_msg;
//    }
//
//    @Override public SystemMsgVH createViewHolder(View view, FlexibleAdapter adapter) {
//        return new SystemMsgVH(view, adapter);
//    }
//
//    @Override public void bindViewHolder(FlexibleAdapter adapter, SystemMsgVH holder, int position, List payloads) {
//        holder.itemImg.setImageResource(type);
//        holder.itemName.setText(ConstantNotification.getNotiStr(type));
//        holder.itemRedDogt.setVisibility(notificationGlance.unread > 0 ? View.VISIBLE : View.GONE);
//        if (notificationGlance.notification != null && notificationGlance.notification.getId() != null) {
//            if (TextUtils.isEmpty(notificationGlance.notification.shop_name)) {
//                holder.itemMsgTag.setVisibility(View.GONE);
//            } else {
//                holder.itemMsgTag.setVisibility(View.VISIBLE);
//                holder.itemMsgTag.setText("[" + notificationGlance.notification.shop_name + "]");
//            }
//
//            holder.itemMsgContent.setText(notificationGlance.notification != null ? notificationGlance.notification.getDescription() : "");
//            holder.itemTime.setText(
//                DateUtils.getNotifacationTimeStr(DateUtils.formatDateFromServer(notificationGlance.notification.getCreated_at())));
//        }
//    }
//
//    @Override public boolean equals(Object o) {
//        return false;
//    }
//
//    public class SystemMsgVH extends FlexibleViewHolder {
//        @BindView(R.id.item_img) ImageView itemImg;
//        @BindView(R.id.item_red_dogt) ImageView itemRedDogt;
//        @BindView(R.id.item_name) TextView itemName;
//        @BindView(R.id.item_time) TextView itemTime;
//        @BindView(R.id.item_msg_tag) TextView itemMsgTag;
//        @BindView(R.id.item_msg_content) TextView itemMsgContent;
//
//        public SystemMsgVH(View view, FlexibleAdapter adapter) {
//            super(view, adapter);
//            ButterKnife.bind(this, view);
//        }
//    }
//}