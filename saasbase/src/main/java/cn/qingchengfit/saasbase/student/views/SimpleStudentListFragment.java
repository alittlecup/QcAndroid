package cn.qingchengfit.saasbase.student.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.saasbase.student.utils.StudentCompareByAlphabet;
import cn.qingchengfit.saasbase.student.utils.StudentCompareJoinAt;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AlphabetLessView;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHeader;
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
    implements FlexibleAdapter.OnItemClickListener,FlexibleAdapter.OnUpdateListener {

  public static int ORDER_TYPE_REGIST = 0;//按注册顺序排序
  public static int ORDER_TYPE_ALPHABET = 1;//按字母表顺序排序
  protected CommonFlexAdapter commonFlexAdapter;
  private int orderType = 0;//默认顺序为 注册时间排序
  private List<QcStudentBean> qcStudentBeens = new ArrayList<>();
  private CommonNoDataItem commonNoDataItem;
  protected List<IFlexible> mDatas = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FrameLayout frameLayout = new FrameLayout(getContext());
    RecyclerView recyclerView = new RecyclerView(getContext());
    recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    commonFlexAdapter
      .setStickyHeaders(true)
      .setStickyHeaderElevation(1)
      .setDisplayHeadersAtStartUp(true);
    recyclerView.setAdapter(commonFlexAdapter);
    //recyclerView.setItemAnimator(new FadeInUpItemAnimator());
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withDivider(R.drawable.divider_grey_left_margin,R.layout.item_saas_staff)
      .withOffset(1)
      .withBottomEdge(true)
    );

    commonNoDataItem = new CommonNoDataItem(getNoDataIconRes(),getNoDataStr());
    frameLayout.addView(recyclerView);
    return frameLayout;
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
        for (QcStudentBean qcStudentBeen : qcStudentBeens) {
          if (qcStudentBeen.head() != null &&( !AlphabetLessView.Alphabet.contains(qcStudentBeen.head())
            || TextUtils.isEmpty(qcStudentBeen.head()))){
            qcStudentBeen.setHead("~");
          }
        }
        Collections.sort(qcStudentBeens, new StudentCompareByAlphabet());//按字母排序
        mDatas.clear();
        if (qcStudentBeens.size() > 0){
          String head = null;
          //mDatas.add(sh);
          StickerDateItem sh = null;
          for (QcStudentBean qcStudentBeen : qcStudentBeens) {
            if (head == null || !qcStudentBeen.head().equalsIgnoreCase(head)){
              head = qcStudentBeen.head();
              sh = new StickerDateItem(head.toUpperCase());
              mDatas.add(new StickerDateItem(qcStudentBeen.head().toUpperCase()));
            }
            mDatas.add(instanceItem(qcStudentBeen,null));
          }
        }else {
          mDatas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe,"暂无会员"));
        }
      } else {//按注册时间排序
        Collections.sort(qcStudentBeens, new StudentCompareJoinAt());
        if (qcStudentBeens.size() > 0){
          for (QcStudentBean qcStudentBeen : qcStudentBeens) {
            mDatas.add(instanceItem(qcStudentBeen,null));
          }
        }else {
          mDatas.add(commonNoDataItem);
        }
      }

      commonFlexAdapter.updateDataSet(mDatas,false);
    }
  }
  public void filter(String s){
    commonFlexAdapter.setSearchText(s);
    commonFlexAdapter.filterItems(mDatas);
  }

  protected IFlexible instanceItem(QcStudentBean qcStudentBean,IHeader iHeader){
    return new StudentItem(qcStudentBean,iHeader);
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

  @Override public void onUpdateEmptyView(int size) {

  }
}
