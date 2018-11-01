package cn.qingchengfit.writeoff.view.verify;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.SoftKeyBoardListener;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.WriteOffBaseFragment;
import cn.qingchengfit.writeoff.databinding.WrWriteOffCheckPageBinding;
import cn.qingchengfit.writeoff.view.WriteOffCheckView;
import cn.qingchengfit.writeoff.view.WriteOffTicketSimpleInfoView;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "writeoff", path = "/ticket/verify") public class WriteOffCheckPage
    extends WriteOffBaseFragment<WrWriteOffCheckPageBinding, WriteOffCheckViewModel> {
  private WriteOffCheckView writeOffCheckView;
  private WriteOffTicketSimpleInfoView simpleInfoView;

  @Override protected void subscribeUI() {
    mViewModel.ticketMutableLiveData.observe(this, this::updateTicket);
  }

  @Override
  public WrWriteOffCheckPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = WrWriteOffCheckPageBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();
    initListener();
    return mBinding;
  }
  private void initListener(){
    mBinding.cmTicketNumber.setOnFocusChangeListener((v, hasFocus) -> {
      if(!hasFocus&&!TextUtils.isEmpty(mBinding.cmTicketNumber.getContent())){
        mViewModel.queryTicket(mBinding.cmTicketNumber.getContent());
      }
    });
    mBinding.imgScan.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
      }
    });
    SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
      @Override
      public void keyBoardShow(int height) {
      }
      @Override
      public void keyBoardHide(int height) {
        if(mBinding.cmTicketNumber.getEditText().hasFocus()){
          mBinding.cmTicketNumber.getEditText().setFocusable(false);
          mBinding.cmTicketNumber.getEditText().setFocusableInTouchMode(true);
        }
      }
    });

  }

  public void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("双十一课程体验券核销"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void updateTicket(Ticket ticket) {
    if (ticket != null) {
      if(ticket.isTicketChecked()){
        showHasVerifyDialog();
        return;
      }
      if (simpleInfoView == null) {
        simpleInfoView = new WriteOffTicketSimpleInfoView();
        stuff(R.id.fl_ticket_simple_view, simpleInfoView);
      }
      simpleInfoView.setTicket(ticket);
    }
  }

  private void initFragment() {
    writeOffCheckView = new WriteOffCheckView();
    stuff(R.id.fl_ticket_check_view, writeOffCheckView);
  }

  private void showHasVerifyDialog() {
    DialogUtils.showConfirm(getContext(), "", "该课程体验券已被核销",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            switch (dialogAction) {
              case POSITIVE:
                routeTo("/ticket/detail/", null);
                break;
              default:
                mBinding.cmTicketNumber.setContent("");
                break;
            }
          }
        });
  }
}
