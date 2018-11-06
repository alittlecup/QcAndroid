package cn.qingchengfit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.cards.views.CardDetailParams;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.utils.RouterUtils;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.reciever.PushBean;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.views.activity.WebActivity;
import com.google.gson.Gson;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
 * Created by Paper on 2017/4/21.
 */

public class PushUtils {
    @Inject SerPermisAction serPermisAction;
    @Inject GymBaseInfoAction gymBaseInfoAction;

    @Inject
    public PushUtils() {
    }

    public void HandlePush(final Context context, String s2) {
        if (!TextUtils.isEmpty(s2)) {
            final PushBean bean = new Gson().fromJson(s2, PushBean.class);
            //标记为已读
            RestRepository restRepository = new RestRepository();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", bean.notification_id + "");
          restRepository.getPost_api()
              .qcClearAllNoti(App.staffId, params)
              .onBackpressureBuffer()
              .subscribeOn(Schedulers.io())
              .subscribe();

            if (TextUtils.isEmpty(bean.url)) {//除了会员卡 分配销售等情况 其他都走schema
                if (bean.type != 0 && bean.type >= 11) {
                    final CoachService coachService = gymBaseInfoAction.getGymByShopIdNow(bean.brand_id, bean.shop_id);
                    String staffid = PreferenceUtils.getPrefString(context, Configs.PREFER_WORK_ID, "");
                    Intent intent = new Intent();
                    intent.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                    intent.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus.Builder().isSin(false).isGuide(false).isSingle(false).build());
                    intent.putExtra(Configs.EXTRA_BRAND, new Brand.Builder().id(bean.brand_id).build());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    switch (bean.type) {
                        case 11:
                            intent.setClass(context, StudentActivity.class);   //打开会员模块
                            break;
                        case 12:
                            intent.setClass(context, AllotSalesActivity.class);
                            intent.putExtra("type", 12); //打开未分配销售列表页
                            break;
                        case 13:
                            if (bean.card_id == 0) return;
                            if (!serPermisAction.check(bean.shop_id, PermissionServerUtils.MANAGE_COSTS)) {
                                DialogUtils.showAlert(context, "抱歉，您无会员卡查看权限");
                                return;
                            }
                            RouterUtils.routeTo(context,"card","/detail/", CardDetailParams.builder().cardid(bean.brand_id).build(),intent);
                            break;
                        //case ConstantNotification.TYPE_CLOUD_JOB_FAIR_ADD_GYM:
                        //case ConstantNotification.TYPE_CLOUD_JOB_FAIR_CHECK:
                        //
                        //    break;
                    }
                    context.startActivity(intent);
                } else {
                    //schema 跳转
                    try {
                        if (Uri.parse(bean.url).getScheme().startsWith("http")) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("url", bean.url);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {

                            final Intent toActivity =
                                new Intent(context.getPackageName(), Uri.parse(bean.url));
                            final CoachService coachService1 = gymBaseInfoAction.getGymByShopIdNow(bean.brand_id, bean.shop_id);
                            String staffid = PreferenceUtils.getPrefString(context, Configs.PREFER_WORK_ID, "");
                            if (!StringUtils.isEmpty(bean.brand_id) && !StringUtils.isEmpty(bean.shop_id)) {

                                if (coachService1 != null) {
                                    HashMap<String, Object> p = new HashMap<>();
                                    p.put("id", coachService1.getId());
                                    p.put("model", coachService1.getModel());
                                    restRepository.getGet_api()
                                        .qcPermission(staffid, p)
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<QcResponsePermission>() {
                                            @Override public void call(QcResponsePermission qcResponse) {
                                                if (ResponseConstant.checkSuccess(qcResponse)) {
                                                    serPermisAction.writePermiss(qcResponse.data.permissions);
                                                    toActivity.putExtra(Configs.EXTRA_GYM_SERVICE, coachService1);
                                                    toActivity.putExtra(Configs.EXTRA_BRAND, new Brand.Builder().id(bean.brand_id).build());
                                                    toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(toActivity);
                                                } else {
                                                    Timber.e(qcResponse.getMsg());
                                                }
                                            }
                                        }, new Action1<Throwable>() {
                                            @Override public void call(Throwable throwable) {
                                                Timber.e(throwable.getMessage());
                                            }
                                        });
                                } else {
                                    throw new Exception("notification native has no such shop");
                                }
                            } else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(bean.url));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        Timber.e("notification: " + e.getMessage());
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", bean.url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        }
    }
}
