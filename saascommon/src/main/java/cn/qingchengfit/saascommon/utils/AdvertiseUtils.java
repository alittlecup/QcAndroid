package cn.qingchengfit.saascommon.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import rx.android.schedulers.AndroidSchedulers;
import java.util.List;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saascommon.model.AdvertiseInfo;
import cn.qingchengfit.saascommon.network.SaasCommonApi;
import cn.qingchengfit.saascommon.views.AdvertiseDialog;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import rx.schedulers.Schedulers;

/**
 * Created by Bob Du on 2018/10/17 17:32
 */
public class AdvertiseUtils {

  public void showAdvertise(QcRestRepository qcRestRepository, String source, Context context) {
    String today = DateUtils.getStringToday();
    String oldDay = PreferenceUtils.getPrefString(context, "advertise_today_"+source, "default");
    if (!today.equals(oldDay)) {
      loadAdvertise(qcRestRepository, source, context);
    }
  }

  public void loadAdvertise(QcRestRepository qcRestRepository, String source, Context context) {
    qcRestRepository.createRxJava1Api(SaasCommonApi.class)
        .qcGetAdvertise(source)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> {
          List<AdvertiseInfo> ad_data = response.getData().ad_data;
          if (ad_data != null && !ad_data.isEmpty() && context != null) {

            AdvertiseDialog dialog = new AdvertiseDialog(context);
            dialog.setClickListener(new AdvertiseDialog.DialogClickListener() {
              @Override public void onCloseClickListener(Dialog dialog) {
                dialog.dismiss();
              }

              @Override public void onItemClickListener(AdvertiseInfo item) {
                WebActivity.startWeb(item.web_url, context);
              }
            });
            dialog.setAdData(ad_data);
            dialog.showDialog();
            PreferenceUtils.setPrefString(context, "advertise_today_"+source, DateUtils.getStringToday());
          }

          if (TextUtils.isEmpty(response.getData().ad_text)) {
            response.getData().ad_text = "";
          }
          PreferenceUtils.setPrefString(context, "discover_text", response.getData().ad_text);
          RxBus.getBus().post(AdvertiseUtils.class, response.getData().ad_text);
        }, new NetWorkThrowable());
  }
}
