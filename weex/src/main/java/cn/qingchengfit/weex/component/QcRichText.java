/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.qingchengfit.weex.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)

public class QcRichText extends WXComponent {

  public static final String GO_BACK = "goBack";
  public static final String GO_FORWARD = "goForward";
  public static final String RELOAD = "reload";
  protected IWebView mWebView;

  public QcRichText(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
    super(instance, dom, parent);
  }
  public static class Ceator implements ComponentCreator {
    public WXComponent createInstance(WXSDKInstance instance, WXDomObject node, WXVContainer parent) throws IllegalAccessException,
        InvocationTargetException, InstantiationException {
      return new QcRichText(instance,node,parent);
    }
  }


  protected void createWebView() {
    mWebView = new QcWeexWebView(getContext());
  }

  @Override protected View initComponentHostView(@NonNull Context context) {
    createWebView();
    mWebView.setOnErrorListener(new IWebView.OnErrorListener() {
      @Override public void onError(String type, Object message) {
        fireEvent(type, message);
      }
    });
    mWebView.setOnPageListener(new IWebView.OnPageListener() {
      @Override public void onReceivedTitle(String title) {
        if (getDomObject().getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
          Map<String, Object> params = new HashMap<>();
          params.put("title", title);
          fireEvent(Constants.Event.RECEIVEDTITLE, params);
        }
      }

      @Override public void onPageStart(String url) {
        if (getDomObject().getEvents().contains(Constants.Event.PAGESTART)) {
          Map<String, Object> params = new HashMap<>();
          params.put("url", url);
          fireEvent(Constants.Event.PAGESTART, params);
        }
      }

      @Override public void onPageFinish(String url, boolean canGoBack, boolean canGoForward) {

        if (getDomObject().getEvents().contains(Constants.Event.PAGEFINISH)) {
          Map<String, Object> params = new HashMap<>();
          params.put("url", url);
          params.put("canGoBack", canGoBack);
          params.put("canGoForward", canGoForward);
          fireEvent(Constants.Event.PAGEFINISH, params);
        }
      }
    });
    return mWebView.getView();
  }

  @Override public void destroy() {
    super.destroy();
    getWebView().destroy();
  }

  @Override protected boolean setProperty(String key, Object param) {
    switch (key) {
      case Constants.Name.SHOW_LOADING:
        Boolean result = WXUtils.getBoolean(param, null);
        if (result != null) setShowLoading(result);
        return true;
      case Constants.Name.SRC:
        String src = WXUtils.getString(param, null);
        if (src != null) setUrl(src);
        return true;
    }
    return super.setProperty(key, param);
  }

  @WXComponentProp(name = Constants.Name.SHOW_LOADING)
  public void setShowLoading(boolean showLoading) {
    getWebView().setShowLoading(showLoading);
  }

  @WXComponentProp(name = Constants.Name.SRC) public void setUrl(String url) {
    if (TextUtils.isEmpty(url) || getHostView() == null) {
      return;
    }
    if (!TextUtils.isEmpty(url)) {
      loadUrl(getInstance().rewriteUri(Uri.parse(url), URIAdapter.WEB).toString());
    }
  }

  @WXComponentProp(name = "content") public void setWebViewContent(String content) {
    if (TextUtils.isEmpty(content) || getHostView() == null) {
      return;
    }
    if (!TextUtils.isEmpty(content)) {
      if (getWebView() instanceof QcWeexWebView) {
        ((QcWeexWebView) getWebView()).loadData(content, "text/html", "UTF-8");
      }
    }
  }

  public void setAction(String action) {
    if (!TextUtils.isEmpty(action)) {
      if (action.equals(GO_BACK)) {
        goBack();
      } else if (action.equals(GO_FORWARD)) {
        goForward();
      } else if (action.equals(RELOAD)) {
        reload();
      }
    }
  }

  private void fireEvent(String type, Object message) {
    if (getDomObject().getEvents().contains(Constants.Event.ERROR)) {
      Map<String, Object> params = new HashMap<>();
      params.put("type", type);
      params.put("errorMsg", message);
      fireEvent(Constants.Event.ERROR, params);
    }
  }

  private void loadUrl(String url) {
    getWebView().loadUrl(url);
  }

  private void reload() {
    getWebView().reload();
  }

  private void goForward() {
    getWebView().goForward();
  }

  private void goBack() {
    getWebView().goBack();
  }

  private IWebView getWebView() {
    return mWebView;
  }
}
