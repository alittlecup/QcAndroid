package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGContents;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGEncoder;
import cn.qingchengfit.saascommon.qrcode.views.QRScanActivity;
import cn.qingchengfit.saascommon.views.CommonSimpleTextActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import com.google.zxing.WriterException;
import timber.log.Timber;

public class GenCodeActivity extends BaseActivity {

  public static final String GEN_CODE_URL = "gen.code.url";
  public static final String GEN_CODE_TITLE = "gen.code.title";
  public static final String GEN_CODE_BG = "gen.code.bg";
  Toolbar toolbar;
  TextView toolbarTitile;
  ImageView qrImg;
  TextView tvTitle;
  FrameLayout bg;
  private Bitmap bitmap;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gen_code);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
    qrImg = (ImageView) findViewById(R.id.qr_img);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    bg = (FrameLayout) findViewById(R.id.bg);

    initToolbar(toolbar);
    String tilte = getIntent().getStringExtra(GEN_CODE_TITLE);
    String url = getIntent().getStringExtra(GEN_CODE_URL);
    int bgRes = getIntent().getIntExtra(GEN_CODE_BG, R.drawable.bg_qr_code);
    bg.setBackgroundResource(bgRes);
    if (StringUtils.isEmpty(tilte)) {
      tilte = "扫码二维码";
    }
    if (StringUtils.isEmpty(url)) {
      url = "未成功获取二维码";
    }
    tvTitle.setText(tilte);
    toolbarTitile.setText(tilte);

    QRGEncoder qrgEncoder =
        new QRGEncoder(url, null, QRGContents.Type.TEXT, MeasureUtils.dpToPx(180f, getResources()));
    try {
      // Getting QR-Code as Bitmap

      bitmap = qrgEncoder.encodeAsBitmap();
      // Setting Bitmap to ImageView
      qrImg.setAdjustViewBounds(true);
      qrImg.setPadding(0, 0, 0, 0);
      qrImg.setImageBitmap(bitmap);
    } catch (WriterException e) {
      Timber.e(e, " qr gen");
    }
    if ("签到二维码".equals(tilte)) {
      findViewById(R.id.btn_scan).setVisibility(View.VISIBLE);
      findViewById(R.id.btn_scan).setOnClickListener(v -> {
        Intent intent = new Intent(GenCodeActivity.this, QRScanActivity.class);
        intent.putExtra("title", "扫描二维码");
        intent.putExtra("point_text", "将取景框对准二维码，即可自动扫描");
        startActivityForResult(intent, 1001);
      });
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1001) {
        if (null != data) {
          String content = data.getStringExtra("content");
          if (!TextUtils.isEmpty(content)) {
            if (content.startsWith("QINGCHENG-STAFF-CHECKIN")) {
              Intent toSignInConfigs = new Intent(this, SignInActivity.class);
              toSignInConfigs.setAction(getResources().getString(R.string.qc_action));
              toSignInConfigs.setData(Uri.parse("checkin/member"));
              toSignInConfigs.putExtra("qrcode", content);
              toSignInConfigs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(toSignInConfigs);
            } else if (content.startsWith("http")) {
              WebActivity.startWeb(content, this);
            } else {
              Intent intent = new Intent(this, CommonSimpleTextActivity.class);
              intent.putExtra("content", content);
              startActivity(intent);
            }
          }
        }
      }
    }
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.slide_hold, R.anim.slide_bottom_out);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (bitmap != null) bitmap.recycle();
  }
}
