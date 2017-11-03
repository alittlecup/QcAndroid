package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.items.StaffItem;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * Created by Paper on 2017/10/11.
 */

public abstract class BaseStaffListFragment extends BaseListFragment implements
    FlexibleAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  List<AbstractFlexibleItem> ret = new ArrayList<>();
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container ,savedInstanceState);
    LinearLayout root = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container,null);
    root.addView(view,1);
    toolbar = (Toolbar)root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView)root.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    initListener(this);
    return root;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    initData();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(getTitle());
  }

  @Override protected void addDivider() {
    rv.setBackgroundResource(R.color.white);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withDivider(R.drawable.divider_grey_left_margin)
      .withOffset(1)
      .withBottomEdge(true)
    );
  }

  abstract void initData();
  abstract String getTitle();


  protected void onGetData(List<Staff> staffs){
    stopRefresh();
    ret.clear();
    for (Staff staff : staffs) {
      ret.add(generateItem(staff));
    }
    setDatas(ret,1);
  }

  @Override public void onRefresh() {
    initData();
  }

  protected StaffItem generateItem(Staff staff){
    return new StaffItem(staff);
  }


  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "";
  }
}
