package cn.qingchengfit.saasbase.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.chat.events.EventChoosePerson;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import cn.qingchengfit.saasbase.common.bottom.BottomStudentsFragment;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/3/30.
 */
public class ConversationFriendsFragment extends BaseFragment
  implements ChatFriendPresenter.MVPView {

  //@BindView(R2.id.tv_left) protected TextView tvLeft;
	protected Toolbar toolbar;
	protected TextView toolbarTitle;
	protected TextView tvAllotsaleSelectCount;
  //@BindView(R.id.et_search) EditText etSearch;
  //@BindView(R.id.search_clear) ImageView searchClear;
  @Inject LoginStatus loginStatus;
  private ChatFriendAllChooseFragment chatfrag;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    chatfrag = new ChatFriendAllChooseFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_conversation_friend, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tvAllotsaleSelectCount = (TextView) view.findViewById(R.id.tv_allotsale_select_count);
    view.findViewById(R.id.btn_modify_sale).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDone();
      }
    });
    view.findViewById(R.id.ll_show_select).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClicked();
      }
    });

    initToolbar(toolbar);
    initView();
    return view;
  }

  protected void initView() {
    if (getFragmentManager() != null) {
      getFragmentManager().beginTransaction()
        .replace(R.id.chat_friend_frag, chatfrag)
        .commitAllowingStateLoss();
    }
    RxBusAdd(EventChoosePerson.class).subscribe(eventChoosePerson -> tvAllotsaleSelectCount.setText(
      DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + ""));
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(v -> {
      if (getActivity() != null)
        getActivity().onBackPressed();
      else {
        CrashUtils.sendCrash(new Throwable(ConversationFriendsFragment.class.getName()+"  activity == null"));
      }
    });
    toolbarTitle.setText(R.string.t_chat_chose_friend);
  }

  @Override public boolean onFragmentBackPress() {
    return getChildFragmentManager().popBackStackImmediate() ;
  }

  public void setLeft(int res) {
    //tvLeft.setText(res);
  }

  @Override public String getFragmentName() {
    return ConversationFriendsFragment.class.getName();
  }

  @Override public void onShowError(String e) {

  }

  @Override public void onShowError(@StringRes int e) {

  }

  @Override public void onUserList(List<Staff> staffs) {

  }

  /**
   * 比 userlist 先调用
   */
  @Override public void onGymList(List<ChatGym> gyms) {

  }

  /**
   * 选择完成
   */
 public void onDone() {
    Intent ret = new Intent();
    ret.putExtra("ids", (ArrayList<String>) qcstudentIds2ListChatExcepteSb(DirtySender.studentList,
      loginStatus.getUserId(), getContext()));
    getActivity().setResult(Activity.RESULT_OK, ret);
    DirtySender.studentList.clear();
    getActivity().finish();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

 public void onViewClicked() {
    BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
    selectSutdentFragment.setListener(list -> {
      DirtySender.studentList.clear();
      DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
      if (chatfrag != null && chatfrag.isAdded()){
        chatfrag.changeSelect();
      }
    });
    selectSutdentFragment.setDatas(DirtySender.studentList);
    selectSutdentFragment.show(getFragmentManager(), "");
  }

  @Override public void onDetach() {
    DirtySender.studentList.clear();
    super.onDetach();
  }

  public static List<String> qcstudentIds2ListChatExcepteSb(List<QcStudentBean> students, String id,
    Context context) {
    List<String> ret = new ArrayList<>();
    for (int i = 0; i < students.size(); i++) {
      if (!TextUtils.equals(students.get(i).getId(), id)) {
        ret.add(context.getString(R.string.chat_user_id_header, students.get(i).getId()));
      }
    }
    return ret;
  }
}
