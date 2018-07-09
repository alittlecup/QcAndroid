package cn.qingchengfit.login.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import cn.qingchengfit.login.LoginActivity;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.filter.model.Content;

public class LoginComponent implements IComponent {
  @Override public String getName() {
    return "login";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "open":
        routeToActivity(qc.getContext());
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        break;
    }
    return false;
  }

  private void routeToActivity(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
