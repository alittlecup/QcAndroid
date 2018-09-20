package cn.qingchengfit.saasbase.course.batch.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.TitleHintItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.coach.event.EventStaffWrap;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.items.BatchCopyItem;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchListPrivatePresenter;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.widget.bubble.BubbleViewUtil;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
 * 私教排课列表
 */
@Leaf(module = "course", path = "/batches/private/list/") public class BatchListPrivateFragment
  extends BatchListFragment implements BatchListPrivatePresenter.MVPView {

  @Inject BatchListPrivatePresenter privatePresenter;
  @Inject IPermissionModel permissionModel;
  @Inject QcRestRepository restRepository;
  @Inject GymWrapper gymWrapper;

  private BubbleViewUtil bubbleViewUtil;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    delegatePresenter(privatePresenter, this);
    RxBusAdd(EventStaffWrap.class)
      .compose(doWhen(FragmentEvent.RESUME))
      .throttleFirst(1000, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<EventStaffWrap>() {
        @Override public void onNext(EventStaffWrap eventStaffWrap) {
          routeTo("/batch/add/",AddBatchParams.builder()
            .mCourse(null)
            .mTeacher(eventStaffWrap.getStaff())
            .build());
        }
      });
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(R.string.t_private_batch_list);
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        DialogList.builder(getContext())
          .list(getResources().getStringArray(R.array.batch_list_private_flow),
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuSelected(position,true);
              }
            })
          .show();
        return true;
      }
    });
    bubbleViewUtil = new BubbleViewUtil(getContext());
    bubbleViewUtil.showBubbleOnceDefaultToolbar(toolbar, "私教的更多操作在这里", "batchListPrivate", 0);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    bubbleViewUtil.closeBubble();
  }

  @Override public void onRefresh() {
    srl.setRefreshing(true);
    privatePresenter.getBatchList();
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item == null) return true;
    if (item instanceof BatchItem) {
      routeTo("/batch/cate/private/",
        new cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryPrivateParams().trainer_id(
          ((BatchItem) item).getBatchCoach().id).build());
    }else if (item instanceof TitleHintItem){
      WebActivity.startWeb(Configs.WEB_HOW_TO_USE_BATCH_PRIVATE,getContext());
    }
    return false;
  }

  @Override public void clickAddBatch() {
    if ( !permissionModel.check(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo("staff","/trainer/choose/",null);
  }

  @Override public void clickCopyBatch() {
    if ( !permissionModel.check(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_WRITE)){
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo(AppUtils.getRouterUri(getContext(), "/course/batch/copy/"), new BatchCopyParams().isPrivate(Boolean.TRUE).build());
  }

  @Override public void onList(List<BatchCoach> coaches) {
    srl.setRefreshing(false);
    if (coaches != null) {
      List<AbstractFlexibleItem> data = new ArrayList<>();
      data.add(new BatchCopyItem(coaches.size() + "节私教", this));
      for (BatchCoach coach : coaches) {
        data.add(new BatchItem(coach));
      }
      data.add(new TitleHintItem("如何添加私教排期"));
      commonFlexAdapter.updateDataSet(data, true);
    }
  }

  @SuppressLint("StringFormatMatches") @Override public void clickPrint() {
    WebActivity.startWeb(
        getResources().getString(R.string.copy_batch_print_url, gymWrapper.getCoachService().host,
            gymWrapper.shop_id(), "type=private"), getContext());
  }
}
