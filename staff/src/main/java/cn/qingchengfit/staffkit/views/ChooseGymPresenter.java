package cn.qingchengfit.staffkit.views;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/5 2016.
 */
public class ChooseGymPresenter implements Presenter {
    @Inject GymUseCase useCase;
    @Inject SerPermisAction serPermisAction;
    private Subscription gymListSp;

    private ChooseGymView view;
    private String allChooseStr, mCurShop;

    @Inject public ChooseGymPresenter(GymUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (ChooseGymView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {
        if (intent != null) {
            allChooseStr = IntentUtils.getIntentString(intent, 0);
            mCurShop = IntentUtils.getIntentString(intent, 1);
            view.setAllChosen(allChooseStr, TextUtils.isEmpty(mCurShop));
        }
    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        if (gymListSp != null) gymListSp.unsubscribe();
        this.view = null;
    }

    public void queryGymList(String brand_id, final String permission) {
        gymListSp = useCase.getGymList(brand_id, new Action1<List<CoachService>>() {
                @Override public void call(List<CoachService> coachServices) {
                    List<ImageTwoTextBean> mDatas = new ArrayList<ImageTwoTextBean>();
                    for (CoachService service : coachServices) {
                        ImageTwoTextBean bean = new ImageTwoTextBean(service.getPhoto(), service.getName(), service.getBrand_name());
                        if (TextUtils.equals(service.getShop_id(), mCurShop)) {
                            bean.rightIcon = R.drawable.ic_green_right;
                            bean.showRight = true;
                        }

                        if (!StringUtils.isEmpty(permission)) {
                            if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_MEMBERS)) {
                                if (!serPermisAction.check(service.getId(), service.getModel(), PermissionServerUtils.MANAGE_MEMBERS)) {
                                    bean.hiden = true;
                                }
                            } else if (permission.equalsIgnoreCase(PermissionServerUtils.MANAGE_COSTS)) {

                                if (!serPermisAction.check(service.getShop_id(), PermissionServerUtils.MANAGE_COSTS)) bean.hiden = true;
                            } else if (permission.equalsIgnoreCase(PermissionServerUtils.SALES_REPORT)) {
                                if (!serPermisAction.check(service.getShop_id(), PermissionServerUtils.SALES_REPORT)) bean.hiden = true;
                            } else if (permission.equalsIgnoreCase(PermissionServerUtils.ORDERS_DAY)) {
                                if (!serPermisAction.check(service.getShop_id(), PermissionServerUtils.ORDERS_DAY)) bean.hiden = true;
                            } else if (!serPermisAction.check(service.getShop_id(), permission)) bean.hiden = true;
                        }

                        bean.tags.put("type", Integer.toString(1));
                        bean.tags.put("id", service.getId());
                        bean.tags.put("model", service.getModel());
                        bean.tags.put("shop_id", service.getShop_id());
                        mDatas.add(bean);
                    }

                    view.onDataSuccess(mDatas);
                }
            }

        );
    }
}
