package cn.qingchengfit.staffkit.views.signin.out;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.rxbus.event.SignInEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInLogEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInManualEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInQrCodeEvent;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.SignInFooterItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.SignOutItem;
import cn.qingchengfit.staffkit.views.adapter.SignInFlexibleAdapter;
import cn.qingchengfit.staffkit.views.custom.SimpleImgDialog;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
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
 * 签出列表
 * Created by yangming on 16/8/25.
 */
public class SignOutListFragment extends BaseFragment implements SignOutListPresenter.SignOutListView {

    @BindView(R.id.ll_signin_qrcode) LinearLayout llSigninQrcode;

    @BindView(R.id.ll_signin_manual) LinearLayout llSigninManual;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.tv_signin_qrcode) TextView tvSigninQrcode;
    @BindView(R.id.tv_signin_manual) TextView tvSigninManual;

    @BindView(R.id.tv_signin_header) TextView tvSigninHeader;
    @BindView(R.id.tv_signin_footer) TextView tvSigninFooter;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeRefreshLayout;
    List<AbstractFlexibleItem> items = new ArrayList<>();
    List<SignInTasks.SignInTask> sourceList = new ArrayList<>();
    List<SignInTasks.SignInTask> newList = new ArrayList<>();
    @Inject SignOutListPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject StudentAction studentAction;
    private LinearLayoutManager mLinearLayoutManager;
    private SignInFlexibleAdapter flexibleAdapter;
    private Subscription subscribe_auto;
    private String mLastModifyAt = "";//最新一条任务的modifyAt
    private String mStudentId;

    public SignOutListFragment() {
    }

    public static SignOutListFragment newInstance() {
        SignOutListFragment fragment = new SignOutListFragment();
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        tvSigninQrcode.setText("签出二维码");
        tvSigninManual.setText("手动签出");
        initView();
        presenter.queryData();
        initRxBus();

        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignOutListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        if (subscribe_auto != null && !subscribe_auto.isUnsubscribed()) {
            subscribe_auto.unsubscribe();
            subscribe_auto = null;
        }
    }

    private void initView() {
        tvSigninHeader.setVisibility(View.GONE);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_check_byhand);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvSigninManual.setCompoundDrawables(drawable, null, null, null);

        Drawable drawableQr = getResources().getDrawable(R.drawable.ic_sign_in_qrcode);
        drawableQr.setBounds(0, 0, drawableQr.getMinimumWidth(), drawableQr.getMinimumHeight());
        tvSigninQrcode.setCompoundDrawables(drawableQr, null, null, null);

        flexibleAdapter = new SignInFlexibleAdapter(items);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(mLinearLayoutManager);
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        //recycleview.setNodataHint("暂无未处理签出");
        recycleview.addItemDecoration(new SpaceItemDecoration(20, getContext()));
        recycleview.setAdapter(flexibleAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        case SignInEvent.ACTION_SIGNOUT_IGNOR://忽略
                            showLoading();
                            presenter.ignor(signInEvent.getPosition(), signInEvent.getCheckInId());
                            break;
                        case SignInEvent.ACTION_SIGNOUT_CONFIRM://确认
                            showLoading();
                            presenter.confirm(signInEvent.getPosition(), signInEvent.getCheckInId());
                            break;
                        case SignInEvent.ACTION_SIGNOUT_ADD_IMG:
                            if (items.size() > signInEvent.getPosition()) {
                                String imgUrl = ((SignOutItem) items.get(signInEvent.getPosition())).bean.getCheckinAvatar();
                                if (StringUtils.isEmpty(imgUrl)) {
                                    if (!serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
                                        PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                                        showAlert(R.string.alert_permission_forbid);
                                        return;
                                    }
                                    mStudentId = ((SignOutItem) items.get(signInEvent.getPosition())).bean.getUserId();
                                    ChoosePictureFragmentDialog dialog = ChoosePictureFragmentDialog.newInstance(true, 2);
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
                if (eventChooseImage.request != 2) return;
                RxRegiste(UpYunClient.rxUpLoad("/signin/", eventChooseImage.filePath)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override public void call(String s) {
                            presenter.changeImage(App.staffId, s, mStudentId);
                            studentAction.updateStudentCheckin(mStudentId, s);
                            hideLoading();
                        }
                    }));
            }
        });
    }

    @Override public void onData(List<SignInTasks.SignInTask> list) {
        //延时3000 ，每间隔3  000，时间单位
        if (subscribe_auto == null) {
            subscribe_auto = Observable.interval(3, 3, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        presenter.interval(mLastModifyAt);
                    }
                });
        }

        this.sourceList = list;
        // 下拉刷新后,无数据时,移除所有 item
        if (list == null || list.size() == 0) {
            if (flexibleAdapter != null) {
                flexibleAdapter.clear();
            }
            swipeRefreshLayout.setRefreshing(false);
            setNoData();
            tvSigninFooter.setVisibility(View.VISIBLE);
            return;
        }

        if(flexibleAdapter != null) flexibleAdapter.clear();
        mLastModifyAt = sourceList.get(0).getModifyAt();
        items.clear();
        for (SignInTasks.SignInTask signInTask : list) {
            items.add(new SignOutItem(signInTask));
        }
        items.add(new SignInFooterItem());
        flexibleAdapter.updateDataSet(items);
        swipeRefreshLayout.setRefreshing(false);
        //recycleview.setNoData(flexibleAdapter.isEmpty());
        setNoData();
        tvSigninHeader.setVisibility(View.GONE);
        tvSigninFooter.setVisibility(View.GONE);
    }

    @Override public void onInterval(List<SignInTasks.SignInTask> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            //有新数据
            tvSigninHeader.setVisibility(View.VISIBLE);
            tvSigninHeader.setText(list.size() + "条未处理签出,请点击查看");
            newList.clear();
            for (SignInTasks.SignInTask signInTask : list) {
                newList.add(signInTask);
            }
        } else {
            tvSigninHeader.setVisibility(View.GONE);
        }
    }

    @Override public void ignorComplete(final int position) {
        hideLoading();
        removeItem(position);
    }

    @Override public void confirComplete(final int position) {
        hideLoading();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(((SignOutItem) flexibleAdapter.getItem(position)).bean.getUserName()).append("签出成功");
        SnackbarUtils.showSnackBar(this.getView(), stringBuffer.toString());
        removeItem(position);
    }

    @Override public void onConfirmFail(int position, String erroCode, String msg) {
        hideLoading();
        if (TextUtils.isEmpty(erroCode) || TextUtils.isEmpty(msg)) {
            ToastUtils.show("操作失败,请检查网络,再重试");
        } else {
            SnackbarUtils.showSnackBar(this.getView(), msg);
            if ("502001".equals(erroCode)) {
                removeItem(position);
            }
        }
    }

    @Override public void onImageChangeSuccess() {
        hideLoading();
        presenter.queryData();
    }

    public void removeItem(final int position) {
        flexibleAdapter.removeItem(position);
        if (flexibleAdapter.getItemCount() == 1) {
            flexibleAdapter.removeItem(0);
            tvSigninFooter.setVisibility(View.VISIBLE);
        }
        //recycleview.setNoData(flexibleAdapter.isEmpty());
        setNoData();
    }

    @OnClick({ R.id.ll_signin_qrcode, R.id.ll_signin_manual, R.id.tv_signin_header, R.id.tv_signin_footer })
    public void onClick(View view) {
        if (!PreferenceUtils.getPrefBoolean(getContext(), "showNotice" + App.staffId, false)) {
            ToastUtils.show("还没有设置进场签到设置,暂时无法操作");
            return;
        }
        switch (view.getId()) {
            case R.id.ll_signin_qrcode:
                RxBus.getBus().post(new SignInQrCodeEvent(false));
                break;
            case R.id.ll_signin_manual:
                SignInManualEvent signInManualEvent = new SignInManualEvent();
                signInManualEvent.setSignIn(false);
                signInManualEvent.setCancel(false);
                RxBus.getBus().post(signInManualEvent);
                break;
            case R.id.tv_signin_header:
                tvSigninHeader.setVisibility(View.GONE);
                if (flexibleAdapter == null) {
                    flexibleAdapter = new SignInFlexibleAdapter(new ArrayList<AbstractFlexibleItem>());
                    recycleview.setAdapter(flexibleAdapter);
                }
                if (newList.size() > 0) {
                    if (flexibleAdapter.getItemCount() == 1 && flexibleAdapter.getItem(0) instanceof CommonNoDataItem){
                        flexibleAdapter.clear();
                    }
                    for (SignInTasks.SignInTask signInTask : newList) {
                        if (sourceList.contains(signInTask)) {
                            int index = sourceList.indexOf(signInTask);
                            sourceList.remove(index);
                            flexibleAdapter.removeItem(index);
                        }
                        sourceList.add(0, signInTask);
                        flexibleAdapter.addItem(0, new SignOutItem(signInTask));
                    }
                    if (newList.size() == flexibleAdapter.getItemCount()) {//添加 footer View
                        flexibleAdapter.addItem(flexibleAdapter.getItemCount(), new SignInFooterItem());
                    }
                    //mLinearLayoutManager.scrollToPosition(0);
                    mLastModifyAt = newList.get(0).getModifyAt();
                }
                newList.clear();
                //recycleview.setNoData(false);
                setNoData();
                tvSigninFooter.setVisibility(View.GONE);
                break;
            case R.id.tv_signin_footer:
                RxBus.getBus().post(new SignInLogEvent());
                break;
        }
    }
    private void setNoData(){
        if (flexibleAdapter != null && flexibleAdapter.isEmpty()){
            items.clear();
            items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂时没有签出记录"));
            flexibleAdapter.updateDataSet(items);
        }
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }
}
