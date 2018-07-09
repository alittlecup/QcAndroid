package cn.qingchengfit.saasbase.cards.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.OnBackEvent;
import cn.qingchengfit.saascommon.qrcode.model.QrEvent;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.views.activity.WebActivity;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/11/21.
 */

public class CardProtocolActivity extends WebActivity{

  @Inject GymWrapper gymWrapper;
  @Inject IPermissionModel permissionModel;

  public static void startWeb(String url, Context context, boolean isHaveModify) {
    Intent intent = new Intent(context, CardProtocolActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("isHave", isHaveModify);
    context.startActivity(intent);
  }

  public static void startWeb(String url, Context context, boolean isHaveModify, String content) {
    Intent intent = new Intent(context, CardProtocolActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("isHave", isHaveModify);
    intent.putExtra("content", content);
    context.startActivity(intent);
  }

  public static void startWeb(String url, Context context, boolean isHaveModify, String content, CardTpl card_tpl) {
    Intent intent = new Intent(context, CardProtocolActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("isHave", isHaveModify);
    intent.putExtra("content", content);
    intent.putExtra("card_tpl", card_tpl);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String toUrl = getIntent().getStringExtra("url");
    if (!getIntent().getBooleanExtra("isHave", false)) {
      webfrag = CardProtocolWebFragment.newInstance(toUrl, getIntent().getStringExtra("content"));
    }else{
      webfrag = CardProtocolWebFragment.newInstance(toUrl, R.menu.menu_modify);
      ((CardProtocolWebFragment)webfrag).setOnMenuClickListener(new CardProtocolWebFragment.OnMenuClickListener() {
        @Override public void onMenuClick() {
          CardTpl cardTpl = getIntent().getParcelableExtra("card_tpl");
          if (!gymWrapper.inBrand() && permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
            Intent intent = new Intent(CardProtocolActivity.this, QRActivity.class);
            intent.putExtra(QRActivity.LINK_MODULE, getString(R.string.qr_code_2web_modify_card,
                cardTpl.id));
            startActivity(intent);
          }else if (gymWrapper.inBrand() && permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,
              cardTpl.getShopIds())){
            Intent intent = new Intent(CardProtocolActivity.this, QRActivity.class);
            intent.putExtra(QRActivity.LINK_MODULE,
                getResources().getString(R.string.qr_code_2web_multi_card_edit,
                    gymWrapper.brand_id(), cardTpl.id));

            startActivity(intent);
          }else{
            showAlert(R.string.alert_edit_cardtype_no_permission);
          }
        }
      });
    }
    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, webfrag).commit();

    initBus();

  }

  private void initBus(){
    RxBus.getBus().register(QrEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(onBackEvent -> finish());
  }

}
