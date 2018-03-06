package cn.qingchengfit.staffkit.card.view;

import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2018/3/2.
 */
// TODO: 2018/3/2 web跳转native进行会员卡充值，1.权限的问题 2.web给的是Id，如果直接展示续卡界面的话应该先拉去会员卡信息
public class WebCardChargeFragment extends StaffCardChargeFragment {
  @Inject CardDetailPresenter presenter;

}
