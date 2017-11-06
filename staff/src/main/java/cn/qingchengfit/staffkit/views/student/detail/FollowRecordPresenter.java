package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.body.AddFollowRecordBody;
import cn.qingchengfit.model.responese.FollowRecord;
import cn.qingchengfit.model.responese.FollowRecords;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */
public class FollowRecordPresenter extends BasePresenter {

    FollowRecordView view;
    @Inject StudentWrapper studentBase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentUsecase usecase;
    private int page = 1, total = 2;
    private Subscription spAdd;
    private Subscription spQuery;

    @Inject public FollowRecordPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (FollowRecordView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spAdd != null) spAdd.unsubscribe();
        if (spQuery != null) spQuery.unsubscribe();
    }

    public void initPage() {
        page = 1;
    }

    public void queryData() {
        if (page > total) {
            view.onData(new ArrayList<FollowRecord>(), 0);
            return;
        }
        spQuery = usecase.queryFollowRecord(studentBase.id(), gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), page,
            new Action1<QcDataResponse<FollowRecords>>() {
                @Override public void call(QcDataResponse<FollowRecords> qcResponseClassRecords) {
                    total = qcResponseClassRecords.data.pages;
                    view.onData(qcResponseClassRecords.data.records, page);

                    page++;
                }
            });
    }

    public void addFollow(String msg, String img) {
        AddFollowRecordBody body = new AddFollowRecordBody();
        body.attachment = img;
        body.content = msg;
        spAdd = usecase.addFollowRecord(studentBase.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), body,
            new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onAdd();
                    } else {
                        // ToastUtils.logHttp(qcResponse);
                    }
                }
            });
    }
}
