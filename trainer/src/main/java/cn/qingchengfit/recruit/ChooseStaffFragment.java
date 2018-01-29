package cn.qingchengfit.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.chat.ConversationFriendsFragment;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.recruit.model.ChatGym;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.views.BottomStudentsFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.event.EventChoosePerson;
import com.qingchengfit.fitcoach.event.EventFresh;
import cn.qingchengfit.saasbase.common.views.UseStaffAppFragmentFragment;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/7/6.
 */

public class ChooseStaffFragment extends ConversationFriendsFragment {
  private ChatGym chatGym;

  public static ChooseStaffFragment newInstance(ChatGym chatGym) {
    Bundle args = new Bundle();
    args.putParcelable("gym", chatGym);
    ChooseStaffFragment fragment = new ChooseStaffFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      chatGym = getArguments().getParcelable("gym");
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择工作人员");
    toolbar.getMenu().clear();
    toolbar.inflateMenu(R.menu.menu_add_txt);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        UseStaffAppFragmentFragment.newInstance().show(getChildFragmentManager(), "");
        return false;
      }
    });
  }

  @Override protected void initView() {
    getChildFragmentManager().beginTransaction()
        .replace(R.id.chat_friend_frag, ChooseStaffInGymFragment.newInstance(chatGym))
        .commit();
    RxBusAdd(EventChoosePerson.class).subscribe(new Action1<EventChoosePerson>() {
      @Override public void call(EventChoosePerson eventChoosePerson) {
        tvAllotsaleSelectCount.setText(
            DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + "");
      }
    });
    tvAllotsaleSelectCount.setText(
        DirtySender.studentList.size() > 99 ? "..." : DirtySender.studentList.size() + "");
  }

  @Override public void onViewClicked() {
    BottomStaffsBanRvSuFragment selectSutdentFragment = new BottomStaffsBanRvSuFragment();
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

  @Override public void onDone() {
    Intent ret = new Intent();
    ret.putExtra("ids", (ArrayList<String>) BusinessUtils.PersonIdsExSu(DirtySender.studentList));
    getActivity().setResult(Activity.RESULT_OK, ret);
    DirtySender.studentList.clear();
    getActivity().finish();
  }
}
