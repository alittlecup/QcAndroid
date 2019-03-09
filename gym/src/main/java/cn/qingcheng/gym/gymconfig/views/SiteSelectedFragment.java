package cn.qingcheng.gym.gymconfig.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saascommon.events.EventSiteSelected;
import cn.qingcheng.gym.gymconfig.item.SiteItem;
import cn.qingcheng.gym.gymconfig.item.SiteSelectedItem;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/12/1.
 */
@Leaf(module = "gym", path = "/site/choose/") public class SiteSelectedFragment
    extends SiteFragment {

  @Need Boolean isPrivate;
  @Need ArrayList<String> selectIds;
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  private EventSiteSelected siteSelected = new EventSiteSelected();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    ViewGroup parent =
        (ViewGroup) inflater.inflate(R.layout.layout_toolbar_container, container, false);
    LinearLayout bottomView =
        (LinearLayout) inflater.inflate(R.layout.view_bottom_add, container, false);
    toolbar = parent.findViewById(R.id.toolbar);
    toolbarTitle = parent.findViewById(R.id.toolbar_title);
    TextView textView = bottomView.findViewById(R.id.tv_bottom_content);
    textView.setText("+ 添加场地");
    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFab();
      }
    });
    bottomView.setVisibility(View.VISIBLE);
    fab.setVisibility(View.GONE);
    initToolbar(toolbar);
    if(v instanceof ViewGroup){
      ((ViewGroup) v).addView(bottomView);
    }
    parent.addView(v, 1);
    return parent;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.getMenu().clear();
    toolbarTitle.setText("选择场地");
    if (isPrivate) {
      toolbar.getMenu().add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      RxMenuItem.clicks(toolbar.getMenu().findItem(0))
          .throttleFirst(500, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new BusSubscribe<Void>() {
            @Override public void onNext(Void aVoid) {

              siteSelected.clear();
              for (Integer integer : commonFlexAdapter.getSelectedPositions()) {
                IFlexible item = commonFlexAdapter.getItem(integer);
                if (item instanceof SiteItem) {
                  siteSelected.add(((SiteItem) item).getSpace());
                }
              }
              RxBus.getBus().post(siteSelected);
              popBack();
            }
          });
    }
  }

  @Override public void onRefresh() {
    setQueryType(isPrivate ? 1 : 0);
    super.onRefresh();
  }

  @Override public void onUpdateEmptyView(int size) {
    if (selectIds != null && selectIds.size() > 0) {
      for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
        IFlexible item = commonFlexAdapter.getItem(i);
        if (item instanceof SiteItem) {
          if (selectIds.contains(((SiteItem) item).getSpace().getId())) {
            commonFlexAdapter.toggleSelection(i);
            commonFlexAdapter.notifyItemChanged(i);
          }
        }
      }
    }
  }

  @Override public int getFbIcon() {
    if (AppUtils.getCurApp(getContext()) == 0) {
      return 0;
    } else {
      return super.getFbIcon();
    }
  }

  @Override protected SiteItem generateItem(Space space) {
    return new SiteSelectedItem(space);
  }

  @Override public boolean onItemClick(int position) {

    //团课直接返回 私教可以多选
    if (isPrivate) {
      commonFlexAdapter.toggleSelection(position);
      commonFlexAdapter.notifyItemChanged(position);
    } else {
      IFlexible item = commonFlexAdapter.getItem(position);
      if (item instanceof SiteItem) {
        siteSelected.clear();
        siteSelected.add(((SiteItem) item).getSpace());
        RxBus.getBus().post(siteSelected);
        popBack();
      }
    }
    return true;
  }
}
