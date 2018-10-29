package cn.qingchengfit.wxpreview.old.newa;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.wxpreview.R;

public class GymShortShareDialog extends BottomSheetDialogFragment {

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.wx_short_share, container, false);
    view.findViewById(R.id.wechat_friend).setOnClickListener(v -> {
      if (listener != null) {
        listener.onWxShareClick(GymShortShareDialog.this, true);
      }
    });
    view.findViewById(cn.qingchengfit.widgets.R.id.wechat_circle).setOnClickListener(view1 -> {
      if (listener != null) {
        listener.onWxShareClick(GymShortShareDialog.this, false);
      }
    });
    return view;
  }

  public void setListener(onWxShareClickListener listener) {
    this.listener = listener;
  }

  private onWxShareClickListener listener;

  public interface onWxShareClickListener {
    void onWxShareClick(DialogFragment dialog, boolean isFriend);
  }
}
