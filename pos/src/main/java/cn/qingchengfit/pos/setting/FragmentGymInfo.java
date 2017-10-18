package cn.qingchengfit.pos.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2017/10/13.
 */
@Leaf(module = "setting",path = "/gym/")
public class FragmentGymInfo extends BaseFragment {

  @BindView(R.id.tv_setting_gym_detail_name) TextView tvSettingGymDetailName;
  @BindView(R.id.input_setting_gym_detail_name) CommonInputView inputSettingGymDetailName;
  @BindView(R.id.input_setting_gym_detail_no) CommonInputView inputSettingGymDetailNo;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_setting_gym_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
