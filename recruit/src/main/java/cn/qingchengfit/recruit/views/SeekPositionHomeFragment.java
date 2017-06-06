package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.SeekPositionPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/23.
 */
public class SeekPositionHomeFragment extends BaseFragment
    implements SeekPositionPresenter.MVPView, FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {
    @Inject SeekPositionPresenter positionPresenter;
    @Inject RecruitPositionsFragment listFragment;
    @Inject RecruitRouter router;

    @BindView(R2.id.et_search) EditText etSearch;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.smooth_app_bar_layout) AppBarLayout smoothAppBarLayout;
    @BindView(R2.id.qft_city) QcFilterToggle qftCity;
    @BindView(R2.id.qft_salary) QcFilterToggle qftSalary;
    @BindView(R2.id.qft_demand) QcFilterToggle qftDemand;
    @BindView(R2.id.frag_recruit_filter) FrameLayout fragRecruitFilter;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_seek_position_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(positionPresenter, this);
        initToolbar(toolbar);
        RxTextView.textChangeEvents(etSearch)
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(new Action1<TextViewTextChangeEvent>() {
                @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                    localFilter(textViewTextChangeEvent.text().toString());
                }
            });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.menu_share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                router.resumeMarketHome();
                return false;
            }
        });
        toolbarTitile.setText("求职招聘");
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();
        listFragment.listener = this;
        listFragment.setListener(this);
        stuff(listFragment);
    }

    //子fragment加载完成
    @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onChildViewCreated(fm, f, v, savedInstanceState);
        if (f instanceof RecruitPositionsFragment) {
            positionPresenter.queryList(1);
        }
    }

    @Override public String getFragmentName() {
        return SeekPositionHomeFragment.class.getName();
    }

    @Override public void onJob(Job job) {

    }

    @Override public void onList(List<Job> jobs, int page, int totalCount) {
        listFragment.setTotalCount(totalCount);
        if (page == 1) {
            listFragment.setData(jobs);
        } else if (page > 1) {
            listFragment.addData(jobs);
        }
    }

    @Override public void starOK() {

    }

    @Override public void unStarOk() {

    }

    @Override public void sendResumeOk() {

    }

    @Override public void onGym(Gym service) {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void localFilter(String s) {

    }

    /**
     * 我发送的简历
     */
    @OnClick(R2.id.layout_i_sent) public void onLayoutISentClicked() {
        router.mySent();
    }

    @OnClick(R2.id.layout_i_invited) public void onLayoutIInvitedClicked() {
        router.myInvited();
    }

    @OnClick(R2.id.layout_i_stared) public void onLayoutIStaredClicked() {
        router.myStarred();
    }

    /**
     * 我的简历
     */
    @OnClick(R2.id.layout_my_resume) public void onLayoutMyResumeClicked() {
        router.myResume();
    }

    /**
     * 我的专场招聘
     */
    @OnClick(R2.id.layout_my_jobfair) public void onLayoutMyJobfairClicked() {
        router.myJobFair();
    }

    /**
     * 筛选条件
     */
    @OnClick({ R2.id.qft_city, R2.id.qft_demand, R2.id.qft_salary }) public void onFilter(View v) {
        smoothAppBarLayout.setExpanded(false, true);
        switch (v.getId()) {
            case R2.id.qft_city:
                qftCity.toggle();
                qftDemand.setChecked(false);
                qftSalary.setChecked(false);
                break;
            case R2.id.qft_demand:
                qftDemand.toggle();
                qftCity.setChecked(false);
                qftSalary.setChecked(false);

                break;
            case R2.id.qft_salary:
                qftSalary.toggle();
                qftCity.setChecked(false);
                qftDemand.setChecked(false);

                break;
        }
    }

    public void showFilter() {

        if (fragRecruitFilter.getVisibility() == View.VISIBLE) {

        } else {
            fragRecruitFilter.setVisibility(View.VISIBLE);
            ViewCompat.animate(fragRecruitFilter).setDuration(getResources().getInteger(R.integer.anim_time)).alpha(1f).start();
        }
    }

    public void hideFilter() {

        ViewCompat.animate(fragRecruitFilter)
            .setDuration(getResources().getInteger(R.integer.anim_time))
            .setListener(new ViewPropertyAnimatorListener() {
                @Override public void onAnimationStart(View view) {

                }

                @Override public void onAnimationEnd(View view) {
                    fragRecruitFilter.setVisibility(View.GONE);
                }

                @Override public void onAnimationCancel(View view) {

                }
            })
            .alpha(0f)
            .start();
    }

    @Override public boolean onItemClick(int i) {
        IFlexible item = listFragment.getItem(i);
        if (item != null && item instanceof RecruitPositionItem) {
            Job job = ((RecruitPositionItem) item).getJob();
            router.goJobDetail(job);
        }
        return true;
    }

    @Override public int getLayoutRes() {
        return R.id.frag_recruit_home;
    }

    @Override public void noMoreLoad(int i) {
        if (listFragment != null) listFragment.stopLoadMore();
    }

    @Override public void onLoadMore(int i, int i1) {

    }
}
