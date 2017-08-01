package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.model.responese.StudentTrackPreview;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.rxbus.event.EventRouter;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */
public class FollowUpFragment extends BaseFragment implements FollowUpPresenter.PresenterView {

    StudentFilter filter = new StudentFilter();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @Inject FollowUpPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    FollowUpGlanceFragment newRegistFragment;
    FollowUpGlanceFragment newFollowFragment;
    FollowUpGlanceFragment newStudentFragment;
    private ArrayList<Fragment> fs = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;

    public FollowUpFragment() {

    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText(R.string.qc_student_follow_up);
    }

    @Override protected void onFinishAnimation() {
        showLoadingTrans();
        presenter.getStudentsStatistics();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_student_status_0:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.NEW_REGISTE_ALL));
                break;
            case R.id.ll_student_status_today_0:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.NEW_REGISTE_TODAY));
                break;
            case R.id.ll_student_status_1:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.FOLLOWUPING_ALL));
                break;
            case R.id.ll_student_status_today_1:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.FOLLOWUPING_TODAY));
                break;
            case R.id.ll_student_status_2:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.STUDENT_ALL));
                break;
            case R.id.ll_student_status_today_2:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.STUDENT_TODAY));
                break;
            case R.id.ll_student_follow_up_statistics_detail:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.FOLLOWUPING_CHART));
                return;
            case R.id.ll_student_follow_up_transfer_detail:
                RxBus.getBus().post(new EventRouter(RouterFollowUp.TRANSFER));
                return;
        }
    }

    @Override public void onTrackPreview(StudentTrackPreview preview) {

    }

    @Override public void onFollowUpStatistics(FollowUpDataStatistic statistics) {
        hideLoadingTrans();
        newRegistFragment = new FollowUpGlanceFragmentBuilder(statistics.new_create_users, 0).build();
        newFollowFragment = new FollowUpGlanceFragmentBuilder(statistics.new_following_users, 1).build();
        newStudentFragment = new FollowUpGlanceFragmentBuilder(statistics.new_member_users, 2).build();
        getChildFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.alpha_in, R.anim.slide_hold)
            .replace(R.id.frag_regist, newRegistFragment)
            .commitAllowingStateLoss();
        getChildFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.alpha_in, R.anim.slide_hold)
            .replace(R.id.frag_follow, newFollowFragment)
            .commitAllowingStateLoss();
        getChildFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.alpha_in, R.anim.slide_hold)
            .replace(R.id.frag_member, newStudentFragment)
            .commitAllowingStateLoss();
    }

    @Override public void onSelfInfo(Staff bean) {
    }

    @Override public void onShowError(String e) {
        ToastUtils.show(e);
    }
}
