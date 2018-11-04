package cn.qingchengfit.writeoff.view.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
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
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
  }

  @Override
  public WrWriteOffDetailPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = WrWriteOffDetailPageBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();
    mViewModel.loadTicketDetail(ticketId);
    mBinding.btnTicketBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    return mBinding;
  }
  private void initToolbar(){
    mBinding.setToolbarModel(new ToolbarModel("已核销课程体验券详情"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initFragment() {
    checkView = new WriteOffCheckView();
    simpleInfoView = new WriteOffTicketSimpleInfoView();
    stuff(R.id.fl_ticket_check_view, checkView);
    stuff(R.id.fl_ticket_simple_view, simpleInfoView);
  }
}
