package cn.qingchengfit.chat;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.chat.model.Record;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.presenters.SystemMsgPresenter;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
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

@FragmentWithArgs
public class RecruitMessageListFragment extends BaseFragment
    implements SystemMsgPresenter.MVPView, ConversationView, FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.recycler_recruit_message_list) RecyclerView recyclerRecruitMessageList;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @Inject SystemMsgPresenter presenter;
  private List<Record> records = new ArrayList<>();
  private HashMap<String, Object> map = new HashMap<>();
  private ConversationPresenter converPresenter;
  private List<ItemRecruitMessage> itemList = new ArrayList<>();
  private CommonFlexAdapter adapter;
  @Arg String identify;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitMessageListFragmentBuilder.injectArguments(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_message_list, container, false);
    unbinder = ButterKnife.bind(this, view);
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

  @Override public void onNotificationList(List<NotificationGlance> list) {

  }

  @Override public void onClearNotiOk() {

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
      map.put("qctest_" + recordList.get(i).staff.id, i);
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
            //TODO 正式环境替换 qc_
            if (map.containsKey(conversation.getPeer())) {
              itemList.add(new ItemRecruitMessage(getContext(),
                  nomalConversation, records.get(
                  (Integer) map.get(conversation.getPeer())).gym.name));
            }
          adapter.notifyDataSetChanged();
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
