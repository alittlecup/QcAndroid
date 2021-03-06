package cn.qingchengfit.saasbase.article.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.saasbase.apis.ArticleApis;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.PostCommentBody;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.model.responese.ArticleCommentListData;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ArticleCommentsPresenter extends BasePresenter {
    @Inject QcRestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public ArticleCommentsPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void addComment(String newsid, @Nullable String reply_id, String text) {
        RxRegiste(restRepository.createRxJava1Api(ArticleApis.class)
            .qcAddComment(newsid, new PostCommentBody.Builder().text(text).reply_id(TextUtils.isEmpty(reply_id) ? null : reply_id).build())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onAddCommentSuccess();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public void queryCommenList(String newsid, int page) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", page);
        RxRegiste(restRepository.createRxJava1Api(ArticleApis.class)
            .qcQueryComments(newsid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<ArticleCommentListData>>() {
                @Override public void call(QcDataResponse<ArticleCommentListData> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onCommentList(qcResponse.getData().comments, qcResponse.data.current_page);
                        view.onCommentTotal(qcResponse.data.total_count, qcResponse.data.pages);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onAddCommentSuccess();

        void onCommentList(List<ArticleComment> comments, int curPage);

        void onCommentTotal(int count, int page);
    }
}
