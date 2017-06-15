package cn.qingchengfit.staffkit.views.gym.site;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.GymSites;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/3/21 2016.
 */
public class ChooseSitePresenter extends BasePresenter {

    ChooseSiteView view;
    GymUseCase useCase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private List<Space> spaces;
    private RestRepository restRepository;

    @Inject public ChooseSitePresenter(GymUseCase gymUseCase, RestRepository restRepository) {
        this.useCase = gymUseCase;
        this.restRepository = restRepository;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ChooseSiteView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void querSiteListPermiss(final int courseType) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.STUDIO_LIST);
        params.put("method", "get");
        RxRegiste(restRepository.getGet_api()
            .qcGetGymSitesPermisson(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<GymSites>>() {
                @Override public void call(QcResponseData<GymSites> qcResponseGymSite) {
                    if (qcResponseGymSite.getStatus() == ResponseConstant.SUCCESS) {
                        spaces = qcResponseGymSite.data.spaces;
                        List<ImageTwoTextBean> beans = new ArrayList<ImageTwoTextBean>();
                        for (Space space : qcResponseGymSite.data.spaces) {
                            String support = "";
                            if (space.is_support_private() && space.is_support_team()) {
                                support = "支持私教和团课";
                            } else if (space.is_support_private()) {
                                support = "支持私教";
                            } else if (space.is_support_team()) {
                                support = "支持团课";
                            }
                            ImageTwoTextBean bean =
                                new ImageTwoTextBean("", space.getName(), "可以容纳" + space.getCapacity() + "人、" + support);
                            bean.tags.put("id", space.getId());
                            bean.showRight = true;
                            if (courseType == Configs.TYPE_PRIVATE) {
                                bean.showRight = true;
                                bean.rightIcon = R.drawable.ic_radio_unchecked;
                            } else {
                                if (courseType == -1) {
                                    bean.showRight = true;
                                    bean.rightIcon = R.drawable.ic_arrow_right;
                                } else {
                                    bean.showRight = false;
                                }
                            }
                            if (courseType < 0) {
                                beans.add(bean);
                            } else {
                                if (courseType == Configs.TYPE_PRIVATE && space.is_support_private()) beans.add(bean);
                                if (courseType == Configs.TYPE_GROUP && space.is_support_team()) beans.add(bean);
                            }
                        }
                        view.onData(beans);
                    } else {
                        // ToastUtils.logHttp(qcResponseGymSite);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void querySiteList(final int courseType) {
        useCase.querySite(gymWrapper.id(), gymWrapper.model(), new Action1<QcResponseData<GymSites>>() {
            @Override public void call(QcResponseData<GymSites> qcResponseGymSite) {

                if (qcResponseGymSite.getStatus() == ResponseConstant.SUCCESS) {
                    spaces = qcResponseGymSite.data.spaces;
                    List<ImageTwoTextBean> beans = new ArrayList<ImageTwoTextBean>();
                    for (Space space : qcResponseGymSite.data.spaces) {
                        String support = "";
                        if (space.is_support_private() && space.is_support_team()) {
                            support = "支持私教和团课";
                        } else if (space.is_support_private()) {
                            support = "支持私教";
                        } else if (space.is_support_team()) {
                            support = "支持团课";
                        }
                        ImageTwoTextBean bean = new ImageTwoTextBean("", space.getName(), "可以容纳" + space.getCapacity() + "人、" + support);
                        bean.tags.put("id", space.getId());
                        bean.showRight = true;
                        if (courseType == Configs.TYPE_PRIVATE) {
                            bean.showRight = true;
                            bean.rightIcon = R.drawable.ic_radio_unchecked;
                        } else {
                            if (courseType == -1) {
                                bean.showRight = true;
                                bean.rightIcon = R.drawable.ic_arrow_right;
                            } else {
                                bean.showRight = false;
                            }
                        }
                        if (courseType < 0) {
                            beans.add(bean);
                        } else {
                            if (courseType == Configs.TYPE_PRIVATE && space.is_support_private()) beans.add(bean);
                            if (courseType == Configs.TYPE_GROUP && space.is_support_team()) beans.add(bean);
                        }
                    }
                    view.onData(beans);
                } else {
                    // ToastUtils.logHttp(qcResponseGymSite);
                }
            }
        });
    }
}
