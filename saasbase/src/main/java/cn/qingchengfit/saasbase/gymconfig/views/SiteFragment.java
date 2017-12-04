package cn.qingchengfit.saasbase.gymconfig.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.gymconfig.IGymConfigModel;
import cn.qingchengfit.saasbase.gymconfig.item.SiteItem;
import cn.qingchengfit.saasbase.gymconfig.network.response.SpaceListWrap;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/12/1.
 */
public class SiteFragment extends BaseListFragment
  implements SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.OnItemClickListener,
  FlexibleAdapter.OnUpdateListener {

  @Inject IGymConfigModel gymConfigModel;
  private int queryType = -1;

  public int getQueryType() {
    return queryType;
  }

  public void setQueryType(int queryType) {
    this.queryType = queryType;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override protected void addDivider() {
    rv.addItemDecoration(
      new FlexibleItemDecoration(getContext()).withDefaultDivider().withBottomEdge(true));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    onRefresh();
  }

  @Override public String getFragmentName() {
    return SiteFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "暂无场地";
  }

  @Override public void onRefresh() {
    RxRegiste(gymConfigModel.getSites()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<SpaceListWrap>>() {
        @Override public void onNext(QcDataResponse<SpaceListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            List<IFlexible> datas = new ArrayList<>();
            if (qcResponse.data.spaces != null){
              for (Space space : qcResponse.data.spaces) {
                if (queryType <0 || (space.is_support_private() && queryType == 1)|| (space.is_support_team() && queryType == 0))
                  datas.add(generateItem(space));
              }
            }
            setDatas(datas,1);
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  protected SiteItem generateItem(Space space){
    return new SiteItem(space);
  }

  @Override public boolean onItemClick(int position) {
    // TODO: 2017/12/1 跳去详情页
    return true;
  }

  @Override public void onUpdateEmptyView(int size) {
    if (size == 0){

    }
  }
}
