package cn.qingchengfit.staffkit.reciever;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.moudle.GymStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.RealCard;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.ConstantNotification;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.EventNewPush;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.student.StudentActivity;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/27 2015.
 */
public class PushReciever extends PushMessageReceiver {

    /**
     * @param context
     * @param i     errorcode
     * @param s     appid
     * @param s1    userId
     * @param s2    channelId
     * @param s3    requestId
     */
    public static String BD_CHANNELID = "bd_channelid";
    public static String BD_USERLID = "bd_userid";

    @Override public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Timber.e("error:" + i + "   " + s + "  " + s1 + "  channelid:" + s2 + "    " + s3);
        PreferenceUtils.setPrefString(context, BD_USERLID, s1);
        PreferenceUtils.setPrefString(context, BD_CHANNELID, s2);
    }

    @Override public void onUnbind(Context context, int i, String s) {

    }

    @Override public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override public void onMessage(Context context, String s, String s1) {
        Timber.e(s + "  " + s1);
        RxBus.getBus().post(new EventNewPush());
    }

    @Override public void onNotificationClicked(final Context context, String s, String s1, String s2) {
        Timber.d("title:" + s + "   content:" + s1 + "   self:" + s2);
        if (!TextUtils.isEmpty(s2)) {
            final PushBean bean = new Gson().fromJson(s2, PushBean.class);

            RestRepository restRepository = new RestRepository();
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", bean.notification_id);
            restRepository.getPost_api().qcClearAllNoti(App.staffId, params);
            if (TextUtils.isEmpty(bean.url)) {
                if (bean.type != 0 && bean.type >= 11) {
                    final CoachService coachService = GymBaseInfoAction.getGymByShopIdNow(bean.brand_id, bean.shop_id);
                    String staffid = PreferenceUtils.getPrefString(context, Configs.PREFER_WORK_ID, "");
                    Intent intent = new Intent();
                    intent.putExtra(Configs.EXTRA_GYM_SERVICE, coachService);
                    intent.putExtra(Configs.EXTRA_GYM_STATUS, new GymStatus.Builder().isSin(false).isGuide(false).isSingle(false).build());
                    intent.putExtra(Configs.EXTRA_BRAND, new Brand.Builder().id(bean.brand_id).build());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    switch (bean.type) {
                        case ConstantNotification.TYPE_FITNESS_TRAINER_CHANGED:
                        case ConstantNotification.TYPE_FITNESS_SELLER_CHANGED:
                            intent.setClass(context, StudentActivity.class);   //打开会员模块
                            break;
                        case ConstantNotification.TYPE_FITNESS_WITHOUT_SELLER:
                            intent.setClass(context, AllotSalesActivity.class);
                            intent.putExtra("type", 12); //打开未分配销售列表页
                            break;
                        case ConstantNotification.TYPE_FITNESS_REMIND_CARD_BALANCE:
                            if (bean.card_id == 0) return;
                            if (!SerPermisAction.check(bean.shop_id, PermissionServerUtils.MANAGE_COSTS)) {
                                DialogUtils.showAlert(context, "抱歉，您无会员卡查看权限");
                                return;
                            }
                            intent.setClass(context, CardDetailActivity.class);
                            RealCard realCard = new RealCard("", "", "", "#70A4A9");
                            realCard.id = bean.card_id + "";
                            intent.putExtra(Configs.EXTRA_REAL_CARD, realCard);
                            break;
                    }
                    context.startActivity(intent);
                }
                return;
            } else {
                try {
                    if (Uri.parse(bean.url).getScheme().startsWith("http")) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", bean.url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {

                        final Intent toActivity = new Intent("cn.qingchengfit.staffkit", Uri.parse(bean.url));

                        String staffid = PreferenceUtils.getPrefString(context, Configs.PREFER_WORK_ID, "");
                        if (!StringUtils.isEmpty(bean.brand_id) && !StringUtils.isEmpty(bean.shop_id)) {
                            final CoachService coachService1 = GymBaseInfoAction.getGymByShopIdNow(bean.brand_id, bean.shop_id);
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
                                                SerPermisAction.writePermiss(qcResponse.data.permissions);
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
                            if (Uri.parse(bean.url).getScheme().startsWith("http")) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.putExtra("url", bean.url);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                Uri uri = Uri.parse(bean.url);
                                Intent tosb = new Intent(Intent.ACTION_VIEW, uri);
                                if (uri.getQueryParameterNames() != null) {
                                    for (String key : uri.getQueryParameterNames()) {
                                        tosb.putExtra(key, uri.getQueryParameter(key));
                                    }
                                }
                                tosb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(tosb);
                            }
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

    @Override public void onNotificationArrived(Context context, String s, String s1, String s2) {
        Timber.d(" recieve title:" + s + "   content:" + s1 + "   self:" + s2);
        RxBus.getBus().post(new EventNewPush());
    }
}
