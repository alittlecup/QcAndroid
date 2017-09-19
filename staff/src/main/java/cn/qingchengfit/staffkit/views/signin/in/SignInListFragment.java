package cn.qingchengfit.staffkit.views.signin.in;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.rxbus.event.SignInEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInLogEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInManualEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInQrCodeEvent;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.SignInFooterItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.SignInItem;
import cn.qingchengfit.staffkit.views.adapter.SignInFlexibleAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseWardrobeActivity;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.SnackbarUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentDialog;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 签到列表
 * Created by yangming on 16/8/25.
 */
public class SignInListFragment extends BaseFragment implements SignInListPresenter.SignInListView {

    public final static int RESULT_LOCKER = 5;
    @BindView(R.id.ll_signin_qrcode) LinearLayout llSigninQrcode;
    @BindView(R.id.tv_signin_manual) TextView tvSigninManual;
    @BindView(R.id.ll_signin_manual) LinearLayout llSigninManual;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
    SignInFlexibleAdapter flexibleAdapter;
    @BindView(R.id.tv_signin_qrcode) TextView tvSigninQrcode;
    @BindView(R.id.tv_signin_header) TextView tvSigninHeader;
    @BindView(R.id.tv_signin_footer) TextView tvSigninFooter;
    List<AbstractFlexibleItem> items = new ArrayList<>();
    List<SignInTasks.SignInTask> sourceList = new ArrayList<>();
    List<SignInTasks.SignInTask> newList = new ArrayList<>();
    @Inject SignInListPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    private LinearLayoutManager mLinearLayoutManager;
    private Subscription subscribe_auto;
    private int mLastId = 0;//最新一条任务的id
    private Locker selectedLocker;//选择的更衣柜
    private int mLockerPos = 0;
    private String mStudentId;

    public SignInListFragment() {
    }

    public static SignInListFragment newInstance() {
        SignInListFragment fragment = new SignInListFragment();
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initView();
        presenter.queryData();
        initRxBus();

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        if (subscribe_auto != null && !subscribe_auto.isUnsubscribed()) {
            subscribe_auto.unsubscribe();
            subscribe_auto = null;
        }
        super.onDestroyView();
    }

    private void initView() {
        tvSigninHeader.setVisibility(View.GONE);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_byhand);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvSigninManual.setCompoundDrawables(drawable, null, null, null);

