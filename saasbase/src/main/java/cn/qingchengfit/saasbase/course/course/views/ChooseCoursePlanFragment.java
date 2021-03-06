package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.CoursePlan;
import cn.qingchengfit.saasbase.course.course.items.ChooseCoursePlanItem;
import cn.qingchengfit.saasbase.course.course.network.response.CoursePlans;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Leaf(module = "course", path = "/plan/list/")
public class ChooseCoursePlanFragment extends BaseListFragment
  implements FlexibleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  @Need public String mChosenId ;
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  @Inject ICourseModel courseModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    ViewGroup p = (ViewGroup) inflater.inflate(R.layout.layout_toolbar_container,container,false);
    toolbar = p.findViewById(R.id.toolbar);
    toolbarTitle = p.findViewById(R.id.toolbar_title);
    p.addView(v,1);
    initToolbar(toolbar);
    if (srl != null) srl.setOnRefreshListener(this);
    onRefresh();
    commonFlexAdapter.addListener(this);
    return p;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("选择默认课程计划");
  }

  @Override public String getFragmentName() {
    return ChooseCoursePlanFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public int getNoDataIconRes() {
    return R.drawable.vd_img_empty_universe;
  }

  @Override public String getNoDataStr() {
    return "暂无课程计划";
  }


  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof ChooseCoursePlanItem) {
      RxBus.getBus().post(((ChooseCoursePlanItem) item).coursePlan);
    }
    popBack();
    return true;
  }

  @Override public void onRefresh() {
    RxRegiste(courseModel.qcGetCoursePlan()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<CoursePlans>>() {
        @Override public void onNext(QcDataResponse<CoursePlans> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.plans != null) {
              datas.clear();
              datas.add(new ChooseCoursePlanItem(
                new CoursePlan.Builder().id(0L).name("不使用任何课程计划模板").build()));
              for (CoursePlan plan : qcResponse.data.plans) {
                datas.add(new ChooseCoursePlanItem(plan));
              }
            }
            commonFlexAdapter.updateDataSet(datas);
          } else {
            onShowError(qcResponse.getMsg());
          }
        }
      }));
  }
}
