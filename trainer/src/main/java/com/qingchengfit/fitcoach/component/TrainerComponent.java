package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.content.Intent;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
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
        qc.getContext().startActivity(intent);
        return true;
    }
    return false;
  }
}
