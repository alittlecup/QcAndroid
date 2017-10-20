package cn.qingchengfit.saasbase.bill.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.bill.beans.PayRequest;
import cn.qingchengfit.saasbase.bill.items.PayRequestItem;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/10/9.
 */
@Leaf(module = "bill", path = "/pay/request/list/") public class PayRequestListFragment
  extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener,FlexibleAdapter.EndlessScrollListener {
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  protected int totalPage = 1;
  @Inject IBillModel billModel;

  @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout root =
      (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, container, false);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    root.addView(v, 1);
    initToolbar(toolbar);
    initListener(this);
    RxBusAdd(EventRecycleClick.class).observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventRecycleClick>() {
        @Override public void onNext(EventRecycleClick eventRecycleClick) {
          //点击去支付
          IFlexible iFlexible = commonFlexAdapter.getItem(eventRecycleClick.postion);
          if (iFlexible instanceof PayRequestItem) {
            onPay(((PayRequestItem) iFlexible).getPayRequest());
          }
        }
      });
    RxBusAdd(EventSaasFresh.PayRequestList.class).observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventSaasFresh.PayRequestList>() {
        @Override public void onNext(EventSaasFresh.PayRequestList payRequestList) {
          onRefresh();
        }
      });
    return root;
  }

  protected void onPay(PayRequest payRequest){

  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("支付请求");
  }

  @Override public void onRefresh() {
    initLoadMore(1000,this);
    onLoadMore(0,0);
  }




  @Override public String getFragmentName() {
    return PayRequestListFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "暂无支付请求";
  }

  @Override public void noMoreLoad(int newItemsSize) {
    //commonFlexAdapter.onLoadMoreComplete(null,500);
  }

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    if (currentPage < totalPage) {
      RxRegiste(billModel.getPayRequestList(currentPage+1)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<PayRequestListWrap>>() {
          @Override public void onNext(QcDataResponse<PayRequestListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              // TODO: 2017/10/20
              //
              commonFlexAdapter.onLoadMoreComplete(null,500);
              totalPage = qcResponse.data.pages;
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }));
    }else {
      commonFlexAdapter.onLoadMoreComplete(null,500);
    }
  }
}
