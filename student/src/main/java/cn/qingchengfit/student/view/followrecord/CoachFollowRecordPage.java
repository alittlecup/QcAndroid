package cn.qingchengfit.student.view.followrecord;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.StPageFollowRecordBinding;
import cn.qingchengfit.student.item.FollowRecordItem;
import cn.qingchengfit.student.view.ptag.CoachPtagQuestionPageParams;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.MeasureUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教练App会员详情跟进记录页面
 */
public class CoachFollowRecordPage extends FollowRecordPage {

  private boolean isShowMenu = false;
  private PopupWindow popupWindow;
  private boolean isShowDialog = true;

  @Override protected void subscribeUI() {
    super.subscribeUI();
    mViewModel.types.observe(this, integers -> {
      Map<String, Object> params = new HashMap<>();
      if (integers != null && integers.size() > 0) {
        params.put("track_types", loadParams(integers));
      }
      mViewModel.loadSource(params);
    });
  }

  @Override
  public StPageFollowRecordBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = super.initDataBinding(inflater, container, savedInstanceState);
    mBinding.llCoachFollowRecordCategory.setVisibility(View.VISIBLE);
    return mBinding;
  }

  @Override public void loadData() {
    //默认展示训练反馈
    mViewModel.onClickCategory(mBinding.tvStudentFollowRecordTrainFeedback, 5);
  }

  @Override public void onClickAddBtn(View v) {
    if (!isShowMenu) {
      showSelectPopWindows(v);
    } else {
      isShowMenu = false;
      if (popupWindow != null) {
        popupWindow.dismiss();
        ((FloatingActionButton) v).setImageResource(R.drawable.vd_edit_black);
      }
    }
  }

  @Override protected void onInVisible() {
    if (popupWindow != null && popupWindow.isShowing()) {
      popupWindow.dismiss();
      mBinding.fab.setImageResource(R.drawable.vd_edit_black);
    }
    super.onInVisible();
  }

  //展示菜单
  private void showSelectPopWindows(View v) {
    if (popupWindow == null) {
      View contentView = LayoutInflater.from(getContext())
          .inflate(R.layout.st_view_coach_follow_add_popwindow, null);
      popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
      popupWindow.setTouchable(true);
      TextView tvAddSellerFollow = contentView.findViewById(R.id.tv_coach_follow_add);
      TextView tvAddTrainerFeedBack =
          contentView.findViewById(R.id.tv_coach_follow_add_train_feedback);

      tvAddSellerFollow.setOnClickListener(v1 -> {
        if (permissionModel.check(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
          Uri uri = AppUtils.getRouterUri(getContext(), "student/student/follow_record_edit");
          routeTo(uri, new FollowRecordEditPageParams().build(), false);
        } else {
          showAlert(R.string.sorry_for_no_permission);
        }
      });

      tvAddTrainerFeedBack.setOnClickListener(v12 -> {
        isShowDialog = (mViewModel.getLiveItems().getValue() != null
            && mViewModel.getLiveItems().getValue().size() == 0);
        routeTo("student", "/coach/ptag/question", CoachPtagQuestionPageParams.builder()
            .type(Constants.MEMBER_TRAIN_FEEDBACK)
            .isShow(isShowDialog)
            .build());
        isShowDialog = false;
      });
    }
    int[] location = new int[2];
    v.getLocationOnScreen(location);
    popupWindow.getContentView()
        .measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        location[0] - (popupWindow.getContentView().getMeasuredWidth() - v.getMeasuredWidth()),
        location[1]
            - v.getMeasuredHeight() / 2
            - popupWindow.getContentView().getMeasuredHeight() / 2
            - MeasureUtils.dpToPx(36f, getResources()));
    isShowMenu = true;
    ((FloatingActionButton) v).setImageResource(R.drawable.vd_close_white_24dp);
  }

  @Override public boolean onItemClick(int position) {
    super.onItemClick(position);
    IFlexible item = adapter.getItem(position);
    if (item instanceof FollowRecordItem) {
      //训练反馈、跳转到修改训练反馈问卷
      if (item.getItemViewType() == 5) {
        routeTo("student", "/coach/ptag/question", CoachPtagQuestionPageParams.builder()
            .type(Constants.MEMBER_TRAIN_FEEDBACK)
            .naireId(((FollowRecordItem) item).getData().getNaire_answer_history_id())
            .build());
      }
    }
    return false;
  }

  //拼接参数
  private String loadParams(List<Integer> typeList) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < typeList.size(); i++) {
      if (i == 0) {
        builder.append(typeList.get(i));
      } else {
        builder.append(",").append(typeList.get(i));
      }
    }
    return builder.toString();
  }
}
