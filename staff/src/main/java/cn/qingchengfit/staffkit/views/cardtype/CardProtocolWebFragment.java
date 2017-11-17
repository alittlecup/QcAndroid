package cn.qingchengfit.staffkit.views.cardtype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.WebFragment;

/**
 * Created by fb on 2017/11/17.
 */

public class CardProtocolWebFragment extends WebFragment {

  @BindView(R.id.tv_read_protocol) TextView tvReadProtocol;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_protocol_web, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
