package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleDetailBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "course", path = "/schedule/detail") public class ScheduleDetailFragment
    extends SaasBindingFragment<FragmentScheduleDetailBinding, ScheduleDetailVM>
    implements FlexibleAdapter.OnItemClickListener {

  CommonFlexAdapter orderAdapter;
  CommonFlexAdapter photoAdapter;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus status;
  @Need CoachService service;

  @Need String scheduleID;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override protected void subscribeUI() {
    mViewModel.detail.observe(this, this::updateDetailView);
    mViewModel.detailOrders.observe(this, this::updateOrderView);
    mViewModel.detailPhotos.observe(this, this::updatePhotoView);
    mViewModel.signOpen.observe(this, aBoolean -> {
      mBinding.groupSign.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    });
  }

  private void updateDetailView(ScheduleDetail scheduleDetail) {
    if (scheduleDetail != null) {
      mBinding.courseName.setText(scheduleDetail.getCourse().getName());
      String during = DateUtils.Date2HHmm(DateUtils.formatDateFromServer(scheduleDetail.start))
          + "-"
          + DateUtils.Date2HHmm(DateUtils.formatDateFromServer(scheduleDetail.end));
      mBinding.courseTime.setText(makeSpanString("时间：",
          DateUtils.getYYYYMMDDfromServer(scheduleDetail.start) + "  " + during));
      mBinding.courseGymName.setText(makeSpanString("场馆：", scheduleDetail.getShop().name));
      mBinding.courseClass.setText(makeSpanString("场地：", scheduleDetail.getSpace().getName()));
      mBinding.courseTrainerName.setText(scheduleDetail.getTeacher().getUsername());
      PhotoUtils.smallCircle(mBinding.imgTrainerPhoto, scheduleDetail.getTeacher().getAvatar());
      PhotoUtils.origin(mBinding.imgCourse, scheduleDetail.getCourse().getPhoto());
      mBinding.trainerScore.setRating(Float.valueOf(scheduleDetail.teacher_score));
      if (scheduleDetail.maxUsers <= scheduleDetail.usersCount) {
        mBinding.tvOrder.setTextColor(getResources().getColor(R.color.text_grey));
        mBinding.tvOrder.setText("该课程已约满");
        mBinding.groupSpell.setVisibility(View.GONE);
      } else if (scheduleDetail.isTrainerClass()) {
        mBinding.groupSpell.setVisibility(View.VISIBLE);
      } else {
        mBinding.groupSpell.setVisibility(View.GONE);
      }
      mBinding.tvOrderCount.setText(
          "预约人数（" + scheduleDetail.usersCount + "/" + scheduleDetail.maxUsers + ")");
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
      List<CircleImageItem> items = new ArrayList<>(orders.size());
      for (ScheduleOrders.ScheduleOrder order : orders) {
        if (order.getStatus() == 2) {
          break;
        }
        int count = order.getCount();
        String countText = "";
        if (count > 99) {
          countText = "99+";
        } else if (count > 1) {
          countText = count + "人";
        }
        items.add(new CircleImageItem(order.getUser().getAvatar(), countText));
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
      List<AbstractFlexibleItem> items = new ArrayList<>(photos.size() + 1);
      items.add(new GirdImageAddItem());
      for (SchedulePhoto photo : photos) {
        items.add(new SquareImageItem(photo));
      }
      photoAdapter.updateDataSet(items);
    }
  }

  private String host;

  @Override
  public FragmentScheduleDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (mBinding != null) return mBinding;
    mBinding = FragmentScheduleDetailBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    mViewModel.loadShopInfo();
    mBinding.setFragment(this);
    mViewModel.loadCourseConfig();
    if (service != null) {
      host = service.getHost();
    } else if (gymWrapper.getCoachService() != null) {
      host = gymWrapper.getCoachService().getHost();
    }
    mViewModel.setService(service);
    return mBinding;
  }

  @Override public void onStart() {
    super.onStart();
    mViewModel.loadCourseDetail(scheduleID);
    mViewModel.loadCoursePhotos(scheduleID);
    mViewModel.loadCouseOrders(scheduleID);
  }

  private void initRecyclerView() {
    mBinding.recyclerOrders.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mBinding.recyclerOrders.setAdapter(orderAdapter = new CommonFlexAdapter(new ArrayList()));

    mBinding.recyclerPhotos.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mBinding.recyclerPhotos.setAdapter(photoAdapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  public void routeAllOrders(View view) {
    routeTo("course", "/schedule/orders", new BundleBuilder().withString("scheduleID", scheduleID)
        .withParcelable("orders", mViewModel.detailOrders.getValue())
        .build());
  }
  public void routeSharePage(View view) {
    routeTo("course", "/schedule/share", new BundleBuilder().withString("scheduleID", scheduleID)
        .build());
  }

  public void routeAllPhotos(View view) {
    routeTo("course", "/schedule/photos", new BundleBuilder().withString("scheduleID", scheduleID)
        .withParcelable("photos", mViewModel.detailPhotos.getValue())
        .build());
  }

  public void routeToPlan(View view) {
    WebActivity.startWeb(
        host + "/shop/" + mViewModel.shopID + "/m/schedules/" + scheduleID + "/plan/edit/",
        getContext());
  }

  public void routeAddPhotos(View view) {
    WebActivity.startWeb(host
        + "/shop/"
        + mViewModel.shopID
        + "/m/upload/photo/?type=coach&schedule_id="
        + scheduleID, getContext());
  }

  public void routeAddOrder(View view) {
    WebActivity.startWeb(
        host + "/shop/" + mViewModel.shopID + "/m/coach/schedules/" + scheduleID + "/group/order/",
        getContext());
  }

  public void routeSignOrder(View view) {
    if (mViewModel.signType == 2) {
      WebActivity.startWeb(
          host + "/shop/" + mViewModel.shopID + "/m/coach/schedules/" + scheduleID + "/checkin/",
          getContext());
    } else {

      WebActivity.startWeb(host
          + "/shop/"
          + mViewModel.shopID
          + "/m/coach/schedules/"
          + scheduleID
          + "/checkin/proxy/", getContext());
    }
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("课程详情");
    toolbarModel.setMenu(R.menu.menu_share);
    toolbarModel.setListener(item -> {
      shareSchedule();
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void shareSchedule() {
    ScheduleDetail value = mViewModel.detail.getValue();
    if (value == null) return;
    String title = new StringBuilder("【").append(
        DateUtils.Date2MMDDV2(DateUtils.formatDateFromServer(value.start)))
        .append(value.course.getName())
        .append("@")
        .append(value.getShop().name)
        .append("】")
        .toString();

    String link = host
        + "shop/"
        + mViewModel.shopID
        + "/m/user/schedules/"
        + scheduleID
        + "/group/order/?recommend_user_id="
        + status.staff_id();
    String desc = "点击了解课程详情或者进行预约";
    String imgUrl = value.getCourse().getPhoto();

    ShareDialogFragment.newInstance(title, desc, imgUrl, link).show(getFragmentManager(), "");
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = photoAdapter.getItem(position);
    if (item instanceof SquareImageItem) {
      showMultiPhotos();
    } else if (item instanceof GirdImageAddItem) {
      routeAddPhotos();
    }
    return false;
  }

  public void routeAddPhotos() {
    String host = mViewModel.gymWrapper.getCoachService().getHost();
    WebActivity.startWeb(host
        + "/shop/"
        + mViewModel.shopID
        + "/m/upload/photo/?type=coach&schedule_id="
        + scheduleID, getContext());
  }

  private void showMultiPhotos() {
    MultiChoosePicFragment fragment = MultiChoosePicFragment.newInstance(mViewModel.getPhotoUrls());
    fragment.setShowFlag(true);
    fragment.show(getFragmentManager(), "");
  }
}