        Drawable drawableQr = getResources().getDrawable(R.drawable.ic_sign_in_qrcode);
        drawableQr.setBounds(0, 0, drawableQr.getMinimumWidth(), drawableQr.getMinimumHeight());
        tvSigninQrcode.setCompoundDrawables(drawableQr, null, null, null);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.addItemDecoration(new SpaceItemDecoration(20, getContext()));
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryData();
            }
        });
    }

    private void initRxBus() {
      RxBusAdd(SignInEvent.class).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SignInEvent>() {
                @Override public void call(SignInEvent signInEvent) {
                    switch (signInEvent.getAction()) {
                        case SignInEvent.ACTION_SIGNIN_IGNOR://忽略
                            showLoading();
                            presenter.ignor(signInEvent.getPosition(), signInEvent.getCheckInId());
                            break;
                        case SignInEvent.ACTION_SIGNIN_CONFIRM://确认
                            showLoading();
                            presenter.confirm(signInEvent.getPosition(), signInEvent.getCheckInId(),
                                selectedLocker == null ? 0 : selectedLocker.id);
                            break;
                        case SignInEvent.ACTION_SIGNIN_LOCKER://更衣柜
                            mLockerPos = signInEvent.getPosition();
                            Intent chooseLocker = new Intent(getActivity(), ChooseWardrobeActivity.class);
                            chooseLocker.putExtra("locker", selectedLocker);
                            startActivityForResult(chooseLocker, RESULT_LOCKER);
                            break;
                        case SignInEvent.ACTION_SIGNIN_ADD_IMG:
                            if (items.size() > signInEvent.getPosition()) {
                                String imgUrl = ((SignInItem) items.get(signInEvent.getPosition())).bean.getCheckinAvatar();
                                if (StringUtils.isEmpty(imgUrl)) {
                                    if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                                        showAlert(R.string.alert_permission_forbid);
                                        return;
                                    }
                                    mStudentId = ((SignInItem) items.get(signInEvent.getPosition())).bean.getUserId();
                                    ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true, 1);
                                    dialog.show(getFragmentManager(), "");
                                } else {
                                    SimpleImgDialog.newInstance(imgUrl).show(getFragmentManager(), "");
                                }
                            }
                            break;
                    }
                }
            });
        RxBusAdd(EventChooseImage.class).subscribe(new Action1<EventChooseImage>() {
            @Override public void call(EventChooseImage eventChooseImage) {
                if (eventChooseImage.request != 1) return;
                showLoading();
                RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            presenter.changeImage(App.staffId, s, mStudentId);
                            StudentAction.newInstance().updateStudentCheckin(mStudentId, s);
                            hideLoading();
                        }
                    }));
            }
        });
    }

    @Override public void onData(List<SignInTasks.SignInTask> list) {
        //延时3000 ，每间隔3000，时间单位
        if (subscribe_auto == null) {
            subscribe_auto = Observable.interval(3, 3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        presenter.interval(mLastId);
                    }
                });
        }

        this.sourceList = list;
        // 下拉刷新后,无数据时,移除所有 item
        if (list == null || list.size() == 0) {
            if (flexibleAdapter != null && flexibleAdapter.getItemCount() > 0) {
                do {
                    flexibleAdapter.removeItem(0);
                } while (flexibleAdapter.getItemCount() > 0);
            }
            recycleview.stopLoading();
            recycleview.setNoData(true);
            tvSigninFooter.setVisibility(View.VISIBLE);
            return;
        }

        items.clear();
        mLastId = sourceList.get(0).getId();
        for (SignInTasks.SignInTask signInTask : sourceList) {
            items.add(new SignInItem(signInTask));
        }

        flexibleAdapter = new SignInFlexibleAdapter(items);
        flexibleAdapter.addItem(flexibleAdapter.getItemCount(), new SignInFooterItem());
        recycleview.setAdapter(flexibleAdapter);
        recycleview.stopLoading();
        recycleview.setNoData(items.size() == 1);

        tvSigninHeader.setVisibility(View.GONE);
        tvSigninFooter.setVisibility(View.GONE);
        selectedLocker = null;
        mLockerPos = 0;
    }

    @Override public void onInterval(List<SignInTasks.SignInTask> list) {
        // 筛除忽略的
        if (list != null && !list.isEmpty() && list.size() > 0) {
            //有新数据
            tvSigninHeader.setVisibility(View.VISIBLE);
            tvSigninHeader.setText(list.size() + "条未处理签到,请点击查看");
            newList.clear();
            for (SignInTasks.SignInTask signInTask : list) {
                newList.add(signInTask);
            }
        } else {
            tvSigninHeader.setVisibility(View.GONE);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOCKER) {
                if (data != null) {
                    Locker locker = data.getParcelableExtra("locker");
                    if (locker != null) {
                        ((SignInItem) flexibleAdapter.getItem(mLockerPos)).bean.setLocker(locker);
                        selectedLocker = locker;
                        flexibleAdapter.notifyItemChanged(mLockerPos);
                    }
                }
            }
        }
    }

    @Override public void ignorComplete(final int position) {
        hideLoading();
        removeItem(position);
    }

    @Override public void confirComplete(final int position) {
        hideLoading();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((SignInItem) flexibleAdapter.getItem(position)).bean.getUserName()).append("签到成功");
        SnackbarUtils.showSnackBar(this.getView(), stringBuffer.toString());
        removeItem(position);
    }

    @Override public void confirmFail(int position, String erroCode, String msg) {
        hideLoading();
        if (TextUtils.isEmpty(erroCode) || TextUtils.isEmpty(msg)) {
            ToastUtils.show("操作失败,请检查网络,再重试");
        } else {
            SnackbarUtils.showSnackBar(this.getView(), msg);
            if ("501001".equals(erroCode)) {
                removeItem(position);
            }
        }
    }

    @Override public void onImageChangeSuccess() {
        presenter.queryData();
    }

    public void removeItem(final int position) {
        flexibleAdapter.removeItem(position);
        if (flexibleAdapter.getItemCount() == 1) {
            flexibleAdapter.removeItem(0);
            tvSigninFooter.setVisibility(View.VISIBLE);
        }
        recycleview.setNoData(flexibleAdapter.isEmpty());
        selectedLocker = null;
        mLockerPos = 0;
    }

    @OnClick({ R.id.ll_signin_qrcode, R.id.ll_signin_manual, R.id.tv_signin_header, R.id.tv_signin_footer })
    public void onClick(View view) {
        if (!PreferenceUtils.getPrefBoolean(getContext(), "showNotice" + App.staffId, false)) {
            ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
            return;
        }
        switch (view.getId()) {
            case R.id.ll_signin_qrcode:
                RxBus.getBus().post(new SignInQrCodeEvent(true));
                break;
            case R.id.ll_signin_manual:
                SignInManualEvent signInManualEvent = new SignInManualEvent();
                signInManualEvent.setCancel(false);
                signInManualEvent.setSignIn(true);
                RxBus.getBus().post(signInManualEvent);
                break;
            case R.id.tv_signin_header:
                tvSigninHeader.setVisibility(View.GONE);
                if (flexibleAdapter == null) {
                    flexibleAdapter = new SignInFlexibleAdapter(new ArrayList<AbstractFlexibleItem>());
                    recycleview.setAdapter(flexibleAdapter);
                }
                if (newList.size() > 0) {
                    for (SignInTasks.SignInTask signInTask : newList) {
                        flexibleAdapter.addItem(0, new SignInItem(signInTask));
                    }
                    if (newList.size() == flexibleAdapter.getItemCount()) {//添加 footer View
                        flexibleAdapter.addItem(flexibleAdapter.getItemCount(), new SignInFooterItem());
                    }
                    mLinearLayoutManager.scrollToPosition(0);
                    mLastId = newList.get(0).getId();
                }
                newList.clear();
                recycleview.setNoData(flexibleAdapter.isEmpty());
                tvSigninFooter.setVisibility(View.GONE);
                break;
            case R.id.tv_signin_footer:
                RxBus.getBus().post(new SignInLogEvent());
                break;
        }
    }

    @Override public void onShowError(String e) {
    }

    @Override public void onShowError(@StringRes int e) {
    }
}
