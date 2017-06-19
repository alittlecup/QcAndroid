package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import javax.inject.Inject;

public class ResumeIntentsPresenter extends BasePresenter {
    @Inject QcRestRepository qcRestRepository;
    private MVPView view;

    @Inject public ResumeIntentsPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onIntents();
    }
}
