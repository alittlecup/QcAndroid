package cn.qingchengfit.saasbase.cards.views.offday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.model.common.PayEvent;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.OffDay;
import cn.qingchengfit.saasbase.cards.network.body.AheadOffDayBody;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 2017/5/5.
 */
@Leaf(module = "card", path = "/offday/ahead") public class AheadOffDayFragment
    extends SaasBaseFragment implements OffDayListView {

  @Need String offDayId;
  @Need Card card;

	TextView tvHint;
	TextView tvOriginPeriod;
	TextView tvAfterPeriod;
	CommonInputView civPayMethod;
	CommonInputView civMoney;
	ExpandedLayout epMoney;
	LinearLayout layoutValidInfo;

  @Inject OffDayListPresenter presenter;
	Toolbar toolbar;
	TextView toolbarTitle;

  private int pay_method;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_off_day_ahead, container, false);
    tvHint = (TextView) view.findViewById(R.id.tv_hint);
    tvOriginPeriod = (TextView) view.findViewById(R.id.tv_origin_period);
    tvAfterPeriod = (TextView) view.findViewById(R.id.tv_after_period);
    civPayMethod = (CommonInputView) view.findViewById(R.id.civ_pay_method);
    civMoney = (CommonInputView) view.findViewById(R.id.civ_money);
    epMoney = (ExpandedLayout) view.findViewById(R.id.ep_money);
    layoutValidInfo = (LinearLayout) view.findViewById(R.id.layout_valid_info);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.civ_pay_method).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onPayMethod();
      }
    });

    delegatePresenter(presenter, this);
    setToolbar();
    onInfo();
    RxBusAdd(PayEvent.class).subscribe(new Action1<PayEvent>() {
      @Override public void call(PayEvent payEvent) {
        switch (payEvent.result) {
          case 2:
            pay_method = Configs.CHARGE_MODE_CASH;
            break;
          case 4:
            pay_method = Configs.CHARGE_MODE_CARD;
            break;
          case 5:
            pay_method = Configs.CHARGE_MODE_TRANSFER;
            break;
          case 6:
            pay_method = Configs.CHARGE_MODE_OTHER;
            break;
        }
        String m = "";
        switch (pay_method) {
          case Configs.CHARGE_MODE_CASH:
            m = "现金支付";
            break;
          case Configs.CHARGE_MODE_CARD:
            m = "刷卡支付";
            break;
          case Configs.CHARGE_MODE_TRANSFER:
            m = "转账支付";
            break;
          case Configs.CHARGE_MODE_OTHER:
            m = "其他";
            break;
        }
        civPayMethod.setContent(m);
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
      }
    });
    return view;
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText("提前销假");
    toolbar.inflateMenu(R.menu.menu_comfirm);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        showLoading();
        AheadOffDayBody body = new AheadOffDayBody();
        if (epMoney.isExpanded()) {
          if (pay_method == 0) {
            ToastUtils.show("请选择支付方式");
            return true;
          }
          body.charge_type = pay_method;
          String money = civMoney.getContent();
          try {
            Float.parseFloat(money);
          } catch (Exception e) {
            ToastUtils.show("请填写正确的金额");
            return true;
          }
          body.price = civMoney.getContent();
        }
        showLoading();
        presenter.aheadOffDay(offDayId, body);
        return true;
      }
    });
  }

  void onInfo() {
    String s = DateUtils.Date2YYYYMMDD(new Date());
    int intervalDay = DateUtils.interval(card.getLock_start(), s);

    tvHint.setText(getString(R.string.hint_ahead_day_off, s, intervalDay));
    if (TextUtils.isEmpty(card.getValid_from())) {
      layoutValidInfo.setVisibility(View.GONE);
      return;
    }
    tvOriginPeriod.setText(DateUtils.getYYYYMMDDfromServer(card.getValid_from())
        + "至"
        + DateUtils.getYYYYMMDDfromServer(card.getValid_to()));
    try {
      if (intervalDay > 0) {
        tvAfterPeriod.setText(
            DateUtils.getYYYYMMDDfromServer(card.getValid_from()) + "至" + DateUtils.Date2YYYYMMDD(
                DateUtils.addDay(DateUtils.formatDateFromYYYYMMDD(card.getValid_to()),
                    intervalDay)));
      }
    } catch (Exception e) {
      LogUtil.e(e.getMessage());
    }
  }

 public void onPayMethod() {
    new PayMethodOfflineDialog().show(getChildFragmentManager(), null);
  }

  @Override public String getFragmentName() {
    return AheadOffDayFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onOffDayList(List<OffDay> offDays) {

  }

  @Override public void onSucceess() {
    hideLoading();
    ToastUtils.show("提前销假成功");
    getActivity().onBackPressed();
  }

  @Override public void onFailed(String s) {
    super.onShowError(s);
  }
}
