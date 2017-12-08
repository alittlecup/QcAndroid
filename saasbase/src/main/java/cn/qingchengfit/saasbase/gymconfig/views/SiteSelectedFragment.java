package cn.qingchengfit.saasbase.gymconfig.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.gymconfig.event.EventSiteSelected;
import cn.qingchengfit.saasbase.gymconfig.item.SiteItem;
import cn.qingchengfit.saasbase.gymconfig.item.SiteSelectedItem;
import cn.qingchengfit.subscribes.BusSubscribe;
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
@Leaf(module = "gym", path = "/site/choose/")
public class SiteSelectedFragment extends SiteFragment {

  @Need boolean isPrivate;
  @Need ArrayList<String> selectIds;
  protected Toolbar toolbar;
  protected TextView toolbarTitle;
  private EventSiteSelected siteSelected = new EventSiteSelected();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View v =  super.onCreateView(inflater, container, savedInstanceState);
    ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.layout_toolbar_container,container,false);
    toolbar = parent.findViewById(R.id.toolbar);
    toolbarTitle = parent.findViewById(R.id.toolbar_title);
    initToolbar(toolbar);
    parent.addView(v,1);
    return parent;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.getMenu().clear();
    if (isPrivate){
      toolbar.getMenu().add("确定").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      RxMenuItem.clicks(toolbar.getMenu().findItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {

            siteSelected.clear();
            for (Integer integer : commonFlexAdapter.getSelectedPositions()) {
              IFlexible item = commonFlexAdapter.getItem(integer);
              if (item instanceof SiteItem){
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
    setQueryType(isPrivate?1:0);
    super.onRefresh();
  }

  @Override public void onUpdateEmptyView(int size) {
    if (selectIds != null && selectIds.size() > 0){
      for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
        IFlexible item = commonFlexAdapter.getItem(i);
        if (item instanceof SiteItem){
          if (selectIds.contains(((SiteItem) item).getSpace().getId())){
            commonFlexAdapter.toggleSelection(i);
            commonFlexAdapter.notifyItemChanged(i);
          }
        }
      }
    }
  }

  @Override protected SiteItem generateItem(Space space) {
    return new SiteSelectedItem(space);
  }

  @Override public boolean onItemClick(int position) {

    //团课直接返回 私教可以多选
    if (isPrivate){
      commonFlexAdapter.toggleSelection(position);
      commonFlexAdapter.notifyItemChanged(position);
    }else {
      IFlexible item = commonFlexAdapter.getItem(position);
      if (item instanceof SiteItem){
        siteSelected.clear();
        siteSelected.add(((SiteItem) item).getSpace());
        RxBus.getBus().post(siteSelected);
        popBack();
      }
    }
    return true;
  }
}
