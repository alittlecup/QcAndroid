package cn.qingchengfit.views.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/7/19.
 */

public class ScanActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

	Toolbar toolbar;
	TextView toolbarTitle;
	TextView tvTips;
	RelativeLayout rootView;
  QRCodeReaderView qrdecoderview;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    tvTips = (TextView) findViewById(R.id.tv_tips);
    rootView = (RelativeLayout) findViewById(R.id.root_view);

    initToolbar(toolbar);
    new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          initCamara();
        } else {
          ToastUtils.show("请打开摄像头权限");
        }
      }
    });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("扫码二维码");
      String tips = getIntent().getStringExtra("tips");
      if (tips != null) {
        tvTips.setText(tips);
      }
  }


  public void initCamara() {
    qrdecoderview = new QRCodeReaderView(this);
    rootView.addView(qrdecoderview, 0,
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    qrdecoderview.setOnQRCodeReadListener(this);
    qrdecoderview.getCameraManager().startPreview();
  }

  @Override protected void onResume() {
    super.onResume();
    if (qrdecoderview != null) qrdecoderview.getCameraManager().startPreview();
  }

  @Override protected void onPause() {
    super.onPause();
    if (qrdecoderview != null) qrdecoderview.getCameraManager().stopPreview();
  }

  @Override public void onQRCodeRead(String text, PointF[] points) {
    Intent ret = new Intent();
    ret.putExtra("web_action", getIntent().getStringExtra("web_action"));
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("content", text);
    } catch (JSONException e) {
    }
    ret.putExtra("json", jsonObject.toString());
    setResult(Activity.RESULT_OK, ret);
    this.finish();
  }

  @Override public void cameraNotFound() {
    showAlert("摄像头已被占用");
  }

  @Override public void QRCodeNotFoundOnCamImage() {
    //showAlert("无法识别二维码");
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }
}
