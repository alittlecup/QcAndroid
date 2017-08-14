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
import cn.qingchengfit.saasbase.SaasRouter;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import cn.qingchengfit.saasbase.course.batch.presenters.BatchListPrivatePresenter;
import cn.qingchengfit.widgets.DialogList;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * 私教排课列表
 *
 */

public class BatchListPrivateFragment extends BatchListFragment
    implements BatchListPrivatePresenter.MVPView{

  @Inject BatchListPrivatePresenter privatePresenter;
  @Inject SaasRouter saasRouter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    delegatePresenter(privatePresenter,this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(R.string.t_private_batch_list);
    toolbar.inflateMenu(R.menu.menu_flow);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        DialogList.builder(getContext())
            .list(getResources().getStringArray(R.array.batch_list_private_flow), new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2017/9/11 跳转响应页面
                switch (position){
                  case 1://课程预约限制
                    break;
                  case 2://预约短信通知
                    break;
                  case 3://课件
                    break;
                  default://课程种类
                    break;
                }
              }
            }).show();
        return true;
      }
    });
  }

  @Override public void onRefresh() {
    privatePresenter.getBatchList();
  }

  @Override public boolean onItemClick(int position) {
    if (commonFlexAdapter.getItem(position) instanceof BatchItem){
      //saasRouter.routerTo(); 某种课程的排期列表 // TODO: 2017/9/11
    }
    return false;
  }

  @Override public void onList(List<BatchCoach> coaches) {
    if (coaches != null){
      List<AbstractFlexibleItem> data = new ArrayList<>();
      data.add(new StickerDateItem(coaches.size()+"节私教"));
      for (BatchCoach coach : coaches) {
        data.add(new BatchItem(coach));
      }
      data.add(new TitleHintItem("如何添加团课排期"));
    }
  }
}
