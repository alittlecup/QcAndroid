package cn.qingchengfit.staffkit.views.student;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.commpont.StudentWrapperComponent;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.FragCallBack;
import cn.qingchengfit.staffkit.views.student.list.StudentListFragment;
import cn.qingchengfit.views.activity.BaseActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
import javax.inject.Provider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/4 2016.
 */
public class StudentActivity extends BaseActivity implements FragCallBack, HasSupportFragmentInjector {

    @BindView(R.id.student_frag) FrameLayout studentFrag;
    @Inject Provider<StudentWrapperComponent.Builder> requestComponentProvider;
    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffWrapper staffWrapper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_no_toolbar);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        staffWrapper = new StaffWrapper();
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Configs.EXTRA_GYM_SERVICE)) {
                CoachService c = getIntent().getExtras().getParcelable(Configs.EXTRA_GYM_SERVICE);
                gymWrapper.setCoachService(c);
                restRepository.getGet_api()
                    .qcPermission(loginStatus.staff_id(), gymWrapper.getParams())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<QcResponsePermission>() {
                        @Override public void call(QcResponsePermission qcResponse) {
                            if (ResponseConstant.checkSuccess(qcResponse)) {
                                SerPermisAction.writePermiss(qcResponse.data.permissions);
                                if (!SerPermisAction.checkAll(PermissionServerUtils.MANAGE_MEMBERS)) {
                                    new MaterialDialog.Builder(StudentActivity.this).autoDismiss(true)
                                        .content(R.string.alert_permission_forbid)
                                        .positiveText(R.string.common_i_konw)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                StudentActivity.this.finish();
                                            }
                                        })
                                        .show();
                                } else {
                                    getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
                                }
                            } else {
                                Timber.e(qcResponse.getMsg());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override public void call(Throwable throwable) {
                            Timber.e(throwable.getMessage());
                        }
                    });
            } else {
                getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
        }
    }

    @Override public int getFragId() {
        return R.id.student_frag;
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

    @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
