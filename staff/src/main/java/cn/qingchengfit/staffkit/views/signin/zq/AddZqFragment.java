package cn.qingchengfit.staffkit.views.signin.zq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;

/**
 * Created by fb on 2017/9/20.
 */

public class AddZqFragment extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.input_gym_name) CommonInputView inputGymName;
  @BindView(R.id.input_gym_address) CommonInputView inputGymAddress;
  @BindView(R.id.btn_find_equip) TextView btnFindEquip;
  @BindView(R.id.input_gym_fun) CommonInputView inputGymFun;
  @BindView(R.id.input_gym_start) CommonInputView inputGymStart;
  @BindView(R.id.input_gym_end) CommonInputView inputGymEnd;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_zq_access, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar();
    return view;
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_save);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
