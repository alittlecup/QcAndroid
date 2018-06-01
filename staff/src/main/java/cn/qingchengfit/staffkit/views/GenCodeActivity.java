package cn.qingchengfit.staffkit.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.activity.BaseActivity;
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

        QRGEncoder qrgEncoder = new QRGEncoder(url, null, QRGContents.Type.TEXT, MeasureUtils.dpToPx(180f, getResources()));
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
