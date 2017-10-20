package cn.qingchengfit.pos.setting;

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
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.di.PosGymWrapper;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.bumptech.glide.Glide;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/13.
 */
@Leaf(module = "setting", path = "/gym/detail/") public class GymInfoFragment extends BaseFragment {

  @BindView(R.id.tv_setting_gym_detail_name) TextView tvSettingGymDetailName;
  @BindView(R.id.input_setting_gym_detail_name) CommonInputView inputSettingGymDetailName;
  @Inject PosGymWrapper gymWrapper;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.img_gym_detail_head) ImageView imgGymDetailHead;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting_gym_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar(toolbar);
    initView();
    return view;
  }

  private void initView() {
    Glide.with(getContext())
        .load(gymWrapper.getCoachService().photo)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(imgGymDetailHead, getContext()));
    inputSettingGymDetailName.setContent(gymWrapper.brand_name() + gymWrapper.name());
  }

  private void setToolbar(Toolbar toolbar) {
    initToolbar(toolbar);
    toolbarTitle.setText("健身房");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
