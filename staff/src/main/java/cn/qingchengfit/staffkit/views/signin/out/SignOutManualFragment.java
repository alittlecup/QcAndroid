package cn.qingchengfit.staffkit.views.signin.out;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.rxbus.event.EventChooseImage;
import cn.qingchengfit.staffkit.rxbus.event.SignInEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignOutManualEvent;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.SignOutItem;
import cn.qingchengfit.staffkit.views.adapter.SignInFlexibleAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.utils.SnackbarUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangming on 16/8/29.
 */
public class SignOutManualFragment extends BaseFragment implements SignOutManualPresenter.SignOutManualView {
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;

    @Inject SignOutManualPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentWrapper;

    List<SignInTasks.SignInTask> list;
    List<AbstractFlexibleItem> items = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private SignInFlexibleAdapter flexibleAdapter;

    @Inject public SignOutManualFragment() {
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout_manual, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        mCallbackActivity.setToolbar("手动签出", false, null, 0, null);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview.addItemDecoration(new SpaceItemDecoration(20, getContext()));
        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.querySignOutList();
            }
        });
        presenter.querySignOutList();

        RxBusAdd(SignOutManualEvent.class).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SignOutManualEvent>() {
                @Override public void call(SignOutManualEvent signOutManualEvent) {
                    if (signOutManualEvent.getType() == SignInEvent.ACTION_SIGNOUT_MANUAL) {
                        //添加照片
                        if (StringUtils.isEmpty(presenter.studentWrapper.checkin_avatar())) {
                            if (!SerPermisAction.check(gymWrapper.id(), gymWrapper.model(),
                                PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                                showAlert(R.string.alert_permission_forbid);
                                return;
                            }
                            ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true);
                            dialog.show(getFragmentManager(), "");
                        } else {
                            SimpleImgDialog.newInstance(presenter.studentWrapper.checkin_avatar()).show(getFragmentManager(), "");
                        }
                    } else {
                        //确认
                        showLoading();
                        presenter.confirm(signOutManualEvent.getPosition(), signOutManualEvent.getCheckInId());
                    }
                }
            });
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            presenter.changeImage(s);
                            StudentAction.newInstance().updateStudentCheckin(studentWrapper.id(), s);
                        }
                    }));
            }
        });
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onData(List<SignInTasks.SignInTask> list) {
        recycleview.stopLoading();
        this.list = list;
        if (list == null || list.isEmpty()) {
            recycleview.setNoData(true);
            return;
        }
        recycleview.setNoData(false);

        items.clear();
        for (SignInTasks.SignInTask signInTask : list) {
            items.add(new SignOutItem(signInTask, true));
        }
        flexibleAdapter = new SignInFlexibleAdapter(items);
        recycleview.setAdapter(flexibleAdapter);
        recycleview.stopLoading();
        recycleview.setNoData(flexibleAdapter.isEmpty());
    }

    @Override public void confirmSignOut(int position) {
        hideLoading();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((SignOutItem) flexibleAdapter.getItem(position)).bean.getUserName()).append("签出成功");
        SnackbarUtils.showSnackBar(this.getView(), stringBuffer.toString());
        flexibleAdapter.removeItem(position);
        recycleview.setNoData(flexibleAdapter.isEmpty());
    }

    @Override public void onConfirmFail() {
        hideLoading();
        ToastUtils.show("操作失败,请检查网络,再重试");
    }

    @Override public void onImageChangeSuccess() {
        presenter.querySignOutList();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }
}
