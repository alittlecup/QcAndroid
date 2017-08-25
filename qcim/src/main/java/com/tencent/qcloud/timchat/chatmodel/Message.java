package com.tencent.qcloud.timchat.chatmodel;

import android.content.Context;
import android.view.View;
import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageStatus;
import com.tencent.qcloud.timchat.adapters.ChatItem;
import com.tencent.qcloud.timchat.chatutils.TimeUtil;

/**
 * 消息数据基类
 */
public abstract class Message {

    protected final String TAG = "Message";

    TIMMessage message;

    private boolean hasTime;

    /**
     * 消息描述信息
     */
    private String desc;


    public TIMMessage getMessage() {
        return message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    public abstract void showMessage(ChatItem.ViewHolder viewHolder, Context context);

    /**
     * 获取显示气泡
     *
     * @param viewHolder 界面样式
     */
    public void getBubbleView(ChatItem.ViewHolder viewHolder){
        viewHolder.systemMessage.setVisibility(hasTime?View.VISIBLE:View.GONE);
        viewHolder.systemMessage.setText(TimeUtil.getNotifacationTimeStr(message.timestamp()));
        showDesc(viewHolder);

        if (message.isSelf()){
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.VISIBLE);
        }else{
            viewHolder.leftPanel.setVisibility(View.VISIBLE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            //群聊显示名称，群名片>个人昵称>identify
            if (message.getConversation().getType() == TIMConversationType.Group){
                viewHolder.sender.setVisibility(View.VISIBLE);
                String name = "";
                if (message.getSenderGroupMemberProfile()!=null) name = message.getSenderGroupMemberProfile().getNameCard();
                if (name.equals("") && message.getSenderProfile()!=null) name = message.getSenderProfile().getNickName();
                if (name.equals("")) name = message.getSender();
                viewHolder.sender.setText(name);
            }else{
                viewHolder.sender.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 显示消息状态
     *
     * @param viewHolder 界面样式
     */
    public void showStatus(ChatItem.ViewHolder viewHolder){
        switch (message.status()){
            case Sending:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.VISIBLE);
                break;
            case SendSucc:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.GONE);
                break;
            case SendFail:
                viewHolder.error.setVisibility(View.VISIBLE);
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.leftPanel.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 判断是否是自己发的
     *
     */
    public boolean isSelf(){
        return message.isSelf();
    }

    /**
     * 获取消息摘要
     *
     */
    public abstract String getSummary();

    /**
     * 保存消息或消息文件
     *
     */
    public abstract void save();


    /**
     * 删除消息
     *
     */
    public void remove(){
        if (message != null){
            message.remove();
        }
    }



    /**
     * 是否需要显示时间获取
     *
     */
    public boolean getHasTime() {
        return hasTime;
    }


    /**
     * 是否需要显示时间设置
     *
     * @param message 上一条消息
     */
    public void setHasTime(TIMMessage message){
        if (message == null){
            hasTime = true;
            return;
        }
        hasTime = Math.abs(this.message.timestamp() - message.timestamp()) > 300;
    }


    /**
     * 消息是否发送失败
     *
     */
    public boolean isSendFail(){
        return message.status() == TIMMessageStatus.SendFail;
    }

    /**
     * 清除气泡原有数据
     *
     */
    protected void clearView(ChatItem.ViewHolder viewHolder){
        //viewHolder.leftMessage.setBackground(ContextCompat.getDrawable(viewHolder.getContentView().getContext(), R.drawable.chat_bubble_grey));
        //viewHolder.rightMessage.setBackground(ContextCompat.getDrawable(viewHolder.getContentView().getContext(), R.drawable.chat_bubble_green));
        //viewHolder.leftMessage.setGravity(Gravity.CENTER);
        //viewHolder.rightMessage.setGravity(Gravity.CENTER);
    }

    /**
     * 获取发送者
     *
     */
    public String getSender(){
        if (message.getSender() == null) return "";
        return message.getSender();
    }

    public String getNotifyName(){
        if (message.getSenderProfile() == null) return "";
        return message.getSenderProfile().getNickName();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    private void showDesc(ChatItem.ViewHolder viewHolder){

        if (desc == null || desc.equals("")){
            viewHolder.rightDesc.setVisibility(View.GONE);
        }else{
            viewHolder.rightDesc.setVisibility(View.VISIBLE);
            viewHolder.rightDesc.setText(desc);
        }
    }
}
