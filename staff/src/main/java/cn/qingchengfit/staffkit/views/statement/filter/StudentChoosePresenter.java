package cn.qingchengfit.staffkit.views.statement.filter;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
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
 * Created by Paper on 16/6/29 2016.
 */
public class StudentChoosePresenter extends BasePresenter {

    StatementUsecase usecase;
    Brand brand;
    CoachService coachService;
    StudentChooseView view;

    @Inject public StudentChoosePresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void attachView(PView v) {
        view = (StudentChooseView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryStudents() {
        //        RxRegiste(usecase.queryCoach(brand.getId(), coachService.getId()
        //                , coachService.getModel(), new Action1<Staffs>() {
        //                    @Override
        //                    public void call(Staffs qcResponseGymCoach) {
        //                        if (ResponseConstant.checkSuccess(qcResponseGymCoach)){
        //                            view.onCoaches(qcResponseGymCoach.data.teachers);
        //                        }
        //                    }
        //                }));
    }
}
