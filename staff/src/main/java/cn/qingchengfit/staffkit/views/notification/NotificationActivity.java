package cn.qingchengfit.staffkit.views.notification;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.notification.page.NotificationFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

public class NotificationActivity extends BaseActivity implements FragCallBack {

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    //@BindView(R.id.toolbar_title)
    //TextView toolbarTitile;
    //@BindView(R.id.activity_notification)
    //LinearLayout activityNotification;
    @Inject StaffRespository mRestRepository;
    @Inject GymWrapper gymWrapper;
    private Subscription mSpClearNoti;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        onBackPressed();
        //    }
        //});
        //toolbarTitile.setText("全部通知");
        //toolbar.inflateMenu(R.menu.menu_all_read);
        //toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        //    @Override
        //    public boolean onMenuItemClick(MenuItem item) {
        //        if (item.getItemId() == R.id.action_all_read){
        //            new MaterialDialog.Builder(NotificationActivity.this)
        //                    .autoDismiss(true)
        //                    .content("全部标记为已读")
        //                    .positiveText(R.string.common_comfirm)
        //                    .negativeText(R.string.common_cancel)
        //                    .onPositive(new MaterialDialog.SingleButtonCallback() {
        //                        @Override
        //                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //                            HashMap<String,Object> params = new HashMap<String, Object>();
        //                            params.put("tab","STAFF_0");
        //                            mSpClearNoti = mRestRepository.getStaffAllApi().qcClearAllNoti(App.staffId,params)
      //                                    .onBackpressureBuffer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //                                    .subscribe(new Action1<QcResponse>() {
        //                                        @Override
        //                                        public void call(QcResponse qcResponse) {
        //                                            if (ResponseConstant.checkSuccess(qcResponse)) {
        //                                                RxBus.getBus().post(new EventClearAllNoti());
        //                                            } else ToastUtils.show(qcResponse.getMsg());
        //                                        }
        //                                    }, new Action1<Throwable>() {
        //                                        @Override
        //                                        public void call(Throwable throwable) {
        //
        //                                        }
        //                                    });
        //                        }
        //                    }).show();
        //
        //
        //        }
        //        return true;
        //    }
        //});
        getSupportFragmentManager().beginTransaction()
            .replace(getFragId(), NotificationFragment.newInstance(getIntent().getStringExtra("type")))
            .commit();
    }

    @Override protected void onDestroy() {
        gymWrapper.setCoachService(null);
        super.onDestroy();
        if (mSpClearNoti != null && mSpClearNoti.isUnsubscribed()) mSpClearNoti.unsubscribe();
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {

    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }
}
