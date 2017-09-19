package cn.qingchengfit.staffkit.views.gym.gym_web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.lang.ref.SoftReference;
import javax.inject.Inject;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

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
 * Created by Paper on 2017/1/21.
 */
public class HomePageQrCodeFragment extends BaseFragment {

    public String mUrl;
    @BindView(R.id.img_qr_code) ImageView imgQrCode;
    @BindView(R.id.tv_gym_name) TextView tvGymName;
    @BindView(R.id.btn_save_qrcode) TextView btnSaveQrcode;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private SoftReference<Bitmap> mBitmapSoftReference;

    @Inject public HomePageQrCodeFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_qrcode, container, false);
        ButterKnife.bind(this, view);
        tvGymName.setText(gymWrapper.name());
        try {
            mBitmapSoftReference = new SoftReference<Bitmap>(encodeAsBitmap(mUrl));
            imgQrCode.setImageBitmap(mBitmapSoftReference.get());
        } catch (WriterException e) {
        }

        return view;
    }

    @Override public String getFragmentName() {
        return HomePageQrCodeFragment.class.getName();
    }

    @OnClick({ R.id.btn_close, R.id.btn_save_qrcode, R.id.root_view }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                getActivity().onBackPressed();
                break;
            case R.id.btn_save_qrcode:
                MediaStore.Images.Media.insertImage(getContext().getContentResolver(), mBitmapSoftReference.get(), gymWrapper.name(),
                    "微信二维码");
                //                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

                break;
            case R.id.root_view:
                getActivity().onBackPressed();
                break;
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, MeasureUtils.dpToPx(170f, getResources()),
                MeasureUtils.dpToPx(170f, getResources()), null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
