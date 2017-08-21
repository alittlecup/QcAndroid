package cn.qingchengfit.saasbase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.router.ModuleRouter;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplFragment;
import cn.qingchengfit.saasbase.routers.ICourseRouter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.HashMap;
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
 * Created by Paper on 2017/8/15.
 */

public class SaasRouter extends ModuleRouter {

  @Inject SaasContainerActivity activity;
  @Inject GymWrapper gymWrapper;

  @Inject public SaasRouter() {
  }

  @Override public void loadMoudle(Context context, Intent intent, int request) {

  }

  @Override public void fragloadMoudle(Fragment context, Intent intent, int request) {
  }

  @Override public String getMoudleName() {
    return "saas";
  }

  public void init(Uri x) {
    BaseFragment f = getFragment(x);
    activity.getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
            R.anim.slide_right_out)
        .replace(activity.getFragId(), f, f.getFragmentName())
        .commit();
  }
  public void routerTo(String uri){

  }

  public void choose(String uri){

  }

  public void routerTo(String uri,HashMap<String,Object> params){

  }
  public void routerTo(Uri x) {
    BaseFragment f = getFragment(x);
    activity.getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in,
            R.anim.slide_right_out)
        .replace(activity.getFragId(), f, f.getFragmentName())
        .addToBackStack(null)
        .commit();
  }

  private BaseFragment getFragment(Uri x) {

    if (x.getHost().startsWith("cardtpl")) {
      //卡模板模块
      if (x.getPath().startsWith("/home")) {
        //卡模板首页
        if (gymWrapper.inBrand()) {
          //品牌下
        } else {
          //场馆下
          return iCourseRouter.cardTplsHomeInGymFragment();
        }
      } else if (x.getPath().startsWith("/choose")) {
        //选择卡模板
        return new ChooseCardTplFragment();
      }
    }else if (x.getHost().startsWith("course")){

    }
    return new EmptyFragment();
  }


  public void back(){
    activity.onBackPressed();
  }
}
