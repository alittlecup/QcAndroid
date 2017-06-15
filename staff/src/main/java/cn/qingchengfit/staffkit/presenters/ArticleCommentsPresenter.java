package cn.qingchengfit.staffkit.presenters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.body.PostCommentBody;
import cn.qingchengfit.model.responese.ArticleComment;
import cn.qingchengfit.model.responese.ArticleCommentListData;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.NetWorkThrowable;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ArticleCommentsPresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public ArticleCommentsPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void addComment(String newsid, @Nullable String reply_id, String text) {
        RxRegiste(restRepository.getPost_api()
            .qcAddComment(newsid, new PostCommentBody.Builder().text(text).reply_id(TextUtils.isEmpty(reply_id) ? null : reply_id).build())
            .observeOn(AndroidSchedulers.mainThread())
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
        RxRegiste(restRepository.getGet_api()
            .qcQueryComments(newsid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<ArticleCommentListData>>() {
                @Override public void call(QcResponseData<ArticleCommentListData> qcResponse) {
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
