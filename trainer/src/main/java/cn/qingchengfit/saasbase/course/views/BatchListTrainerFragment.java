package cn.qingchengfit.saasbase.course.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCopyCoach;
import cn.qingchengfit.saasbase.course.batch.items.BatchCopyItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saasbase.course.batch.views.BatchCopyParams;
import cn.qingchengfit.saasbase.course.batch.views.BatchListTrainerSpanFragment;
import cn.qingchengfit.saasbase.course.batch.views.EditBatchParams;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseParams;
import cn.qingchengfit.saasbase.course.course.views.CourseListParams;
import cn.qingchengfit.saasbase.course.presenters.CourseBatchDetailPresenter;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.widget.bubble.BubbleViewUtil;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.DialogList;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.items.HideBatchItem;
import com.qingchengfit.fitcoach.items.HintItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class BatchListTrainerFragment extends BatchListTrainerSpanFragment
    implements CourseBatchDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener {

  @Inject GymWrapper gymWrapper;
  @Inject CourseBatchDetailPresenter presenter;
  @Inject IPermissionModel permissionModel;
  @Inject LoginStatus loginStatus;
  @Inject QcRestRepository restRepository;

  List<AbstractFlexibleItem> mDatas = new ArrayList<>();
  List<AbstractFlexibleItem> mOutdateDatas = new ArrayList<>();
  private DialogSheet delCourseDialog;
  private MaterialDialog delDialog;
  /**
   * 记录是否展示已过期排期
   */
  private boolean isShow;
  private DialogList dialogList;
  private BubbleViewUtil bubbleViewUtil;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter, this);
    SensorsUtils.trackScreen(this.getClass().getCanonicalName()+"_"+(mType==1?"private":"group"));
    return v;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(mType != 1 ? "团课排期" : "私教排期");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(item -> {
      bubbleViewUtil.closeBubble();
      if (dialogList == null) {
        dialogList = new DialogList(getContext());
        ArrayList<String> flows = new ArrayList<>();
        flows.add("课程种类");
        flows.add("课件");
        dialogList.list(flows, (parent, view, position, id) -> {
          dialogList.dismiss();
          if (position == 0) {
            routeTo("/list/", CourseListParams.builder().mIsPrivate(mType == 1).build());//课程种类
          } else if (position == 1) {
            Intent toPlan = new Intent(getActivity(), FragActivity.class);
            toPlan.putExtra("type", 8);
            toPlan.putExtra("service", gymWrapper.getCoachService());
            startActivity(toPlan);
          }
        });
      }
      dialogList.show();
      return true;
      });
    bubbleViewUtil = new BubbleViewUtil(getContext());
    if(mType == 0) {
      bubbleViewUtil.showBubbleOnce(toolbarTitile, "点击这里管理团课种类", "batchCategoryGroup",
              3050, -370, 1);
    } else if(mType == 1) {
      bubbleViewUtil.showBubbleOnce(toolbarTitile, "点击这里管理私教种类", "batchCategoryPrivate",
              3050, -370, 1);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    bubbleViewUtil.closeBubble();
  }

  private void showDelCourse() {
    if (delCourseDialog == null) {
      delCourseDialog = new DialogSheet(getContext());
      delCourseDialog.addButton("编辑", v -> {
        //编辑课程
        delCourseDialog.dismiss();
      });
      delCourseDialog.addButton("删除", new View.OnClickListener() {
        @Override public void onClick(View v) {
          //删除课程
          delCourseDialog.dismiss();
          delCourse();
        }
      });
    }
    delCourseDialog.show();
  }

  /**
   * 删除课程
   */
  private void delCourse() {
    if (delDialog == null) {
      delDialog = DialogUtils.initConfirmDialog(getContext(), "", "是否删除课程?", (dialog, action) -> {
        dialog.dismiss();
        if (action == DialogAction.POSITIVE) {
          presenter.delCourse();
        }
      });
    }
    delDialog.show();
  }

  @Override protected void onFinishAnimation() {
    if (mType == Configs.TYPE_PRIVATE) {
      presenter.queryPrivate();
    } else {
      presenter.queryGroup();
    }
  }

  @Override public void clickCopyBatch() {
    if (!permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)) {
      showAlert(cn.qingchengfit.saasbase.R.string.sorry_for_no_permission);
      return;
    }
    BatchCopyCoach coach = new BatchCopyCoach();
    coach.setName(loginStatus.staff_name());
    coach.setId(loginStatus.staff_id());
    coach.setGender(0);
    routeTo(AppUtils.getRouterUri(getContext(), "/course/batch/copy/"),
        new BatchCopyParams().isPrivate(mType != 1 ? Boolean.FALSE : Boolean.TRUE)
            .coach(coach)
            .build());
  }

  /**
   * 添加课程排期
   */
  @Override public void clickAddBatch() {
    if (!permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)) {
      showAlert(cn.qingchengfit.saasbase.R.string.sorry_for_no_permission);
      return;
    }

    routeTo("/choose/", CourseChooseParams.builder().mIsPrivate(mType == 1).src(TARGET).build());
  }

  @Override public String getFragmentName() {
    return BatchListTrainerFragment.class.getName();
  }

  @Override public boolean onItemClick(int position) {
    if (commonFlexAdapter.getItem(position) instanceof BatchItem) {
      BatchItem batchItem = (BatchItem) commonFlexAdapter.getItem(position);
      if (batchItem != null && batchItem.getBatchModel() != null) {
        routeTo("course", "/batch/edit/", EditBatchParams.builder()
            .batchId(batchItem.getId())
            .isPrvite(mType == Configs.TYPE_PRIVATE)
            .build());
      }
    } else if (commonFlexAdapter.getItem(position) instanceof HideBatchItem) {
      commonFlexAdapter.toggleSelection(position);
      if (commonFlexAdapter.isSelected(position)) {
        isShow = true;
        mDatas.addAll(mOutdateDatas);
      } else {
        isShow = false;
        mDatas.removeAll(mOutdateDatas);
        commonFlexAdapter.clear();
      }
      commonFlexAdapter.updateDataSet(mDatas);
    }
    return true;
  }

  //@OnClick(R.id.preview) public void onClick() {
  //  WebActivity.startWeb(
  //    (mType == Configs.TYPE_PRIVATE ? Configs.PRIVATE_PRIVEIW : Configs.GROUP_PRIVEIW)
  //      + "?id="
  //      + gymWrapper.id()
  //      + "&model="
  //      +gymWrapper.model(), getContext());
  //}

  @Override public void noMoreLoad(int i) {

  }

  @Override public void onLoadMore(int i, int i1) {

  }

  @Override public void onBatchList(List<QcResponsePrivateDetail.PrivateBatch> batch) {
    if (srl != null) srl.setRefreshing(false);
    mDatas.clear();
    mOutdateDatas.clear();
    commonFlexAdapter.clear();
    boolean isOutofDate = false;
    int pos = -1;
    for (int i = 0; i < batch.size(); i++) {
      if (!isOutofDate) {
        if (DateUtils.isOutOfDate(DateUtils.formatDateFromYYYYMMDD(batch.get(i).to_date))) {
          if (mDatas.size() == 0) {
            mDatas.add(new HintItem.Builder().text(
                mType == Configs.TYPE_PRIVATE ? getString(R.string.hint_no_private_course)
                    : getString(R.string.hint_no_group_course)).resBg(R.color.white).build());
          }
          mDatas.add(new HideBatchItem());
          isOutofDate = true;
          pos = mDatas.size() - 1;
          mOutdateDatas.add(new BatchItem(batch.get(i)));
        } else {
          mDatas.add(new BatchItem(batch.get(i)));
        }
      } else {
        mOutdateDatas.add(new BatchItem(batch.get(i)));
      }
    }
    if (mDatas.size() == 0) {
      mDatas.add(new CommonNoDataItem(R.drawable.no_batch,
          mType == Configs.TYPE_PRIVATE ? getString(R.string.hint_no_private_course)
              : getString(R.string.hint_no_group_course)));
    } else {
      mDatas.add(0, new BatchCopyItem(batch.size() + "节课", this));
    }

    commonFlexAdapter.notifyDataSetChanged();
    if (pos >= 0 && isShow) {
      commonFlexAdapter.toggleSelection(pos);
      mDatas.addAll(mOutdateDatas);
    }
    commonFlexAdapter.updateDataSet(mDatas);
  }

  @Override public void clickPrint() {
    WebActivity.startWeb(
        getResources().getString(cn.qingchengfit.saasbase.R.string.copy_batch_print_url,
            gymWrapper.getCoachService().host, gymWrapper.shop_id(),
            (mType != 1 ? "type=group" : "type=private") + "&coach_id=" + loginStatus.staff_id()),
        getContext());
  }

  @Override public void onRefresh() {
    onFinishAnimation();
  }
}
