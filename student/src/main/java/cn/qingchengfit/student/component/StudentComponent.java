package cn.qingchengfit.student.component;

import android.os.Bundle;
import android.os.Parcelable;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.student.view.choose.ChooseAndSearchStudentParams;
import java.util.ArrayList;
import java.util.Map;

public class StudentComponent implements IComponent {

  @Override public String getName() {
    return "student";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/student/home":
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, null);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return true;
      case "/student/follow_record":
        Bundle params=new Bundle();
        params.putParcelable("qcStudentBean", (Parcelable) qc.getParams().get("qcStudentBean"));
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, params);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return true;
      case "/choose/student/":
        Bundle studentIdList =
            new ChooseAndSearchStudentParams()
                .chooseType(qc.getParams().get("chooseType")==null?null:(int)qc.getParams().get("chooseType"))
                .source(qc.getParams().get("source")==null?null:(String) qc.getParams().get("source"))
                .studentIdList(qc.getParams().get("studentIdList")==null?null:((ArrayList<String>)qc.getParams().get("studentIdList")))
                .build();
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, studentIdList);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return true;
      case "/student/birthday":
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, null);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
    }
    return false;
  }
}
