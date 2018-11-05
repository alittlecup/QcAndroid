package lib_zxing.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.writeoff.R;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.camera);
    new RxPermissions(this).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          CaptureFragment captureFragment = new CaptureFragment();
          captureFragment.setAnalyzeCallback(analyzeCallback);
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.fl_zxing_container, captureFragment)
              .commit();
          captureFragment.setCameraInitCallBack(new CaptureFragment.CameraInitCallBack() {
            @Override public void callBack(Exception e) {
              if (e == null) {

              } else {
                Log.e("TAG", "callBack: ", e);
              }
            }
          });
        } else {
          ToastUtils.show("请打开摄像头权限");
        }
      }
    });
  }

  /**
   * 二维码解析回调函数
   */
  CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
    @Override public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
      Intent resultIntent = new Intent();
      Bundle bundle = new Bundle();
      bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
      bundle.putString(CodeUtils.RESULT_STRING, result);
      resultIntent.putExtras(bundle);
      CaptureActivity.this.setResult(RESULT_OK, resultIntent);
      CaptureActivity.this.finish();
    }

    @Override public void onAnalyzeFailed() {
      Intent resultIntent = new Intent();
      Bundle bundle = new Bundle();
      bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
      bundle.putString(CodeUtils.RESULT_STRING, "");
      resultIntent.putExtras(bundle);
      CaptureActivity.this.setResult(RESULT_OK, resultIntent);
      CaptureActivity.this.finish();
    }
  };
}