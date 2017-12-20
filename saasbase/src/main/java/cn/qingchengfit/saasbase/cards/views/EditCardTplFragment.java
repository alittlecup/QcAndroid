package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.subscribes.BusSubscribe;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxMenuItem;
import java.util.concurrent.TimeUnit;

/**
 * Created by fb on 2017/12/19.
 */

@Leaf(module = "card", path = "/cardtpl/edit")
public class EditCardTplFragment extends CardTplDetailFragment {

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    initView();
    return view;
  }

  @OnClick(R2.id.civ_input_card_name)
  public void onName(){
    routeTo("common", "/input/",
        new CommonInputParams().content(presenter.getCardName())
            .title("编辑会员卡种类名称")
            .hint("填写会员卡种类名称")
            .build());
  }

  @Override public String getFragmentName() {
    return EditCardTplFragment.class.getName();
  }

  private void initView(){

    layoutCardValueDesc.setVisibility(View.GONE);
    layoutCardOption.setVisibility(View.GONE);
    civInputCardname.setVisibility(View.VISIBLE);
    civInputCardDesc.setVisibility(View.VISIBLE);
    expandSettingLimit.setVisibility(View.VISIBLE);
    expandCardProtocol.setVisibility(View.VISIBLE);

    civInputCardname.setContent(cardTpl.getName());
    civInputCardDesc.setContent(TextUtils.isEmpty(cardTpl.getDescription()) ? "选填" : "查看");
    expandSettingLimit.setExpanded(cardTpl.is_limit());
    if (cardLimit.is_limit) {
      preOrderCount.setContent(String.valueOf(cardTpl.getPre_times()));
      if (cardTpl.getMonth_times() > 0) {
        duringCount.setContent("每月," + cardTpl.getMonth_times() + "节");
        cardLimit.month_times = cardTpl.getMonth_times();
      } else if (cardTpl.getWeek_times() > 0) {
        duringCount.setContent("每周," + cardTpl.getWeek_times() + "节");
        cardLimit.week_times = cardTpl.getWeek_times();
      } else {
        duringCount.setContent("每天," + cardTpl.getDay_times() + "节");
        cardLimit.day_times = cardTpl.getDay_times();
      }
      limitBugCount.setContent(cardTpl.getBuy_limit() == 0 ? getString(R.string.buy_card_no_limit)
          : getString(R.string.card_count_unit, cardTpl.getBuy_limit()));
    }
  }

  @Override public void setToolbar(@NonNull Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("编辑会员卡种类");
    toolbar.inflateMenu(R.menu.menu_save);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {
            //TODO 保存编辑会员卡种类
          }
        });
  }

}
