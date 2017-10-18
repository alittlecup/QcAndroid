package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionItem;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DrawableUtils;
import com.anbillon.flabellum.annotations.Leaf;
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
  @Inject BindCardModel.SelectedData selectedData;
  @Inject GymWrapper gymWrapper;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    comonAdapter.addItem(new AddCardtplStantardItem());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    civInputCardname.setVisibility(View.VISIBLE);
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

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("新增会员卡价格");
    toolbar.getMenu().clear();
    toolbar.getMenu().add("保存").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        // TODO: 2017/10/16 新建卡种类
        return true;
      }
    });
  }

  @Override protected void onFinishAnimation() {

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view =  super.onCreateView(inflater, container, savedInstanceState);
    RxBusAdd(OptionBody.class)
        .subscribe(new BusSubscribe<OptionBody>() {
          @Override public void onNext(OptionBody optionBody) {
            comonAdapter.addItem(0,new CardtplOptionItem(CardBusinessUtils.optionBody2Option(optionBody),selectedData.cardcategory));
          }
        });
    initCardTpl();
    return view;
  }

  /**
   * 填写一些基础信息
   */
  private void initCardTpl(){
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(selectedData.cardcategory,getContext()));
    tvGymName.setText(gymWrapper.name());
    cardview.setBackground(DrawableUtils.generateBg(8,CardBusinessUtils.getDefaultCardbgColor(selectedData.cardcategory)));
  }


}
