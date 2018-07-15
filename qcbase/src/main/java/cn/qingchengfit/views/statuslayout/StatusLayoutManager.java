package cn.qingchengfit.views.statuslayout;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.widgets.R;

/**
 * 状态布局管理器
 * https://bakumon.me
 *
 * @author Bakumon
 * @date 2017/12/18
 */

public class StatusLayoutManager {

  private View contentLayout;
  private String emptyLayoutTitle;
  private @StringRes int emptyLayoutTitleRes;
  private String emptyLayoutContent;
  private @StringRes int emptyLayoutContentRes;
  private @DrawableRes int emptyLayoutDrawableRes;
  private Drawable emptyLayoutDrawable;

  private String emptyPartLayoutContent;
  private @StringRes int emptyPartLayoutContentRes;

  private String networkErrorLayoutTitle;
  private @StringRes int networkErrorLayoutTitleRes;
  private String networkErrorLayoutContent;
  private @StringRes int networkErrorLayoutContentRes;

  private @DrawableRes int networkErrorLayoutDrawableRes;
  private Drawable networkErrorLayoutDrawable;

  private String networkBtnText;
  private @StringRes int networkBtnTextRes;

  private ReplaceLayoutHelper replaceLayoutHelper;

  private LayoutInflater inflater;
  private DefaultOnStatusChildClickListener onStatusChildClickListener;

  private View emptyLayout;
  private View emptyPartLayout;
  private View networkErrorLayout;

  private StatusLayoutManager(Builder builder) {
    this.contentLayout = builder.contentLayout;
    this.emptyLayoutTitle = builder.emptyLayoutTitle;
    this.emptyLayoutTitleRes = builder.emptyLayoutTitleRes;
    this.emptyLayoutContent = builder.emptyLayoutContent;
    this.emptyLayoutContentRes = builder.emptyLayoutContentRes;
    this.emptyLayoutDrawableRes = builder.emptyLayoutDrawableRes;
    this.emptyLayoutDrawable = builder.emptyLayoutDrawable;
    this.emptyPartLayoutContent = builder.emptyPartLayoutContent;
    this.emptyPartLayoutContentRes = builder.emptyPartLayoutContentRes;

    this.networkErrorLayoutTitle = builder.networkErrorLayoutTitle;
    this.networkErrorLayoutTitleRes = builder.networkErrorLayoutTitleRes;
    this.networkErrorLayoutContent = builder.networkErrorLayoutContent;
    this.networkErrorLayoutContentRes = builder.networkErrorLayoutContentRes;
    this.networkErrorLayoutDrawableRes = builder.networkErrorLayoutDrawableRes;
    this.networkErrorLayoutDrawable = builder.networkErrorLayoutDrawable;
    this.networkBtnText = builder.networkBtnText;
    this.networkBtnTextRes = builder.networkBtnTextRes;

    this.replaceLayoutHelper = new ReplaceLayoutHelper(contentLayout);
  }

  private View inflate(@LayoutRes int resource) {
    if (inflater == null) {
      inflater = LayoutInflater.from(contentLayout.getContext());
    }
    return inflater.inflate(resource, null);
  }

  ///////////////////////////////////////////
  /////////////////原有布局////////////////////
  ///////////////////////////////////////////

  /**
   * 显示原有布局
   */
  public void restoreLayout() {
    replaceLayoutHelper.restoreLayout();
  }

  public void showEmptyLayout() {
    createEmptyLayout();
    replaceLayoutHelper.showStatusLayout(emptyLayout);
  }

  public void showEmptyPartLayout() {
    createEmptyPartLayout();
    replaceLayoutHelper.showStatusLayout(emptyPartLayout);
  }

  private void createEmptyPartLayout() {
    if (emptyPartLayout == null) {
      emptyPartLayout = inflate(R.layout.empty_part_layout);
    }
    updateEmptyPartLayout();
  }

