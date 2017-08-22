package cn.qingchengfit.views.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
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

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.tv_tips) TextView tvTips;
  @BindView(R2.id.root_view) RelativeLayout rootView;
  QRCodeReaderView qrdecoderview;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
    ButterKnife.bind(this);
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

  private void initToolbar(Toolbar toolbar) {
    toolbar.setNavigationIcon(R.drawable.md_nav_back);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
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
