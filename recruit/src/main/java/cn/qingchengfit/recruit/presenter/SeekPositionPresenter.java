package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import javax.inject.Inject;

public class SeekPositionPresenter extends BasePresenter {
    protected MVPView view;


    @Inject public SeekPositionPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    /**
     * 查询职位列表（分页）
     */
    public void queryList(){

    }

    /**
     * 查询职位详情
     */
    public void queryDetail(){

    }

    /**
     * 联系HR
     */
    public void contactHR(){

    }

    /**
     * 收藏职位
     */
    public void collectPosition(){

    }

    /**
     * 投递简历
     */
    public void sendResume(){

    }



    public interface MVPView extends CView {

    }
}
