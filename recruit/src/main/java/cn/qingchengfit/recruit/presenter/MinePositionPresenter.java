package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import javax.inject.Inject;

public class MinePositionPresenter extends BasePresenter {

    private MVPView view;
    @Inject public MinePositionPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    /**
     * 收藏的职位
     */
    public void queryPositionOfCollect(){}

    /**
     * 我投递的职位
     */
    public void queryPositionOfSent(){}

    /**
     * 被邀请的职位
     */
    public void queryPositionOfInvited(){}


    public interface MVPView extends CView {

    }
}
