package cn.qingchengfit.saasbase.course.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saasbase.course.batch.views.BatchListTrainerSpanFragment;
import cn.qingchengfit.saasbase.course.batch.views.EditBatchParams;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseParams;
import cn.qingchengfit.saasbase.course.course.views.CourseListParams;
import cn.qingchengfit.saasbase.course.presenters.CourseBatchDetailPresenter;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.DialogSheet;
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

  List<AbstractFlexibleItem> mDatas = new ArrayList<>();
  List<AbstractFlexibleItem> mOutdateDatas = new ArrayList<>();
  private DialogSheet delCourseDialog;
  private MaterialDialog delDialog;
  /**
   * 记录是否展示已过期排期
   */
  private boolean isShow;
  private DialogList dialogList;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter, this);
    return v;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(mType != 1 ? "团课排期" : "私教排期");
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(item -> {
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
      delDialog = new MaterialDialog.Builder(getContext()).autoDismiss(true)
        .content("是否删除课程?")
        .positiveText("确定")
        .negativeText("取消")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog dialog, DialogAction which) {
            presenter.delCourse();
          }
        })
        .cancelable(false)
        .build();
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
      }
      commonFlexAdapter.notifyDataSetChanged();
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
    }

    commonFlexAdapter.notifyDataSetChanged();
    if (pos >= 0 && isShow) {
      commonFlexAdapter.toggleSelection(pos);
      mDatas.addAll(mOutdateDatas);
    }
    commonFlexAdapter.updateDataSet(mDatas);
  }

  @Override public void onRefresh() {
    onFinishAnimation();
  }
}
