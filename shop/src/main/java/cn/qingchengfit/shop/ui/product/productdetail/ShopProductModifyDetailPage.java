package cn.qingchengfit.shop.ui.product.productdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.PageModifyDetailBinding;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by huangbaole on 2018/3/16.
 */
@Leaf(module = "shop",path = "/modify/detail")
public class ShopProductModifyDetailPage extends SaasBaseFragment {
  PageModifyDetailBinding mBinding;
  @Need String content;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = DataBindingUtil.inflate(inflater, R.layout.page_modify_detail, container, false);
    initToolbar();
    initWebView();
    mBinding.webview.loadData(content, "text/html; charset=UTF-8", null);
    return mBinding.getRoot();
  }

  private void initWebView() {
    WebSettings webSetting = mBinding.webview.getSettings();
    webSetting.setJavaScriptEnabled(true);
    webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
    webSetting.setAllowFileAccess(true);
    webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    webSetting.setSupportZoom(false);
    webSetting.setBuiltInZoomControls(false);
    webSetting.setUseWideViewPort(true);
    webSetting.setSupportMultipleWindows(true);
    webSetting.setLoadWithOverviewMode(true);
    webSetting.setAppCacheEnabled(true);
    webSetting.setDatabaseEnabled(true);
    webSetting.setDomStorageEnabled(true);
    webSetting.setGeolocationEnabled(true);
    webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSetting.setAppCacheEnabled(false);
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("商品描述详情"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
