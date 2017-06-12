package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.JobFairFooterItem;
import cn.qingchengfit.recruit.item.JobFairItem;
import cn.qingchengfit.support.animator.FlipAnimation;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * Created by Paper on 2017/6/6.
 */
public class ResumeMarketHomeFragment extends BaseFragment {

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R2.id.et_search) EditText etSearch;
    @BindView(R2.id.rv_jobfairs) RecyclerView rvJobfairs;
    @Inject RecruitRouter router;
    private CommonFlexAdapter jobFailsAdapter;
    private FlexibleAdapter.OnItemClickListener jobFairsListener = new FlexibleAdapter.OnItemClickListener() {
        @Override public boolean onItemClick(int i) {
            if (jobFailsAdapter.getItem(i) instanceof JobFairItem) {
                router.toJobFairDetail();
            } else if (jobFailsAdapter.getItem(i) instanceof JobFairFooterItem) {
                router.myJobFair();
            }
            return true;
        }
    };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_market_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        inflatJobFairs();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("求职招聘");
        toolbar.inflateMenu(R.menu.menu_i_seek_job);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                getActivity().onBackPressed();
                return true;
            }
        });
    }

    @Override public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == R.anim.card_flip_left_in
            || nextAnim == R.anim.card_flip_right_in
            || nextAnim == R.anim.card_flip_left_out
            || nextAnim == R.anim.card_flip_right_out) {
            Animation animation;
            if (nextAnim == R.anim.card_flip_left_in) {
                animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 300);
            } else if (nextAnim == R.anim.card_flip_right_in) {
                animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 300);
            } else if (nextAnim == R.anim.card_flip_left_out) {
                animation = FlipAnimation.create(FlipAnimation.LEFT, enter, 300);
            } else {
                animation = FlipAnimation.create(FlipAnimation.RIGHT, enter, 300);
            }

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {

                }

                @Override public void onAnimationEnd(Animation animation) {
                    onFinishAnimation();
                }

                @Override public void onAnimationRepeat(Animation animation) {

                }
            });
            return animation;
        } else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    public void inflatJobFairs() {
        List<AbstractFlexibleItem> items = new ArrayList<>();
        jobFailsAdapter = new CommonFlexAdapter(items, jobFairsListener);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(rvJobfairs);
        rvJobfairs.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvJobfairs.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider_translate_5dp));
        rvJobfairs.setAdapter(jobFailsAdapter);
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairItem());
        jobFailsAdapter.addItem(new JobFairFooterItem(20));
    }

    @Override public String getFragmentName() {
        return ResumeMarketHomeFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
