package cn.qingchengfit.design.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.design.SearchActivity;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/10/24.
 */

public class EmptyFragment extends BaseFragment {

  public static final String TAG = "EmptyFragment";

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.img_empty) ImageView imgEmpty;
  @BindView(R.id.tv_empty_title) TextView tvEmptyTitle;
  @BindView(R.id.tv_empty_hint) TextView tvEmptyHint;
  private String toolbarStr;
  private String title;
  private String hint;
  private int drawable;

  public static EmptyFragment newInstance(String toolbarStr, String title, String hint) {
    Bundle args = new Bundle();
    args.putString("toolbar", toolbarStr);
    args.putString("title", title);
    args.putString("hint", hint);
    EmptyFragment fragment = new EmptyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static EmptyFragment newInstance(String toolbarStr, String title, String hint,
      int drawable) {
    Bundle args = new Bundle();
    args.putString("toolbar", toolbarStr);
    args.putString("title", title);
    args.putString("hint", hint);
    args.putInt("drawable", drawable);
    EmptyFragment fragment = new EmptyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LogUtil.e(TAG, "---- onCreate");
    if (getArguments() != null){
      toolbarStr = getArguments().getString("toolbar");
      title = getArguments().getString("title");
      hint = getArguments().getString("hint");
      if (getArguments().containsKey("drawable")){
        drawable = getArguments().getInt("drawable");
      }
    }
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    LogUtil.e(TAG, "---- onAttach");
  }

  @Override public void onResume() {
    super.onResume();
    LogUtil.e(TAG, "---- onResume");
  }

  @Override public void onDetach() {
    super.onDetach();
    LogUtil.e(TAG, "---- onDetach");
  }

  @OnClick(R.id.tv_empty_title)
  public void onClick(){
    getFragmentManager().beginTransaction()
    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        .replace(R.id.frag_empty, new SearchFragment())
        .addToBackStack(null)
        .commit();
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    LogUtil.e(TAG, "---- onFinishAnimation");
    toolbarTitle.setText(toolbarStr);
    tvEmptyTitle.setText(title);
    tvEmptyHint.setText(hint);
  }

  @OnClick(R.id.tv_empty_hint)
  public void onClickHint(){
    Intent intent = new Intent(getContext(), SearchActivity.class);
    getContext().startActivity(intent);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    LogUtil.e(TAG, "---- onCreateView");
    View view = inflater.inflate(R.layout.fragment_empty, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    if (drawable != 0){
      imgEmpty.setImageResource(drawable);
    }
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    LogUtil.e(TAG, "---- onDestroyView");
  }
}
