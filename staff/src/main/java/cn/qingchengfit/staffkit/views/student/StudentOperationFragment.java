package cn.qingchengfit.staffkit.views.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Shop;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.AllocateCoachActivity;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.gym.upgrate.UpgradeInfoDialogFragment;
import cn.qingchengfit.staffkit.views.student.attendance.AttendanceActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpActivity;
import cn.qingchengfit.staffkit.views.student.followup.RouterFollowUp;
import cn.qingchengfit.staffkit.views.student.list.StudentOperationItem;
import cn.qingchengfit.staffkit.views.student.sendmsgs.SendMsgsActivity;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class StudentOperationFragment extends BaseFragment
    implements HandleClickAllotSale, HandleClickFollowUp, FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.recycleview) RecyclerView recycleview;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject SerPermisAction serPermisAction;
  @BindView(R.id.indicator) MyIndicator indicator;
  private List<AbstractFlexibleItem> datas = new ArrayList<>();
  private CommonFlexAdapter mCommonFlexAdapter;
  private boolean proGym = false;

    @Inject public StudentOperationFragment() {
    }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_student_operation, container, false);
    unbinder = ButterKnife.bind(this, v);

    mCommonFlexAdapter = new CommonFlexAdapter(datas, this);

    final SmoothScrollGridLayoutManager gridLayoutManager =
        new SmoothScrollGridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false);
    MySnapHelper pagerSnapHelper = new MySnapHelper();
    pagerSnapHelper.attachToRecyclerView(recycleview);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    recycleview.setLayoutManager(gridLayoutManager);
    recycleview.setAdapter(mCommonFlexAdapter);
    recycleview.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
          int position = gridLayoutManager.findFirstVisibleItemPosition();
          if (position % 8 < 4){
            indicator.onPageSelected(position / 8);
          }else{
            indicator.onPageSelected(position / 8 + 1);
          }
        }
        return false;
      }
    });
    return v;
  }

  @Override protected void onFinishAnimation() {
      super.onFinishAnimation();
      RxRegiste(GymBaseInfoAction.getGymByModel(gymWrapper.id(), gymWrapper.model()).filter(new Func1<List<CoachService>, Boolean>() {
          @Override public Boolean call(List<CoachService> list) {
              return list != null && list.size() > 0;
          }
      }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
          @Override public void call(List<CoachService> list) {
              CoachService now = list.get(0);
              proGym = GymUtils.getSystemEndDay(now) >= 0;
              datas.clear();
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_sales, R.string.qc_student_allotsale, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_coach, R.string.qc_student_allot_coach, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_follow, R.string.qc_student_follow_up, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vd_student_transfer, R.string.qc_student_follow_transfer, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_attend, R.string.qc_student_attendance, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vector_student_send_sms, R.string.qc_student_send_sms, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.ic_student_management_export, R.string.fun_name_export, proGym, true));
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_birthday, R.string.qc_student_birthday_notice, proGym, false));
              datas.add(new StudentOperationItem(R.drawable.vector_student_management_tag, R.string.qc_student_vip, proGym, false));
              setRecyclerPadding(datas.size());
              indicator.createIndicators(datas.size() / 8 + 1);
              if (mCommonFlexAdapter != null) mCommonFlexAdapter.notifyDataSetChanged();
          }
      }));
  }

  private void setRecyclerPadding(int lastPosition) {
    if (lastPosition % 2 == 0) {
      lastPosition -= 1;
    }
    recycleview.setPadding(0, 0,
        MeasureUtils.getScreenWidth(getResources()) * (4 - lastPosition % 8) / 4, 0);
  }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
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
                    if (!SerPermisAction.check(shops.id, PermissionServerUtils.MANAGE_MEMBERS_CAN_WRITE)) {
                        showAlert("抱歉!您无该场馆权限");
                        return;
                    }
                    CoachService coachService = GymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shops.id);
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
        if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE) || !serPermisAction.check(
            PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
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
        if (position == 0) {
            allotSaleClick(null);
        } else if (position == 2) {
            followUpClick(null);
        } else if (position == 3) {
            Intent toFollowUp = new Intent(getActivity(), FollowUpActivity.class);
            toFollowUp.putExtra("router", RouterFollowUp.TRANSFER);
            startActivity(toFollowUp);
        } else if (position == 4) {
            Intent intent = new Intent(getActivity(), AttendanceActivity.class);
            startActivity(intent);
        } else if (position == 5) {
            Intent toChoose = new Intent(getActivity(), SendMsgsActivity.class);
            toChoose.putExtra("to", ChooseActivity.CHOOSE_MULTI_STUDENTS);
            startActivity(toChoose);
        } else if (position == 1) {
            Intent intent = new Intent(getActivity(), AllocateCoachActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
