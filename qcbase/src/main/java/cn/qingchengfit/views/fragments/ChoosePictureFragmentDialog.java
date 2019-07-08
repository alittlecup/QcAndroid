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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import rx.functions.Action1;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

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
public class ChoosePictureFragmentDialog extends DialogFragment {

  public static final int CHOOSE_CAMERA = 701;
  public static final int CHOOSE_GALLERY = 702;
  public static final int CLIP = 703;
  File fileClip;
  File fileCamera;
  private ChoosePicResult mResult;

  public static ChoosePictureFragmentDialog newInstance() {
    Bundle args = new Bundle();
    ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static ChoosePictureFragmentDialog newInstance(int chooseCount) {
    Bundle args = new Bundle();
    ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
    args.putInt("chooseCount", chooseCount);
    fragment.setArguments(args);
    return fragment;
  }

  public static ChoosePictureFragmentDialog newInstance(boolean clip) {
    Bundle args = new Bundle();
    args.putBoolean("c", clip);
    ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static ChoosePictureFragmentDialog newInstance(boolean clip, int request) {
    Bundle args = new Bundle();
    args.putBoolean("c", clip);
    args.putInt("r", request);
    ChoosePictureFragmentDialog fragment = new ChoosePictureFragmentDialog();
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

  int count = 1;

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
    if (getArguments() != null) {
      count = getArguments().getInt("chooseCount");
    }
    new RxPermissions(getActivity()).request(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          Matisse.from(ChoosePictureFragmentDialog.this)
              .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.WEBP, MimeType.BMP), false)
              .theme(R.style.QcPicAppTheme)
              .countable(true)
              .capture(true)
              .maxSelectable(count)
              .captureStrategy(
                  new CaptureStrategy(true, getContext().getPackageName() + ".provider"))
              .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
              .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
              .thumbnailScale(0.85f)
              .imageEngine(new GlideEngine())
              .forResult(CHOOSE_CAMERA);
        } else {
          ToastUtils.show("请开启拍照及存储权限");
        }
      }
    });
    return view;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == Activity.RESULT_OK) {
        Uri resultUri = result.getUri();
        RxBus.getBus()
            .post(new EventChooseImage(FileUtils.getFilePath(getContext(), resultUri),
                getArguments().getInt("r", 0)));
        dismissAllowingStateLoss();
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
        ToastUtils.show(error.getMessage());
      }
    }

    String filepath = "";
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == CHOOSE_CAMERA || requestCode == CHOOSE_GALLERY) {
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
        } else {
          RxBus.getBus()
              .post(new EventChooseImage(filepath,
                  getArguments() == null ? 0 : getArguments().getInt("r", 0)));
          dismissAllowingStateLoss();
        }
      } else if (requestCode == CLIP) {
        filepath = fileClip.getAbsolutePath();
        RxBus.getBus().post(new EventChooseImage(filepath, getArguments().getInt("r", 0)));
        dismissAllowingStateLoss();
      }
      if (mResult != null) {
        if (mResult instanceof MultiChooseResult) {
          ((MultiChooseResult) mResult).onMultiChoosePicResult(true,
              Matisse.obtainPathResult(data));
        } else {
          mResult.onChoosePicResult(true, filepath);
        }
      }
    } else {
      dismissAllowingStateLoss();
    }
  }

  /* 裁剪图片方法实现
   * @param uri
   */
  public void clipPhoto(Uri uri) {
    CropImage.activity(uri).setAspectRatio(1, 1).start(getContext(), this);
  }

  public abstract static class MultiChooseResult implements ChoosePicResult {
    @Override public void onChoosePicResult(boolean isSuccess, String filePath) {
      List<String> path = new ArrayList<>();
      path.add(filePath);
      onMultiChoosePicResult(isSuccess, path);
    }

    abstract void onMultiChoosePicResult(boolean isSuccess, List<String> paths);
  }

  public interface ChoosePicResult {
    void onChoosePicResult(boolean isSuccess, String filePath);
  }
}
