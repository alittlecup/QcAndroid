package cn.qingchengfit.pos.routers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.pos.card.PosCardBuyFragment;
import cn.qingchengfit.pos.card.PosCardChargeFragment;
import cn.qingchengfit.pos.card.PosCardListHomeFragment;
import cn.qingchengfit.saasbase.routers.cardImpl;

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

public class CardRouters extends cardImpl {
  @Override public Fragment toCardChargeFragment(Bundle args) {
    PosCardChargeFragment fragment = new PosCardChargeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toCardBuyFragment(Bundle args) {
    PosCardBuyFragment fragment = new PosCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toCardListHomeFragment(Bundle args) {
    PosCardListHomeFragment fragment = new PosCardListHomeFragment();
    fragment.setArguments(args);
    return fragment;
  }

}
