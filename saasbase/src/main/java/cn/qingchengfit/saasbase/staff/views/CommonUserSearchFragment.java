package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.animator.SlideInDownItemAnimator;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentCommonUserSearchBinding;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2018/1/10.
 */

public class CommonUserSearchFragment extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener,FlexibleAdapter.OnUpdateListener {

  @Inject IStaffModel iStaffModel;

  private List<CommonUserItem> items = new ArrayList<>();

  FragmentCommonUserSearchBinding db;
  CommonNoDataItem commonNoDataItem;
  CommonNoDataItem commonFirstItem;
  CommonFlexAdapter commonFlexAdapter;

  public static void start(BaseFragment fragment, List<? extends ICommonUser> d){
    CommonUserSearchFragment f = new CommonUserSearchFragment();
    f.initItems(d);
    fragment.routeTo(f,"search");
  }


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFirstItem = new CommonNoDataItem(R.drawable.vd_search_grey_14dp, "输入手机号或姓名搜索");
    commonNoDataItem = new CommonNoDataItem(R.drawable.vd_img_empty_universe, "抱歉，没有找到");
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    db = FragmentCommonUserSearchBinding.inflate(inflater);
    initRv();
    initSearch();
    try {
      ViewGroup v = (ViewGroup) db.layoutEarchView.getParent().getParent();
      v.setPadding(0, MeasureUtils.getStatusBarHeight(getContext()),0,0);
    }catch (Exception e){

    }
    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    db.etSearch.requestFocus();
    AppUtils.showKeyboard(getContext(),db.etSearch);
  }

  void initRv() {
    db.rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    db.rv.setItemAnimator(new SlideInDownItemAnimator());
    db.rv.addItemDecoration(
      new FlexibleItemDecoration(getContext()).withDefaultDivider(R.layout.item_common_user)
        .withOffset(1)
        .withBottomEdge(true));
    db.rv.setAdapter(commonFlexAdapter);
  }

  void initSearch() {
    db.btnClose.setOnClickListener(view -> db.etSearch.setText(""));
    RxTextView.afterTextChangeEvents(db.etSearch)
      .throttleLast(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<TextViewAfterTextChangeEvent>() {
        @Override public void onNext(TextViewAfterTextChangeEvent t) {
          commonFlexAdapter.setSearchText(t.editable().toString());
          if (TextUtils.isEmpty(t.editable().toString())) {
            commonFlexAdapter.clear();
          } else {
            commonFlexAdapter.filterItems(items,500);
          }
        }
      });
    commonFlexAdapter.addItem(commonFirstItem);
  }

  public void initItems(List<? extends ICommonUser> lis) {
    items.clear();
    for (ICommonUser li : lis) {
      items.add(new CommonUserItem(li));
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof CommonUserItem) {
      Object data = ((CommonUserItem) item).getUser();
      if (data instanceof StaffShip) {//工作人员
        routeTo("/detail/", StaffDetailParams.builder().staffShip(((StaffShip) data)).build());
      } else if (data instanceof Staff) {//教练
        //routeTo("/trainer/detail/", StaffDetailParams.builder().staffShip((Staff) data).build());
      } else if (data instanceof Invitation) {//邀请函
        Invitation invitation = (Invitation) ((CommonUserItem) item).getUser();
        if (invitation.getStatus() ==2 || invitation.getStatus() == 3)
          return true;
        DialogSheet.builder(getContext()).addButton("撤销邀请", R.color.red, view -> {
          RxRegiste(iStaffModel.cancelInvite(invitation.getId())
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetSubscribe<QcDataResponse>() {
              @Override public void onNext(QcDataResponse qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                  invitation.setStatus(3);
                  ((CommonUserItem) item).setUser(invitation);
                  commonFlexAdapter.notifyItemChanged(position);
                } else {
                  onShowError(qcResponse.getMsg());
                }
              }
            }));
        }).addButton("重新发送邀请",R.color.text_dark, view -> {
          routeTo("/reinvite/",StaffReInviteParams.builder().invitation(invitation).build());
        }).show();
      }
    }
    return true;
  }

  @Override public void onUpdateEmptyView(int size) {
    if (size == 0 && commonNoDataItem != null && commonFirstItem != null && commonFlexAdapter != null){
      if (TextUtils.isEmpty(commonFlexAdapter.getSearchText())){
        commonFlexAdapter.addItem(commonFirstItem);
      }else
        commonFlexAdapter.addItem(commonNoDataItem);
    }
  }
}
