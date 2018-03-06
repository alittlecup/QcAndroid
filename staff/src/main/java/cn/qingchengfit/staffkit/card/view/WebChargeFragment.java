package cn.qingchengfit.staffkit.card.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.weex.module.QcNavigatorModule;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.gson.Gson;

/**
 * Created by huangbaole on 2018/3/6.
 */
@Leaf(module = "card",path = "/web/charge/")
public class WebChargeFragment extends  StaffCardChargeFragment {
  String web_action;
  public void setWebAction(String webAction){
    web_action=webAction;
  }
  @Override public void onSuccess() {
    ToastUtils.show("续卡成功");
    sendBack(new Gson().toJson(card));
  }
  private void sendBack(String json){
    if(TextUtils.isEmpty(web_action)){
      RxBus.getBus().post(QcNavigatorModule.class,json);
    }else {
      Intent intent = new Intent();
      intent.putExtra("web_action", web_action);
      intent.putExtra("json", json);
      getActivity().setResult(Activity.RESULT_OK, intent);
    }
    getActivity().finish();
  }

  @Override public void onFailed(String s) {

    ToastUtils.show(s);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(!(resultCode== Activity.RESULT_OK)){
      ToastUtils.show("充值失败");
    }
  }
}
