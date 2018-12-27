package cn.qingchengfit.saascommon.qrcode.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * 简单的扫码界面 用来扫码并获取码内信息返回
 */
public class QRScanActivity extends SaasCommonActivity
    implements QRCodeReaderView.OnQRCodeReadListener {

  private QRCodeReaderView qrdecoderview;
  private RelativeLayout rootView;
  private TextView title;

  public static void start(Context context) {
    Intent intent = new Intent(context, QRScanActivity.class);
    context.startActivity(intent);
  }

  public static void startWithTitle(Context context, String title) {
    Intent intent = new Intent(context, QRScanActivity.class);
    intent.putExtra("title", title);
    context.startActivity(intent);
  }

  public static void startWithContent(Context context, String title, String content) {
    Intent intent = new Intent(context, QRScanActivity.class);
    intent.putExtra("title", title);
    intent.putExtra("point_text", content);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.qr_scan_qctivity);
    initToolbar(findViewById(R.id.toolbar));
    title = findViewById(R.id.toolbar_title);
    title.setText("扫码二维码");
    String title = getIntent().getStringExtra("title");
    if (!TextUtils.isEmpty(title)) {
      this.title.setText(title);
    }
    TextView pointText = findViewById(R.id.tv_point_text);
    String point_text = getIntent().getStringExtra("point_text");
    if (!TextUtils.isEmpty(point_text)) {
      pointText.setText(point_text);
    }
    rootView = findViewById(R.id.root_view);
  }

  private void checkPermission() {
    new RxPermissions(this).requestEach(Manifest.permission.CAMERA).subscribe(permission -> {
      if (permission.granted) {
        startPreView();
      } else if (permission.shouldShowRequestPermissionRationale) {
        ToastUtils.show("请允许使用摄像头权限");
      } else {
        canCheck = false;
        DialogUtils.showAlert(this, "应用需要开启摄像头权限，才能正常使用扫码等相机相关功能",
            (materialDialog, dialogAction) -> {
              materialDialog.dismiss();
              AppUtils.doAppSettingPageTo(QRScanActivity.this);
            });
      }
    });
  }

  private boolean canCheck = true;

  private void startPreView() {
    if (qrdecoderview == null) {
      qrdecoderview = new QRCodeReaderView(QRScanActivity.this);
      rootView.addView(qrdecoderview, 0,
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));
      qrdecoderview.setOnQRCodeReadListener(QRScanActivity.this);
    }
    qrdecoderview.getCameraManager().startPreview();
  }

  @Override protected void onResume() {
    super.onResume();
    if (canCheck) {
      checkPermission();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
  }

  @Override protected void onStop() {
    super.onStop();
    canCheck = true;
  }

  @Override public void onQRCodeRead(String text, PointF[] points) {
    if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
    Intent intent = new Intent();
    intent.putExtra("content", text);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  @Override public void cameraNotFound() {

  }

  @Override public void QRCodeNotFoundOnCamImage() {

  }
}
