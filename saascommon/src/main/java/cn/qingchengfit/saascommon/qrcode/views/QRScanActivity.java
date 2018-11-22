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
import cn.qingchengfit.utils.ToastUtils;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/**
 * 简单的扫码界面 用来扫码并获取码内信息返回
 */
public class QRScanActivity extends SaasCommonActivity
    implements QRCodeReaderView.OnQRCodeReadListener {

  private QRCodeReaderView qrdecoderview;
  private RelativeLayout rootView;
  private TextView title;
  public static final int REQUEST_CODE = 1093;

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

  private void startPreview() {
    new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          qrdecoderview = new QRCodeReaderView(QRScanActivity.this);
          rootView.addView(qrdecoderview, 0,
              new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT));
          qrdecoderview.setOnQRCodeReadListener(QRScanActivity.this);
          qrdecoderview.getCameraManager().startPreview();
        } else {
          ToastUtils.show("请打开摄像头权限");
        }
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    if (qrdecoderview != null) {
      qrdecoderview.getCameraManager().startPreview();
    } else {
      startPreview();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
  }

  @Override public void onQRCodeRead(String text, PointF[] points) {
    if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
    Intent intent = new Intent();
    intent.putExtra("content", text);
    setResult(Activity.RESULT_OK,intent);
    finish();
  }

  @Override public void cameraNotFound() {

  }

  @Override public void QRCodeNotFoundOnCamImage() {

  }
}
