package cn.qingchengfit.component;

import android.content.Intent;
import android.os.Parcelable;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;

public class StaffComponent implements IComponent {
  @Override public String getName() {
    return "staff";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/add/student":
        Intent add = new Intent(qc.getContext(), StudentActivity.class);
        add.putExtra("remote", "add");
        qc.getContext().startActivity(add);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return true;
      case "/home/student":
        Intent it = new Intent(qc.getContext(), StudentsDetailActivity.class);
        it.putExtra("qcstudent", (Parcelable) qc.getParams().get("qcstudent"));
        qc.getContext().startActivity(it);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return true;
    }
    return false;
  }
}
