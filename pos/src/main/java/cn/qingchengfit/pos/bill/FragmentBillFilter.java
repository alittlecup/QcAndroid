package cn.qingchengfit.pos.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcAutoLineRadioGroup;

/**
 * Created by fb on 2017/10/11.
 */

public class FragmentBillFilter extends BaseFragment {

  @BindView(R.id.qrg_status) QcAutoLineRadioGroup qrgStatus;
  @BindView(R.id.tv_bill_time_start) TextView tvBillTimeStart;
  @BindView(R.id.tv_bill_time_end) TextView tvBillTimeEnd;
  @BindView(R.id.qrg_bill_type) QcAutoLineRadioGroup qrgBillType;
  @BindView(R.id.qrg_bill_receive_crash) QcAutoLineRadioGroup qrgBillReceiveCrash;
  @BindView(R.id.qrg_bill_platform) QcAutoLineRadioGroup qrgBillPlatform;
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

  @OnClick(R.id.tv_bill_time_start) public void timeStart() {

  }

  @OnClick(R.id.tv_bill_time_end) public void timeEnd() {

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
