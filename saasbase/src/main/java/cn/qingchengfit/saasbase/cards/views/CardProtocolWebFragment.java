package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.views.fragments.WebFragment;
import com.tencent.smtt.sdk.WebView;
import java.io.Serializable;

/**
 * Created by fb on 2017/11/17.
 */

public class CardProtocolWebFragment extends WebFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.tv_read_protocol) TextView tvReadProtocol;
  @BindView(R2.id.refresh_network) Button refreshNetwork;
  @BindView(R2.id.no_newwork) LinearLayout noNewwork;
  @BindView(R2.id.webview_root) LinearLayout webviewRoot;
  private String content;
  private String url;
  @MenuRes
  private int menuId;
  private OnMenuClickListener onMenuClickListener;

  public static CardProtocolWebFragment newInstance(String url, String content) {
    Bundle args = new Bundle();
    args.putString("url", url);
    args.putString("content", content);
    CardProtocolWebFragment fragment = new CardProtocolWebFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static CardProtocolWebFragment newInstance(String url, @MenuRes int menuId) {
    Bundle args = new Bundle();
    args.putString("url", url);
    args.putInt("menu", menuId);
    CardProtocolWebFragment fragment = new CardProtocolWebFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
    this.onMenuClickListener = onMenuClickListener;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      url = getArguments().getString("url");
      content = getArguments().getString("content");
      if (getArguments().containsKey("menu")) {
        menuId = getArguments().getInt("menu");
      }
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_protocol_web, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar();
    tvReadProtocol = (TextView) view.findViewById(R.id.tv_read_protocol);
    mWebviewWebView = (WebView) view.findViewById(R.id.webview);
    if (!TextUtils.isEmpty(content)) {
      tvReadProtocol.setVisibility(View.VISIBLE);
      tvReadProtocol.setText(content);
    } else {
      tvReadProtocol.setVisibility(View.GONE);
    }
    initWebSetting();
    //initWebClient();
    mWebviewWebView.loadUrl(url);
    return view;
  }

  private void setToolbar(){
    toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    toolbarTitle.setText("会员卡服务条款");
    if (menuId != 0) {
      toolbar.inflateMenu(menuId);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (onMenuClickListener != null) {
            onMenuClickListener.onMenuClick();
          }
          return false;
        }
      });
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public interface OnMenuClickListener extends Serializable{
    void onMenuClick();
  }

}
