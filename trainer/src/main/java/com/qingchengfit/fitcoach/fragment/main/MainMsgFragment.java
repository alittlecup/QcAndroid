package com.qingchengfit.fitcoach.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.NotificationDeleted;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.model.responese.NotificationMsg;
import cn.qingchengfit.presenters.SystemMsgPresenter;
import cn.qingchengfit.saasbase.chat.model.Record;
import cn.qingchengfit.saasbase.constant.ConstantNotification;
import cn.qingchengfit.saasbase.items.SystemMsgItem;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.NotificationCompartor;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.container.ContainerActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.ChooseActivity;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.NotificationActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.tencent.qcloud.sdk.Constant;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.common.AppData;
import com.tencent.qcloud.timchat.ui.qcchat.AddConversationProcessor;
import com.tencent.qcloud.timchat.ui.qcchat.ConversationFragment;
import com.tencent.qcloud.timchat.ui.qcchat.LoginProcessor;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import tencent.tls.platform.TLSErrInfo;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/3/6.
 */
public class MainMsgFragment extends BaseFragment
    implements LoginProcessor.OnLoginListener, FlexibleAdapter.OnItemClickListener,
    SystemMsgPresenter.MVPView, FlexibleAdapter.OnItemLongClickListener {

	FloatingActionButton addConversation;
	Toolbar toolbar;
	TextView toolbarTitile;
	RecyclerView recyclerview;
	View divider;

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SystemMsgPresenter presenter;
	SwipeRefreshLayout refresh;
  ConversationFragment conversationFragment;
  private List<AbstractFlexibleItem> items = new ArrayList<>();
  private CommonFlexAdapter adapter;
  private LoginProcessor loginProcessor;
  private int unReadNoti = 0;
  private NotificationDeleted notificationDeleted = new NotificationDeleted();
  private boolean init = false;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String ds =
        PreferenceUtils.getPrefString(getContext(), loginStatus.staff_id() + "dele_noti", "");
    if (!TextUtils.isEmpty(ds)) {
      notificationDeleted = new Gson().fromJson(ds, NotificationDeleted.class);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main_msg, container, false);
    addConversation = (FloatingActionButton) view.findViewById(R.id.fab_add_conversation);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
    recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
    divider = (View) view.findViewById(R.id.divider);
    refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
    view.findViewById(R.id.fab_add_conversation).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        addCoversation();
      }
    });

    delegatePresenter(presenter, this);
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerview.addItemDecoration(
        new QcLeftRightDivider(getContext(), 0, R.layout.item_system_msg, 15, 15));
    recyclerview.setNestedScrollingEnabled(false);
    adapter = new CommonFlexAdapter(items, this);
    recyclerview.setAdapter(adapter);
    refresh.setNestedScrollingEnabled(false);
    refresh.setOnRefreshListener(() -> refresh());
    RxBusAdd(EventLoginChange.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(eventLoginChange -> changeLogin());
    changeLogin();
    return view;
  }

  @Override public void onHiddenChanged(boolean hidden) {
    if (!hidden && init) {
      refresh();
    }
  }

  private void changeLogin() {
    initToolbar(toolbar);
    if (loginStatus.isLogined()) {
      addConversation.setVisibility(View.VISIBLE);
      refresh();
    } else {
      if (adapter != null) {
        adapter.clear();
        adapter.addItem(new CommonNoDataItem(R.drawable.vd_no_notifications, "", "暂无消息"));
      }
      addConversation.setVisibility(View.GONE);
      if (getChildFragmentManager().findFragmentByTag("chat") != null) {
        getChildFragmentManager().beginTransaction()
            .hide(getChildFragmentManager().findFragmentByTag("chat"))
            .commitAllowingStateLoss();
      }
    }
  }

  public void loginIM() {
    if (loginStatus.isLogined()) {
      MyApplication myApplication = new MyApplication(getActivity().getApplication());
      addConversation.setVisibility(View.VISIBLE);
      try {
        Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
        Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
        Constant.setXiaomiPushAppid(
            BuildConfig.DEBUG ? "2882303761517418101" : "2882303761517418101");
        Constant.setBussId(BuildConfig.DEBUG ? 611 : 605);
        Constant.setXiaomiPushAppkey("5361741818101");
        Constant.setHuaweiBussId(612);
        if (loginProcessor == null) {
          loginProcessor = new LoginProcessor(getActivity().getApplicationContext(),
              getString(R.string.chat_user_id_header, loginStatus.getUserId()),
              Uri.parse(Configs.Server).getHost(), this);
        }
        if (!loginProcessor.isLogin() || !init) {
          loginProcessor.sientInstall();
          init = true;
        } else {
          onLoginSuccess();
        }
      } catch (Exception e) {
        LogUtil.e(e.getMessage());
        ToastUtils.show(e.getMessage());
      }
    } else {
      AppData.clear(getContext());
      addConversation.setVisibility(View.GONE);
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  public void refresh() {
    if (loginStatus.isLogined()) {
      presenter.querySimpleList(ConstantNotification.getNotiQueryJson(getContext()));
      loginIM();
    } else {
      refresh.setRefreshing(false);
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    toolbarTitile.setText(R.string.title_msg);
    if (loginStatus.isLogined()) {
      toolbar.getMenu().clear();
      toolbar.inflateMenu(R.menu.menu_clear_noti);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          presenter.clearNoti(null);
          unReadNoti = 0;
          conversationFragment.setAllMessageRead();
          if (getActivity() instanceof Main2Activity) {
            ((Main2Activity) getActivity()).freshNotiCount(0);
          }
          return false;
        }
      });
    } else {
      toolbar.getMenu().clear();
    }
  }

  @Override public String getFragmentName() {
    return MainMsgFragment.class.getName();
  }

  @Override public void onLoginSuccess() {
    if (recyclerview == null) return;
    if (loginStatus != null && loginStatus.getLoginUser() != null) {
      loginProcessor.setUserInfo(loginStatus.getLoginUser().username,
          loginStatus.getLoginUser().avatar);
    }
    if (conversationFragment == null) {
      conversationFragment = new ConversationFragment();
      conversationFragment.setOnUnReadMessageListener(
          new ConversationFragment.OnUnReadMessageListener() {
            @Override public void onUnReadMessage(long l) {
              if (getActivity() instanceof Main2Activity) {
                ((Main2Activity) getActivity()).freshNotiCount(getUnredCount());
              }
            }

            @Override public void onLongClickListener(final int i) {
              DialogUtils.instanceDelDialog(getContext(), "删除该消息",
                  new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog,
                        @NonNull DialogAction which) {
                      conversationFragment.deleteConversationItem(i);
                      checkNoInfo();
                      if (getActivity() instanceof Main2Activity) {
                        ((Main2Activity) getActivity()).freshNotiCount(getUnredCount());
                      }
                    }
                  }).show();
            }

            @Override public void onUpdateRecruitListener(String s) {

            }
          });
    }
    if (getChildFragmentManager().findFragmentByTag("chat") == null) {
      getChildFragmentManager().beginTransaction()
          .replace(R.id.frame_chat, conversationFragment, "chat")
          .commitAllowingStateLoss();
    }else {
      getChildFragmentManager().beginTransaction()
        .show(getChildFragmentManager().findFragmentByTag("chat"))
        .commitAllowingStateLoss();
    }
    if (getActivity() instanceof Main2Activity) {
      ((Main2Activity) getActivity()).freshNotiCount(getUnredCount());
    }
    presenter.querySimpleList(ConstantNotification.getNotiQueryJson(getContext()));
  }

  @Override public void onLoginFailed(TLSErrInfo tlsErrInfo) {
    //LogUtil.e("聊天系统登录失败：请重新打开应用  " + tlsErrInfo.ErrCode + "  " + tlsErrInfo.Msg);
  }

 public void addCoversation() {
    Intent toChooseStaffs = new Intent(getContext(), ChooseActivity.class);
    toChooseStaffs.putExtra("to", ChooseActivity.CONVERSATION_FRIEND);
    startActivityForResult(toChooseStaffs, ChooseActivity.CONVERSATION_FRIEND);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == ChooseActivity.CONVERSATION_FRIEND) {
        AddConversationProcessor addConversationProcessor =
            new AddConversationProcessor(getContext());
        addConversationProcessor.setOnCreateConversation(
            new AddConversationProcessor.OnCreateConversation() {
              @Override public void onCreateSuccess(String s) {

              }

              @Override public void onCreateFailed(int i, String s) {
                LogUtil.e("code:" + i + "  " + s);
              }
            });
        List<String> ret = data.getStringArrayListExtra("ids");
        if (ret == null || ret.size() == 0) {
          ToastUtils.show("您没有选择除自己以外的任何人");
          return;
        }
        DirtySender.studentList.clear();
        addConversationProcessor.creaetGroupWithName(ret);
      }
    } else {
      if (requestCode == ChooseActivity.CONVERSATION_FRIEND) {
        DirtySender.studentList.clear();
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onShowError(String e) {
    refresh.setRefreshing(false);
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }

  @Override public void onNotificationList(List<NotificationGlance> list) {
    refresh.setRefreshing(false);
    unReadNoti = 0;
    if (list != null) {
      Collections.sort(list, new NotificationCompartor());
      items.clear();
      for (int i = 0; i < list.size(); i++) {
        NotificationMsg msg = list.get(i).notification != null ? list.get(i).notification : null;
        if (list.get(i).unread > 0 && msg !=null ) unReadNoti++;
        if (msg == null || (msg.getId() != null && notificationDeleted.contains(
            Long.toString(msg.getId())))) {
          //如果被删除 就不展示
        } else {
          items.add(new SystemMsgItem(list.get(i).key,
              list.get(i)));
        }
      }
      NotificationGlance recruitNoty = new NotificationGlance();
      NotificationMsg recruitMsg = new NotificationMsg();
      //recruitMsg.setDescription("点击查看简历投递和职位邀约详细消息");
      recruitMsg.setTitle("点击查看简历投递和职位邀约详细消息");
      recruitMsg.setId(0x11L);
      recruitNoty.notification = recruitMsg;
      items.add(new SystemMsgItem(ConstantNotification.JOB_NOTIFICATION_STR, recruitNoty));
      adapter.clear();
      adapter.updateDataSet(items);

      try {
        //判断是否要填充空页面
        if (adapter.getItemCount() + conversationFragment.getTotalItemCount() == 0) {
          adapter.addItem(
              new CommonNoDataItem(R.drawable.vd_no_notifications, "您可以点击右下角按钮发起会话", "暂无消息"));
          divider.setVisibility(View.GONE);
        } else {
          divider.setVisibility(View.VISIBLE);
        }
      } catch (Exception e) {

      }
    }
  }

  @Override public void onClearNotiOk() {
    refresh();
  }

  @Override public void onMessageList(List<Record> recordList) {

  }

  @Override public boolean onItemClick(int i) {
    if (items.get(i) instanceof SystemMsgItem) {
      String type = ((SystemMsgItem) items.get(i)).getType();
      if (!TextUtils.equals(type,ConstantNotification.COMMENT_NOTIFICATION_STR) && !TextUtils.equals(type,ConstantNotification.JOB_NOTIFICATION_STR)) {
        Intent toNoti = new Intent(getActivity(), NotificationActivity.class);
        toNoti.putExtra("type", type);
        startActivity(toNoti);
      } else if (TextUtils.equals(type,ConstantNotification.COMMENT_NOTIFICATION_STR)) {
          presenter.clearNoti(
                  ConstantNotification.getCategloreStr(getContext(),ConstantNotification.COMMENT_NOTIFICATION_STR));
          ContainerActivity.router("/replies", getContext());
      } else {
          ContainerActivity.router("/recruit/message_list", getContext(),
                  getString(R.string.chat_user_id_header, loginStatus.getUserId()));
      }
    }
    return false;
  }

  @Override public void onItemLongClick(final int i) {
    if (items.get(i) instanceof SystemMsgItem) {
      final NotificationMsg msg =
          ((SystemMsgItem) items.get(i)).getNotificationGlance().notification;
      final String type = ((SystemMsgItem) items.get(i)).getType();
      DialogUtils.instanceDelDialog(getContext(), "删除该消息", (dialog, which) -> {
        if (msg != null && msg.getId() != null) {
          notificationDeleted.add(Long.toString(msg.getId()), type);
          PreferenceUtils.setPrefString(getContext(), loginStatus.staff_id() + "dele_noti",
              new Gson().toJson(notificationDeleted, NotificationDeleted.class));
        }
        presenter.clearNoti(ConstantNotification.getCategloreStr(getContext(),type));
        adapter.removeItem(i);
        checkNoInfo();
        if (getActivity() instanceof Main2Activity) {
          ((Main2Activity) getActivity()).freshNotiCount(getUnredCount());
        }
      }).show();
    }
  }

  private void checkNoInfo() {
    try {

      if (adapter.getItemCount() == 0 || adapter.getItem(0) instanceof CommonNoDataItem) {
        divider.setVisibility(View.GONE);
      } else {
        divider.setVisibility(View.VISIBLE);
      }
      //判断是否要填充空页面
      if (adapter.getItemCount() + conversationFragment.getTotalItemCount() == 0) {
        adapter.addItem(
            new CommonNoDataItem(R.drawable.vd_no_notifications, "您可以点击右下角按钮发起会话", "暂无消息"));
      }
    } catch (Exception e) {

    }
  }

  public int getUnredCount() {
    try {
      return unReadNoti + (int) conversationFragment.getTotalUnreadNum();
    } catch (Exception e) {
      return 0;
    }
  }
}
