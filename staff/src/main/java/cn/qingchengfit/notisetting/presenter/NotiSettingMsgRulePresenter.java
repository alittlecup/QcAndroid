package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.StringUtils;
import java.util.Arrays;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingMsgRulePresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  private MVPView view;
  private String[] keys = new String[] {
      ShopConfigs.SMS_VERIFY_CODE,

      ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_SAVE,
      ShopConfigs.TEAM_SMS_USER_AFTER_USER_ORDER_SAVE,
      ShopConfigs.TEAM_SMS_TEACHER_AFTER_USER_ORDER_CANCEL,
      ShopConfigs.TEAM_SMS_USER_AFTER_USER_ORDER_CANCEL,
      ShopConfigs.TEAM_SMS_TEACHER_AFTER_TEACHER_ORDER_SAVE,
      ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_SAVE,
      ShopConfigs.TEAM_SMS_TEACHER_AFTER_TEACHER_ORDER_CANCEL,
      ShopConfigs.TEAM_SMS_USER_AFTER_TEACHER_ORDER_CANCEL,
      ShopConfigs.TEAM_BEFORE_REMIND_USER_MINUTES,
      ShopConfigs.TEAM_BEFORE_REMIND_USER,
      ShopConfigs.TEAM_COURSE_REMIND_TEACHER,
      ShopConfigs.TEAM_COURSE_REMIND_TEACHER_MINUTES,

      ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_SAVE,
      ShopConfigs.PRIVATE_SMS_USER_AFTER_USER_ORDER_SAVE,
      ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_USER_ORDER_CANCEL,
      ShopConfigs.PRIVATE_SMS_USER_AFTER_USER_ORDER_CANCEL,
      ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_TEACHER_ORDER_SAVE,
      ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_SAVE,
      ShopConfigs.PRIVATE_SMS_TEACHER_AFTER_TEACHER_ORDER_CANCEL,
      ShopConfigs.PRIVATE_SMS_USER_AFTER_TEACHER_ORDER_CANCEL,
      ShopConfigs.PRIVATE_BEFORE_REMIND_USER_MINUTES,
      ShopConfigs.PRIVATE_BEFORE_REMIND_USER,
      //ShopConfigs.PRIVATE_COURSE_REMIND_TEACHER,


  };

  @Inject public NotiSettingMsgRulePresenter() {
  }

  private void queryInfo() {
    RxRegiste(qcRestRepository.createRxJava1Api(Get_Api.class)
        .qcGetShopConfig(loginStatus.staff_id(), StringUtils.array2str(keys),
            gymWrapper.getParams())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
          @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data != null) {
                String[] content = new String[keys.length];
                for (SignInConfig.Config config : qcResponse.data.configs) {
                  int idx = Arrays.asList(keys).indexOf(config.getKey());
                  if (idx >= 0) {
                    content[idx] = config.show_text == null?"":config.show_text;
                  }
                }
                if (!CmStringUtils.isEmpty(content[0]))
                  view.onSysSmsNoti(content[0]);
                StringBuilder teamStr = new StringBuilder();
                for (int i = 1; i < 13; i++) {
                  if (!CmStringUtils.isEmpty(content[i]))
                    teamStr.append(content[i]).append("\n");
                }
                view.onGroupNoti(teamStr.toString());
                StringBuilder privateStr = new StringBuilder();
                for (int i = 13; i < 23; i++) {
                  if (!CmStringUtils.isEmpty(content[i]))
                    privateStr.append(content[i]).append("\n");
                }
                view.onPrivateNoti(privateStr.toString());
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    queryInfo();
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onSysSmsNoti(String s);

    void onGroupNoti(String s);

    void onPrivateNoti(String s);
  }
}
