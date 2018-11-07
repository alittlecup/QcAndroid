package cn.qingchengfit.staffkit.views.student.attendance.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.views.student.attendance.model.NotSignStudent;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/18.
 */

public class NotSignPresenter extends BasePresenter {

  private MVPView view;
  @Inject QcRestRepository restRepository;
  @Inject GymWrapper gymWrapper;

  @Inject
  public NotSignPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null)
      view = null;
  }

  public void getNotSignStudent(String start, String end, int limit){
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("start", start);
    params.put("end", end);
    params.put("limit", limit);
    RxRegiste(restRepository.createRxJava1Api(Get_Api.class)
        .qcGetNotSignStudent(App.staffId, params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<List<NotSignStudent>>>() {
          @Override public void call(QcDataResponse<List<NotSignStudent>> listQcDataResponse) {
            if (ResponseConstant.checkSuccess(listQcDataResponse)){
              if (view != null)
                view.onGetNotSign(listQcDataResponse.data);
            }else{
              view.onShowError(listQcDataResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public StudentBean convertStudentBean(NotSignStudent student){
    StudentBean bean = new StudentBean();
    bean.id = student.id;
    bean.username = student.username;
    bean.avatar = student.avatar;
    bean.phone = student.phone;
    bean.gender = (student.gender == 1);
    return bean;
  }

  public interface MVPView extends CView{
    void onGetNotSign(List<NotSignStudent> studentList);
  }

}