  private void updateEmptyPartLayout() {
    if (emptyPartLayout == null) return;
    TextView content = emptyPartLayout.findViewById(R.id.tv_empty_content);
    if (!TextUtils.isEmpty(emptyPartLayoutContent)) {
      content.setText(emptyPartLayoutContent);
    }
    if (emptyPartLayoutContentRes > 0) {
      content.setText(emptyPartLayoutContentRes);
    }
  }

  private void createEmptyLayout() {
    if (emptyLayout == null) {
      emptyLayout = inflate(R.layout.empty_layout);
    }
    updateEmptyLayout();
  }

  private void updateEmptyLayout() {
    if (emptyLayout == null) return;
    ImageView image = emptyLayout.findViewById(R.id.img_empty);
    TextView title = emptyLayout.findViewById(R.id.tv_empty_title);
    TextView content = emptyLayout.findViewById(R.id.tv_empty_hint);
    if (emptyLayoutDrawable != null) {
      image.setImageDrawable(emptyLayoutDrawable);
    }
    if (emptyLayoutDrawableRes > 0) {
      image.setImageResource(emptyLayoutDrawableRes);
    }

    if (!TextUtils.isEmpty(emptyLayoutTitle)) {
      title.setText(emptyLayoutTitle);
    }
    if (emptyLayoutTitleRes > 0) {
      title.setText(emptyLayoutTitleRes);
    } else if (TextUtils.isEmpty(emptyLayoutTitle)) {
      title.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(emptyLayoutContent)) {
      content.setText(emptyLayoutContent);
    }
    if (emptyLayoutContentRes > 0) {
      content.setText(emptyLayoutContentRes);
    }
  }
  ///////////////////////////////////////////
  ////////////////自定义布局///////////////////
  ///////////////////////////////////////////

  /**
   * 显示自定义状态布局
   *
   * @param customLayout 自定义布局
   */
  public void showCustomLayout(@NonNull View customLayout) {
    replaceLayoutHelper.showStatusLayout(customLayout);
  }

  /**
   * 显示自定义状态布局
   *
   * @param customLayoutID 自定义状态布局 ID
   * @return 通过 customLayoutID 生成的 View
   */
  public View showCustomLayout(@LayoutRes int customLayoutID) {
    View customerView = inflate(customLayoutID);
    showCustomLayout(customerView);
    return customerView;
  }

