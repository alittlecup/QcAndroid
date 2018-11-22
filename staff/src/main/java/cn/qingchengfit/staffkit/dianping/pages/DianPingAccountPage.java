package cn.qingchengfit.staffkit.dianping.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staffkit.databinding.PageDianpingAccountBinding;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseDataEvent;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseType;
import cn.qingchengfit.staffkit.dianping.vo.DianPingShop;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import cn.qingchengfit.utils.DialogUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.List;

@Leaf(module = "dianping", path = "/dianping/account") public class DianPingAccountPage
    extends SaasBindingFragment<PageDianpingAccountBinding, DianPingAccountViewModel> {
  @Need String barCode = "";

  @Override protected void subscribeUI() {
    mViewModel.gymInfo.observe(this, this::updateGymInfo);
  }

  @Override
  public PageDianpingAccountBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageDianpingAccountBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("完善信息"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mViewModel.loadGymInfo();

    return mBinding;
  }

  private void initRxbus() {
    RxRegiste(RxBus.getBus().register(DianPingChooseDataEvent.class).subscribe(event -> {
      if (event.getType() == DianPingChooseType.CHOOSE_TAGS) {
        updateGymTags(event.getDatas());
      } else if (event.getType() == DianPingChooseType.CHOOSE_FACILITY) {
        updateGymFacilities(event.getDatas());
      }
    }));
  }

  private void updateGymTags(List<ISimpleChooseData> data) {

  }

  private void updateGymFacilities(List<ISimpleChooseData> data) {

  }

  private void updateGymInfo(DianPingShop gymInfo) {
    // TODO: 2018/11/21  更新界面信息
  }

  private void btnConfirm() {
    DialogUtils.shwoConfirm(getContext(), "确认场馆【" + mViewModel.gymInfo.getValue() + "】成为美团点评认证商家吗？",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {
              mViewModel.putGymInfo(mViewModel.gymInfo.getValue(), barCode);
            }
          }
        });
  }
}
