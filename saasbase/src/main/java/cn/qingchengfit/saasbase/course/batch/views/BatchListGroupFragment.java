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
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchListGroupPresenter;
import cn.qingchengfit.saasbase.course.course.views.CourseListParams;
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
 *
 * 团课
 *
 */
@Leaf(module = "course",path = "/batches/group/list/")
public class BatchListGroupFragment extends BatchListFragment
    implements BatchListGroupPresenter.MVPView{

  @Inject BatchListGroupPresenter privatePresenter;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    delegatePresenter(privatePresenter,this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(R.string.t_group_batch_list);
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(item -> {
      DialogList.builder(getContext())
          .list(getResources().getStringArray(R.array.batch_list_group_flow),
            (parent, view, position, id) -> {
              // TODO: 2017/9/11 跳转响应页面
              switch (position){
                case 1://课程预约限制
                  /**
                   * 预约限制 {@link OrderLimitFragment}
                   */
                  if (!serPermisAction.check(PermissionServerUtils.TEAM_COURSE_LIMIT)) {
                    showAlert(R.string.sorry_for_no_permission);
                    return;
                  }
                  //getFragmentManager().beginTransaction()
                  //  .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                  //  .replace(R.id.frag, new OrderLimitFragmentBuilder(mIsPrivate).build())
                  //  .addToBackStack(null)
                  //  .commit();
                  break;
                case 2://预约短信通知
                  /**
                   * 短信通知{@link MsgNotiFragment}
                   */
                  if (!serPermisAction.check(PermissionServerUtils.TEAM_COURSE_MSG_SETTING)) {
                    showAlert(R.string.sorry_for_no_permission);
                    return;
                  }
                  //getFragmentManager().beginTransaction()
                  //  .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                  //  .replace(R.id.frag, new MsgNotiFragmentBuilder(mIsPrivate).build())
                  //  .addToBackStack(null)
                  //  .commit();
                  break;
                case 3://课件
                  /**
                   * 课件
                   */
                  if (!serPermisAction.check(PermissionServerUtils.PLANSSETTING)) {
                    showAlert(R.string.sorry_for_no_permission);
                    return;
                  }
                  //QRActivity.start(getContext(),);
                  //gymFunctionFactory.goQrScan(this,
                  //  mIsPrivate ? GymFunctionFactory.PLANS_SETTING_PRIVATE : GymFunctionFactory.PLANS_SETTING_GROUP, null,
                  //  gymWrapper.getCoachService());
                  break;
                default://课程种类
                  routeTo("/list/",new CourseListParams().mIsPrivate(false).build());
                  break;
              }
            }).show();
      return true;
    });
  }

  /**
   * 新增团课
   */
  @Override public void clickAddBatch() {
    routeTo("/choose/",null);
  }

  @Override public void onRefresh() {
    privatePresenter.getBatchList();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof BatchItem){
      routeTo("/batch/cate/group/",new cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryGroupParams()
        .course_id(((BatchItem) item).getBatchCourse().getId()).build());
    }
    return false;
  }

  @Override public void onList(List<BatchCourse> course) {
    srl.setRefreshing(false);
    if (course != null){
      List<AbstractFlexibleItem> data = new ArrayList<>();
      data.add(new StickerDateItem(course.size()+"节团课"));
      for (BatchCourse coach : course) {
        data.add(new BatchItem(coach));
      }
      data.add(new TitleHintItem("如何添加团课排期"));
      commonFlexAdapter.updateDataSet(data);
    }
  }


}
