package com.qingchengfit.fitcoach.Utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.net.URL;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


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


    private IWXAPI api;

    private String mTitle, mText, mImg, mUrl;
    private Bitmap mBitmap;
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            mText = getArguments().getString("text");
            mImg = getArguments().getString("img");
            mUrl = getArguments().getString("url");
            mBitmap = getArguments().getParcelable("bitmap");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        api = WXAPIFactory.createWXAPI(getActivity(), getString(R.string.wechat_code), true);
        api.registerApp(getString(R.string.wechat_code));
        if (TextUtils.isEmpty(mUrl) && (!TextUtils.isEmpty(mImg) || mBitmap != null))
            isImg = true;
        else isImg = false;

        return view;
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.wechat_friend, R.id.wechat_circle, R.id.copy_link})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_friend:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        sendToWx(true);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe();
                break;
            case R.id.wechat_circle:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        sendToWx(false);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe();
                break;
            case R.id.copy_link:
                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clipData = new ClipData(mTitle+mUrl,"text",)
//                cmb.setPrimaryClip();
                cmb.setText(mTitle + mUrl);
                ToastUtils.showDefaultStyle("已复制");
//                ToastUtils.showS(getString(R.string.copy_download_link));
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
            if (!TextUtils.isEmpty(mImg)) {
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(mImg).openStream());
                Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                bitmap.recycle();
                msg.thumbData = Util.bmpToByteArray(thumb, true);
            }else if (mBitmap != null){
                msg.thumbData = Util.bmpToByteArray(mBitmap, true);
            }

//            WXImageObject imgObject = new WXImageObject(bitmap);
//            WXMediaMessage mediaMessage = new WXMediaMessage();
//            mediaMessage.mediaObject = imgObject;
//            Bitmap tumbl = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//            bitmap.recycle();
//            mediaMessage.thumbData = Util.bmpToByteArray(tumbl, true);


            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = (!isFriend) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            e.printStackTrace();
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.show("分享失败");
//                }
//            });

        }
    }

}
