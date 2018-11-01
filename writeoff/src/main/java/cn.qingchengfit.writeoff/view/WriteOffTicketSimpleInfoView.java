package cn.qingchengfit.writeoff.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.databinding.WrTicketSimpleViewBinding;
import cn.qingchengfit.writeoff.vo.ITicketDetailData;

public class WriteOffTicketSimpleInfoView extends SaasCommonFragment {
  WrTicketSimpleViewBinding mBinding;
  WriteOffCheckView writeOffCheckView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = WrTicketSimpleViewBinding.inflate(inflater, container, false);
    if (data != null) {
      mBinding.setTicket(data);
    }
    return mBinding.getRoot();
  }

  private ITicketDetailData data;

  public void setTicket(ITicketDetailData data) {
    if (mBinding != null) {
      mBinding.setTicket(data);
    } else {
      this.data = data;
    }
  }
}
