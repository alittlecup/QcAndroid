package cn.qingchengfit.staffkit.views.cardtype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.QRActivity;
import cn.qingchengfit.views.activity.WebActivity;
import java.io.Serializable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/11/21.
 */

public class CardProtocolActivity extends WebActivity implements Serializable{

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

  public static void startWeb(String url, Context context, boolean isHaveModify, String content, String card_tpl_id) {
    Intent intent = new Intent(context, CardProtocolActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("isHave", isHaveModify);
    intent.putExtra("content", content);
    intent.putExtra("card_tpl_id", card_tpl_id);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String toUrl = getIntent().getStringExtra("url");
    if (!getIntent().getBooleanExtra("isHave", false)) {
      webfrag = CardProtocolWebFragment.newInstance(toUrl, getIntent().getStringExtra("content"));
    }else{
      webfrag = CardProtocolWebFragment.newInstance(toUrl, R.menu.menu_modify, new CardProtocolWebFragment.OnMenuClickListener() {
        @Override public void onMenuClick() {
          Intent intent = new Intent(CardProtocolActivity.this, QRActivity.class);
          intent.putExtra(QRActivity.LINK_MODULE, getString(R.string.qr_code_2web_modify_card, getIntent().getStringExtra("card_tpl_id")));
          startActivity(intent);
        }
      });
    }
    getSupportFragmentManager().beginTransaction().replace(R.id.student_frag, webfrag).commit();

    initBus();

  }

  private void initBus(){
    RxBus.getBus().register(OnBackEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<OnBackEvent>() {
          @Override public void call(OnBackEvent onBackEvent) {
            finish();
          }
        });
  }

}
