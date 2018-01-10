package cn.qingchengfit.weex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.weex.R;
import cn.qingchengfit.weex.utils.WeexUtil;

/**
 * Created by huangbaole on 2018/1/10.
 */

public class WeexSplashPage extends BaseFragment {
  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View inflate = inflater.inflate(R.layout.view_weex_splash, container,false);
    inflate.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WeexUtil.loadJsMap();

      }
    });
    if (inflate.getParent() != null) {
      ((ViewGroup) inflate.getParent()).removeView(inflate);
    }
    return inflate;
  }
}
