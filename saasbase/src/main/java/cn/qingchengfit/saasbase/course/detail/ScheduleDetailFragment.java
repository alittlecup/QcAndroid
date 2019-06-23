package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleDetailBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "course", path = "/schedule/detail") public class ScheduleDetailFragment
    extends SaasBindingFragment<FragmentScheduleDetailBinding, CourseDetailViewModel> {

  CommonFlexAdapter orderAdapter;
  CommonFlexAdapter photoAdapter;

  @Need String scheduleID;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override protected void subscribeUI() {
    mViewModel.detail.observe(this, this::updateDetailView);
    mViewModel.detailOrders.observe(this, this::updateOrderView);
    mViewModel.detailPhotos.observe(this, this::updatePhotoView);
  }

  private void updateDetailView(ScheduleDetail scheduleDetail) {
    if (scheduleDetail != null) {
      mBinding.courseName.setText(scheduleDetail.getCourse().getName());
      String during = DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(scheduleDetail.start))
          + "-"
          + DateUtils.Date2MMDDHHmm(DateUtils.formatDateFromServer(scheduleDetail.end));
      mBinding.courseTime.setText(makeSpanString("时间：", during));
      mBinding.courseGymName.setText(makeSpanString("场馆：", scheduleDetail.getShop().name));
      mBinding.courseClass.setText(makeSpanString("场地：", scheduleDetail.getSpace().getName()));
      mBinding.courseTrainerName.setText(scheduleDetail.getTeacher().getUsername());
      PhotoUtils.smallCircle(mBinding.imgTrainerPhoto, scheduleDetail.getTeacher().getAvatar());
      PhotoUtils.origin(mBinding.imgCourse, scheduleDetail.getCourse().getPhoto());
      mBinding.trainerScore.setRating(Float.valueOf(scheduleDetail.teacher_score));
    }
  }

  private String makeSpanString(String head, String content) {
    return new SpanUtils().append(head)
        .setForegroundColor(getResources().getColor(R.color.text_grey))
        .append(content)
        .setForegroundColor(getResources().getColor(R.color.text_warm))
        .create()
        .toString();
  }

  private void updateOrderView(ScheduleOrders scheduleOrders) {
    if (scheduleOrders == null
        || scheduleOrders.orders == null
        || scheduleOrders.orders.isEmpty()) {
      mBinding.flOrders.setVisibility(View.VISIBLE);
    } else {
      mBinding.flOrders.setVisibility(View.GONE);
      List<ScheduleOrders.ScheduleOrder> orders = scheduleOrders.orders;
      mBinding.tvOrderCount.setText("预约人数（" + orders.size() + ")");
      List<CircleImageItem> items = new ArrayList<>(orders.size());
      for (ScheduleOrders.ScheduleOrder order : orders) {
        items.add(new CircleImageItem(order.getUser().getAvatar()));
      }
      orderAdapter.updateDataSet(items);
    }
  }

  private void updatePhotoView(SchedulePhotos schedulePhotos) {
    if (schedulePhotos == null
        || schedulePhotos.photos == null
        || schedulePhotos.photos.isEmpty()) {
      mBinding.flPhotos.setVisibility(View.VISIBLE);
    } else {
      mBinding.flPhotos.setVisibility(View.GONE);
      List<SchedulePhoto> photos = schedulePhotos.photos;
      mBinding.tvPhotoCount.setText("课程相册（" + photos.size() + ")");
      List<SquareImageItem> items = new ArrayList<>(photos.size());
      for (SchedulePhoto photo : photos) {
        items.add(new SquareImageItem(photo.getPhoto()));
      }
      photoAdapter.updateDataSet(items);
    }
  }

  @Override
  public FragmentScheduleDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentScheduleDetailBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    mViewModel.loadCourseDetail(scheduleID);
    mViewModel.loadCoursePhotos(scheduleID);
    mViewModel.loadCouseOrders(scheduleID);
    mBinding.setFragment(this);
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.recyclerOrders.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mBinding.recyclerOrders.setAdapter(orderAdapter = new CommonFlexAdapter(new ArrayList()));

    mBinding.recyclerPhotos.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mBinding.recyclerPhotos.setAdapter(photoAdapter = new CommonFlexAdapter(new ArrayList()));
  }

  public void routeAllOrders(View view) {

  }

  public void routeAllPhotos(View view) {

  }

  public void routeAddPhotos(View view) {

  }

  public void routeAddOrder(View view) {

  }

  public void routeSignOrder(View view) {

  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("课程详情"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
