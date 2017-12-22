package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.cards.event.EventCardTplOption;
import cn.qingchengfit.saasbase.cards.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionItem;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DrawableUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.UUID;
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
 * Created by Paper on 2017/10/12.
 */
@Leaf(module = "card",path = "/cardtpl/add/")
public class CardtplAddFragment extends CardTplDetailFragment {
  @Inject GymWrapper gymWrapper;
  @Need Integer cardCategory = 1;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    comonAdapter.addItem(new AddCardtplStantardItem());
    RxBus.getBus().register(EventCardTplOption.class)
      .compose(this.<EventCardTplOption>bindToLifecycle())
      .compose(this.<EventCardTplOption>doWhen(FragmentEvent.CREATE_VIEW))
      .subscribe(new BusSubscribe<EventCardTplOption>() {
        @Override public void onNext(EventCardTplOption event) {
          OptionBody optionBody = event.body;
          CardtplOptionItem item = new CardtplOptionItem(CardBusinessUtils.optionBody2Option(optionBody),cardCategory);
          if (comonAdapter.contains(item)){
            if (event.action < 0){//删除
              comonAdapter.removeItem(comonAdapter.index(item));
            }else
              comonAdapter.updateItem(item,1);
          }else {
            item.getOption().id = UUID.randomUUID().toString();
            comonAdapter.addItem(0,item);
          }
        }
      });

  }

  @Override public void initCardProtocol() {

    inputCardProtocol.setVisibility(View.GONE);
    presenter.stashCardTplInfo();
  }

  @Override public void onRefresh() {

  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.setCardCate(cardCategory);
    civInputCardname.setVisibility(View.VISIBLE);
    civInputCardDesc.setVisibility(View.VISIBLE);
    expandSettingLimit.setVisibility(View.VISIBLE);
    expandCardProtocol.setVisibility(View.VISIBLE);
    civInputCardname.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCardtplName.setText(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });
  }

  @Override public void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("新增会员卡种类");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {
            presenter.createCardTpl();
          }
        });
  }

  @Override protected void onFinishAnimation() {
  }

  @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container,
      Bundle savedInstanceState) {
    View view =  super.onCreateView(inflater, container, savedInstanceState);
    initCardTpl();

    return view;
  }

  /**
   * 填写一些基础信息
   */
  private void initCardTpl(){
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardCategory,getContext()));
    tvGymName.setText(gymWrapper.name());
    cardview.setBackground(DrawableUtils.generateBg(8,CardBusinessUtils.getDefaultCardbgColor(cardCategory)));
  }


}
