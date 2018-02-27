package cn.qingchengfit.saasbase.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.chat.events.EventChoosePerson;
import cn.qingchengfit.saasbase.chat.events.EventFresh;
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
  @BindView(R2.id.toolbar) protected Toolbar toolbar;
  @BindView(R2.id.toolbar_title) protected TextView toolbarTitle;
  @BindView(R2.id.tv_allotsale_select_count) protected TextView tvAllotsaleSelectCount;
  //@BindView(R.id.et_search) EditText etSearch;
  //@BindView(R.id.search_clear) ImageView searchClear;
  @Inject LoginStatus loginStatus;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_conversation_friend, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    initView();
    return view;
  }

  protected void initView() {
    if (getFragmentManager() != null) {
      getFragmentManager().beginTransaction()
        .replace(R.id.chat_friend_frag, new ChatFriendAllChooseFragment())
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
  @OnClick(R2.id.btn_modify_sale) public void onDone() {
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

  @OnClick(R2.id.ll_show_select) public void onViewClicked() {
    BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
    selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
      @Override public void onBottomStudents(List<Personage> list) {
        DirtySender.studentList.clear();
        DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
        RxBus.getBus().post(new EventFresh());
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
