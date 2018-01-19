package cn.qingchengfit.saasbase.coach.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 16/5/11 2016.
 */
public class CoachListPresenter extends BasePresenter<CoachListPresenter.MvpView> {
    @Inject IStaffModel staffModel;

    @Inject public CoachListPresenter() {}



    public void filter(String keyword) {

    }

    public void queryData( String keyword) {
        //sp = useCase.getAllCoach(staffId, gymWrapper.id(), gymWrapper.model(), keyword, new Action1<QcResponseData<Staffs>>() {
        //    @Override public void call(QcResponseData<Staffs> qcResponseGymCoach) {
        //        if (qcResponseGymCoach.getStatus() == ResponseConstant.SUCCESS) {
        //            view.onList(qcResponseGymCoach.data.teachers);
        //        } else {
        //            // ToastUtils.logHttp(qcResponseGymCoach);
        //            view.onFailed();
        //        }
        //    }
        //});
    }

    public interface MvpView extends CView {

        void onList(List<Staff> coach);

        void onFailed();

        void goDetail(Staff coach);
    }
}
