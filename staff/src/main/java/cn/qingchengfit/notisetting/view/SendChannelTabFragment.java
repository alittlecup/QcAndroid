package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.CmBottomListChosenItem;
import cn.qingchengfit.items.CmBottomListItem;
import cn.qingchengfit.notisetting.presenter.NotiSettingChannelPresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.views.VpFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.CommonInputView;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static eu.davidea.flexibleadapter.SelectableAdapter.MODE_SINGLE;

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
public class SendChannelTabFragment extends VpFragment
    implements NotiSettingChannelPresenter.MVPView, TitleFragment {

  @BindView(R.id.tv_msg_left) CompatTextView tvMsgLeft;
  @BindView(R.id.tv_has_verify) CompatTextView tvHasVerify;
  @BindView(R.id.civ_send_student) CommonInputView civSendStudent;
  @BindView(R.id.civ_send_staff) CommonInputView civSendStaff;

  @Inject NotiSettingChannelPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  BottomListFragment bottomListFragment;

  public static SendChannelTabFragment newInstance() {
    Bundle args = new Bundle();
    SendChannelTabFragment fragment = new SendChannelTabFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = null;

    if (serPermisAction.check(PermissionServerUtils.MESSAGECHANNELS)) {
      view = inflater.inflate(R.layout.fragment_send_channel_tab, container, false);
      unbinder = ButterKnife.bind(this, view);
      delegatePresenter(presenter, this);
      presenter.queryCurSMSleft();
      presenter.querySendChannel();
    } else {
      view = inflater.inflate(R.layout.item_common_no_data, container, false);
      ((ImageView) ButterKnife.findById(view, R.id.img)).setImageResource(
          R.drawable.vd_img_no_permission);
      ((TextView) ButterKnife.findById(view, R.id.tv_title)).setText(R.string.no_read_permission);
    }
    return view;
  }

  @Override public String getFragmentName() {
    return SendChannelTabFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.layout_channel_msg) public void onLayoutChannelMsgClicked() {
    if (!serPermisAction.check(PermissionServerUtils.MESSAGESETTING_CAN_WRITE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    routeTo(new NotiSettingMsgChargeFragment());
  }

  @OnClick(R.id.layout_channel_wx) public void onLayoutChannelWxClicked() {
    if (!serPermisAction.check(PermissionServerUtils.MESSAGESETTING_CAN_WRITE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    routeTo(new NotiSettingWxTemplateFragment());
  }

  @OnClick(R.id.layout_channel_app) public void onLayoutChannelAppClicked() {
    showAlert("通过「青橙健身APP」和「青橙健身教练APP」的推送消息通知工作人员和教练");
  }

  @OnClick(R.id.civ_send_student) public void onCivSendStudentClicked() {
    if (!serPermisAction.check(PermissionServerUtils.MESSAGESETTING_CAN_WRITE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    bottomListFragment = BottomListFragment.newInstance("发送给会员的通知", MODE_SINGLE);
    bottomListFragment.loadData(getStudentBottomItems());
    bottomListFragment.setListener(new BottomListFragment.ComfirmChooseListener() {
      @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
        if (selectedPos != null && selectedPos.size() > 0) {
          int realPos = presenter.getRealSendMethodType(selectedPos.get(0));
          presenter.postSendStudentNoti(realPos + 1);
          sendToStudentNoti(realPos);
        }
      }
    });
    bottomListFragment.setSelectedPos(presenter.getToStudentMethod());
    bottomListFragment.show(getChildFragmentManager(), "");
  }

  @OnClick(R.id.civ_send_staff) public void onCivSendStaffClicked() {
    if (!serPermisAction.check(PermissionServerUtils.MESSAGESETTING_CAN_WRITE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    bottomListFragment = BottomListFragment.newInstance("发送给员工的通知", MODE_SINGLE);
    bottomListFragment.loadData(getStaffBottomItems());
    bottomListFragment.setListener(new BottomListFragment.ComfirmChooseListener() {
      @Override public void onComfirmClick(List<IFlexible> dats, List<Integer> selectedPos) {
        if (selectedPos != null && selectedPos.size() > 0) {
          int realPos = selectedPos.get(0);
          presenter.postSendStaffNoti(realPos + 1);//后端对应的是 1234
          sendToStaffNoti(realPos);
        }
      }
    });
    bottomListFragment.setSelectedPos(presenter.getToStaffMethod());
    bottomListFragment.show(getChildFragmentManager(), "");
  }

  public List<AbstractFlexibleItem> getStudentBottomItems() {
    List<AbstractFlexibleItem> ret = new ArrayList<>();
    int i = 0;
    for (String s : getResources().getStringArray(R.array.noti_setting_methods_user)) {
      if (i == 0) {
        ret.add(new CmBottomListChosenItem(s, "优先发送微信模板消息，不能发送时自动转为短信发送",true));
      } else if (i == 1){
        ret.add(new CmBottomListChosenItem(s, "",true));
      } else {
        ret.add(new CmBottomListChosenItem(s, "",presenter.isHasWXbinded()));
      }
      i++;
    }
    if (!presenter.isHasWXbinded()) {
      ret.add(new CmBottomListItem(GravityCompat.START | Gravity.CENTER_VERTICAL, R.color.text_grey,
          "您还没有授权公众号发送微信模板消息"));
    }
    return ret;
  }

  public List<AbstractFlexibleItem> getStaffBottomItems() {
    List<AbstractFlexibleItem> ret = new ArrayList<>();
    int i = 0;
    for (String s : getResources().getStringArray(R.array.noti_setting_methods_staff)) {
      if (i == 0) {
        ret.add(new CmBottomListChosenItem(s, "优先发送APP推送消息，不能发送时自动转为短信发送"));
      } else {
        ret.add(new CmBottomListChosenItem(s, ""));
      }
      i++;
    }
    return ret;
  }

  @Override public String getTitle() {
    return "发送渠道";
  }

  @Override public void onSMSleft(int count) {
    if (count < 100) {
      tvMsgLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
      tvMsgLeft.setText(count <= 0 ? "剩余0条" : "余额不足100条");
    } else {
      tvMsgLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey));
      tvMsgLeft.setText(getString(R.string.str_left_d_sms, count));
    }
  }

  @Override public void onWeChatleft(boolean authored) {
    tvHasVerify.setText(authored ? "已授权" : "未授权");
  }

  @Override public void sendToStudentNoti(int x) {
    if (x < 4) {
      civSendStudent.setContent(
          getResources().getStringArray(R.array.noti_setting_methods_user)[x]);
    } else {
      CrashUtils.sendCrash(new Throwable("消息设置 短信 发送给会员的方式 大于3"));
    }
  }

  @Override public void sendToStaffNoti(int x) {
    if (x < 4) {
      civSendStaff.setContent(getResources().getStringArray(R.array.noti_setting_methods_staff)[x]);
    } else {
      CrashUtils.sendCrash(new Throwable("消息设置 短信 发送给员工的方式 大于3"));
    }
  }
}
