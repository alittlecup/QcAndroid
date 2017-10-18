package cn.qingchengfit.pos.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.ImageActionItem;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/10/9.
 */
public class PosMainFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.rv) RecyclerView rv;
  private CommonFlexAdapter flexAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    List<ImageActionItem> datas = new ArrayList<>();
    datas.add(new ImageActionItem(R.drawable.vd_btn_home_card, "会员卡种类管理"));
    datas.add(new ImageActionItem(R.drawable.vd_btn_home_bill, "账单"));
    datas.add(new ImageActionItem(R.drawable.vd_btn_home_sell, "销售人员"));
    datas.add(new ImageActionItem(R.drawable.vd_btn_home_logout, "交班"));
    flexAdapter = new CommonFlexAdapter(datas, this);
    GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    rv.setLayoutManager(manager);
    rv.setAdapter(flexAdapter);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {

  }

  @Override public String getFragmentName() {
    return PosMainFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = flexAdapter.getItem(position);
    if (item instanceof ImageActionItem) {
      switch (((ImageActionItem) item).getResDrawable()) {
        case R.drawable.vd_btn_home_card:
          routeTo(AppUtils.getRouterUri(getContext(), "/card/cardtpl/list/"), null);
          break;
        case R.drawable.vd_btn_home_bill:
          routeTo(AppUtils.getRouterUri(getContext(), "/bill/home/list/"), null);
          break;
        case R.drawable.vd_btn_home_sell:
          routeTo(AppUtils.getRouterUri(getContext(), "/staff/seller/list/"), null);
          break;
        case R.drawable.vd_btn_home_logout:
          break;
      }
    }
    return true;
  }

  /**
   * 直接收银
   */
  @OnClick(R.id.btn_cash) public void onBtnCashClicked() {

  }

  /**
   * 新开会员卡
   */
  @OnClick(R.id.btn_create_card) public void onBtnCreateCardClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/card/choose/cardtpl/"), null);
  }

  /**
   * 会员卡续卡
   */
  @OnClick(R.id.btn_charge_card) public void onBtnChargeCardClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/card/list/home/"), null);
  }
}
