package cn.qingchengfit.recruit.views;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.net.URL;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/5.
 */

public class RecruitPublishShareDialog extends DialogFragment {

  private IWXAPI api;

  private String mTitle, mText, mImg, mUrl;
  private Bitmap mBitmap;
  private boolean isImg;

  private String wechat_code;

  public static RecruitPublishShareDialog newInstance(String title, String text, String img,
      String url) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putString("img", img);
    args.putString("url", url);
    RecruitPublishShareDialog fragment = new RecruitPublishShareDialog();
    fragment.setArguments(args);
    return fragment;
  }

  public static RecruitPublishShareDialog newInstance(String title, String text, Bitmap img,
      String url) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putParcelable("bitmap", img);
    args.putString("url", url);

    RecruitPublishShareDialog fragment = new RecruitPublishShareDialog();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mTitle = getArguments().getString("title");
      mText = getArguments().getString("text");
      mImg = getArguments().getString("img");
      mUrl = getArguments().getString("url");
      if (mUrl != null && mUrl.contains("?")) {
        mUrl = mUrl + "&app=" + (AppUtils.getCurApp(getContext()) == 0 ? "coach" : "staff");
      } else {
        mUrl = mUrl + "?app=" + (AppUtils.getCurApp(getContext()) == 0 ? "coach" : "staff");
      }
      wechat_code = AppUtils.getManifestData(getActivity(), "WX_ID");

      mBitmap = getArguments().getParcelable("bitmap");
    }
  }

  @Override public void onResume() {
    super.onResume();
    Dialog dialog = getDialog();
    if (dialog != null) {
      DisplayMetrics dm = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
      dialog.getWindow()
          .setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_publish_successed, container, false);
    view.findViewById(R.id.wechat_friend).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickFriend();
      }
    });
    view.findViewById(R.id.wechat_circle).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickCircle();
      }
    });
    view.findViewById(R.id.copy_link).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickCopy();
      }
    });
    view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        close();
      }
    });

    api = WXAPIFactory.createWXAPI(getActivity(), wechat_code, true);
    api.registerApp(wechat_code);
    if (TextUtils.isEmpty(mUrl) && (!TextUtils.isEmpty(mImg) || mBitmap != null)) {
      isImg = true;
    } else {
      isImg = false;
    }

    return view;
  }

  private String buildTransaction(final String type) {
    return (type == null) ? String.valueOf(System.currentTimeMillis())
        : type + System.currentTimeMillis();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public int getLayoutRes() {
    return cn.qingchengfit.widgets.R.layout.fragment_share;
  }

  public void clickFriend() {
    Observable.create(new Observable.OnSubscribe<Boolean>() {
      @Override public void call(Subscriber<? super Boolean> subscriber) {
        sendToWx(true);
        subscriber.onCompleted();
      }
    }).onBackpressureBuffer().subscribeOn(Schedulers.io()).subscribe();
    dismiss();
  }

  public void clickCircle() {
    Observable.create(new Observable.OnSubscribe<Boolean>() {
      @Override public void call(Subscriber<? super Boolean> subscriber) {
        sendToWx(false);
        subscriber.onCompleted();
      }
    }).onBackpressureBuffer().subscribeOn(Schedulers.io()).subscribe();
    dismiss();
  }

  public void clickCopy() {
    ClipboardManager cmb =
        (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    cmb.setPrimaryClip(ClipData.newPlainText("qingcheng", mUrl));
    ToastUtils.showDefaultStyle("已复制");
    dismiss();
  }

  public void close() {
    dismiss();
  }

  public void sendToWx(boolean isFriend) {
    try {
      WXWebpageObject webpage = new WXWebpageObject();
      webpage.webpageUrl = mUrl;
      WXMediaMessage msg = new WXMediaMessage(webpage);
      msg.title = mTitle;
      msg.description = mText;
      if (!TextUtils.isEmpty(mImg)) {
        Bitmap bitmap = BitmapFactory.decodeStream(new URL(mImg).openStream());
        Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumb, true);
      } else if (mBitmap != null) {
        msg.thumbData = Util.bmpToByteArray(mBitmap, true);
      }
      SendMessageToWX.Req req = new SendMessageToWX.Req();
      req.transaction = buildTransaction("webpage");
      req.message = msg;
      req.scene =
          (!isFriend) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
      api.sendReq(req);

    } catch (Exception e) {
      LogUtil.e(e.getMessage());
      e.printStackTrace();
    }
  }


}
