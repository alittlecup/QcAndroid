package cn.qingchengfit.pos.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.ImageActionItem;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;

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
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.rv) RecyclerView rv;
  @BindView(R.id.tv_noti_count) TextView tvNotiCount;
  @BindView(R.id.btn_to_pay) Button btnToPay;
  @BindView(R.id.img_title_left) ImageView imgLeft;
  @BindView(R.id.layout_notification) LinearLayout layoutNotification;
  @Inject GymWrapper gymWrapper;
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
    toolbar.inflateMenu(R.menu.menu_setting);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        routeTo(AppUtils.getRouterUri(getContext(), "/setting/home/"), null);
        return true;
      }
    });
    toolbarLayout.setPadding(0,MeasureUtils.getStatusBarHeight(getContext()),0,0);
    toolbarTitle.setText(gymWrapper.name());
    PhotoUtils.smallCircle(imgLeft, gymWrapper.getCoachService().getPhoto());
    toolbarTitle.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showNoti(Math.abs(new Random(System.currentTimeMillis()).nextInt()) % 2);
      }
    });
  }

  public void showNoti(int count) {
    ViewCompat.animate(layoutNotification)
      .translationY(count > 0 ? 0f : MeasureUtils.dpToPx(100f, getResources()))
      .setDuration(100L)
      .setInterpolator(new DecelerateInterpolator())
      .start();
    tvNotiCount.setText(Integer.toString(count));
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
          routeTo(AppUtils.getRouterUri(getContext(), "/staff/saler/list/"), null);
          break;
        case R.drawable.vd_btn_home_logout:
          routeTo(AppUtils.getRouterUri(getContext(), "/exchange/home/"), null);
          break;
      }
    }
    return true;
  }

  /**
   * 直接收银
   */
  @OnClick(R.id.btn_cash) public void onBtnCashClicked() {
    routeTo("desk", "/home/", null);
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

  @OnClick(R.id.btn_to_pay) public void onViewClicked() {
    routeTo("bill", "/pay/request/list/", null);
  }
}
