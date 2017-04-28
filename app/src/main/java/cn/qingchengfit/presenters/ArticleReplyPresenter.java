package cn.qingchengfit.presenters;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.model.responese.ArticleCommentListData;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import com.anbillon.qcmvplib.CView;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.di.BasePresenter;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ArticleReplyPresenter extends BasePresenter {
    private MVPView view;

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @Inject public ArticleReplyPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }
    public void queryReplies(int page){
        HashMap<String,Object> params = new HashMap<>();
        params.put("page",page);
        RxRegiste(restRepository.getGet_api().qcQueryReplies(params)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<ArticleCommentListData>>() {
                @Override public void call(QcResponseData<ArticleCommentListData> articleCommentListDataQcResponseData) {
                    if (ResponseConstant.checkSuccess(articleCommentListDataQcResponseData)){
                        view.onArticleReplies(articleCommentListDataQcResponseData.getData().comments,articleCommentListDataQcResponseData.data.current_page);
                        view.onTotalCount(articleCommentListDataQcResponseData.data.total_count,articleCommentListDataQcResponseData.data.pages);

                    }else view.onShowError(articleCommentListDataQcResponseData.getMsg());
                }
            },new NetWorkThrowable())
        );
    }
    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onArticleReplies(List<ArticleComment> list, int page);
        void onTotalCount(int count, int pages);
    }
}
