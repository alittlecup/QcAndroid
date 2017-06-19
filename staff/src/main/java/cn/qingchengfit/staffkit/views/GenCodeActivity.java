package cn.qingchengfit.staffkit.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.StringUtils;
import com.google.zxing.WriterException;
import timber.log.Timber;

public class GenCodeActivity extends AppCompatActivity {

    public static final String GEN_CODE_URL = "gen.code.url";
    public static final String GEN_CODE_TITLE = "gen.code.title";
    public static final String GEN_CODE_BG = "gen.code.bg";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.qr_img) ImageView qrImg;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.bg) FrameLayout bg;
    private Bitmap bitmap;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_code);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });
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
