package cn.qingchengfit.design.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.testmodule.R;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/10/26.
 */

public class SearchFragment extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    toolbarTitle.setText("搜索");
    toolbar.inflateMenu(R.menu.menu_search);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
//        initSearch(item, toolbarTitle);
        return false;
      }
    });
    return view;
  }

  @Override public void onTextSearch(String text) {
    super.onTextSearch(text);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
