package cn.qingchengfit.weex.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexLoadView;
import com.taobao.weex.WXSDKInstance;

/**
 * Created by huangbaole on 2018/1/8.
 */

public class WeexPage extends BaseFragment {


  public WeexPage() {
  }

  public static WeexPage newInstance(String url) {
    Bundle args = new Bundle();
    WeexPage fragment = new WeexPage();
    args.putString(WXSDKInstance.BUNDLE_URL, url);
    fragment.setArguments(args);
    return fragment;
  }

  View rootView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.weex_container, null);
    FrameLayout weexContainer = rootView.findViewById(R.id.container);

    String mBundleUrl =
        getArguments() != null ? getArguments().getString(WXSDKInstance.BUNDLE_URL) : null;
    if (mBundleUrl == null) {
      throw new RuntimeException("the url cant be null");
    }
    Uri mUri = Uri.parse(mBundleUrl);

    new WeexLoadView().loadUri(mUri, getActivity(), weexContainer);

    if (rootView.getParent() != null) {
      ((ViewGroup) rootView.getParent()).removeView(rootView);
    }

    return rootView;
  }
}
