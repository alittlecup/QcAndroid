//package com.qingchengfit.fitcoach.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.gson.Gson;
//import cn.qingchengfit.model.common.Contact;
//import com.paper.paperbaselibrary.utils.AppUtils;
//import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
//import com.paper.paperbaselibrary.utils.PreferenceUtils;
//import com.qingchengfit.fitcoach.R;
//import cn.qingchengfit.model.common.PlatformInfo;
//
//import org.xwalk.core.JavascriptInterface;
//import org.xwalk.core.XWalkView;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 15/8/18 2015.
// */
//public class BaseXwalkFragment extends Fragment {
//
//    @BindView(R.id.webview)
//    XWalkView mWebview;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_xwalk, container, false);
//        unbinder=ButterKnife.bind(this, view);
//
//        return view;
//    }
//
//
//    public void startLoadUrl(String url) {
//        mWebview.load(url, null);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mWebview != null) {
//            mWebview.resumeTimers();
//            mWebview.onShow();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mWebview != null) {
//            mWebview.pauseTimers();
//            mWebview.onHide();
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//
//    public class JsInterface {
//
//        JsInterface() {
//        }
//
//        @JavascriptInterface
//        public String getToken() {
//            return PreferenceUtils.getPrefString(getActivity(), "token", "");
//        }
//
//
//        @JavascriptInterface
//        public void openDrawer() {
//
//
//        }
//
//        @JavascriptInterface
//        public String getContacts() {
//            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
//            Gson gson = new Gson();
//            return gson.toJson(contacts);
//        }
//
//        @JavascriptInterface
//        public String getPlatform() {
//            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(getActivity()));
//            Gson gson = new Gson();
//            return gson.toJson(info);
//        }
//
//        @JavascriptInterface
//        public String getSessionId() {
//            return PreferenceUtils.getPrefString(getActivity(), "session_id", "");
//        }
//
//        @JavascriptInterface
//        public void shareTimeline(String title, String link, String imgurl, String successCallback, String failedCallback) {
//
//        }
//
//
//    }
//
//}
