package com.qingchengfit.fitcoach.fragment.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.bean.FunctionBean;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.responese.HomeStatement;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.events.EventChartTitle;
import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.course.batch.views.BatchListTrainerSpanParams;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragment;
import cn.qingchengfit.saascommon.widget.BaseStatementChartFragmentBuilder;
import cn.qingchengfit.saascommon.widget.GuideView;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.container.ContainerActivity;
import cn.qingchengfit.wxpreview.old.WebActivityForGuide;
import cn.qingchengfit.wxpreview.old.newa.MiniProgramUtil;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.PopFromBottomActivity;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.databinding.ManageFragmentBinding;
import com.qingchengfit.fitcoach.event.EventChooseGym;
import com.qingchengfit.fitcoach.http.bean.QcResponsePermission;
import com.qingchengfit.fitcoach.items.DailyWorkItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

public class Manage2Fragment extends SaasBindingFragment<ManageFragmentBinding, ManageViewModel>
    implements FlexibleAdapter.OnItemClickListener, AdapterView.OnItemClickListener {
  CommonFlexAdapter adapter;
  GymDetailChartAdapter mPageAdapter;
  private GuideView guideView;
  @Inject GymWrapper gymWrapper;

  @Override protected void subscribeUI() {
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
    mViewModel.permissionData.observe(this, data -> {
      String json = new Gson().toJson(data, QcResponsePermission.Data.class);
      PreferenceUtils.setPrefString(getContext(), App.coachid + "permission", json);
      upDatePremission(data);
    });
    mViewModel.coachServiceData.observe(this, this::setGymInfo);
    mViewModel.quitAction.observe(this, aVoid -> {
      getServer();
    });
    mViewModel.chartData.observe(this, chartdata -> {
      if (chartdata != null) {
        setChartData(chartdata);
      }
    });
    mViewModel.hasPrivate.observe(this, aBoolean -> {
      upDateItem(aBoolean, 0);
    });
    mViewModel.hasGroup.observe(this, aBoolean -> {
      upDateItem(aBoolean, 1);
    });
    mViewModel.miniProgramMutableLiveData.observe(this, miniProgram -> {
      if (miniProgram != null) {
        MiniProgramUtil.saveMiniProgream(getContext(), gymWrapper.getGymId(), miniProgram);
      }
    });
  }

  private void upDateItem(Boolean hasSetted, int pos) {
    if (hasSetted != null) {
      IFlexible item = adapter.getItem(pos);
      if (item instanceof DailyWorkItem) {
        ((DailyWorkItem) item).setRightTopText(hasSetted?"":"未设置");
      }
      adapter.notifyItemChanged(pos);
    }
  }

  private void setChartData(HomeStatement stats) {
    if (mPageAdapter != null) {
      if (stats != null) {
        if (stats.new_sells != null) mPageAdapter.setData(0, stats.new_sells.date_counts);
        if (stats.new_orders != null) mPageAdapter.setData(1, stats.new_orders.date_counts);
      }
    }
  }

  @Override
  public ManageFragmentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ManageFragmentBinding.inflate(inflater, container, false);
    initRecyclerView();
    initRecyclerData();
    initViewPager();
    initRxbus();
    checkPermission();
    initListener();
    getServer();
    isInit = true;
    SensorsUtils.trackScreen(this.getClass().getCanonicalName());
    return mBinding;
  }

  private DialogList dialogList;

  private void initListener() {
    mBinding.angleShow.setOnClickListener(v -> {
      Intent toGym = new Intent(getActivity(), PopFromBottomActivity.class);
      toGym.putExtra("service", gymWrapper.getCoachService());
      startActivity(toGym);
    });
    mBinding.actionFlow.setOnClickListener(v -> {
      if (dialogList == null) {
        dialogList = new DialogList(getContext());
        ArrayList<String> flows = new ArrayList<>();
        flows.add("离职退出该场馆");
        dialogList.list(flows, Manage2Fragment.this);
      }
      dialogList.show();
    });
    mBinding.showGym.setOnClickListener(v -> {
      guideToStudentPreview(mViewModel.previewUrl.getValue(),mViewModel.copyUrl.getValue());
    });
  }

  private void guideToStudentPreview(String preViewUrl, String copyUrl) {
    Intent toWebForGuide = new Intent(getActivity(), WebActivityForGuide.class);
    toWebForGuide.putExtra("url", preViewUrl);
    toWebForGuide.putExtra("copyurl", copyUrl);
    startActivity(toWebForGuide);
  }

  private void showGuide() {
    guideView = GuideView.getInstance();
    guideView.initGuide(getActivity(), "ManagerFragment");
    guideView.addGuide(R.layout.page_guide_manager);
    guideView.setNoScroll();
  }

  private void getServer() {
    Long ccid = 0l;
    try {
      ccid = PreferenceUtils.getPrefLong(getContext(), "coachservice_id", 0L);
    } catch (Exception e) {

    }
    String newCcid = PreferenceUtils.getPrefString(getContext(), "coachservice_id_str", "");
    if (ccid != 0 && TextUtils.isEmpty(newCcid)) {
      newCcid = ccid + "";
    }
    mViewModel.loadCoachService(newCcid);
  }

  private void checkPermission() {
    String s = PreferenceUtils.getPrefString(getContext(), App.coachid + "permission", "");
    if (s != null && !s.isEmpty()) {
      QcResponsePermission.Data d = new Gson().fromJson(s, QcResponsePermission.Data.class);
      upDatePremission(d);
    }
  }

  private void initRxbus() {
    RxBusAdd(EventChooseGym.class).subscribe(new Action1<EventChooseGym>() {
      @Override public void call(EventChooseGym coachService) {
        if (coachService != null) {
          gymWrapper.setCoachService(coachService.getCoachService());
          PreferenceUtils.setPrefString(getContext(), "coachservice_id_str",
              gymWrapper.getCoachService().getId());
          setGymInfo(coachService.getCoachService());
        }
      }
    }, new HttpThrowable());
  }

  private void setGymInfo(CoachService coachService) {
    //TODO 设置顶部title
    mBinding.title.setText(coachService.getName());
    PhotoUtils.smallCircle(mBinding.imgGymPhoto, coachService.getPhoto());
    mViewModel.loadPremission(App.coachid + "");
    mViewModel.loadGymWelcomeDeta();
    showGuide();
  }

  private void upDatePremission(QcResponsePermission.Data data) {
    CurentPermissions.newInstance().permissionList.clear();
    for (int i = 0; i < data.permissions.size(); i++) {
      CurentPermissions.newInstance().permissionList.put(data.permissions.get(i).key,
          data.permissions.get(i).value);
    }
  }

  private void initViewPager() {
    mBinding.vpCharts.setAdapter(
        mPageAdapter = new GymDetailChartAdapter(getChildFragmentManager()));
    mBinding.indicator.setViewPager(mBinding.vpCharts);
    RxBusAdd(EventChartTitle.class).subscribe(eventChartTitle -> {
      switch (eventChartTitle.getChartType()) {
        case 1:
          if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.COST_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return ;
          }
          Intent toCourseStatement = new Intent(getActivity(), FragActivity.class);
          toCourseStatement.putExtra("type", 0);
          toCourseStatement.putExtra("service", gymWrapper.getCoachService());
          startActivity(toCourseStatement);
          break;

        case 0:
          if (!CurentPermissions.newInstance()
              .queryPermission(PermissionServerUtils.PERSONAL_SALES_REPORT)) {
            showAlert(R.string.alert_permission_forbid);
            return ;
          }
          Intent tosale = new Intent(getActivity(), FragActivity.class);
          tosale.putExtra("type", 1);
          tosale.putExtra("service", gymWrapper.getCoachService());
          startActivity(tosale);

          break;
      }
    });
  }

  private void initRecyclerData() {
    List<DailyWorkItem> items = new ArrayList<>();
    DailyWorkItem privateSetting = new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.moudule_service_private).text("私教排期").build());
    items.add(privateSetting);
    items.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.moudule_service_group).text("团课排期").build()));
    items.add(new DailyWorkItem(new FunctionBean.Builder().resImg(R.drawable.ic_users_student)
        .text(getString(R.string.student))
        .build()));
    items.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ck_ic_modules_workbench_counter)
            .text("收银台")
            .build()));
    items.add(new DailyWorkItem(
        new FunctionBean.Builder().resImg(R.drawable.ic_function_more).text("管理功能").build()));
    items.add(new DailyWorkItem(new FunctionBean.Builder().build()));
    items.add(new DailyWorkItem(new FunctionBean.Builder().build()));
    items.add(new DailyWorkItem(new FunctionBean.Builder().build()));
    if (adapter != null) {
      adapter.updateDataSet(items);
    }
  }

  private void initRecyclerView() {
    SmoothScrollGridLayoutManager layoutManager =
        new SmoothScrollGridLayoutManager(getContext(), 4);
    mBinding.recyclerView.setLayoutManager(layoutManager);
    mBinding.recyclerView.setHasFixedSize(true);
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int position) {
    switch (position) {
      case 0:
        if (CurentPermissions.newInstance()
            .queryPermission(PermissionServerUtils.PRIARRANGE_CALENDAR)) {
          routeTo("course", "/batch/list/", BatchListTrainerSpanParams.builder().mType(1).build());
        } else {
          showAlert(getString(R.string.sorry_no_permission));
        }
        break;
      case 1:
        if (CurentPermissions.newInstance()
            .queryPermission(PermissionServerUtils.TEAMARRANGE_CALENDAR)) {

          routeTo("course", "/batch/list/", BatchListTrainerSpanParams.builder().mType(0).build());
        } else {
          showAlert(getString(R.string.sorry_no_permission));
        }
        break;
      case 2:
        if (CurentPermissions.newInstance()
            .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS)) {
          Intent toStudent = new Intent(getActivity(), FragActivity.class);
          toStudent.putExtra("type", 9);
          toStudent.putExtra("service", gymWrapper.getCoachService());
          startActivity(toStudent);
        } else {
          showAlert(getString(R.string.sorry_no_permission));
        }
        break;
      case 3:
        QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/home"))
            .call();
        break;
      case 4:
        Intent toStudent = new Intent(getActivity(), FragActivity.class);
        toStudent.putExtra("type", 15);
        toStudent.putExtra("service", gymWrapper.getCoachService());
        startActivity(toStudent);
        break;
    }
    return false;
  }

  //离职场馆的点击事件处理
  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (dialogList != null) {
      dialogList.dismiss();
      QuitGymFragment build = new QuitGymFragmentBuilder(gymWrapper.getCoachService()).build();
      build.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mViewModel.quitGym(App.coachid + "");
        }
      });
      build.show(getFragmentManager(), "");
    }
  }

  /**
   * 报表适配器
   */
  class GymDetailChartAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;

    public GymDetailChartAdapter(FragmentManager fm) {
      super(fm);
      this.mFm = fm;
    }

    @Override public int getCount() {
      return 2;
    }

    @Override public Fragment getItem(int position) {
      return new BaseStatementChartFragmentBuilder(position).build();
    }

    @Override public long getItemId(int position) {
      return position;
    }

    public String getItemName(int position) {
      return "android:switcher:" + R.id.vp_charts + ":" + position;
    }

    public void setData(int pos, List<FollowUpDataStatistic.DateCountsBean> datas) {
      Fragment f = mFm.findFragmentByTag(getItemName(pos));
      if (f != null && f instanceof BaseStatementChartFragment) {
        ((BaseStatementChartFragment) f).doData(datas);
      }
    }
  }
}
