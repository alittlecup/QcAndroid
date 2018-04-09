package cn.qingchengfit.weex.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;
import cn.qingchengfit.weex.R;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine.DestroyableModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.utils.WXLogUtils;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbaole on 2018/2/6.
 */

public class QcWxModalUIModule extends DestroyableModule {

  /**
   * WXModalUIModule module provide toast、alert、confirm、prompt to display the message.
   * for example(weex JS):
   * this.$call('modal','toast',{'message':'test toast','duration': 2.0});
   */
  public static final String OK = "知道了";
  public static final String CONFIM = "确定";
  public static final String CANCEL = "取消";
  public static final String RESULT = "result";
  public static final String DATA = "data";
  public static final String MESSAGE = "message";
  public static final String DURATION = "duration";
  public static final String OK_TITLE = "okTitle";
  public static final String CANCEL_TITLE = "cancelTitle";
  public static final String DEFAULT = "default";
  private Toast toast;
  private Dialog activeDialog;

  @JSMethod(uiThread = true) public void toast(String param) {

    String message = "";
    int duration = Toast.LENGTH_SHORT;
    if (!TextUtils.isEmpty(param)) {
      try {
        param = URLDecoder.decode(param, "utf-8");
        JSONObject jsObj = JSON.parseObject(param);
        message = jsObj.getString(MESSAGE);
        duration = jsObj.getInteger(DURATION);
      } catch (Exception e) {
        WXLogUtils.e("[WXModalUIModule] alert param parse error ", e);
      }
    }
    if (TextUtils.isEmpty(message)) {
      WXLogUtils.e("[WXModalUIModule] toast param parse is null ");
      return;
    }

    if (duration > 3) {
      duration = Toast.LENGTH_LONG;
    } else {
      duration = Toast.LENGTH_SHORT;
    }
    if (toast == null) {
      toast = Toast.makeText(mWXSDKInstance.getContext(), message, duration);
    } else {
      toast.setDuration(duration);
      toast.setText(message);
    }
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  @JSMethod(uiThread = true) public void alert(String param, final JSCallback callback) {

    if (mWXSDKInstance.getContext() instanceof Activity) {

      String message = "";
      String okTitle = OK;
      if (!TextUtils.isEmpty(param)) {
        try {
          param = URLDecoder.decode(param, "utf-8");
          JSONObject jsObj = JSON.parseObject(param);
          message = jsObj.getString(MESSAGE);
          okTitle = jsObj.getString(OK_TITLE);
        } catch (Exception e) {
          WXLogUtils.e("[WXModalUIModule] alert param parse error ", e);
        }
      }
      if (TextUtils.isEmpty(message)) {
        message = "";
      }
      final String okTitle_f = TextUtils.isEmpty(okTitle) ? OK : okTitle;

      MaterialDialog alertDialog =
          new MaterialDialog.Builder(mWXSDKInstance.getContext()).positiveText(okTitle_f)
              .autoDismiss(true)
              .content(message)
              .positiveColorRes(R.color.colorPrimary)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  if (callback != null) {
                    callback.invoke(okTitle_f);
                  }
                }
              })
              .canceledOnTouchOutside(false)
              .build();
      alertDialog.show();
      tracking(alertDialog);
    } else

    {
      WXLogUtils.e(
          "[WXModalUIModule] when call alert mWXSDKInstance.getContext() must instanceof Activity");
    }
  }

  @JSMethod(uiThread = true) public void confirm(String param, final JSCallback callback) {

    if (mWXSDKInstance.getContext() instanceof Activity) {
      String message = "";
      String okTitle = CONFIM;
      String cancelTitle = CANCEL;

      if (!TextUtils.isEmpty(param)) {
        try {
          param = URLDecoder.decode(param, "utf-8");
          JSONObject jsObj = JSON.parseObject(param);
          message = jsObj.getString(MESSAGE);
          okTitle = jsObj.getString(OK_TITLE);
          cancelTitle = jsObj.getString(CANCEL_TITLE);
        } catch (Exception e) {
          WXLogUtils.e("[WXModalUIModule] confirm param parse error ", e);
        }
      }
      if (TextUtils.isEmpty(message)) {
        message = "";
      }

      final String okTitleFinal = TextUtils.isEmpty(okTitle) ? CONFIM : okTitle;
      final String cancelTitleFinal = TextUtils.isEmpty(cancelTitle) ? CANCEL : cancelTitle;

      MaterialDialog alertDialog =
          new MaterialDialog.Builder(mWXSDKInstance.getContext()).positiveText(okTitleFinal)
              .negativeText(cancelTitleFinal)
              .positiveColorRes(R.color.colorPrimary)
              .negativeColorRes(R.color.qc_text_grey)
              .autoDismiss(true)
              .content(message)
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  if (callback != null) {
                    callback.invoke(okTitleFinal);
                  }
                }
              })
              .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  if (callback != null) {
                    callback.invoke(cancelTitleFinal);
                  }
                }
              })
              .canceledOnTouchOutside(false)
              .build();
      alertDialog.show();
      tracking(alertDialog);
    } else {
      WXLogUtils.e(
          "[WXModalUIModule] when call confirm mWXSDKInstance.getContext() must instanceof Activity");
    }
  }

  @JSMethod(uiThread = true) public void prompt(String param, final JSCallback callback) {
    if (mWXSDKInstance.getContext() instanceof Activity) {
      String message = "";
      String defaultValue = "";
      String okTitle = OK;
      String cancelTitle = CANCEL;

      if (!TextUtils.isEmpty(param)) {
        try {
          param = URLDecoder.decode(param, "utf-8");
          JSONObject jsObj = JSON.parseObject(param);
          message = jsObj.getString(MESSAGE);
          okTitle = jsObj.getString(OK_TITLE);
          cancelTitle = jsObj.getString(CANCEL_TITLE);
          defaultValue = jsObj.getString(DEFAULT);
        } catch (Exception e) {
          WXLogUtils.e("[WXModalUIModule] confirm param parse error ", e);
        }
      }

      if (TextUtils.isEmpty(message)) {
        message = "";
      }
      AlertDialog.Builder builder = new AlertDialog.Builder(mWXSDKInstance.getContext());
      builder.setMessage(message);

      final EditText editText = new EditText(mWXSDKInstance.getContext());
      editText.setText(defaultValue);
      builder.setView(editText);
      final String okTitleFinal = TextUtils.isEmpty(okTitle) ? OK : okTitle;
      final String cancelTitleFinal = TextUtils.isEmpty(cancelTitle) ? CANCEL : cancelTitle;
      builder.setPositiveButton(okTitleFinal, new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          if (callback != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put(RESULT, okTitleFinal);
            result.put(DATA, editText.getText().toString());
            callback.invoke(result);
          }
        }
      }).setNegativeButton(cancelTitleFinal, new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          if (callback != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put(RESULT, cancelTitleFinal);
            result.put(DATA, editText.getText().toString());
            callback.invoke(result);
          }
        }
      });
      AlertDialog alertDialog = builder.create();
      alertDialog.setCanceledOnTouchOutside(false);
      alertDialog.show();
      tracking(alertDialog);
    } else {
      WXLogUtils.e("when call prompt mWXSDKInstance.getContext() must instanceof Activity");
    }
  }

  private void tracking(Dialog dialog) {
    activeDialog = dialog;
    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override public void onDismiss(DialogInterface dialog) {
        activeDialog = null;
      }
    });
  }

  @Override public void destroy() {
    if (activeDialog != null && activeDialog.isShowing()) {
      WXLogUtils.w("Dismiss the active dialog");
      activeDialog.dismiss();
    }
  }
}
