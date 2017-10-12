package cn.qingchengfit.pos.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/10/11.
 */

public class FragmentBillFilter extends BaseFragment {

  @BindView(R.id.btn_bill_filter_reset) TextView btnBillFilterReset;
  @BindView(R.id.btn_bill_filter_confirm) TextView btnBillFilterConfirm;
  Unbinder unbinder;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_bill_filter, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
