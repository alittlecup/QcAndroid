package cn.qingchengfit.shop.ui.product.choosepic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.shop.ui.widget.LoadingImageDialog;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/24.
 */

public class MultiChoosePicFragment extends ChoosePictureFragmentNewDialog {

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    return view;
  }

  public static MultiChoosePicFragment newInstance(@Nullable ArrayList<Uri> uris) {
    Bundle args = new Bundle();
    MultiChoosePicFragment fragment = new MultiChoosePicFragment();
    if (uris != null && !uris.isEmpty()) {
      args.putParcelableArrayList("uris", uris);
    }
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.setVisibility(View.GONE);
  }

  @Override public void chooseImages() {
    ArrayList<Uri> uris = getArguments().getParcelableArrayList("uris");
    if (uris != null && !uris.isEmpty()) {
      ArrayList<Item> items = new ArrayList<>();
      for (Uri uri : uris) {
        Item item = new Item(uri);
        item.setPath(uri.toString());
        items.add(item);
      }
      chooseImages(items);
    } else {
      chooseImage();
    }
  }

  private void chooseImage() {
    Matisse.from(MultiChoosePicFragment.this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .capture(true)
        .dragable(true)
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

  public void chooseImages(ArrayList<Item> uris) {
    Matisse.from(MultiChoosePicFragment.this)
        .choose(MimeType.ofAll(), false)
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
          Flowable.fromIterable(filePaths)
              .flatMap(s -> {
                if (s.startsWith("http")) {
                  return Flowable.just(s);
                }
                return RxJavaInterop.toV2Flowable(UpYunClient.rxUpLoad("/android/", s));
              })
              .subscribeOn(io.reactivex.schedulers.Schedulers.io())
              .doAfterTerminate(() -> {
                hideDialog();
                dismiss();
              })
              .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
              .subscribe(s -> {
                httpUriPath.add(s);
                if (loadingDialog != null) {
                  loadingDialog.setTextView(
                      "正在上传(" + httpUriPath.size() + "/" + filePaths.size() + ")");
                }
              }, throwable -> ToastUtils.show("图片上传失败，请重新上传"), () -> {
                if (callback != null) {
                  callback.onSuccess(httpUriPath);
                }
                ToastUtils.show("图片全部上传成功");
              });
        }
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