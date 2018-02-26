package cn.qingchengfit.saasbase.coach.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import cn.qingchengfit.animator.FadeInUpItemAnimator;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.items.CommonUserItem;
import cn.qingchengfit.saasbase.staff.items.StaffSelectSingleItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.views.BaseStaffListFragment;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CrashUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/10 2016.
 */
public class CoachListFragment extends BaseStaffListFragment {

  @Inject IPermissionModel serPermisAction;
  @Inject IStaffModel staffModel;
  @Inject LoginStatus loginStatus;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }


  @Override public void initToolbar(@NonNull final Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("教练");
    toolbar.getMenu().clear();
    toolbar.inflateMenu(R.menu.menu_search);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_flow) {
          //TODO 移动BottomSheetListDialogFragment
          //BottomSheetListDialogFragment.start(CoachListFragment.this, RESULT_FLOW, new String[] { "教练权限设置" });
        }else if (item.getItemId() == R.id.action_search){
          showSearch(toolbarRoot);
        }
        return true;
      }
    });
    initSearch(toolbarRoot,"搜索教练");
  }

  @Override public void onTextSearch(String text) {
    commonFlexAdapter.setSearchText(text != null?text:"");
    commonFlexAdapter.filterItems();
  }

  @Override protected void addDivider() {
    super.addDivider();
    rv.setItemAnimator(new FadeInUpItemAnimator());
  }

  public void initData() {
    RxRegiste(staffModel.getTrainers()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<StaffShipsListWrap>>() {
        @Override public void onNext(QcDataResponse<StaffShipsListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.staffships != null){
              List<CommonUserItem> staffItems  = new ArrayList<>();
              for (StaffShip coach : qcResponse.data.staffships) {
                try {
                  coach.id = coach.user.id;
                  staffItems.add(generateItem(coach));
                }catch (Exception e){
                  CrashUtils.sendCrash(e);
                }
              }
              setDatas(staffItems ,1);
            }
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override public void setDatas(List<? extends IFlexible> ds, int page) {
      commonFlexAdapter.updateDataSet(ds,true);
  }

  @Override protected CommonUserItem generateItem(ICommonUser staff) {
    return new StaffSelectSingleItem(staff);
  }

  @Override protected String getTitle() {
    return "教练";
  }

  @Override public int getFbIcon() {
    return R.drawable.ic_add_staff;
  }

  @Override public void onClickFab() {
    if (!serPermisAction.check(PermissionServerUtils.COACHSETTING_CAN_WRITE)) {
        showAlert(R.string.alert_permission_forbid);
        return;
    }
    routeTo("/trainer/add/",null);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 11) {
        initData();
      }
    }
  }

  @Override public String getFragmentName() {
    return CoachListFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    // TODO: 2017/11/30 跳去详情
    return false;
  }
}
