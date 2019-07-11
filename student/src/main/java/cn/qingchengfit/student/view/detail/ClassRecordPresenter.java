package cn.qingchengfit.student.view.detail;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.respository.IStudentModel;
import java.util.HashMap;
import javax.inject.Inject;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */

public class ClassRecordPresenter extends BasePresenter {
  @Inject StudentWrap studentBase;
  public String requestUserID="";
  @Inject IStudentModel studentModel;
  private ClassRecordView view;

  @Inject public ClassRecordPresenter() {
  }

  @Override public void onStart() {

  }

  @Override public void onStop() {

  }

  @Override public void onPause() {

  }

  @Override public void attachView(PView v) {
    view = (ClassRecordView) v;
  }

  @Override public void attachIncomingIntent(Intent intent) {

  }

  @Override public void onCreate() {

  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryData(HashMap<String, Object> params) {
    if (params.containsKey("type")) {
      if (TextUtils.isEmpty(String.valueOf(params.get("type")))) {
        params.remove("type");
      }
    }

    if (params.containsKey("status")) {
      if (String.valueOf(params.get("status")).equals("0")) {
        params.remove("status");
      }
    }

    if (params.containsKey("start")) {
      if (TextUtils.isEmpty(String.valueOf(params.get("start")))) {
        params.remove("start");
      }
    }

    if (params.containsKey("end")) {
      if (TextUtils.isEmpty(String.valueOf(params.get("end")))) {
        params.remove("end");
      }
    }

    if (StringUtils.isEmpty(String.valueOf(params.get("shop_ids")))) {
      params.put("shop_ids", 0);
    }

    RxRegiste(studentModel.qcGetStudentClassRecords(studentBase.id(), params)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.attendances != null
                && qcResponse.data.stat != null
                && qcResponse.data.shops != null) {
              requestUserID = qcResponse.data.requestUserId;
              view.onData(qcResponse.getData().attendances, qcResponse.getData().stat,
                  qcResponse.getData().shops);
            }
          }
        }, throwable -> {
        }));
  }
}
