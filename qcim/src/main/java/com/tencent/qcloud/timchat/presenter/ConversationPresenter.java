package com.tencent.qcloud.timchat.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.timchat.common.AppData;
import com.tencent.qcloud.timchat.event.AddConversationEvent;
import com.tencent.qcloud.timchat.event.FriendshipEvent;
import com.tencent.qcloud.timchat.event.GroupEvent;
import com.tencent.qcloud.timchat.event.MessageEvent;
import com.tencent.qcloud.timchat.event.RefreshEvent;
import com.tencent.qcloud.timchat.ui.qcchat.ConversationBean;
import com.tencent.qcloud.timchat.viewfeatures.ConversationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 会话界面逻辑
 */
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;
    private Context context;

    public ConversationPresenter(ConversationView view){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        //注册添加对话监听
        AddConversationEvent.getInstance().addObserver(this);
        this.view = view;
    }

    public ConversationPresenter(ConversationView view, Context context){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        //注册添加对话监听
        AddConversationEvent.getInstance().addObserver(this);
        this.view = view;
        this.context = context;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            final TIMMessage msg = (TIMMessage) data;
            if (msg != null) {
                List<String> list = new ArrayList<>();
                list.add(msg.getConversation().getPeer());
                view.updateMessage(msg);
                TIMFriendshipManager.getInstance().getUsersProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override public void onError(int i, String s) {

                    }

                    @Override public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        for (TIMUserProfile profile : timUserProfiles) {
                            if (TextUtils.isEmpty(AppData.getConversationAvatar(context, profile.getIdentifier()))) {
                                AppData.putConversationAvatar(context, profile.getIdentifier(),
                                    profile.getFaceUrl());
                                AppData.putConversationName(context, profile.getIdentifier(),
                                    profile.getNickName());
                            }
                        }
                        //view.updateMessage(msg);
                    }
                });
            }else{
                view.updateMessage(msg);
            }
        }else if (observable instanceof FriendshipEvent){
            FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
            switch (cmd.type){
                case ADD_REQ:
                case READ_MSG:
                case ADD:
                    view.updateFriendshipMessage();
                    break;
            }
        }else if (observable instanceof GroupEvent){
            GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
            switch (cmd.type){
                case UPDATE:
                case ADD:
                    view.updateGroupInfo((TIMGroupCacheInfo) cmd.data);
                    break;
                case DEL:
                    view.removeConversation((String) cmd.data);
                    break;

            }
        }else if (observable instanceof RefreshEvent){
            view.refresh();
        }else if (observable instanceof AddConversationEvent){
            ConversationBean bean = (ConversationBean)data;
            view.createGroup(bean.getIdentifyList(), bean.getMemberList());
        }
    }



    public void getConversation(){
        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System) continue;
            Log.e(TAG,"id:"+conversation.getIdentifer() + "   peer"+conversation.getPeer());
            result.add(conversation);
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateMessage(timMessages.get(0));
                    }
                }
            });

        }
        view.initView(result);
    }

    /**
     * 删除会话
     *
     * @param type 会话类型
     * @param id 会话对象id
     */
    public boolean delConversation(TIMConversationType type, String id){
        return TIMManager.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


}
