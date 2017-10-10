package cn.qingchengfit.saasbase.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.routers.Istudent;
import cn.qingchengfit.saasbase.routers.RouterCenter;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentFragment;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
import rx.functions.Action1;

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
@Trunk(fragments = {
    ChooseAndSearchStudentFragment.class,

})
public class StudentActivity extends BaseActivity
    implements FragCallBack, HasSupportFragmentInjector {

  //@BindView(R2.id.student_frag) FrameLayout studentFrag;
  @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;
  @Inject QcRestRepository restRepository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject RouterCenter routerCenter;
  @Inject Istudent istudent;



  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    ButterKnife.bind(this);
    initView();
    onNewIntent(getIntent());
  }

  private void initView() {
    routerCenter.registe(istudent);

    //routerCenter.getFragment(Uri.parse("xx"), ;

    // TODO: 2017/8/10 权限的检查
    //if (getIntent() != null && getIntent().getExtras() != null) {
    //    if (getIntent().getExtras().containsKey(Configs.EXTRA_GYM_SERVICE)) {
    //        CoachService c = getIntent().getExtras().getParcelable(Configs.EXTRA_GYM_SERVICE);
    //        gymWrapper.setCoachService(c);
    //        restRepository.getGet_api()
    //            .qcPermission(loginStatus.staff_id(), gymWrapper.getParams())
    //            .onBackpressureBuffer()
    //            .subscribeOn(Schedulers.io())
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .subscribe(new Action1<QcResponsePermission>() {
    //                @Override public void call(QcResponsePermission qcResponse) {
    //                    if (ResponseConstant.checkSuccess(qcResponse)) {
    //                        SerPermisAction.writePermiss(qcResponse.data.permissions);
    //                        if (!SerPermisAction.checkAll(PermissionServerUtils.MANAGE_MEMBERS)) {
    //                            new MaterialDialog.Builder(StudentActivity.this).autoDismiss(true)
    //                                .content(R.string.alert_permission_forbid)
    //                                .positiveText(R.string.common_i_konw)
    //                                .onPositive(new MaterialDialog.SingleButtonCallback() {
    //                                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
    //                                        StudentActivity.this.finish();
    //                                    }
    //                                })
    //                                .show();
    //                        } else {
    //                            getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
    //                        }
    //                    } else {
    //                        Timber.e(qcResponse.getMsg());
    //                    }
    //                }
    //            }, new Action1<Throwable>() {
    //                @Override public void call(Throwable throwable) {
    //                    LogUtil.e(throwable.getMessage());
    //                }
    //            });
    //    } else {
    //        getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
    //    }
    //} else {
    //    getSupportFragmentManager().beginTransaction().replace(getFragId(), new StudentListFragment()).commit();
    //}

  }

  @Override public int getFragId() {
    return R.id.web_frag_layout;
  }

  @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick,
      @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
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
