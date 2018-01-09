package cn.qingchengfit.weex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

/**
 * Created by huangbaole on 2018/1/8.
 */

public class WeexPage extends BaseFragment implements IWXRenderListener {

  private FrameLayout rootView;
  private String mBundleUrl;
  private WXSDKInstance mWXSDKInstance;

  public WeexPage() {
  }

  public static WeexPage newInstance(String url) {
    Bundle args = new Bundle();
    WeexPage fragment = new WeexPage();
    args.putString(WXSDKInstance.BUNDLE_URL, url);
    fragment.setArguments(args);
    return fragment;
  }

  View view;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = View.inflate(getActivity(), R.layout.weex_container, null);
    rootView = (FrameLayout) view.findViewById(R.id.container);
    mBundleUrl = getArguments() != null ? getArguments().getString(WXSDKInstance.BUNDLE_URL) : null;
    createWXSDKInstance();
    render("weex", mBundleUrl);
  }

  private void createWXSDKInstance() {
    destoryWXSDKInstance();
    mWXSDKInstance = new WXSDKInstance(getActivity());
    mWXSDKInstance.registerRenderListener(this);
  }

  private void render(String pageName, String path) {
    mWXSDKInstance.render(pageName, WXFileUtils.loadAsset(path, getActivity()), null, null,
        WXRenderStrategy.APPEND_ASYNC);
  }

  private void destoryWXSDKInstance() {
    if (mWXSDKInstance != null) {
      mWXSDKInstance.registerRenderListener(null);
      mWXSDKInstance.destroy();
      mWXSDKInstance = null;
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    destoryWXSDKInstance();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (view.getParent() != null) {
      ((ViewGroup) view.getParent()).removeView(view);
    }
    return view;
  }

  @Override public void onViewCreated(WXSDKInstance mWXSDKInstance, View view) {
    rootView.removeAllViews();
    rootView.addView(view);
  }

  @Override public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
  }

  @Override public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override public void onException(WXSDKInstance instance, String errCode, String msg) {
    LogUtil.d("ERROR: " + errCode + " --> " + msg);
  }
}
