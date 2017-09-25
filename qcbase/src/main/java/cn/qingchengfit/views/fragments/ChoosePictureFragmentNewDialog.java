package cn.qingchengfit.views.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.R;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import java.io.File;
import java.util.UUID;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/12/7 2015.
 */
public class ChoosePictureFragmentNewDialog extends DialogFragment {

  public static final int CHOOSE_CAMERA = 701;
  public static final int CHOOSE_GALLERY = 702;
  public static final int CLIP = 703;
  File fileClip;
  File fileCamera;
  private ChoosePicResult mResult;
  private Subscription spUpload;

  public static ChoosePictureFragmentNewDialog newInstance() {
    Bundle args = new Bundle();
    ChoosePictureFragmentNewDialog fragment = new ChoosePictureFragmentNewDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static ChoosePictureFragmentNewDialog newInstance(boolean clip) {
    Bundle args = new Bundle();
    args.putBoolean("c", clip);
    ChoosePictureFragmentNewDialog fragment = new ChoosePictureFragmentNewDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static ChoosePictureFragmentNewDialog newInstance(boolean clip, int request) {
    Bundle args = new Bundle();
    args.putBoolean("c", clip);
    args.putInt("r", request);
    ChoosePictureFragmentNewDialog fragment = new ChoosePictureFragmentNewDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public ChoosePicResult getResult() {
    return mResult;
  }

  public void setResult(ChoosePicResult mResult) {
    this.mResult = mResult;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setStyle(STYLE_NO_TITLE, R.style.ChoosePicDialogStyle);
    fileClip = FileUtils.getTmpImageFileName(getActivity(), "clip" + UUID.randomUUID());
    fileCamera = FileUtils.getTmpImageFileName(getActivity(), "camara" + UUID.randomUUID());
  }

  @Nullable @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Window window = this.getDialog().getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(lp);
    window.setWindowAnimations(R.style.ButtomDialogStyle);

    View view = inflater.inflate(R.layout.dialog_pic_choose, container, false);
    if (savedInstanceState != null) {
      mResult = (ChoosePicResult) savedInstanceState.getSerializable("callback");
    }
    new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
        .subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean aBoolean) {
            if (aBoolean) {
              Matisse.from(ChoosePictureFragmentNewDialog.this)
                  .choose(MimeType.ofAll(), false)
                  .countable(true)
                  .capture(true)
                  .theme(R.style.QcAppTheme)
                  .maxSelectable(1)
                  .captureStrategy(
                      new CaptureStrategy(true, getContext().getPackageName() + ".provider"))
                  //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                  .gridExpectedSize(
                      getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                  .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                  .thumbnailScale(0.85f)
                  .imageEngine(new GlideEngine())
                  .forResult(CHOOSE_CAMERA);
              //Uri uri = Uri.fromFile(fileCamera);
              //startActivityForResult(new CameraActivity.IntentBuilder(getContext()).confirmationQuality(0.7f).to(uri).build(),
              //    CHOOSE_CAMERA);
            } else {
              ToastUtils.show("请开启拍照权限");
            }
          }
        });
    return view;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String filepath = "";
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == Activity.RESULT_OK) {
        filepath = FileUtils.getPath(getContext(), result.getUri());
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
        ToastUtils.show(error.getMessage());
        return;
      }
    }

    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == CHOOSE_CAMERA || requestCode == CHOOSE_GALLERY) {
        //if (requestCode == CHOOSE_GALLERY) {
        //    filepath = FileUtils.getPath(getActivity(), data.getData());
        //} else {
        //    filepath = fileCamera.getAbsolutePath();
        //}
        filepath = Matisse.obtainPathResult(data).get(0);
        if (getArguments() != null && getArguments().getBoolean("c") && !TextUtils.isEmpty(
            filepath)) {
          Uri uri;
          if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getContext(),
                getContext().getApplicationContext().getPackageName() + ".provider",
                new File(filepath));
            getContext().grantUriPermission(getContext().getPackageName(), uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
          } else {
            uri = Uri.fromFile(new File(filepath));
          }
          clipPhoto(uri);
          return;
        }
      } else if (requestCode == CLIP) {
        filepath = fileClip.getAbsolutePath();
      }
    } else {
      return;
    }
    if (!TextUtils.isEmpty(filepath)) {
      if (mResult != null) mResult.onChoosefile(filepath);
      if (getActivity() instanceof BaseActivity) ((BaseActivity) getActivity()).showLoading();

      final String fp = filepath;
      spUpload = UpYunClient.rxUpLoad("/android/", fp)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<String>() {
            @Override public void call(String s) {
              if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).hideLoading();
              }
              if (mResult != null) {
                mResult.onUploadComplete(fp, s);
              }
              dismissAllowingStateLoss();
            }
          }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
              if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).hideLoading();
              }
              ToastUtils.show(throwable.getMessage());
            }
          });
    }
  }

  @Override public void onDestroyView() {
    if (spUpload != null && spUpload.isUnsubscribed()) spUpload.unsubscribe();
    super.onDestroyView();
  }

  /* 裁剪图片方法实现
      * @param uri
      */
  public void clipPhoto(Uri uri) {
    CropImage.activity(uri).setAspectRatio(1, 1).start(getContext(), this);
  }

  public interface ChoosePicResult {
    void onChoosefile(String filePath);

    void onUploadComplete(String filePaht, String url);
  }
}
