package cn.qingchengfit.staffkit.views.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.export.ImportExportActivity;
import cn.qingchengfit.saasbase.course.batch.views.UpgradeInfoDialogFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpActivity;
import cn.qingchengfit.staffkit.views.student.followup.RouterFollowUp;
import cn.qingchengfit.staffkit.views.student.list.StudentOperationItem;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class StudentOperationFragment extends BaseFragment
    implements HandleClickAllotSale, HandleClickFollowUp, FlexibleAdapter.OnItemClickListener,
    MySnapHelper.OnSelectListener {

	RecyclerView recycleview;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  @Inject GymBaseInfoAction gymBaseInfoAction;
	MyIndicator indicator;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private CommonFlexAdapter mCommonFlexAdapter;
  private boolean proGym = false;

   public StudentOperationFragment() {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mCommonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
   }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_student_operation, container, false);
    recycleview = (RecyclerView) v.findViewById(R.id.recycleview);
    indicator = (MyIndicator) v.findViewById(R.id.indicator);

    final SmoothScrollGridLayoutManager gridLayoutManager =
        new SmoothScrollGridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false);
    MySnapHelper pagerSnapHelper = new MySnapHelper();
    pagerSnapHelper.setCount(8);
    pagerSnapHelper.setListener(this);
    pagerSnapHelper.attachToRecyclerView(recycleview);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    recycleview.setLayoutManager(gridLayoutManager);
    //recycleview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recycleview.setItemAnimator(new ScaleInAnimator());

    recycleview.setAdapter(mCommonFlexAdapter);


    return v;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    RxRegiste(gymBaseInfoAction.getGymByModel(gymWrapper.id(), gymWrapper.model())
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .subscribe(now -> {
          proGym = GymUtils.getSystemEndDay(now) >= 0;
          datas.clear();
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_sales,
            R.string.qc_student_allotsale, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_attend,
            R.string.qc_student_attendance, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_coach,
            R.string.qc_student_allot_coach, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vector_student_send_sms,
            R.string.qc_student_send_sms, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_follow,
            R.string.qc_student_follow_up, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.ic_student_management_export,
            R.string.fun_name_export, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vd_student_transfer,
            R.string.qc_student_follow_transfer, proGym, true));
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_birthday,
            R.string.qc_student_birthday_notice, proGym, false));
          datas.add(new StudentOperationItem(R.drawable.vector_student_management_tag,
            R.string.qc_student_vip, proGym, false));
          setRecyclerPadding(datas.size());
          indicator.createIndicators(datas.size() / 8 + 1);
          mCommonFlexAdapter.updateDataSet(datas, true);
        }));
  }

  private void setRecyclerPadding(int lastPosition) {
    if (lastPosition % 2 == 0) {
      lastPosition -= 1;
    }
    recycleview.setPadding(0, 0,
        MeasureUtils.getScreenWidth(getResources()) * (4 - lastPosition % 8) / 4, 0);
  }



  @Override public String getFragmentName() {
    return this.getClass().getName();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 2) {// 跳转分配销售
        Shop shops = (Shop) IntentUtils.getParcelable(data);
        if (shops != null) {
          ArrayList<String> shopIds = new ArrayList<>();
          shopIds.add(shops.id);
          if (!serPermisAction.check(shops.id, PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
            showAlert("抱歉!您无该场馆权限");
            return;
          }
          CoachService coachService =
              gymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shops.id);
          if (coachService == null) {
            ToastUtils.show("数据错误");
            return;
          }
          toAllotSale();
        }
      }
    }
  }

  /**
   * {@link AllotSalesActivity}
   */
  public void toAllotSale() {
    Intent toAllot = new Intent(getActivity(), AllotSalesActivity.class);
    startActivity(toAllot);
  }

  /**
   * 分配销售点击
   *
   * @param view view
   */
  @Override public void allotSaleClick(View view) {
    if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
        || !serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    toAllotSale();
  }

  /**
   * 会员跟踪点击
   *
   * @param view view
   */
  @Override public void followUpClick(View view) {
    Intent toFollowUp = new Intent(getActivity(), FollowUpActivity.class);
    startActivity(toFollowUp);
  }

  @Override public boolean onItemClick(int position) {
    if (!gymWrapper.isPro()) {
      new UpgradeInfoDialogFragment().show(getFragmentManager(), "");
      return true;
    }
    if (mCommonFlexAdapter.getItem(position) instanceof StudentOperationItem) {
      int strId = ((StudentOperationItem) mCommonFlexAdapter.getItem(position)).getStrRes();
      switch (strId) {
        case R.string.qc_student_allotsale:
          allotSaleClick(null);
          break;
        case R.string.qc_student_follow_up:
          followUpClick(null);
          break;
        case R.string.qc_student_follow_transfer:
          Intent toFollowUp = new Intent(getActivity(), FollowUpActivity.class);
          toFollowUp.putExtra("router", RouterFollowUp.TRANSFER);
          startActivity(toFollowUp);
          break;
        case R.string.qc_student_attendance:
          Intent intent = new Intent(getActivity(), AttendanceActivity.class);
          startActivity(intent);
          break;
        case R.string.qc_student_send_sms:
          if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
            showAlert(R.string.alert_permission_forbid);
            return false;
          }
          routeTo("student","/student/sendmsg/", null);

          //Intent toChoose = new Intent(getActivity(), SendMsgsActivity.class);
          //toChoose.putExtra("to", ChooseActivity.CHOOSE_MULTI_STUDENTS);
          //startActivity(toChoose);
          break;
        case R.string.qc_student_allot_coach:
          if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)
            || !serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
            showAlert(R.string.alert_permission_forbid);
            return true;
          }
          Intent i = new Intent(getActivity(), AllocateCoachActivity.class);
          startActivity(i);
          break;
        case R.string.fun_name_export:
          //Intent exportIntent = new Intent(getActivity(), ImportExportActivity.class);
          //exportIntent.putExtra("type", ImportExportActivity.TYPE_EXPORT);
          //startActivity(exportIntent);
          routeTo("student","/student/export",null);
          break;
        case R.string.qc_student_birthday_notice:
          DialogUtils.showAlert(getContext(),"即将上线，敬请期待");
          break;
        case R.string.qc_student_vip:
          break;
      }
    }
    return true;
  }

  @Override public void onPageSelect(int position) {
    indicator.onPageSelected(position);
  }
}
