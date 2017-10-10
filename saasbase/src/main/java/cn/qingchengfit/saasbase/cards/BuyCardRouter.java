package cn.qingchengfit.saasbase.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 2017/8/28.
 */

public class BuyCardRouter extends BaseFragment{

  @Inject
  public BuyCardRouter() {
  }

  //CreateCardBody createCardBody = new CreateCardBody();


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    RxBusAdd(CardTpl.class)
        .subscribe(new BusSubscribe<CardTpl>() {
          @Override public void onNext(CardTpl cardTpl) {
            //createCardBody.card_tpl_id = cardTpl.getId();
            toStep();
          }
        });
    toStep();
  }

  void toStep(){
    //先选卡模板
    //if (TextUtils.isEmpty(createCardBody.card_tpl_id)){
    //  routeTo("/choose/cardtpl/",null);
    //  return;
    //}
    //设置买卡细节
    routeTo("/pay/",null);
  }


}
