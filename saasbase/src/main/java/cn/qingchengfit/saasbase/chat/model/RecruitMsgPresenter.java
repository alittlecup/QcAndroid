package cn.qingchengfit.saasbase.chat.model;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.apis.ChatApis;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecruitMsgPresenter extends BasePresenter {
    @Inject QcRestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public RecruitMsgPresenter() {
    }



    public void queryRecruitMsgList(){
        RxRegiste(restRepository.createGetApi(ChatApis.class)
            .qcGetRecruitMessageList()
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(recordWrapQcResponseData -> {
                if (ResponseConstant.checkSuccess(recordWrapQcResponseData)){
                    view.onMessageList(recordWrapQcResponseData.data.records);
                }else{
                    view.onShowError(recordWrapQcResponseData.getMsg());
                }
            }, new NetWorkThrowable()));
    }


    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {


        void onMessageList(List<Record> recordList);
    }
}
