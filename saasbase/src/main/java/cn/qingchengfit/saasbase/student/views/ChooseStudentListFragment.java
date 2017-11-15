package cn.qingchengfit.saasbase.student.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.student.items.ChosenStudentItem;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHeader;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/8/28.
 */

public class ChooseStudentListFragment extends SimpleStudentListFragment {
  private ArrayList<String> studentIdList;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    commonFlexAdapter.setMode(SelectableAdapter.Mode.MULTI);
    return v;
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof StudentItem) {
      commonFlexAdapter.toggleSelection(i);
      commonFlexAdapter.notifyItemChanged(i);
    }
    return true;
  }

  @Override protected IFlexible instanceItem(QcStudentBean qcStudentBean,IHeader iHeader) {
    return new ChosenStudentItem(qcStudentBean,iHeader);
  }

  public List<QcStudentBean> getSelectedStudent() {
    List<QcStudentBean> ret = new ArrayList<>();
    for (Integer i : commonFlexAdapter.getSelectedPositions()) {
      if (commonFlexAdapter.getItem(i) instanceof StudentItem) {
        ret.add(((StudentItem) commonFlexAdapter.getItem(i)).getQcStudentBean());
      }
    }
    return ret;
  }

  public int getSelectedCount() {
    return commonFlexAdapter.getSelectedItemCount();
  }

  public void selectStudent(ArrayList<String> studentIdList) {
    this.studentIdList = studentIdList;
    //if (studentIdList != null) {
    //  for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
    //    IFlexible item = commonFlexAdapter.getItem(i);
    //    if (item instanceof ChosenStudentItem) {
    //      if (studentIdList.contains(((ChosenStudentItem) item).getId())) {
    //        commonFlexAdapter.toggleSelection(i);
    //        commonFlexAdapter.notifyItemChanged(i);
    //      }
    //    }
    //  }
    //}
  }

  @Override public void onUpdateEmptyView(int size) {
    if (studentIdList != null) {
      for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
        IFlexible item = commonFlexAdapter.getItem(i);
        if (item instanceof ChosenStudentItem) {
          if (studentIdList.contains(((ChosenStudentItem) item).getId())) {
            commonFlexAdapter.toggleSelection(i);
            commonFlexAdapter.notifyItemChanged(i);
          }
        }
      }
    }
  }
}
