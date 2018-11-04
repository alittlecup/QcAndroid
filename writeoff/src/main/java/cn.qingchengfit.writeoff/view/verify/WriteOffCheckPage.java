package cn.qingchengfit.writeoff.view.verify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.SoftKeyBoardListener;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.WriteOffBaseFragment;
import cn.qingchengfit.writeoff.databinding.WrWriteOffCheckPageBinding;
import cn.qingchengfit.writeoff.view.WriteOffCheckView;
import cn.qingchengfit.writeoff.view.WriteOffTicketSimpleInfoView;
import cn.qingchengfit.writeoff.view.detail.WriteOffTicketPageParams;
import cn.qingchengfit.writeoff.vo.impl.Ticket;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import lib_zxing.activity.CaptureActivity;
import lib_zxing.activity.CodeUtils;

@Leaf(module = "writeoff", path = "/ticket/verify") public class WriteOffCheckPage
    extends WriteOffBaseFragment<WrWriteOffCheckPageBinding, WriteOffCheckViewModel> {
  private WriteOffCheckView writeOffCheckView;
  private WriteOffTicketSimpleInfoView simpleInfoView;

  @Override protected void subscribeUI() {
    mViewModel.ticketMutableLiveData.observe(this, this::updateTicket);
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
    mViewModel.ticketVerifyResult.observe(this,aBoolean -> {
      if (aBoolean) {
        ToastUtils.show("核销成功");
        getActivity().onBackPressed();
      } else {
        ToastUtils.show("核销失败，请重试");

      }
    });
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

  private void initListener() {
    mBinding.cmTicketNumber.setOnFocusChangeListener((v, hasFocus) -> {
      if (!hasFocus && !TextUtils.isEmpty(mBinding.cmTicketNumber.getContent())) {
        mViewModel.queryTicket(mBinding.cmTicketNumber.getContent());
        mBinding.btnTicketCheck.setEnabled(true);
      }
    });
    mBinding.imgScan.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, 1001);
      }
    });
    SoftKeyBoardListener.setListener(getActivity(),
        new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
          @Override public void keyBoardShow(int height) {
          }

          @Override public void keyBoardHide(int height) {
            if (mBinding.cmTicketNumber.getEditText().hasFocus()) {
              mBinding.cmTicketNumber.getEditText().setFocusable(false);
              mBinding.cmTicketNumber.getEditText().setFocusableInTouchMode(true);
              mViewModel.ticketPostBody.getValue().setE_code(mBinding.cmTicketNumber.getContent());
            }
          }
        });
    mBinding.btnTicketCheck.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(mViewModel.ticketPostBody.getValue().checkBody()){
          mViewModel.postTicketBody();
        }
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1001) {
      //处理扫描结果（在界面上显示）
      if (null != data) {
        Bundle bundle = data.getExtras();
        if (bundle == null) {
          return;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
          String result = bundle.getString(CodeUtils.RESULT_STRING);
          mBinding.cmTicketNumber.setContent(result);
          mViewModel.queryTicket(result);
          mViewModel.ticketPostBody.getValue().setE_code(result);
          mBinding.btnTicketCheck.setEnabled(true);
        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
          Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  public void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("双十一课程体验券核销"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void updateTicket(Ticket ticket) {
    if (ticket != null) {
      if (ticket.isTicketChecked()) {
        showHasVerifyDialog(ticket.getTickerId());
        return;
      }
      mBinding.flTicketSimpleView.setVisibility(View.VISIBLE);
      simpleInfoView.setTicket(ticket);
    }else{
      mBinding.flTicketSimpleView.setVisibility(View.GONE);

    }
  }

  private void initFragment() {
    writeOffCheckView = new WriteOffCheckView();
    stuff(R.id.fl_ticket_check_view, writeOffCheckView);
    simpleInfoView = new WriteOffTicketSimpleInfoView();
    stuff(R.id.fl_ticket_simple_view, simpleInfoView);
    mBinding.flTicketSimpleView.setVisibility(View.GONE);
  }

  private void showHasVerifyDialog(String id) {
    DialogUtils.showConfirm(getContext(), "", "该课程体验券已被核销",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            switch (dialogAction) {
              case POSITIVE:
                routeTo("/ticket/detail", new WriteOffTicketPageParams().ticketId(id).build());
                break;
              default:
                mBinding.cmTicketNumber.setContent("");
                break;
            }
          }
        });
  }
}
