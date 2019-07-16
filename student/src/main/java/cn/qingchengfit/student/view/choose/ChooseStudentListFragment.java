package cn.qingchengfit.student.view.choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.item.ChosenStudentItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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

  public static ChooseStudentListFragment newInstance(int choosetype) {
    Bundle args = new Bundle();
    args.putInt("c", choosetype);
    ChooseStudentListFragment fragment = new ChooseStudentListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    commonFlexAdapter.setMode(getArguments().getInt("c", SelectableAdapter.Mode.MULTI));
    return v;
  }

  private FlexibleAdapter.OnItemClickListener onItemClickListener;

  public void setOnItemClickListener(FlexibleAdapter.OnItemClickListener listener) {
    this.onItemClickListener = listener;
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof StudentItem) {
      int lastChoose = -1;

      if (commonFlexAdapter.getMode() == 1) {//单选模式
        lastChoose = commonFlexAdapter.getSelectedPositions().size() > 0
            ? commonFlexAdapter.getSelectedPositions().get(0) : -1;
        if (onItemClickListener != null) {
          return onItemClickListener.onItemClick(i);
        }
      }
      StudentItem item = (StudentItem) commonFlexAdapter.getItem(i);
      if (item instanceof ChosenStudentItem) {
        ((ChosenStudentItem) item).setSelected(!((ChosenStudentItem) item).isSelected());
      }
      if (studentIdList != null) {
        if (studentIdList.contains(item.getId())) {
          studentIdList.remove(item.getId());
        } else {
          studentIdList.add(item.getId());
        }
      }
      if(qcStudentBeans!=null&&commonFlexAdapter.isSelected(i)){
        QcStudentBean qcStudentBean =
            ((StudentItem) commonFlexAdapter.getItem(i)).getQcStudentBean();
        if(qcStudentBeans.contains(qcStudentBean)){
          qcStudentBeans.remove(qcStudentBean);
        }
      }
      commonFlexAdapter.toggleSelection(i);
      commonFlexAdapter.notifyItemChanged(i);
      commonFlexAdapter.notifyItemChanged(lastChoose);
    }
    return true;
  }

  public List<QcStudentBean> getQcStudentBeans() {
    return qcStudentBeans;
  }

  private List<QcStudentBean >qcStudentBeans=new ArrayList<>();
  public void setSelctedStudents(List<QcStudentBean> list){
    qcStudentBeans=list;
  }

  public StudentItem getItem(int position) {
    if (commonFlexAdapter == null) return null;
    return (StudentItem) commonFlexAdapter.getItem(position);
  }

  @Override protected IFlexible instanceItem(QcStudentBean qcStudentBean, IHeader iHeader) {
    return new ChosenStudentItem(qcStudentBean, iHeader);
  }

  public List<QcStudentBean> getSelectedStudent() {
    List<QcStudentBean> ret = new ArrayList<>();
    for (int i = 0; i < mDatas.size(); i++) {
      IFlexible item = mDatas.get(i);
      if (item instanceof ChosenStudentItem && ((ChosenStudentItem) item).isSelected()) {
        ret.add(((ChosenStudentItem) item).getQcStudentBean());
      }
    }
    return ret;
  }

  public int getSelectedCount() {
    return commonFlexAdapter.getSelectedItemCount();
  }

  public void selectStudent(ArrayList<String> studentIdList) {
    this.studentIdList = studentIdList;
    if (studentIdList != null) {
      for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
        IFlexible item = commonFlexAdapter.getItem(i);
        if (item instanceof ChosenStudentItem) {
          if (studentIdList.contains(((ChosenStudentItem) item).getId())) {
            ((ChosenStudentItem) item).setSelected(true);
            if (!commonFlexAdapter.isSelected(i)) {
              commonFlexAdapter.addSelection(i);
            }
            commonFlexAdapter.notifyItemChanged(i);
          }
        }
      }
    }
  }

  @Override public void onUpdateEmptyView(int size) {
    if (studentIdList != null && studentIdList.size() > 0) {
      selectStudent(studentIdList);
    }

  }
}
