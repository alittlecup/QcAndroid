package com.qingchengfit.fitcoach.component;

import android.content.Intent;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import com.qingchengfit.fitcoach.activity.FragActivity;

public class TrainerComponent implements IComponent {
  @Override public String getName() {
    return "trainer";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/add/student":
        Intent intent = new Intent(qc.getContext(), FragActivity.class);
        intent.putExtra("type", 7);
        intent.putExtra("qcCallId",qc.getCallId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        qc.getContext().startActivity(intent);
        return true;
      case "open/gymdetail":
        Intent toGymdetail = new Intent(qc.getContext(), FragActivity.class);
        toGymdetail.putExtra("type", 10);
        qc.getContext().startActivity(toGymdetail);
        return false;
    }
    return false;
  }
}
