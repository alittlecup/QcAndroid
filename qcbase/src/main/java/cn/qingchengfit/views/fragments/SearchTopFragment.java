package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.R;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by huangbaole on 2018/4/19.
 */

public class SearchTopFragment extends BaseFragment {
  EditText editText;
  ImageView imClear;
  TextView tvCancel, content;
  LinearLayout llTopContainer, llContainer, commonView;

  @Retention(RetentionPolicy.RUNTIME) @StringDef({ Style.DARK, Style.WHITE }) @interface Style {
    String DARK = "dark";
    String WHITE = "white";
  }

  public static SearchTopFragment newInstance(String hint) {

    return newInstance(hint, Style.DARK);
  }

  public static SearchTopFragment newInstance(String hint, @Style String style) {
    SearchTopFragment fragment = new SearchTopFragment();
    Bundle bundle = new Bundle();
    bundle.putString("hint", hint);
    bundle.putString("style", style);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View root = inflater.inflate(R.layout.search_top_fragment, container, false);
    editText = root.findViewById(R.id.edit_search);
    imClear = root.findViewById(R.id.img_clear);
    tvCancel = root.findViewById(R.id.tv_cancel);
    content = root.findViewById(R.id.content);
    imClear.setVisibility(View.GONE);
    llTopContainer = root.findViewById(R.id.ll_top_container);
    llContainer = root.findViewById(R.id.ll_container);
    commonView = root.findViewById(R.id.common_view);
    commonView.setVisibility(View.GONE);
    RxRegiste(RxTextView.afterTextChangeEvents(editText)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          if (curFragment != null && curFragment instanceof onEditChangeListener) {
            ((onEditChangeListener) curFragment).afterTextChange(event.editable().toString());
          }
        }));
    RxRegiste(RxTextView.afterTextChangeEvents(editText)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          if (event != null) {
            if (TextUtils.isEmpty(event.editable().toString())) {
              imClear.setVisibility(View.GONE);
            } else {
              imClear.setVisibility(View.VISIBLE);
            }
          }
        }));
    imClear.setOnClickListener(v -> editText.setText(""));

    tvCancel.setOnClickListener(v -> getActivity().onBackPressed());
    if (curFragment != null) {
      stuff(curFragment);
    }
    if (getArguments() != null) {
      String hint = getArguments().getString("hint");
      editText.setHint(hint);
      String style = getArguments().getString("style");
      if (Style.WHITE.equals(style)) {
        llContainer.setBackgroundColor(getResources().getColor(R.color.bg_white));
        llTopContainer.setBackgroundResource(R.drawable.bg_grey_corners);
      } else {
        llContainer.setBackgroundColor(getResources().getColor(R.color.toolbar));
        llTopContainer.setBackgroundResource(R.drawable.bg_white_corners);
      }
    }
    if (!CompatUtils.less21() && llTopContainer.getParent() instanceof ViewGroup ) {
      ((ViewGroup) llTopContainer.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
    }

    return root;
  }

  private Fragment curFragment;

  @Override public int getLayoutRes() {
    return R.id.frag_container;
  }

  @Override public String getFragmentName() {
    return SearchTopFragment.class.getName();
  }

  public void setFragmentContainer(Fragment fragmentContainer) {
    this.curFragment = fragmentContainer;
    if (isAdded()) {
      stuff(fragmentContainer);
    }
    if (!(fragmentContainer instanceof onEditChangeListener)) {
      Log.e(SearchTopFragment.class.getName(),
          "setFragmentContainer: fragmentContainer must implements onEditChangeListener");
    }
  }

  public void setInitContent(String content) {
    commonView.setVisibility(View.VISIBLE);
    this.content.setText(content);
  }

  public void showNotFoundView() {
    showNotFoundView("未找到搜索结果");
  }

  public void showNotFoundView(String content) {
    commonView.setVisibility(View.VISIBLE);
    this.content.setText(content);
  }

  public interface onEditChangeListener {
    void afterTextChange(String text);
  }
}
