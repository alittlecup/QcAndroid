package cn.qingchengfit.saasbase.article.presenter;

import cn.qingchengfit.saasbase.apis.ArticleApis;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticleReplyPresenter extends BasePresenter {
    @Inject QcRestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public ArticleReplyPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void queryReplies(int page) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", page);
        RxRegiste(restRepository.createGetApi(ArticleApis.class)
            .qcQueryReplies(params).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(articleCommentListDataQcResponseData -> {
                if (ResponseConstant.checkSuccess(articleCommentListDataQcResponseData)) {
                    view.onArticleReplies(articleCommentListDataQcResponseData.getData().comments,
                        articleCommentListDataQcResponseData.data.current_page);
                    view.onTotalCount(articleCommentListDataQcResponseData.data.total_count,
                        articleCommentListDataQcResponseData.data.pages);
                } else {
                    view.onShowError(articleCommentListDataQcResponseData.getMsg());
                }
            }, new NetWorkThrowable()));
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
