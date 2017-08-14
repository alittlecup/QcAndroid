package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.notisetting.presenter.NotiSettingWxTemplatePresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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
public class NotiSettingWxTemplateFragment extends BaseFragment
    implements NotiSettingWxTemplatePresenter.MVPView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.img_brand) ImageView imgBrand;
  @BindView(R.id.tv_has_bind) TextView tvHasBind;
  @BindView(R.id.tv_hint) TextView tvHint;
  @BindView(R.id.tv_not_ready) TextView tvNotReady;
  @BindView(R.id.btn_authored) Button btnAuthored;

  @Inject GymWrapper gymWrapper;
  @Inject NotiSettingWxTemplatePresenter presenter;
  @Inject GymFunctionFactory gymFunctionFactory;
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_noti_setting_wx_template, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("微信模板消息");
  }

  @Override public String getFragmentName() {
    return NotiSettingWxTemplateFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R.id.btn_authored) public void onViewClicked() {
    gymFunctionFactory.goQrScan(this, "/message/channels", null, gymWrapper.getCoachService());
  }

  @Override public void onAuthored(int type,boolean author ,boolean isready) {
      if (author) {
        //已授权
        PhotoUtils.middle(imgBrand, gymWrapper.getBrand().getPhoto());
        tvHasBind.setText("已授权");
        if (type == 2) {//服务号
          tvNotReady.setVisibility(isready?View.GONE:View.VISIBLE);
          tvHint.setText("您可以在「发送渠道优先级」中设置通知的发送渠道");
        }else{
          tvHint.setText("抱歉当前公众号为订阅号，仅服务号支持发送模板消息");
        }
        btnAuthored.setBackgroundResource(R.drawable.btn_qc_rect_round_coner_black);
        btnAuthored.setText("取消授权");
        btnAuthored.setTextColor(ContextCompat.getColor(getContext(),R.color.text_dark));
      } else {
        imgBrand.setImageResource(R.drawable.vd_authorize_wechat);
        tvHasBind.setText("未授权");
        tvHint.setText("完成场馆公众号授权之后，即可通过微信公众号发送通知消息");
        btnAuthored.setBackgroundResource(R.drawable.btn_prime);
        btnAuthored.setText("去授权");
        btnAuthored.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
      }
    //}else if (type == 1){
      //type == 1 //订阅号
    //}else {//10 开放平台
    //  imgBrand.setImageResource(R.drawable.vd_authorize_wechat);
    //  tvHasBind.setText("未授权");
    //  tvHint.setText("抱歉当前公众号为订阅号，仅服务号支持发送模板消息");
    //  btnAuthored.setBackgroundResource(R.drawable.btn_prime);
    //  btnAuthored.setText("重新绑定");
    //  btnAuthored.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
    //}

  }
}
