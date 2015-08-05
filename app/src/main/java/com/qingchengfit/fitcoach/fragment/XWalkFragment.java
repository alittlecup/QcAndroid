package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.bean.Contact;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.bean.PlatformInfo;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkCookieManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class XWalkFragment extends Fragment {

    @Bind(R.id.webview)
    XWalkView mWebview;
    @Bind(R.id.web_floatbtn)
    FloatingActionsMenu webFloatbtn;
    FloatingActionButton btn1;
    FloatingActionButton btn2;
    private XWalkCookieManager mCookieManager;

    public XWalkFragment() {
    }

    public void setCookie(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xwalk, container, false);
        ButterKnife.bind(this, view);
//        mWebview.load("http://feature.qingchengfit.cn/welcome/", null);
        mWebview.load("http://192.168.31.154:8888/welcome/", null);
        mWebview.addJavascriptInterface(new JsInterface(), "NativeMethod");
        mCookieManager = new XWalkCookieManager();
        mCookieManager.setAcceptCookie(true);

        btn1 = new FloatingActionButton(getActivity());
        btn1.setIcon(R.drawable.ic_menu_share_holo_light);
        btn1.setTitle("你好");
        btn2 = new FloatingActionButton(getActivity());
        btn2.setIcon(R.drawable.ic_menu_share_holo_light);
        btn2.setTitle("你好");
        webFloatbtn.addButton(btn1);
        webFloatbtn.addButton(btn2);
        btn1.setOnClickListener(view1 -> PhoneFuncUtils.queryCalender(getActivity()));
        btn2.setOnClickListener(view1 -> {
//            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
//            Gson gson = new Gson();
//            LogUtil.e(gson.toJson(contacts));
        });
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mWebview != null) {
            mWebview.resumeTimers();
            mWebview.onShow();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebview != null) {
            mWebview.pauseTimers();
            mWebview.onHide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public class JsInterface {

        JsInterface() {
        }

        @JavascriptInterface
        public String getToken() {
            return "123123123";
        }


        @JavascriptInterface
        public void openDrawer() {
            openmainDrawer();

        }

        @JavascriptInterface
        public String getContacts() {
            List<Contact> contacts = PhoneFuncUtils.initContactList(getActivity());
            Gson gson = new Gson();
            return gson.toJson(contacts);
        }

        @JavascriptInterface
        public String getPlatform(){
            PlatformInfo info = new PlatformInfo("android", AppUtils.getAppVer(getActivity()));
            Gson gson = new Gson();
            return gson.toJson(info);
        }

        @JavascriptInterface
        public String getSessionId(){
            return PreferenceUtils.getPrefString(getActivity(),"session_id","");
        }


    }

    public void openmainDrawer(){
        getActivity().runOnUiThread(() -> RxBus.getBus().send(new OpenDrawer()));
    }

    public void setCookie(String url, String key, String value) {

    }

}
