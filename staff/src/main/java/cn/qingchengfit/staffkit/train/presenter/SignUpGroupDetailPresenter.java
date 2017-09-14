package cn.qingchengfit.staffkit.train.presenter;

import android.text.TextUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.train.model.CreateGroupBody;
import cn.qingchengfit.staffkit.train.model.OperationMemberBody;
import cn.qingchengfit.staffkit.train.model.TeamDetailResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/28.
 */

public class SignUpGroupDetailPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    private SignUpView signUpView;

    @Inject public SignUpGroupDetailPresenter() {
    }

    @Override public void attachView(PView v) {
        signUpView = (SignUpView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        signUpView = null;
    }

    public void queryGroupDetail(final String teamId) {
        RxRegiste(restRepository.getGet_api()
            .qcGetGroupDetail(teamId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<TeamDetailResponse>>() {
                @Override public void call(QcDataResponse<TeamDetailResponse> teamDetail) {
                    if (ResponseConstant.checkSuccess(teamDetail)) {
                        if (signUpView != null) {
                            signUpView.onGetSignUpDataSuccess(teamDetail.data.groupBean);
                        }
                    } else {
                        signUpView.onFailed(teamDetail.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    //删除分组
    public void deleteGroup(String teamId) {

        RxRegiste(restRepository.getPost_api()
            .qcDeleteGroup(teamId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        signUpView.onSuccess();
                        signUpView.onDelSuccess();
                    } else {
                        signUpView.onFailed(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public void createGroup(String name, String competition, String gymId, List<QcStudentBean> userIds) {

        CreateGroupBody createGroupBody = new CreateGroupBody();
        createGroupBody.setGym_id(gymId);
        createGroupBody.setCompetition_id(competition);
        createGroupBody.setName(name);
        createGroupBody.setUser_ids(handleData(userIds));

        RxRegiste(restRepository.getPost_api()
            .qcPostCreateGroup(createGroupBody).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        signUpView.onSuccess();
                    } else {
                        signUpView.onFailed(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public List<Integer> handleData(List<QcStudentBean> list) {
        List<Integer> userIds = new ArrayList<>();
        for (QcStudentBean bean : list) {
            userIds.add(Integer.valueOf(bean.id()));
        }

        return userIds;
    }

    //修改分组名称 & 添加，删除群成员
    public void operationGroup(String teamId, String name, List<String> users) {

        OperationMemberBody operationMemberBody = new OperationMemberBody();

        if (!TextUtils.isEmpty(name)) {
            operationMemberBody.setName(name);
        }

        if (users != null && users.size() != 0) {
            operationMemberBody.setUseridList(users);
        }

        RxRegiste(restRepository.getPost_api()
            .qcPostMemberOperation(teamId, operationMemberBody)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        signUpView.onSuccess();
                    } else {
                        signUpView.onFailed(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }
}
