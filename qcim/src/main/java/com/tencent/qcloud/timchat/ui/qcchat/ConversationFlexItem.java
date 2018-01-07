package com.tencent.qcloud.timchat.ui.qcchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tencent.TIMConversationType;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.chatmodel.Conversation;
import com.tencent.qcloud.timchat.chatmodel.NomalConversation;
import com.tencent.qcloud.timchat.chatutils.TimeUtil;
import com.tencent.qcloud.timchat.common.AppData;
import com.tencent.qcloud.timchat.widget.CircleImageView;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/3/17.
 */

public class ConversationFlexItem extends AbstractFlexibleItem<ConversationFlexItem.ConversationViewHolder> implements Comparable {

    private Conversation conversation;
    private Context context;
    private String faceUrl;

    public ConversationFlexItem(Context context, Conversation conversation) {
        this.context = context;
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public long getLastMessageTime(){
        if (conversation != null){
            return conversation.getLastMessageTime();
        }
        return 0;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void onChangeName(ConversationViewHolder holder){

    }

    @Override public int getLayoutRes() {
        return R.layout.item_conversation;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, final ConversationViewHolder holder, int position, final List payloads) {
        if (((NomalConversation)conversation).getType() == TIMConversationType.Group) {
            faceUrl = TextUtils.isEmpty(
                AppData.getConversationAvatar(context, conversation.getIdentify()))
                ? AppData.defaultGroupAvatar : AppData.getConversationAvatar(context, conversation.getIdentify());
        }else{
            faceUrl = TextUtils.isEmpty(
                AppData.getConversationAvatar(context, conversation.getIdentify()))
                ? AppData.defaultAvatar : AppData.getConversationAvatar(context, conversation.getIdentify());
        }
        Glide.with(context)
            .load(PhotoUtils.getSmall(faceUrl))
            .asBitmap()
            .into(holder.avator);
        holder.tvName.setText(AppData.getConversationName(context, conversation.getIdentify()));
        holder.lastMessage.setText(conversation.getLastMessageSummary());
        holder.time.setText(TimeUtil.getNotifacationTimeStr(conversation.getLastMessageTime()));
        long unRead = conversation.getUnreadNum();
        if (unRead <= 0) {
            holder.imageDot.setVisibility(View.GONE);
        } else {
            holder.imageDot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ConversationViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ConversationViewHolder vh = new ConversationViewHolder(view, adapter);
        return vh;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof ConversationFlexItem){
            ConversationFlexItem item = (ConversationFlexItem) o;
            long timeGap = item.getConversation().getLastMessageTime() - getLastMessageTime();
            if (timeGap > 0) return  1;
            else if (timeGap < 0) return -1;
            return 0;
        }else{
            throw new ClassCastException();
        }
    }

    public static class ConversationViewHolder extends FlexibleViewHolder{

        public TextView tvName;
        private CircleImageView avator;
        private TextView lastMessage;
        private TextView time;
        private ImageView imageDot;

        public ConversationViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            tvName = (TextView) view.findViewById(R.id.name);
            avator = (CircleImageView) view.findViewById(R.id.avatar);
            lastMessage = (TextView) view.findViewById(R.id.last_message);
            time = (TextView) view.findViewById(R.id.message_time);
            imageDot = (ImageView) view.findViewById(R.id.new_red_dot);
        }



    }

}
