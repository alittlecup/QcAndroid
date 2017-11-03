package cn.qingchengfit.saasbase.course.batch.presenters;

import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventBatchLooperConfict;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchLoop;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/4 2016.
 */
public abstract class IBatchPresenter extends BasePresenter<IBatchPresenter.MVPView> {

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ICourseModel courseApi;


  protected Staff mTeacher;
  protected BatchCourse mCourse;
  protected ArrangeBatchBody body = new ArrangeBatchBody();

  protected List<BatchLoop> batchloopers = new ArrayList<>();
  protected List<Rule> cardRules = new ArrayList<>();
  protected Rule rulePayOnline;

  //是否为私教课程
  private boolean isPrivate;


  public boolean isPro() {
    return !body.is_free;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }



  /**
   * 判断规则合法性（type //1 立即开放 2 固定时间 3 提前X小时）
   */
  @Nullable public BatchOpenRule getBatchOpenRule() {
    if (body.open_rule == null) {
      return null;
    }
    if (body.open_rule.type == 2 && CmStringUtils.isEmpty(body.open_rule.open_datetime)) {
      return null;
    }
    if (body.open_rule.type == 3 && body.open_rule.advance_hours == null) return null;
    return body.open_rule;
  }

  public void setBatchOpenRule(BatchOpenRule batchOpenRule) {
    this.body.open_rule = batchOpenRule;
  }

  public void setOpenRuleType(int type) {
    this.body.open_rule.type = type;
  }

  public void setOpenRuleTime(String time, Integer abeadHoure) {
    this.body.open_rule.advance_hours = abeadHoure;
    this.body.open_rule.open_datetime = time;
  }

  public void removeLooper(@IntRange(from = 0) int x) {
    if (batchloopers.size() > x) {
      batchloopers.remove(x);
    }
  }

  public void removeAllLoppers() {
    batchloopers.clear();
  }

  @Override public void onStart() {

  }

  @Override public void onStop() {

  }

  @Override public void onPause() {

  }

  public BatchCourse getCourse() {
    return mCourse;
  }

  @Override public void attachView(PView v) {
    super.attachView(v);
    if (!gymWrapper.isPro()) {
      body.is_free = true;
    }
    RxBusAdd(BatchLoop.class).onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<BatchLoop>() {
          @Override public void onNext(BatchLoop cmBean) {
            addOrEditBatchLooper(cmBean);
          }
        });
  }

  private void addOrEditBatchLooper(BatchLoop cmBean) {
    if (cmBean.dateEnd == null && cmBean.dateStart == null && (cmBean.week == null
        || cmBean.week.size() == 0)) {
      return;
    }
    if (cmBean.position >= 0 && batchloopers.size() > cmBean.position) {
      BatchLoop cb = batchloopers.get(cmBean.position);
      //修改
      cb.dateStart = cmBean.dateStart;
      cb.dateEnd = cmBean.dateEnd;
      cb.week.clear();
      cb.week.addAll(cmBean.week);
      mvpView.onLoppers(batchloopers);
      RxBus.getBus().post(new EventBatchLooperConfict(false));
    } else {
      if (BatchLoop.CheckCmBean(batchloopers, cmBean)) {
        //添加
        batchloopers.add(cmBean);
        body.time_repeats = BatchLoop.geTimeRepFromBean(batchloopers);
        mvpView.onLoppers(batchloopers);
        RxBus.getBus().post(new EventBatchLooperConfict(false));
      } else {
        RxBus.getBus().post(new EventBatchLooperConfict(true));
      }
    }
  }

  @Override public void attachIncomingIntent(Intent intent) {

  }

  @Override public void onCreate() {

  }

  @Override public void unattachView() {
    super.unattachView();
  }

  protected void buildBody() {
    // TODO: 2017/9/20
    //body.rules
    body.is_free = !mvpView.needPay();
    body.max_users = mvpView.suportMemberNum();
  }

  /**
   * 检查排课是否冲突
   */
  public void checkBatch() {
    buildBody();
    int ret = body.checkAddBatch();
    if (ret > 0) mvpView.onShowError(ret);
    RxRegiste(courseApi.qcCheckBatch(isPrivate, body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcResponse>() {
          @Override public void onNext(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              arrangeBatch();
            } else {
              mvpView.showAlert(qcResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 像服务端发送排课请求
   */
  public abstract void arrangeBatch();

  /**
   * 删除排期
   */
  public abstract void delBatch();

  /**
   * 获取排课模板（上次排课的记录）
   *
   * @param isPrivate 是否为私教课
   * @param teacher_id 教练ID
   * @param course_id 课程id
   */
  public void getBatchTemplete(boolean isPrivate, String teacher_id, String course_id) {
    RxRegiste(courseApi.qcGetBatchTemplate(isPrivate, teacher_id, course_id)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<ScheduleTemplete>>() {
          @Override public void onNext(QcDataResponse<ScheduleTemplete> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              body.is_free = qcResponse.data.is_free;
              body.max_users = qcResponse.data.max_users;
              /**
               * time repeats -> CmBean
               */
              body.time_repeats = qcResponse.data.time_repeats;

              HashMap<String, ArrayList<Integer>> mTimeRep = new HashMap<>();
              for (int i = 0; i < body.time_repeats.size(); i++) {
                Time_repeat time_repeat = body.time_repeats.get(i);
                String key = time_repeat.getStart() + "-" + time_repeat.getEnd();
                if (mTimeRep.get(key) != null) {
                  mTimeRep.get(key).add(time_repeat.getWeekday());
                } else {
                  ArrayList<Integer> weeks = new ArrayList<>();
                  weeks.add(time_repeat.getWeekday());
                  mTimeRep.put(key, weeks);
                }
              }
              body.rules = qcResponse.data.rule;
              if (body.rules != null) {
                cardRules.clear();
                rulePayOnline = null;
                for (int i = 0; i < body.rules.size(); i++) {
                  if (body.rules.get(i).channel.equals(Constants.CHANNEL_CARD)) {
                    cardRules.add(body.rules.get(i));
                  } else {
                    rulePayOnline = body.rules.get(i);
                  }
                }
              }
              mvpView.onLoppers(BatchLoop.getBeansFromTimeRep(mTimeRep));
              mvpView.onTemplete(qcResponse.data.is_free, rulePayOnline != null, qcResponse.data.max_users);
            } else {
              mvpView.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  public interface MVPView extends CView {
    void onSuccess();

    void onTemplete(boolean isFree, boolean oepnOnlie, int maxuer);

    void onLoppers(List<BatchLoop> loopers);
    void addBatchLooper(BatchLoop loop);



    String getCourseId();

    String getTrainerId();


    boolean supportMutiMember();

    int suportMemberNum();

    boolean needPay();
  }
}
