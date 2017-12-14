package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.SingleBatch;
import cn.qingchengfit.saasbase.course.batch.network.response.SingleBatchWrap;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2017/9/24.
 */

public class BatchSinglePresenter extends BasePresenter<BatchSinglePresenter.MVPView> {

  @Inject ICourseModel courseApi;

  String scheduleId;
  boolean isPrivate;
  SingleBatch batchDetail;

  public BatchDetail getBatchDetail() {
    return batchDetail;
  }

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  @Inject public BatchSinglePresenter() {
  }



  public void queryData(){
    RxRegiste(courseApi.qcGetSingleBatch(isPrivate,scheduleId)
          .onBackpressureLatest()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new NetSubscribe<QcDataResponse<SingleBatchWrap>>() {
                @Override public void onNext(QcDataResponse<SingleBatchWrap> qcResponse) {
                  if (ResponseConstant.checkSuccess(qcResponse)) {
                    batchDetail = isPrivate?qcResponse.data.timetable:qcResponse.data.schedule;
                    mvpView.onDetail(isPrivate?qcResponse.data.timetable:qcResponse.data.schedule);
                  } else {
                    mvpView.onShowError(qcResponse.getMsg());
                  }
                }
              }));

  }

  public void setOpenRuleType(int type) {
    //this.body.open_rule.type = type;
  }

  public void setOpenRuleTime(String time, Integer abeadHoure) {
    //this.body.open_rule.advance_hours = abeadHoure;
    //this.body.open_rule.open_datetime = time;
  }

  public void editSchedule(){

  }

  public void delSchedule(){

  }

  public interface MVPView extends CView {
    void onDetail(SingleBatch detial);

  }
}
