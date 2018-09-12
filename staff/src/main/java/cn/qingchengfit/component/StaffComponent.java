package cn.qingchengfit.component;

import android.content.Intent;
import android.os.Parcelable;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.staffkit.views.ChooseActivity;
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
        add.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        add.putExtra("remote", "add");
        qc.getContext().startActivity(add);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
      case "/home/student":
        Intent it = new Intent(qc.getContext(), StudentsDetailActivity.class);
        it.putExtra("qcstudent", (Parcelable) qc.getParams().get("qcstudent"));
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        qc.getContext().startActivity(it);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
      case "ChooseActivity":
        Intent intent = new Intent(qc.getContext(), ChooseActivity.class);
        int to = (int) qc.getParams().get("to");
        intent.putExtra("to", to);
        intent.putExtra("open", (boolean) qc.getParams().get("open"));
        intent.putExtra("callId", qc.getCallId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        qc.getContext().startActivity(intent);
        return true;
    }
    return false;
  }
}
