package cn.qingchengfit.saasbase.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.chat.model.Record;
import cn.qingchengfit.saasbase.chat.model.RecruitMsgPresenter;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.tencent.TIMConversation;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.timchat.chatmodel.Conversation;
import com.tencent.qcloud.timchat.chatmodel.MessageFactory;
import com.tencent.qcloud.timchat.chatmodel.NomalConversation;
import com.tencent.qcloud.timchat.presenter.ConversationPresenter;
import com.tencent.qcloud.timchat.viewfeatures.ConversationView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/7/10.
 */

public class RecruitMessageListFragment extends BaseFragment
    implements RecruitMsgPresenter.MVPView, ConversationView, FlexibleAdapter.OnItemClickListener {

	RecyclerView recyclerRecruitMessageList;
	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
  @Inject RecruitMsgPresenter presenter;
  private List<Record> records = new ArrayList<>();
  private HashMap<String, Object> map = new HashMap<>();
  private ConversationPresenter converPresenter;
  private List<ItemRecruitMessage> itemList = new ArrayList<>();
  private CommonFlexAdapter adapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_message_list, container, false);
    recyclerRecruitMessageList =
        (RecyclerView) view.findViewById(R.id.recycler_recruit_message_list);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);

    delegatePresenter(presenter, this);
    adapter = new CommonFlexAdapter(itemList, this);
    presenter.queryRecruitMsgList();
    recyclerRecruitMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerRecruitMessageList.addItemDecoration(new QcLeftRightDivider(getContext()));
    converPresenter = new ConversationPresenter(this);
    recyclerRecruitMessageList.setAdapter(adapter);
    initView();
    return view;
  }

  private void initView() {
    initToolbar(toolbar);
    toolbarTitle.setText(R.string.notification_job);
  }

  @Override public String getFragmentName() {
    return RecruitMessageListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }


  @Override public void onMessageList(List<Record> recordList) {
    filterList(recordList);
    converPresenter.getConversation();
  }

  private void filterList(List<Record> recordList) {
    map.clear();
    records.clear();
    records.addAll(recordList);
    for (int i = 0; i < recordList.size(); i++) {
      map.put(getString(R.string.chat_user_id_header, recordList.get(i).staff.id) , i);
    }
  }

  @Override public void initView(List<TIMConversation> list) {

    for (final TIMConversation conversation : list) {
      conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
        @Override
        public void onError(int i, String s) {
          LogUtil.e("get message error" + s);
        }

        @Override
        public void onSuccess(List<TIMMessage> timMessages) {
          if (timMessages.size() > 0){
            NomalConversation nomalConversation = dealMessage(timMessages.get(0));
            if (map.containsKey(conversation.getPeer())) {
              itemList.add(new ItemRecruitMessage(getContext(),
                  nomalConversation, records.get(
                  (Integer) map.get(conversation.getPeer())).gym.name));
            }
          adapter.clear();
            adapter.updateDataSet(itemList);
          }
        }
      });
    }
  }

  private NomalConversation dealMessage(TIMMessage message){
    NomalConversation normalConversation = new NomalConversation(message.getConversation());
    Iterator<ItemRecruitMessage> iterator = itemList.iterator();
    while (iterator.hasNext()) {
      Conversation c = iterator.next().getConversation();
      if (normalConversation.equals(c)) {
        normalConversation = (NomalConversation) c;
        iterator.remove();
        break;
      }
    }
    normalConversation.setLastMessage(MessageFactory.getMessage(message));
    return normalConversation;
  }

  @Override public void updateMessage(TIMMessage timMessage) {

  }

  @Override public void updateFriendshipMessage() {

  }

  @Override public void removeConversation(String s) {

  }

  @Override public void updateGroupInfo(TIMGroupCacheInfo timGroupCacheInfo) {

  }

  @Override public void refresh() {

  }

  @Override public void createGroup(List<String> list, List<String> list1) {

  }

  @Override public boolean onItemClick(int position) {
    NomalConversation conversation =
        (NomalConversation) itemList.get(position).getConversation();
    TIMManager.getInstance()
        .getConversation(conversation.getType(), conversation.getIdentify())
        .setReadMessage();
    adapter.notifyItemChanged(position);
    conversation.navToDetail(getActivity());
    return false;
  }
}
