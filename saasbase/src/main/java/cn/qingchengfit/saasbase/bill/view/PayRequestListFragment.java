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
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.bill.beans.PayRequest;
import cn.qingchengfit.saasbase.bill.items.PayRequestItem;
import cn.qingchengfit.saasbase.bill.presenter.PayRequestListPresenter;
import cn.qingchengfit.saasbase.events.EventPayRequest;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
@Leaf(module = "bill", path = "/pay/request/list/") public abstract class PayRequestListFragment
  extends BaseListFragment implements
  SwipeRefreshLayout.OnRefreshListener,FlexibleAdapter.EndlessScrollListener,PayRequestListPresenter.MVPView {
  protected Toolbar toolbar;
  protected TextView toolbarTitle;


  @Inject PayRequestListPresenter presenter;

  @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    LinearLayout root =
      (LinearLayout) inflater.inflate(R.layout.layout_toolbar_container, container, false);
    toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) root.findViewById(R.id.toolbar_title);
    root.addView(v, 1);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    initListener(this);
    RxBusAdd(EventSaasFresh.PayRequestList.class).observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventSaasFresh.PayRequestList>() {
        @Override public void onNext(EventSaasFresh.PayRequestList payRequestList) {
          onRefresh();
        }
      });

    RxBusAdd(EventPayRequest.class)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventPayRequest>() {
        @Override public void onNext(EventPayRequest eventPayRequest) {
          if (eventPayRequest.type == 0){
            presenter.pay(eventPayRequest.payRequest);
          }else {
            presenter.cancelTask(eventPayRequest.payRequest.task_no);
          }
        }
      });
    return root;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    srl.setRefreshing(true);
    onRefresh();
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
    if (currentPage < presenter.getTotalPage()) {
      presenter.loadMoreData(currentPage+1);
    }else {
      commonFlexAdapter.onLoadMoreComplete(null,500);
    }
  }

  @Override public void onGetData(List<PayRequest> datas, int page) {
    stopRefresh();
    if (page == 1 && (datas == null || datas.size() == 0)) {
      commonFlexAdapter.clear();
      commonFlexAdapter.addItem(new CommonNoDataItem(getNoDataIconRes(), getNoDataStr()));
    } else {
      List<AbstractFlexibleItem> items = new ArrayList<>();
      for (PayRequest data : datas) {
        items.add(new PayRequestItem(data));
      }
      commonFlexAdapter.onLoadMoreComplete(items,500);
    }
    if (datas == null)
      commonFlexAdapter.onLoadMoreComplete(null,500);
  }

  /**
   * 具体支付实现在各个App不同
   */
  @Override public abstract void onPay(PayRequest payRequest);

  @Override public void onRemoveTaskNo(String taskNo) {
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof PayRequestItem){
        if (((PayRequestItem) item).getPayRequest().task_no.equalsIgnoreCase(taskNo)){
          commonFlexAdapter.removeItem(i);
          break;
        }
      }
    }
  }
}
