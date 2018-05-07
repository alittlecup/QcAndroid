package cn.qingchengfit.saasbase.course.batch.views;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.event.CourseTypeEvent;
import cn.qingchengfit.saasbase.course.course.bean.CourseType;
import cn.qingchengfit.saasbase.course.course.items.CourseItem;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2018/4/16.
 */

@Leaf(module = "course", path = "/batch/choose/course") public class BatchCourseChooseFragment
    extends CourseChooseFragment {

  @Inject ViewModelProvider.Factory factory;
  @Need ArrayList<CourseType> dataList;
  @Need Boolean isPrivate;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    toolbarTitle.setText(isPrivate ? "选择私教课":"选择团课");
    floatingActionButton.setVisibility(View.GONE);
    return view;
  }

  @Override public void onRefresh() {

    //viewModel.courseListValue.addOnPropertyChangedCallback(
    //    new Observable.OnPropertyChangedCallback() {
    //      @Override public void onPropertyChanged(Observable sender, int propertyId) {
    List<AbstractFlexibleItem> datas = new ArrayList();
    //ArrayList<CourseType> dataList = (ArrayList<CourseType>) getArguments().get("courseList");
    srl.setRefreshing(false);
    if (dataList == null || dataList.size() == 0) {
      datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无课程种类"));
    } else {
      for (CourseType course : dataList) {
        datas.add(new CourseItem(course));
      }
    }
    commonFlexAdapter.updateDataSet(datas, true);
    //  }
    //});

    //RxRegiste(courseApi.qcGetCourses(mIsPrivate)
    //    .onBackpressureLatest()
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .map(qcResponse -> {
    //      List<AbstractFlexibleItem> datas = new ArrayList();
    //
    //      srl.setRefreshing(false);
    //      if (ResponseConstant.checkSuccess(qcResponse)) {
    //        if (qcResponse.data != null && qcResponse.data.courses != null) {
    //          if (qcResponse.data.courses.size() == 0) {
    //            datas.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无课程种类"));
    //          }else {
    //            datas.add(0, new CourseItem(createAllCourse()));
    //            for (CourseType course : qcResponse.data.courses) {
    //              datas.add(new CourseItem(course));
    //            }
    //          }
    //        }
    //      } else {
    //        onShowError(qcResponse.getMsg());
    //      }
    //      return datas;
    //    }).subscribe(iFlexibles -> {
    //      commonFlexAdapter.updateDataSet(iFlexibles, true);
    //    }, new NetWorkThrowable()));
  }

  @Override public boolean onItemClick(int position) {
    //return super.onItemClick(position);
    if (commonFlexAdapter.getItem(position) instanceof CourseItem){
      if (((CourseItem) commonFlexAdapter.getItem(position)).getData() != null){
        RxBus.getBus()
            .post(new CourseTypeEvent(
                ((CourseItem) commonFlexAdapter.getItem(position)).getData()));
      }
    }
    popBack();
    return true;
  }
}
