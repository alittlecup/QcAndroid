package cn.qingchengfit.staffkit.views.card;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.body.FixCard;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.widgets.AlphabetView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Paper on 16/6/16.
 * <p>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public class FixRealcardStudentPresenter extends BasePresenter {
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private Subscription spQuery;
    private Subscription spQueryNet;
    private Subscription spFilter;
    private FixRealcardStudnetView view;
    private int queryCount = 0;

    @Inject public FixRealcardStudentPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (FixRealcardStudnetView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spQuery != null) spQuery.unsubscribe();
        if (spFilter != null) spFilter.unsubscribe();
        if (spQueryNet != null) spQueryNet.unsubscribe();
    }

    public void subsribeDb() {
        if (gymWrapper.inBrand()) {//连锁运营
            RxRegiste(StudentAction.newInstance()
                .getStudentByBrand(gymWrapper.brand_id())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<QcStudentBean>>() {
                    @Override public void call(List<QcStudentBean> qcStudentBeen) {
                        handleData(qcStudentBeen, gymWrapper.brand_id(), null, null);
                    }
                }));
        } else {//场馆
            RxRegiste(StudentAction.newInstance()
                .getStudentByGym(gymWrapper.id(), gymWrapper.model())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<QcStudentBean>>() {
                    @Override public void call(List<QcStudentBean> qcStudentBeen) {
                        handleData(qcStudentBeen, gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model());
                    }
                }));
        }
    }

    public void queryStudent(String staffid, String shopid) {
        HashMap<String, Object> params = GymUtils.getParams(gymWrapper.getCoachService(), gymWrapper.getBrand(), shopid);
        params.put("key", PermissionServerUtils.MANAGE_COSTS);
        params.put("method", "put");
        RxRegiste(restRepository.getGet_api()
            .qcGetCardBundldStudents(staffid, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .map(new Func1<QcResponseData<Students>, Boolean>() {
                @Override public Boolean call(QcResponseData<Students> qcResponseAllStudent) {

                    if (ResponseConstant.checkSuccess(qcResponseAllStudent)) {
                        for (QcStudentBean bean : qcResponseAllStudent.data.users) {
                            bean.setBrand_id(gymWrapper.brand_id());
                            if (bean.getShops() != null && bean.getShops().size() > 0) {
                                String support = "";
                                String support_ids = "";
                                for (int i = 0; i < bean.getShops().size(); i++) {
                                    Shop shop = bean.getShops().get(i);
                                    support = TextUtils.concat(support, shop.name).toString();
                                    support_ids = TextUtils.concat(support_ids, shop.id).toString();
                                    if (i != bean.getShops().size() - 1) {
                                        support = TextUtils.concat(support, ",").toString();
                                        support_ids = TextUtils.concat(support_ids, ",").toString();
                                    }
                                }
                                bean.setSupoort_gym_ids(support_ids);
                                bean.setSupport_gym(support);
                            }
                        }
                    }
                    return true;
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean b) {
                    if (b) {
                        view.onStopFresh();
                    } else {
                        view.onFaied();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFaied();
                }
            })

        );
    }

    private void handleData(List<QcStudentBean> data, String brand_id, String gymid, String gymmodel) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            bean.brandid = brand_id;
            bean.modelid = gymid;
            bean.model = gymmodel;
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        if (view != null) view.onStudentList(studentBeanList);
    }

    private void handleFilter(List<QcStudentBean> data, String brand_id, String gymid, String gymmodel) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            bean.brandid = brand_id;
            bean.modelid = gymid;
            bean.model = gymmodel;
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        if (view != null) view.onFilterStudentList(studentBeanList);
    }

    public void filter(String keyword, String brandid) {
        spFilter = StudentAction.newInstance()
            .getStudentByKeyWord(keyword, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> qcStudentBeen) {
                    handleFilter(qcStudentBeen, "", "", "");
                }
            })

        ;
    }

    public void filter(String keyword, String id, String model) {
        spFilter = StudentAction.newInstance()
            .getStudentByKeyWord(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<QcStudentBean>>() {
                @Override public void call(List<QcStudentBean> qcStudentBeen) {
                    handleFilter(qcStudentBeen, "", "", "");
                }
            })

        ;
    }

    public void fixBundleStudent(String users, String id, String model) {
        FixCard card = new FixCard();
        card.user_ids = users;
        RxRegiste(restRepository.getPost_api()
            .qcUndateCard(App.staffId, realCard.id(), null, id, model, card)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    } else {
                        view.onFaied();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }
}
