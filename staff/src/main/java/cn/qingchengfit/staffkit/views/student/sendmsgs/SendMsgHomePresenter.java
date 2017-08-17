package cn.qingchengfit.staffkit.views.student.sendmsgs;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import javax.inject.Inject;

public class SendMsgHomePresenter extends BasePresenter {
    @Inject Brand brand;
    @Inject CoachService coachService;
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public SendMsgHomePresenter() {
    }

    public void queryScripts() {
    }

    public void querySendedMsg() {

    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
    }
}