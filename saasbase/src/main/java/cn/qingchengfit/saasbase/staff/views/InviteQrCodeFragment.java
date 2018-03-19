package cn.qingchengfit.saasbase.staff.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentQrcodeInviteBinding;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.BitmapUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.zxing.WriterException;
import com.jakewharton.rxbinding.view.RxView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

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
 * Created by Paper on 2018/1/1.
 */
@Leaf(module = "staff", path = "/invite/qrcode/") public class InviteQrCodeFragment
  extends SaasBaseFragment {

  @Need String url;

  FragmentQrcodeInviteBinding db;
  private Bitmap bitmap;
  @Inject GymWrapper gymWrapper;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = FragmentQrcodeInviteBinding.inflate(inflater);
    db.setToolbarModel(new ToolbarModel("邀请二维码"));
    db.getToolbarModel().setMenu(R.menu.menu_compelete);
    db.getToolbarModel().setListener(item -> {popBack();return true;});
    initToolbar(db.layoutToolbar.toolbar);
    db.btnSave.setOnClickListener(view -> {
      if (bitmap != null && !bitmap.isRecycled()) {
        BitmapUtils.saveImageToGallery(getContext(), bitmap);
        ToastUtils.showDefaultStyle("已保存到相册");
      }
    });
    RxView.clicks(db.btnShare)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          if (bitmap != null && !bitmap.isRecycled()) {
            ShareDialogFragment.newInstance("邀请函", gymWrapper.name()+"邀请您加入", bitmap, url)
              .show(getFragmentManager(), "");
          }
        }
      });
    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    QRGEncoder qrgEncoder =
      new QRGEncoder(url, null, QRGContents.Type.TEXT, MeasureUtils.dpToPx(180f, getResources()));
    try {
      // Getting QR-Code as Bitmap
      bitmap = qrgEncoder.encodeAsBitmap();
      // Setting Bitmap to ImageView
      db.img.setAdjustViewBounds(true);
      db.img.setPadding(0, 0, 0, 0);
      db.img.setImageBitmap(bitmap);
    } catch (WriterException e) {
      Timber.e(e, " qr gen");
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (bitmap != null) bitmap.recycle();
  }

  @Override public String getFragmentName() {
    return InviteQrCodeFragment.class.getName();
  }
}