  /**
   * 显示自定义状态布局
   *
   * @param customLayout 自定义布局
   * @param clickViewID 可点击 View ID
   */
  public void showCustomLayout(@NonNull View customLayout, @IdRes int... clickViewID) {
    replaceLayoutHelper.showStatusLayout(customLayout);
    if (onStatusChildClickListener == null) {
      return;
    }

    for (int aClickViewID : clickViewID) {
      View clickView = customLayout.findViewById(aClickViewID);
      if (clickView == null) {
        return;
      }

      // 设置点击按钮点击时事件回调
      clickView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          onStatusChildClickListener.onCustomerChildClick(view);
        }
      });
    }
  }

  public void showNetWorkErrorLayout(@NetWorkStatus int type, View.OnClickListener listener) {
    createNetWorkErrorLayout();
    updateNetWorkErrorLayout(type, listener);
    replaceLayoutHelper.showStatusLayout(networkErrorLayout);
  }

  public void showNetWorkErrorCustomLayout(View.OnClickListener listener) {
    createNetWorkErrorLayout();
    updateNetWorkErrorLayout(listener);
    replaceLayoutHelper.showStatusLayout(networkErrorLayout);
  }

  private void updateNetWorkErrorLayout(View.OnClickListener listener) {
    TextView title = networkErrorLayout.findViewById(R.id.tv_empty_title);
    TextView content = networkErrorLayout.findViewById(R.id.tv_empty_hint);
    ImageView image = networkErrorLayout.findViewById(R.id.img_empty);
    Button networkBtn = networkErrorLayout.findViewById(R.id.network_btn);

    if (networkErrorLayoutDrawable != null) {
      image.setImageDrawable(networkErrorLayoutDrawable);
    }
    if (networkErrorLayoutDrawableRes > 0) {
      image.setImageResource(networkErrorLayoutDrawableRes);
    }

    if (!TextUtils.isEmpty(networkErrorLayoutTitle)) {
      title.setText(emptyLayoutTitle);
    }
    if (networkErrorLayoutTitleRes > 0) {
      title.setText(networkErrorLayoutTitle);
    } else if (TextUtils.isEmpty(networkErrorLayoutTitle)) {
      title.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(networkErrorLayoutContent)) {
      content.setText(emptyLayoutContent);
    }
    if (networkErrorLayoutContentRes > 0) {
      content.setText(networkErrorLayoutContentRes);
    }
    if (!TextUtils.isEmpty(networkBtnText)) {
      networkBtn.setText(emptyLayoutContent);
    }
    if (networkBtnTextRes > 0) {
      networkBtn.setText(networkBtnTextRes);
    }
    networkBtn.setOnClickListener(listener);
  }

  private void updateNetWorkErrorLayout(@NetWorkStatus int type, View.OnClickListener listener) {
    TextView title = networkErrorLayout.findViewById(R.id.tv_empty_title);
    TextView content = networkErrorLayout.findViewById(R.id.tv_empty_hint);
    ImageView image = networkErrorLayout.findViewById(R.id.img_empty);
    Button networkBtn = networkErrorLayout.findViewById(R.id.network_btn);

    String contentText = "";
    String titleText = "";
    Drawable drawable = null;
    String buttonText = "返回";
    switch (type) {
      case NetWorkStatus.FIVE_ZERO_ZERO:
        titleText = "服务器出错";
        contentText = "程序员小哥正在加急抢修中";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_img_500);
        break;
      case NetWorkStatus.FOUR_ZERO_FOUR:
        titleText = "页面不存在";
        contentText = "该页面可能已经删除，更名或暂时不可用";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_img_404);
        break;
      case NetWorkStatus.FOUR_ZERO_THREE:
        titleText = "禁止访问";
        contentText = "您没有当前页面的访问权限";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_img_403);
        break;
      case NetWorkStatus.NETWORK_OVER_TIME:
        titleText = "服务器超时";
        contentText = "请点击下方按钮重试";
        buttonText="点击重试";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_net_timeout);
        break;
      case NetWorkStatus.NETWORK_ERROR:
        titleText = "网络异常";
        contentText = "请点击下方按钮重试";
        buttonText="点击重试";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_net_notfound);

        break;
      case NetWorkStatus.NETWORK_NOT_CONNECT:
        titleText = "网络异常";
        contentText = "请连接网络后重试";
        buttonText="点击重试";
        drawable = contentLayout.getContext().getResources().getDrawable(R.drawable.vd_net_notfound);

        break;
    }
    title.setText(titleText);
    content.setText(contentText);
    networkBtn.setText(buttonText);
    image.setImageDrawable(drawable);
    networkBtn.setOnClickListener(listener);
  }

  private void createNetWorkErrorLayout() {
    if (networkErrorLayout == null) {
      networkErrorLayout = inflate(R.layout.network_error);
    }
  }

  /**
   * 显示自定义状态布局
   *
   * @param customLayoutID 自定义布局 ID
   * @param clickViewID 点击按钮 ID
   */
  public View showCustomLayout(@LayoutRes int customLayoutID, @IdRes int... clickViewID) {
    View customLayout = inflate(customLayoutID);
    showCustomLayout(customLayout, clickViewID);
    return customLayout;
  }

  public static final class Builder {

    private View contentLayout;

    private String emptyLayoutTitle;
    private @StringRes int emptyLayoutTitleRes;
    private String emptyLayoutContent;
    private @StringRes int emptyLayoutContentRes;
    private @DrawableRes int emptyLayoutDrawableRes;
    private Drawable emptyLayoutDrawable;

    private String emptyPartLayoutContent;
    private @StringRes int emptyPartLayoutContentRes;

    private String networkErrorLayoutTitle;
    private @StringRes int networkErrorLayoutTitleRes;
    private String networkErrorLayoutContent;
    private @StringRes int networkErrorLayoutContentRes;

    private @DrawableRes int networkErrorLayoutDrawableRes;
    private Drawable networkErrorLayoutDrawable;

    private String networkBtnText;
    private @StringRes int networkBtnTextRes;

    private OnStatusChildClickListener onStatusChildClickListener;

    public Builder setNetworkErrorLayoutTitle(String networkErrorLayoutTitle) {
      this.networkErrorLayoutTitle = networkErrorLayoutTitle;
      return this;
    }

    public Builder setNetworkErrorLayoutTitleRes(int networkErrorLayoutTitleRes) {
      this.networkErrorLayoutTitleRes = networkErrorLayoutTitleRes;
      return this;
    }

    public Builder setNetworkErrorLayoutContent(String networkErrorLayoutContent) {
      this.networkErrorLayoutContent = networkErrorLayoutContent;
      return this;
    }

    public Builder setNetworkErrorLayoutContentRes(int networkErrorLayoutContentRes) {
      this.networkErrorLayoutContentRes = networkErrorLayoutContentRes;
      return this;
    }

    public Builder setNetworkErrorLayoutDrawableRes(int networkErrorLayoutDrawableRes) {
      this.networkErrorLayoutDrawableRes = networkErrorLayoutDrawableRes;
      return this;
    }

    public Builder setNetworkErrorLayoutDrawable(Drawable networkErrorLayoutDrawable) {
      this.networkErrorLayoutDrawable = networkErrorLayoutDrawable;
      return this;
    }

    public Builder setNetworkBtnText(String networkBtnText) {
      this.networkBtnText = networkBtnText;
      return this;
    }

    public Builder setNetworkBtnTextRes(int networkBtnTextRes) {
      this.networkBtnTextRes = networkBtnTextRes;
      return this;
    }

    public Builder setEmptyLayoutTitle(String emptyLayoutTitle) {
      this.emptyLayoutTitle = emptyLayoutTitle;
      return this;
    }

    public Builder setEmptyLayoutContent(String emptyLayoutContent) {
      this.emptyLayoutContent = emptyLayoutContent;
      return this;
    }

    public Builder setEmptyLayoutDrawableRes(int emptyLayoutDrawableRes) {
      this.emptyLayoutDrawableRes = emptyLayoutDrawableRes;
      return this;
    }

    public Builder setEmptyLayoutDrawable(Drawable emptyLayoutDrawable) {
      this.emptyLayoutDrawable = emptyLayoutDrawable;
      return this;
    }

    public Builder setEmptyLayoutTitleRes(int emptyLayoutTitleRes) {
      this.emptyLayoutTitleRes = emptyLayoutTitleRes;
      return this;
    }

    public Builder setEmptyLayoutContentRes(int emptyLayoutContentRes) {
      this.emptyLayoutContentRes = emptyLayoutContentRes;
      return this;
    }

    public Builder setEmptyPartLayoutContent(String emptyPartLayoutContent) {
      this.emptyPartLayoutContent = emptyPartLayoutContent;
      return this;
    }

    public Builder setEmptyPartLayoutContentRes(int emptyPartLayoutContentRes) {
      this.emptyPartLayoutContentRes = emptyPartLayoutContentRes;
      return this;
    }

    /**
     * 创建状态布局 Build 对象
     *
     * @param contentLayout 原有布局，内容布局
     */
    public Builder(@NonNull View contentLayout) {
      this.contentLayout = contentLayout;
    }

    /**
     * 设置点击事件监听器
     *
     * @param listener 点击事件监听器
     * @return 状态布局 Build 对象
     */
    public Builder setOnStatusChildClickListener(OnStatusChildClickListener listener) {
      this.onStatusChildClickListener = listener;
      return this;
    }

    /**
     * 创建状态布局管理器
     *
     * @return 状态布局管理器
     */
    @NonNull public StatusLayoutManager build() {
      return new StatusLayoutManager(this);
    }
  }
}
