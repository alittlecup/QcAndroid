package cn.qingchengfit.saasbase.course.batch.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.items.TitleHintItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.coach.event.EventStaffWrap;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchListPrivatePresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
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

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    delegatePresenter(privatePresenter, this);
    RxBusAdd(EventStaffWrap.class)
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
    }
    return false;
  }

  @Override public void clickAddBatch() {
    routeTo("staff","/trainer/choose/",null);
  }

  @Override public void onList(List<BatchCoach> coaches) {
    srl.setRefreshing(false);
    if (coaches != null) {
      List<AbstractFlexibleItem> data = new ArrayList<>();
      data.add(new StickerDateItem(coaches.size() + "节私教"));
      for (BatchCoach coach : coaches) {
        data.add(new BatchItem(coach));
      }
      data.add(new TitleHintItem("如何添加团课排期"));
      commonFlexAdapter.updateDataSet(data, true);
    }
  }
}
