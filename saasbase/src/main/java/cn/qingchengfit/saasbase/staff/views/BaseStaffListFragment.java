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
import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.items.CommonUserItem;
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

public abstract class BaseStaffListFragment extends BaseListFragment
  implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
  FlexibleAdapter.OnUpdateListener {
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  protected ViewGroup toolbarRoot;
  List<AbstractFlexibleItem> ret = new ArrayList<>();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout root = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, null);
    root.addView(view, 1);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    toolbarRoot = root.findViewById(R.id.toolbar_layout);
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
    rv.setItemAnimator(new FadeInUpItemAnimator());
    rv.setBackgroundResource(R.color.white);
    rv.addItemDecoration(
      new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey_left_margin,
        R.layout.item_common_user).withOffset(1).withBottomEdge(true));
  }

  protected abstract void initData();

  protected abstract String getTitle();

  protected void onGetData(List<? extends ICommonUser> staffs) {
    stopRefresh();
    ret.clear();
    for (ICommonUser staff : staffs) {
      ret.add(generateItem(staff));
    }
    setDatas(ret, 1);
  }

  @Override public void onRefresh() {
    initData();
    if (getParentFragment() != null && getParentFragment() instanceof StaffHomeFragment){
      ((StaffHomeFragment) getParentFragment()).freshData();
    }
  }

  protected CommonUserItem generateItem(ICommonUser staff) {
    return new CommonUserItem(staff);
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "";
  }

  @Override public void onUpdateEmptyView(int size) {

  }
}
