package cn.qingchengfit.views.fragments;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import cn.qingchengfit.utils.MD5;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.R;
import com.bumptech.glide.Glide;
import uk.co.senab.photoview.PhotoView;

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
 * Created by Paper on 2017/3/16.
 *
 *
 */
@Deprecated
public class SingleImageShowFragment extends BottomSheetDialogFragment {
	PhotoView photoView;
	Button btnDownload;
	ImageView close;

  private BottomSheetBehavior<ViewGroup> mBehavior;
  private String s;

  public static SingleImageShowFragment newInstance(String url) {
    Bundle args = new Bundle();
    args.putString("r", url);
    SingleImageShowFragment fragment = new SingleImageShowFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_single_iamge_show, container, false);
    photoView = (PhotoView) v.findViewById(R.id.photo_view);
    btnDownload = (Button) v.findViewById(R.id.btn_download);
    close = (ImageView) v.findViewById(R.id.close);
    s = getArguments().getString("r");
    if (s != null && s.contains("!")) s = s.split("!")[0];

    Glide.with(getContext()).load(s).placeholder(R.drawable.img_loadingimage).into(photoView);
    btnDownload.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        downloadFile(s, "image/jpg");
      }
    });
    close.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismissAllowingStateLoss();
      }
    });
    v.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    return v;
  }

  @Override public void setupDialog(Dialog dialog, int style) {
    //super.setupDialog(dialog, style);
    //dialog.setOnShowListener(new DialogInterface.OnShowListener() {
    //  @Override public void onShow(DialogInterface dialog) {
    //    BottomSheetDialog d = (BottomSheetDialog) dialog;
    //    FrameLayout bottomSheet =
    //        (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
    //    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
    //  }
    //});
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

  }

  /**
   * 暂时只处理了图片
   */
  public void downloadFile(String url, String mime) {
    try {
      DownloadManager downloadManager =
          (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
      request.setMimeType(mime);
      request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_PICTURES,
          MD5.genTimeStamp() + "." + MimeTypeMap.getFileExtensionFromUrl(url));
      downloadManager.enqueue(request);
      ToastUtils.show("文件已下载");
    } catch (Exception e) {

    }
  }
}
