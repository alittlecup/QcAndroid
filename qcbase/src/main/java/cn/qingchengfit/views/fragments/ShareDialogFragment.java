package cn.qingchengfit.views.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Util;
import cn.qingchengfit.widgets.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.net.URL;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/5/12 2016.
 */
public class ShareDialogFragment extends BottomSheetDialogFragment {

  public static final String SHARE_TYPE_ACTION = "action";
  public static final String SHARE_TYPE_INFO = "info";
  public LinearLayout layoutExtends;

  private IWXAPI api;

  private String mTitle, mText, mImg, mUrl;
  private Bitmap mBitmap;
  private boolean isImg;

  private String wechat_code;

  public static ShareDialogFragment newInstance(String title, String text, String img, String url) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putString("img", img);
    args.putString("url", url);
    ShareDialogFragment fragment = new ShareDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static ShareDialogFragment newInstance(String title, String text, Bitmap img, String url) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("text", text);
    args.putParcelable("bitmap", img);
    args.putString("url", url);

    ShareDialogFragment fragment = new ShareDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
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
      try {
        wechat_code = AppUtils.getManifestData(getActivity(), "WX_ID");
      } catch (PackageManager.NameNotFoundException e) {
        //e.printStackTrace();
      }
      mBitmap = getArguments().getParcelable("bitmap");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutRes(), container, false);
    api = WXAPIFactory.createWXAPI(getActivity(), wechat_code, true);
    api.registerApp(wechat_code);
    layoutExtends = (LinearLayout) view.findViewById(R.id.layout_extends);
    if (layoutExtends != null)
      layoutExtends.setVisibility(GONE);
    if (TextUtils.isEmpty(mUrl) && (!TextUtils.isEmpty(mImg) || mBitmap != null)) {
      isImg = true;
    } else {
      isImg = false;
    }
    view.findViewById(R.id.wechat_friend).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        clickFriend();
      }
    });
    view.findViewById(R.id.wechat_circle).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        clickCircle();
      }
    });
    view.findViewById(R.id.copy_link).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        clickCopy();
      }
    });
    view.findViewById(R.id.bottom_cancel).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
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
    return R.layout.fragment_share;
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
    sensorTrack("qc_copyurl");
    sensorTrack("qc_copyurl", "1");
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

      sensorTrack(isFriend ? "qc_sharetofriends" : "qc_moments");
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
      e.printStackTrace();
    }
  }

  public void sensorTrack(String channel) {
    sensorTrack(channel, "0");
  }

  public void sensorTrack(String channel, String success) {
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("qc_page_url", mUrl);
      jsonObject.put("qc_share_title", mTitle);
      jsonObject.put("qc_share_channel", channel);
      jsonObject.put("qc_sharesuccess", success);
      SensorsUtils.track("page_share", jsonObject.toString(), getContext());
      PreferenceUtils.setPrefString(getContext(), "share_tmp", jsonObject.toString());
    } catch (Exception e) {

    }
  }
}
