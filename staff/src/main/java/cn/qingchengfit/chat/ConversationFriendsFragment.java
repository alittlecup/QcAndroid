package cn.qingchengfit.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.chat.model.ChatGym;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.EventChoosePerson;
import cn.qingchengfit.staffkit.rxbus.event.EventFresh;
import cn.qingchengfit.staffkit.views.bottom.BottomStudentsFragment;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class ConversationFriendsFragment extends BaseFragment implements ChatFriendPresenter.MVPView {

  @BindView(R.id.tv_left) protected TextView tvLeft;
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.toolbar_title) protected TextView toolbarTitle;
  @BindView(R.id.tv_allotsale_select_count) protected TextView tvAllotsaleSelectCount;
    //@BindView(R.id.et_search) EditText etSearch;
    //@BindView(R.id.search_clear) ImageView searchClear;
    @Inject LoginStatus loginStatus;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_friend, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
      initView();
        return view;
    }

  protected void initView() {
    getFragmentManager().beginTransaction()
        .replace(R.id.chat_friend_frag, new ChatFriendAllChooseFragment())
        .commit();
    RxBusAdd(EventChoosePerson.class).subscribe(new Action1<EventChoosePerson>() {
      @Override public void call(EventChoosePerson eventChoosePerson) {
        tvAllotsaleSelectCount.setText(
            DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + "");
      }
    });
  }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        tvLeft.setText(R.string.cancel);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!getChildFragmentManager().popBackStackImmediate()) getActivity().onBackPressed();
            }
        });
        toolbarTitle.setText(R.string.t_chat_chose_friend);
    }

    public void setLeft(int res) {
        tvLeft.setText(res);
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
    @OnClick(R.id.btn_modify_sale) public void onDone() {
        Intent ret = new Intent();
        ret.putExtra("ids",
            (ArrayList<String>) BusinessUtils.qcstudentIds2ListChatExcepteSb(DirtySender.studentList, loginStatus.getUserId(),
                getContext()));
        getActivity().setResult(Activity.RESULT_OK, ret);
        DirtySender.studentList.clear();
        getActivity().finish();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.ll_show_select) public void onViewClicked() {
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
}
