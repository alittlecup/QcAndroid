package cn.qingchengfit.saasbase.student.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.saasbase.student.utils.StudentCompareByAlphabet;
import cn.qingchengfit.saasbase.student.utils.StudentCompareJoinAt;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Collections;
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
 * Created by Paper on 2017/8/11.
 */
public class SimpleStudentListFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

  public static int ORDER_TYPE_REGIST = 0;//按注册顺序排序
  public static int ORDER_TYPE_ALPHABET = 1;//按字母表顺序排序
  protected CommonFlexAdapter commonFlexAdapter;
  private int orderType = 0;//默认顺序为 注册时间排序
  private List<QcStudentBean> qcStudentBeens = new ArrayList<>();
  private CommonNoDataItem commonNoDataItem;


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    RecyclerView recyclerView = new RecyclerView(getContext());
    recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    commonFlexAdapter.setStickyHeaders(true).setStickyHeaderElevation(1).setDisplayHeadersAtStartUp(true);
    recyclerView.setAdapter(commonFlexAdapter);
    commonNoDataItem = new CommonNoDataItem(getNoDataIconRes(),getNoDataStr());
    return recyclerView;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  public void setData(List<QcStudentBean> studentBeen) {
    if (studentBeen != null) {
      qcStudentBeens.clear();
      qcStudentBeens.addAll(studentBeen);
    }
    addItems();
  }

  public void setData(List<QcStudentBean> studentBeen, int type) {
    if (studentBeen != null) {
      qcStudentBeens.clear();
      qcStudentBeens.addAll(studentBeen);
    }
    changeOrderType(type);
  }

  public void changeOrderType(int type) {
    orderType = type;
    addItems();
  }

  private void addItems() {
    if (commonFlexAdapter != null) {
      if (orderType == 0) {
        Collections.sort(qcStudentBeens, new StudentCompareByAlphabet());//按字母排序
        if (qcStudentBeens.size() > 0){
          String head = qcStudentBeens.get(0).head();
          commonFlexAdapter.addItem(new StickerDateItem(head.toUpperCase()));
          for (QcStudentBean qcStudentBeen : qcStudentBeens) {
            if (!qcStudentBeen.head().equalsIgnoreCase(head)){
              commonFlexAdapter.addItem(new StickerDateItem(qcStudentBeen.head().toUpperCase()));
            }
            commonFlexAdapter.addItem(instanceItem(qcStudentBeen));
          }
        }else {
          commonFlexAdapter.addItem(new CommonNoDataItem(R.drawable.vd_img_empty_universe,"暂无会员"));
        }
      } else {//按注册时间排序
        Collections.sort(qcStudentBeens, new StudentCompareJoinAt());
        if (qcStudentBeens.size() > 0){
          for (QcStudentBean qcStudentBeen : qcStudentBeens) {
            commonFlexAdapter.addItem(instanceItem(qcStudentBeen));
          }
        }else {
          commonFlexAdapter.addItem(commonNoDataItem);
        }
      }
    }
  }
  public void filter(String s){
    commonFlexAdapter.setSearchText(s);
    commonFlexAdapter.filterItems(commonFlexAdapter.getMainItems());
  }

  protected IFlexible instanceItem(QcStudentBean qcStudentBean){
    return new StudentItem(qcStudentBean);
  }

  @Override public String getFragmentName() {
    return SimpleStudentListFragment.class.getName();
  }

   protected int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

   protected String getNoDataStr() {
    return "暂无会员";
  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof StudentItem) {

    }
    return true;
  }
}
