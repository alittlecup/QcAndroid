package cn.qingchengfit.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.chat.ChatChooseInGymFragment;
import cn.qingchengfit.saasbase.chat.model.ChatGym;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.rxbus.event.EventChoosePerson;
import cn.qingchengfit.saasbase.items.ChooseStaffItem;
import cn.qingchengfit.saasbase.items.PositionHeaderItem;
import cn.qingchengfit.utils.ToastUtils;

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
 * Created by Paper on 2017/7/14.
 */

public class ChooseStaffInGymFragment extends ChatChooseInGymFragment {

  public static ChooseStaffInGymFragment newInstance(ChatGym chatGym) {
    Bundle args = new Bundle();
    args.putParcelable("chatgym", chatGym);
    ChooseStaffInGymFragment fragment = new ChooseStaffInGymFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    checkbox.setVisibility(View.GONE);
    return v;
  }

  @Override public boolean onItemClick(int i) {
    if (adapter.getItem(i) instanceof PositionHeaderItem) {
      adapter.notifyItemChanged(i);
      return false;
    } else if (adapter.getItem(i) instanceof ChooseStaffItem) {
      Personage p = ((ChooseStaffItem) adapter.getItem(i)).getStaff();
      if (DirtySender.studentList.contains(p)) {
        if (p.is_superuser) {
          ToastUtils.show("超级管理员无法被移除");
        } else {
          DirtySender.studentList.remove(p);
        }
      } else {
        QcStudentBean studentBean = new QcStudentBean(p);
        if (!DirtySender.studentList.contains(studentBean)) {
          DirtySender.studentList.add(studentBean);
        }
      }
      adapter.notifyItemChanged(i);
      adapter.notifyItemChanged(
          adapter.getGlobalPositionOf(adapter.getExpandableOf(adapter.getItem(i))));

      RxBus.getBus().post(new EventChoosePerson());
      return true;
    }
    return false;
  }

  @Override protected PositionHeaderItem newPositionHeader(String s) {
    return new PositionHeaderBanRvSuItem(s);
  }

  @Override public void onClickLayoutGym() {
  }
}
