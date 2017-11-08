package com.tencent.qcloud.timchat.chatmodel;

import android.content.Context;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.ui.qcchat.ChatActivity;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {

  private TIMConversation conversation;

  //最后一条消息
  private Message lastMessage;

  public NomalConversation(TIMConversation conversation) {
    this.conversation = conversation;
    type = conversation.getType();
    identify = conversation.getPeer();
  }

  public void setLastMessage(Message lastMessage) {
    this.lastMessage = lastMessage;
  }



  @Override public String getAvatar() {
    //ArrayList<String> list = new ArrayList<>();
    //list.add(identify);
    //if (!TextUtils.isEmpty(avator)){
    //  return avator;
    //}
    //if (type == TIMConversationType.Group) {
    //  TIMGroupManager.getInstance()
    //      .getGroupDetailInfo(list, new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
    //        @Override public void onError(int i, String s) {
    //          avator = AppData.defaultGroupAvatar;
    //        }
    //
    //        @Override public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
    //          for (TIMGroupDetailInfo profile : timGroupDetailInfos) {
    //            avator = TextUtils.isEmpty(profile.getFaceUrl()) ? AppData.defaultGroupAvatar
    //                : profile.getFaceUrl();
    //            name = profile.getGroupName();
    //          }
    //        }
    //      });
    //} else {
    //  TIMFriendshipManager.getInstance()
    //      .getUsersProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {
    //        @Override public void onError(int i, String s) {
    //          avator = AppData.defaultAvatar;
    //        }
    //
    //        @Override public void onSuccess(List<TIMUserProfile> timUserProfiles) {
    //          for (TIMUserProfile profile : timUserProfiles) {
    //            avator = TextUtils.isEmpty(profile.getFaceUrl()) ? AppData.defaultAvatar
    //                : profile.getFaceUrl();
    //            ;
    //            name = profile.getNickName();
    //          }
    //        }
    //      });
    //}
    return avator;
  }

  /**
   * 跳转到聊天界面或会话详情
   *
   * @param context 跳转上下文
   */
  @Override public void navToDetail(Context context) {
    ChatActivity.navToChat(context, identify, type);
  }

  public void startChat(Context context, String faceUrl) {
    ChatActivity.navToChat(context, identify, faceUrl, type);
  }

  /**
   * 获取最后一条消息摘要
   */
  @Override public String getLastMessageSummary() {
    if (conversation.hasDraft()) {
      TextMessage textMessage = new TextMessage(conversation.getDraft());
      if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft()
          .getTimestamp()) {
        return MyApplication.getContext().getString(R.string.conversation_draft)
            + textMessage.getSummary();
      } else {
        return lastMessage.getSummary();
      }
    } else {
      if (lastMessage == null) return "";
      return lastMessage.getSummary();
    }
  }

  /**
   * 获取名称
   */
  @Override public String getName() {
    //        if (type == TIMConversationType.Group){
    //            name=GroupInfo.getInstance().getGroupName(identify);
    //            if (name.equals("")) name = identify;
    //        }else{
    //            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
    //            name=profile == null?identify:profile.getName();
    //        }
    return name;
  }

  /**
   * o
   */
  @Override public long getUnreadNum() {
    if (conversation == null) return 0;
    return conversation.getUnreadMessageNum();
  }

  /**
   * 将所有消息标记为已读
   */
  @Override public void readAllMessage() {
    if (conversation != null) {
      conversation.setReadMessage();
    }
  }

  /**
   * 获取最后一条消息的时间
   */
  @Override public long getLastMessageTime() {
    if (conversation.hasDraft()) {
      if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft()
          .getTimestamp()) {
        return conversation.getDraft().getTimestamp();
      } else {
        return lastMessage.getMessage().timestamp();
      }
    }
    if (lastMessage == null) return 0;
    return lastMessage.getMessage().timestamp();
  }

  /**
   * 获取会话类型
   */
  public TIMConversationType getType() {
    return conversation.getType();
  }
}
