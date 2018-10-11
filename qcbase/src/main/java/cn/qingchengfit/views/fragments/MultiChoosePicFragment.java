package cn.qingchengfit.views.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.widgets.LoadingImageDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2018/1/24.
 */

public class MultiChoosePicFragment extends ChoosePictureFragmentNewDialog {

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Window window = this.getDialog().getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(lp);
    window.setWindowAnimations(cn.qingchengfit.widgets.R.style.ButtomDialogStyle);

    View view =
        inflater.inflate(cn.qingchengfit.widgets.R.layout.dialog_pic_choose, container, false);

    new RxPermissions(getActivity()).request(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          chooseImages();
        } else {
          ToastUtils.show("请开启拍照及存储权限");
        }
      }
    });
    return view;
  }

  public static MultiChoosePicFragment newInstance(@Nullable List<String> uris) {
    Bundle args = new Bundle();
    MultiChoosePicFragment fragment = new MultiChoosePicFragment();
    if (uris != null && !uris.isEmpty()) {
      args.putStringArrayList("uris", new ArrayList<>(uris));
    }
    fragment.setArguments(args);
    return fragment;
  }

  private boolean showFlag = false;

  public void setShowFlag(boolean flag) {
    showFlag = flag;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.setVisibility(View.GONE);
  }

  public void chooseImages() {
    ArrayList<String> uris = getArguments().getStringArrayList("uris");
    if (uris != null && !uris.isEmpty()) {
      ArrayList<Item> items = new ArrayList<>();
      for (String uri : uris) {
        Item item = new Item(Uri.parse(uri));
        item.setPath(uri);
        items.add(item);
      }
      if (showFlag) {
        showImages(items);
      } else {
        chooseImages(items);
      }
    } else {
      chooseImage();
    }
  }

  private void chooseImage() {
    Matisse.from(MultiChoosePicFragment.this)
        .choose(MimeType.ofImage(), false)
        .countable(true)
        .capture(true)
        .dragable(true)
        .itemUris(null)
        .theme(cn.qingchengfit.widgets.R.style.QcPicAppTheme)
        .maxSelectable(5)
        .captureStrategy(new CaptureStrategy(true, getContext().getPackageName() + ".provider"))
        .gridExpectedSize(getResources().getDimensionPixelSize(
            cn.qingchengfit.widgets.R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        .thumbnailScale(0.85f)
        .imageEngine(new GlideEngine())
        .forResult(CHOOSE_CAMERA);
  }

  private void chooseImages(ArrayList<Item> uris) {
    Matisse.from(MultiChoosePicFragment.this)
        .choose(MimeType.ofImage(), false)
        .countable(true)
        .capture(true)
        .dragable(true)
        .itemUris(uris)
        .theme(cn.qingchengfit.widgets.R.style.QcPicAppTheme)
        .maxSelectable(5)
        .captureStrategy(new CaptureStrategy(true, getContext().getPackageName() + ".provider"))
        .gridExpectedSize(getResources().getDimensionPixelSize(
            cn.qingchengfit.widgets.R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        .thumbnailScale(0.85f)
        .imageEngine(new GlideEngine())
        .forResult(CHOOSE_CAMERA);
  }

  private void showImages(ArrayList<Item> uris) {
    Matisse.from(MultiChoosePicFragment.this)
        .choose(MimeType.ofImage(), false)
        .countable(true)
        .capture(true)
        .itemUris(uris)
        .theme(cn.qingchengfit.widgets.R.style.QcPicAppTheme)
        .maxSelectable(5)
        .captureStrategy(new CaptureStrategy(true, getContext().getPackageName() + ".provider"))
        .gridExpectedSize(getResources().getDimensionPixelSize(
            cn.qingchengfit.widgets.R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        .thumbnailScale(0.85f)
        .imageEngine(new GlideEngine())
        .forResult(706);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
      if (requestCode == CHOOSE_CAMERA) {
        List<String> filePaths = Matisse.obtainPathResult(data);
        if (!filePaths.isEmpty()) {
          List<String> httpUriPath = new ArrayList<>();
          showDialog();
          Flowable.fromIterable(filePaths).flatMap(s -> {
            if (s.startsWith("http")) {
              return Flowable.just(s);
            }
            return RxJavaInterop.toV2Flowable(UpYunClient.rxUpLoad("/android/", s, false));
          }).subscribeOn(io.reactivex.schedulers.Schedulers.io()).doAfterTerminate(() -> {
            hideDialog();
            dismiss();
          }).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            httpUriPath.add(s);
            if (loadingDialog != null) {
              loadingDialog.setTextView(
                  "正在上传(" + (httpUriPath.size() + 1) + "/" + filePaths.size() + ")");
            }
          }, throwable -> ToastUtils.show("图片上传失败，请重新上传"), () -> {
            if (callback != null) {
              callback.onSuccess(httpUriPath);
            }
            ToastUtils.show("图片全部上传成功");
          });
        }
      } else {
        dismiss();
      }
    }
  }

  private onUploadImageCallback callback;

  public void setUpLoadImageCallback(onUploadImageCallback callback) {
    this.callback = callback;
  }

  public interface onUploadImageCallback {
    void onSuccess(List<String> uris);
  }

  private LoadingImageDialog loadingDialog;

  private void showDialog() {
    if (loadingDialog == null) loadingDialog = new LoadingImageDialog(getActivity());
    if (loadingDialog.isShowing()) loadingDialog.dismiss();
    if (!getActivity().isFinishing()) loadingDialog.show();
  }

  public void hideDialog() {
    if (loadingDialog != null) loadingDialog.dismiss();
  }
}