package cn.qingchengfit.staffkit.views.wardrobe.choose;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.List;
import javax.inject.Inject;

public class SearchResultPresenter extends BasePresenter {
    private PView view;
    private CoachService coachService;
    private StaffRespository restRepository;

    @Inject public SearchResultPresenter(CoachService coachService, StaffRespository restRepository) {
        this.coachService = coachService;
        this.restRepository = restRepository;
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void serarch(String staffid, String keyword) {

    }

    public interface MVPView extends CView {
        void onList(List<Locker> lockers);
    }
}
