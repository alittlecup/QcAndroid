package cn.qingchengfit.staffkit.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.net.URL;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
 * Created by Paper on 16/5/12 2016.
 */
public class ShareDialogFragment extends BaseBottomSheetDialogFragment {

    private IWXAPI api;

    private String mTitle, mText, mImg, mUrl;
    private boolean isImg;
    //    public static void start(String title,String text,String img,String url){
    //        ShareDialogFragment f = newInstance(title,text,img,url);
    //        f
    //    }

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

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            mText = getArguments().getString("text");
            mImg = getArguments().getString("img");
            mUrl = getArguments().getString("url");
            //            if (mImg.contains("!")){
            //                mImg = mImg.split("!")[0];
            //            }
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        api = WXAPIFactory.createWXAPI(getActivity(), getString(R.string.wechat_code), true);
        api.registerApp(getString(R.string.wechat_code));
        if (TextUtils.isEmpty(mUrl) && !TextUtils.isEmpty(mImg)) {
            isImg = true;
        } else {
            isImg = false;
        }

        return view;
    }

    @LayoutRes public int getLayoutRes() {
        return R.layout.fragment_share;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.wechat_friend, R.id.wechat_circle, R.id.copy_link }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_friend:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override public void call(Subscriber<? super Boolean> subscriber) {
                        sendToWx(true);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
                break;
            case R.id.wechat_circle:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override public void call(Subscriber<? super Boolean> subscriber) {
                        sendToWx(false);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).subscribe();
                break;
            case R.id.copy_link:
                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText("qingcheng", mUrl));

                ToastUtils.showS("链接已复制");
                sensorTrack("qc_copyurl");
                sensorTrack("qc_copyurl", "1");
                break;
        }
        dismiss();
    }

    public void sendToWx(boolean isFriend) {
        try {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = mUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = mTitle;
            msg.description = mText;
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(mImg).openStream());
            Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

            msg.thumbData = Util.bmpToByteArray(thumb, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = (!isFriend) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
            bitmap.recycle();

            sensorTrack(isFriend ? "qc_sharetofriends" : "qc_moments");
        } catch (Exception e) {
            Timber.e("share error:" + e.getMessage());
            Observable.just("").observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override public void call(String s) {
                    ToastUtils.show("分享失败");
                }
            });
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
            SensorsUtils.track("page_share", jsonObject.toString());
            PreferenceUtils.setPrefString(getContext(), "share_tmp", jsonObject.toString());
        } catch (Exception e) {

        }
    }
}
