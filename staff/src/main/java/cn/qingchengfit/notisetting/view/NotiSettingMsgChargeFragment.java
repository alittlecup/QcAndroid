package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.notisetting.item.NotiSettingChargeItem;
import cn.qingchengfit.notisetting.presenter.NotiSettingMsgChargePresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/7/31.
 */
public class NotiSettingMsgChargeFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, NotiSettingMsgChargePresenter.MVPView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.rv) RecyclerView rv;
  @BindView(R.id.tv_sms_left) TextView tvSmsLeft;
  @BindView(R.id.tv_count_after_charge) TextView tvCountAfterCharge;
  @BindView(R.id.btn_confirm) Button btnConfirm;

  @Inject NotiSettingMsgChargePresenter presenter;
  private CommonFlexAdapter adapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_noti_setting_msg_charge, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    btnConfirm.setEnabled(false);
    initToolbar(toolbar);
    List<AbstractFlexibleItem> list = new ArrayList<>();
    list.add(new NotiSettingChargeItem(50, 1000));
    list.add(new NotiSettingChargeItem(100, 2000));
    list.add(new NotiSettingChargeItem(200, 4000));
    list.add(new NotiSettingChargeItem(500, 10000));
    list.add(new NotiSettingChargeItem(1000, 20000));
    list.add(new NotiSettingChargeItem(2000, 40000));
    adapter = new CommonFlexAdapter(list, this);
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(),
        getResources().getInteger(R.integer.grid_item_count));
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    rv.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(10)
        .withRightEdge(true)
        .withBottomEdge(true));
    rv.setLayoutManager(gridLayoutManager);
    rv.setAdapter(adapter);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("短信充值");
  }

  @Override public String getFragmentName() {
    return NotiSettingMsgChargeFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.btn_confirm) public void onComfirm() {
    showLoading();
    presenter.chargeSMS(adapter.getSelectedPositions().get(0), getString(R.string.wechat_code));
  }

  @Override public boolean onItemClick(int i) {
    int lastPos = -1;
    if (adapter.getSelectedPositions().size() > 0) {
      lastPos = adapter.getSelectedPositions().get(0);
    }
    adapter.clearSelection();
    adapter.notifyItemChanged(lastPos);
    adapter.toggleSelection(i);
    adapter.notifyItemChanged(i);
    btnConfirm.setEnabled(adapter.getSelectedItemCount() > 0);
    String newCount = Integer.toString(presenter.getNewCount(i));
    eu.davidea.flexibleadapter.utils.FlexibleUtils.highlightWords(tvCountAfterCharge,
        "充值后剩余条数：" + newCount + "条", newCount);
    return true;
  }

  @Override public void onPayPage(String s) {
    hideLoading();
    WebActivity.startWeb(s, getContext());
    //onPaySuccess();
  }

  @Override public void onPaySuccess() {
    rmAndTo(this, NotiSettingChargeResultFragment.newInstanceSuccess(
        presenter.getNewCount(adapter.getSelectedPositions().get(0))), null);
  }

  @Override public void onPayFailed() {
    rmAndTo(this, NotiSettingChargeResultFragment.newInstanceFailed(), null);
  }

  @Override public void onSmsLeft(int count) {
    tvSmsLeft.setText(count + "");
  }
}
