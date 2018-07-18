package cn.qingchengfit.student.component;

import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.utils.RouteUtil;

public class StudentComponent implements IComponent {
  @Override public String getName() {
    return "student";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName){
      case "/student/home":
        RouteUtil.routeTo(qc.getContext(),getName(),actionName,null);
        QC.sendQCResult(qc.getCallId(),QCResult.success());
        return true;
    }
    return false;
  }
}
