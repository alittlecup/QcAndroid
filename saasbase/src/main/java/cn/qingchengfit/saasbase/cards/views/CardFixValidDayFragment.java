package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.common.PayEvent;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.body.UpdateCardValidBody;
import cn.qingchengfit.saasbase.cards.presenters.CardFixValidDayPresenter;
import cn.qingchengfit.saasbase.cards.views.offday.PayMethodOfflineDialog;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Date;
import javax.inject.Inject;
import rx.functions.Action1;

import static cn.qingchengfit.utils.DateUtils.formatDateFromYYYYMMDD;

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

@Leaf(module = "card", path = "/modify/validate") public class CardFixValidDayFragment
    extends SaasBaseFragment implements CardFixValidDayPresenter.MVPView {

  @BindView(R2.id.btn_start) CommonInputView btnStart;
  @BindView(R2.id.btn_end) CommonInputView btnEnd;
  @BindView(R2.id.civ_pay_method) CommonInputView civPayMethod;
  @BindView(R2.id.civ_money) CommonInputView civMoney;
  @BindView(R2.id.ep_money) ExpandedLayout epMoney;
  @BindView(R2.id.ep_vilid_day) ExpandedLayout epValid;

  @Inject CardFixValidDayPresenter presenter;
  @Need Card card;
  @BindView(R2.id.view_disable) View viewDisable;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  private TimeDialogWindow pwTime;
  private int pay_method;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_realcard_modify_valid_day, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    if (card.getType() == Configs.CATEGORY_DATE) {
      viewDisable.setVisibility(View.VISIBLE);
    }
    initToolbar(toolbar);
    toolbarTitle.setText("修改会员卡有效期");
    toolbar.inflateMenu(R.menu.menu_comfirm);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        UpdateCardValidBody body = new UpdateCardValidBody();
        if (epValid.isExpanded()) {
          if (TextUtils.isEmpty(btnStart.getContent()) || TextUtils.isEmpty(
              btnEnd.getContent())) {
            ToastUtils.show("请选择开始日期和结束日期");
            return true;
          }

          body.check_valid = true;
          if (formatDateFromYYYYMMDD(btnStart.getContent()).getTime() > formatDateFromYYYYMMDD(
              btnEnd.getContent()).getTime()) {
            ToastUtils.show("开始日期不能小于结束日期");
            return true;
          }
          if (TextUtils.isEmpty(btnStart.getContent()) || TextUtils.isEmpty(
              btnEnd.getContent())) {
            ToastUtils.show("请选择开始日期与结束日期");
            return true;
          }
          body.valid_from = btnStart.getContent();
          body.valid_to = btnEnd.getContent();
        } else {
          body.check_valid = false;
          body.valid_from = null;
          body.valid_to = null;
        }
        if (epMoney.isExpanded()) {
          if (pay_method == 0) {
            ToastUtils.show("请选择支付方式");
            return true;
          }
          try {
            float f = Float.parseFloat(civMoney.getContent());
            if (f < 0) {
              showAlert("收费金额请填写正数");
              return true;
            }
          } catch (Exception e) {
            ToastUtils.show("请填写正确金额");
            return true;
          }
          body.charge_type = pay_method;
          body.price = civMoney.getContent();
        }
        showLoading();
        presenter.updateValidDay(card.getId(), body);

        return true;
      }
    });

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

    onCardValidInfo();
    return view;
  }

  void onCardValidInfo() {
    epValid.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            epValid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            if (card.getType() == Configs.CATEGORY_DATE) {
              epValid.setExpanded(true);
              btnStart.setContent(DateUtils.getYYYYMMDDfromServer(card.getStart()));
              btnEnd.setContent(DateUtils.getYYYYMMDDfromServer(card.getEnd()));
            } else {
              epValid.setExpanded(card.isCheck_valid());
              btnStart.setContent(DateUtils.getYYYYMMDDfromServer(card.getValid_from()));
              btnEnd.setContent(DateUtils.getYYYYMMDDfromServer(card.getValid_to()));
            }
          }
        });
  }

  @Override public String getFragmentName() {
    return CardFixValidDayFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick({ R2.id.btn_start, R2.id.btn_end, R2.id.civ_pay_method })
  public void onViewClicked(View view) {
    int i = view.getId();
    if (i == R.id.btn_start) {
      Date start = new Date();
      if (!TextUtils.isEmpty(btnStart.getContent())) {
        start = DateUtils.formatDateFromYYYYMMDD(btnStart.getContent());
      }
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          btnStart.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, start);
    } else if (i == R.id.btn_end) {
      Date end = new Date();
      if (!TextUtils.isEmpty(btnEnd.getContent())) {
        end = formatDateFromYYYYMMDD(btnEnd.getContent());
      }
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          btnEnd.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, end);
    } else if (i == R.id.civ_pay_method) {
      new PayMethodOfflineDialog().show(getChildFragmentManager(), null);
    }
  }

  @Override public void onOk() {
    hideLoading();
    getActivity().onBackPressed();
  }

  @OnClick(R2.id.view_disable) public void onViewClicked() {
    ToastUtils.show("期限卡无法关闭有效期");
  }
}
