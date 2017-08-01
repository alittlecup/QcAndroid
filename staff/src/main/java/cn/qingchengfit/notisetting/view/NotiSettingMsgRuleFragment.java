package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.notisetting.presenter.NotiSettingMsgRulePresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.TitleFragment;
import cn.qingchengfit.views.VpFragment;
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
 * Created by Paper on 2017/8/1.
 */
public class NotiSettingMsgRuleFragment extends VpFragment
    implements NotiSettingMsgRulePresenter.MVPView, TitleFragment {

  @BindView(R.id.tv_sms) TextView tvSms;
  @BindView(R.id.tv_group) TextView tvGroup;
  @BindView(R.id.tv_private) TextView tvPrivate;
  @Inject NotiSettingMsgRulePresenter presenter;
  @Inject SerPermisAction serPermisAction;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = null;
    if (serPermisAction.check(PermissionServerUtils.MESSAGESETTING)) {
      view = inflater.inflate(R.layout.fragment_noti_setting_send_rule, container, false);
      unbinder = ButterKnife.bind(this, view);
      delegatePresenter(presenter, this);
    } else {
      view = inflater.inflate(R.layout.item_common_no_data, container, false);
      ((ImageView) ButterKnife.findById(view, R.id.img)).setImageResource(
          R.drawable.vd_img_no_permission);
      ((TextView) ButterKnife.findById(view, R.id.tv_title)).setText(R.string.no_read_permission);
    }
    return view;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Override public String getFragmentName() {
    return NotiSettingMsgRuleFragment.class.getName();
  }

  @Override public String getTitle() {
    return "发送规则";
  }

  @Override public void onSysSmsNoti(String s) {
    if (s != null) tvSms.setText(s);
  }

  @Override public void onGroupNoti(String s) {
    if (s != null) tvGroup.setText(s);
  }

  @Override public void onPrivateNoti(String s) {
    if (s != null) tvPrivate.setText(s);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
