package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.staff.items.StaffItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
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
 * MMMMxMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/10/10.
 */
@Leaf(module = "staff", path = "/choose/saler/") public class ChooseSalerFragment
    extends BaseListFragment
    implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  @Inject IStaffModel staffModel;
  @Need Boolean hasNoStaff = true;
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  protected ViewGroup toolbarLayout;
  protected List<AbstractFlexibleItem> ret = new ArrayList<>();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    initListener(this);
    LinearLayout root = (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, null);
    root.addView(view, 1);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    toolbarLayout = root.findViewById(R.id.toolbar_layout);
    initToolbar(toolbar);
    return root;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择业绩归属");
    initSearch(toolbarLayout, "输入姓名搜索");
    toolbar.inflateMenu(R.menu.menu_search);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {
            showSearch(toolbarLayout);
          }
        });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    if (srl != null) srl.setRefreshing(true);
    onRefresh();
  }

  @Override public void onTextSearch(String text) {
    commonFlexAdapter.setSearchText(text);
    commonFlexAdapter.filterItems(ret, 500);
  }

  @Override public String getFragmentName() {
    return ChooseSalerFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "";
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof StaffItem) {
      RxBus.getBus().post(((StaffItem) item).getStaff());
    }
    getActivity().onBackPressed();
    return true;
  }

  @Override public void onRefresh() {

    RxRegiste(staffModel.getSalers()
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<SalerListWrap>>() {
          @Override public void onNext(QcDataResponse<SalerListWrap> qcResponse) {
            stopRefresh();
            if (ResponseConstant.checkSuccess(qcResponse)) {
              ret.clear();
              if (hasNoStaff) {
                Staff no = new Staff("无销售", "", Constants.AVATAR_COACH_MALE, 0);
                no.setId("0");
                ret.add(new StaffItem(no));
              }
              for (Staff user : qcResponse.data.sellers) {
                ret.add(new StaffItem(user));
              }
              onTextSearch(commonFlexAdapter.getSearchText());
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }));
  }
}
