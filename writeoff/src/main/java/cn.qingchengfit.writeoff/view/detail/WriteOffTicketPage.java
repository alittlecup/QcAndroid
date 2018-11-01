package cn.qingchengfit.writeoff.view.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.WriteOffBaseFragment;
import cn.qingchengfit.writeoff.databinding.WrWriteOffDetailPageBinding;
import cn.qingchengfit.writeoff.view.WriteOffCheckView;
import cn.qingchengfit.writeoff.view.WriteOffTicketSimpleInfoView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "writeoff", path = "/ticket/detail")

public class WriteOffTicketPage
    extends WriteOffBaseFragment<WrWriteOffDetailPageBinding, WriteOffTicketDetailViewModel> {
  WriteOffCheckView checkView;
  WriteOffTicketSimpleInfoView simpleInfoView;
  @Need String ticketId;

  @Override protected void subscribeUI() {
    mViewModel.ticketMutableLiveData.observe(this, ticket -> {
      if (checkView != null && simpleInfoView != null && ticket != null) {
        simpleInfoView.setTicket(ticket);
        checkView.setTicket(ticket);
      }
    });
  }

  @Override
  public WrWriteOffDetailPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = WrWriteOffDetailPageBinding.inflate(inflater, container, false);
    initFragment();
    return mBinding;
  }

  private void initFragment() {
    checkView = new WriteOffCheckView();
    simpleInfoView = new WriteOffTicketSimpleInfoView();
    stuff(R.id.fl_ticket_check_view, checkView);
    stuff(R.id.fl_ticket_simple_view, simpleInfoView);
  }
}
