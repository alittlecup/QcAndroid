package cn.qingchengfit.saasbase.component;

import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.model.common.Course;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import com.google.gson.Gson;
import retrofit2.Retrofit;

public class SaasBaseComponent implements IComponent {
  @Override public String getName() {
    return "saasbase";
  }

  @Override public boolean onCall(QC qc) {
    switch (qc.getActionName()){
      case "load/batch/courses":
        ICourseModel iCourseModel = ComponentModuleManager.get(ICourseModel.class);//这里的实现类是通过构造函数初始化的，所以会默认注入到ComponentModuleManager中
        boolean is_private = (boolean) qc.getParams().get("is_private");
        if(iCourseModel!=null){
          iCourseModel.qcGetCourses(is_private)
              .compose(RxHelper.schedulersTransformer())
              .subscribe(response->{
                if(ResponseConstant.checkSuccess(response)){
                  QC.sendQCResult(qc.getCallId(),QCResult.success("data",new Gson().toJson(response.data.courses)));
                }else{
                  QC.sendQCResult(qc.getCallId(),QCResult.error(response.getMsg()));
                }
              },throwable -> {
                QC.sendQCResult(qc.getCallId(),QCResult.error(throwable.getMessage()));
              });
        }
        return true;
      case "load/trainers":
        IStaffModel iStaffModel = ComponentModuleManager.get(IStaffModel.class);
        if(iStaffModel!=null){
          iStaffModel.getTrainers()
              .compose(RxHelper.schedulersTransformer())
              .subscribe(response->{
                if(ResponseConstant.checkSuccess(response)){
                  QC.sendQCResult(qc.getCallId(),QCResult.success("data",new Gson().toJson(response.data.staffships)));
                }else{
                  QC.sendQCResult(qc.getCallId(),QCResult.error(response.getMsg()));
                }
              },throwable -> {
                QC.sendQCResult(qc.getCallId(),QCResult.error(throwable.getMessage()));
              });
        }
        return true;
    }
    return false;
  }
}
