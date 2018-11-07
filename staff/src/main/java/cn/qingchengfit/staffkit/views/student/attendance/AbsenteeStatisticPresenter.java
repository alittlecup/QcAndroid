package cn.qingchengfit.staffkit.views.student.attendance;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.common.Absentce;
import cn.qingchengfit.model.common.Absentces;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.GymUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AbsenteeStatisticPresenter extends BasePresenter {
    int left = 7, right = 30, pages = 2, curPage = 1;
    private MVPView view;
    private Brand brand;
    private CoachService coachService;
    private StaffRespository restRepository;

    @Inject public AbsenteeStatisticPresenter(Brand brand, CoachService coachService, StaffRespository restRepository) {
        this.brand = brand;
        this.coachService = coachService;
        this.restRepository = restRepository;
    }

    /**
     * 下拉刷新
     */
    public void refreshData() {
        curPage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        getAbsentceList();
    }

    /**
     * 筛选空间  为空时传0
     *
     * @param left 大于left
     * @param right 小于right
     */
    public void setPeriod(int left, int right) {
        this.left = left;
        this.right = right;
        curPage = 1;
        pages = 1;
        if (view != null) view.clearDatas();
        getAbsentceList();
    }

    /**
     * 获取缺勤数据 使用前必须先设置
     */
    public void getAbsentceList() {
        HashMap<String, Object> params = GymUtils.getParams(coachService, brand);
        if (left == 0) {
            params.put("absence__lt", right);//大于左边
        } else if (right == 0) {
            params.put("absence__ge", left);//小于右边
        } else {
            params.put("absence__get", left);//双边限制
            params.put("absence__lte", right);
        }
        if (curPage <= pages) {

            params.put("page", curPage);
            RxRegiste(restRepository.getStaffAllApi()
                .qcGetUsersAbsences(App.staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<QcDataResponse<Absentces>, Boolean>() {
                    @Override public Boolean call(QcDataResponse<Absentces> absentcesQcResponseData) {
                        return absentcesQcResponseData.getData() != null;
                    }
                })
                .subscribe(new Action1<QcDataResponse<Absentces>>() {
                    @Override public void call(QcDataResponse<Absentces> qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            view.onAbsenteeList(qcResponse.getData().attendances);
                            curPage++;
                            //pages = qcResponse.pages;todo
                        } else {
                            view.onShowError(qcResponse.getMsg());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        view.onShowError(throwable.getMessage());
                    }
                }));
        } else {
            view.onNoMore();
        }
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onAbsenteeList(List<Absentce> absentces);

        void onNoMore();

        void clearDatas();
    }
}
