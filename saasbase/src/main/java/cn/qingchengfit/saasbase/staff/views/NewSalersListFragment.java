package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.saasbase.staff.items.SalerItem;
import cn.qingchengfit.saasbase.staff.items.StaffItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2017/10/18.
 */
@Leaf(module = "staff", path = "/saler/list/") public class NewSalersListFragment
  extends BaseStaffListFragment {

  @Inject IStaffModel staffModel;
  @Inject LoginStatus loginStatus;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RxBus.getBus().register(EventSaasFresh.StaffList.class)
        .compose(this.<EventSaasFresh.StaffList>bindToLifecycle())
        .compose(this.<EventSaasFresh.StaffList>doWhen(FragmentEvent.CREATE_VIEW))
        .subscribe(new BusSubscribe<EventSaasFresh.StaffList>() {
          @Override public void onNext(EventSaasFresh.StaffList eventSaasFresh) {
            onRefresh();
          }
        });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.getMenu().clear();
    toolbar.inflateMenu(R.menu.menu_search_and_add);
    RxMenuItem.clicks(toolbar.getMenu().findItem(R.id.action_add))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          routeTo("/add/", null);
        }
      });
    RxMenuItem.clicks(toolbar.getMenu().findItem(R.id.action_search))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          showSearch(toolbarRoot);
        }
      });
    initSearch(toolbarRoot, "输入销售姓名或者手机号来");
  }

  @Override public void onTextSearch(String text) {
    commonFlexAdapter.setSearchText(text);
    commonFlexAdapter.filterItems(ret, 500);
  }

  @Override protected void initData() {
    RxRegiste(staffModel.getSalers()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<SalerListWrap>>() {
        @Override public void onNext(QcDataResponse<SalerListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            onGetData(qcResponse.data.sellers);
            //if (!TextUtils.isEmpty(commonFlexAdapter.getSearchText())){
            //  onTextSearch(commonFlexAdapter.getSearchText());
            //}
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override protected CommonUserItem generateItem(ICommonUser staff) {
    return new SalerItem(staff);
  }


  @Override protected String getTitle() {
    return "销售";
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = commonFlexAdapter.getItem(position);
    if (iFlexible instanceof StaffItem) {
      Staff staff = ((StaffItem) iFlexible).getStaff();
      try {
        if (staff.is_superuser && !staff.getPhone()
          .equalsIgnoreCase(loginStatus.getLoginUser().getPhone())) {
          showAlert("该用户为超级管理员\n仅超级管理员本人可以查看");
          return true;
        }
      } catch (Exception e) {
        return true;
      }

      routeTo("/saler/data/", new cn.qingchengfit.saasbase.staff.views.SalerDataParams().staff(
          ((StaffItem) iFlexible).getStaff()).build());
    }

    return true;
  }

}
