package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.items.TitleHintItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchListGroupPresenter;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseParams;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/9/11.
 *
 * 团课
 */
@Leaf(module = "course", path = "/batches/group/list/") public class BatchListGroupFragment
  extends BatchListFragment implements BatchListGroupPresenter.MVPView {

  @Inject BatchListGroupPresenter privatePresenter;
  @Inject IPermissionModel permissionModel;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    delegatePresenter(privatePresenter, this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(R.string.t_group_batch_list);
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(item -> {
      DialogList.builder(getContext())
        .list(getResources().getStringArray(R.array.batch_list_group_flow),
          (parent, view, position, id) -> menuSelected(position, false))
        .show();
      return true;
    });
  }

  /**
   * 新增团课
   */
  @Override public void clickAddBatch() {
    if ( !permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }

    routeTo("/choose/", CourseChooseParams.builder().mIsPrivate(false).src(TARGET).build());
  }

  @Override public void onRefresh() {
    srl.setRefreshing(true);
    privatePresenter.getBatchList();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof BatchItem) {
      if ( !permissionModel.check(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE)){
        showAlert(R.string.sorry_for_no_permission);
        return true;
      }
      routeTo("/batch/cate/group/",
        new BatchListCategoryGroupParams().course_id(
          ((BatchItem) item).getBatchCourse().getId()).build());
    }else if (item instanceof TitleHintItem){
      WebActivity.startWeb(Configs.WEB_HOW_TO_USE_BATCH_GROUP,getContext());
    }
    return false;
  }

  @Override public void onList(List<BatchCourse> course) {
    srl.setRefreshing(false);
    if (course != null) {
      List<AbstractFlexibleItem> data = new ArrayList<>();
      data.add(new StickerDateItem(course.size() + "节团课"));
      for (BatchCourse coach : course) {
        data.add(new BatchItem(coach));
      }
      data.add(new TitleHintItem("如何添加团课排期"));
      commonFlexAdapter.updateDataSet(data);
    }
  }
}
