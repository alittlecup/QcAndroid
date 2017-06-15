package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.ResumeBaseInfoItem;
import cn.qingchengfit.recruit.item.ResumeCertificateItem;
import cn.qingchengfit.recruit.item.ResumeEduExpItem;
import cn.qingchengfit.recruit.item.ResumeEmptyItem;
import cn.qingchengfit.recruit.item.ResumeIntentImgShowItem;
import cn.qingchengfit.recruit.item.ResumeIntentItem;
import cn.qingchengfit.recruit.item.ResumeTitleItem;
import cn.qingchengfit.recruit.item.ResumeWebDescItem;
import cn.qingchengfit.recruit.item.ResumeWorkExpItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 2017/6/9.
 */
public class ResumeHomeFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, ResumePresenter.MVPView {

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R2.id.tv_resume_completed) TextView tvResumeCompleted;
    @BindView(R2.id.tv_resume_open) TextView tvResumeOpen;
    @BindView(R2.id.rv) RecyclerView rv;
    CommonFlexAdapter commonFlexAdapter;

    @Inject ResumePresenter presenter;
    @Inject RecruitRouter router;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rv.setAdapter(commonFlexAdapter);
        presenter.queryResumeHome();
        RxBusAdd(ResumeTitleItem.class).subscribe(new Action1<ResumeTitleItem>() {
            @Override public void call(ResumeTitleItem resumeTitleItem) {
                router.routeToEdit(resumeTitleItem.getPos());
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("我的简历");
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();

    }

    @Override public String getFragmentName() {
        return ResumeHomeFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public boolean onItemClick(int i) {
        if (commonFlexAdapter.getItem(i) instanceof ResumeEmptyItem) {
            router.routeToAdd(((ResumeEmptyItem) commonFlexAdapter.getItem(i)).getPos());
        }
        return true;
    }

    @Override public void onBaseInfo(ResumeHome resumeHome) {
        commonFlexAdapter.addItem(new ResumeTitleItem(0, getContext(), true));
        commonFlexAdapter.addItem(new ResumeBaseInfoItem(resumeHome));
        commonFlexAdapter.addItem(new ResumeTitleItem(1, getContext(), true));
        commonFlexAdapter.addItem(new ResumeIntentItem(getContext(), resumeHome.exp_jobs, resumeHome.exp_cities,
            RecruitBusinessUtils.getSalary(resumeHome.min_salary, resumeHome.max_salary), resumeHome.status));
        if (resumeHome.photos != null && resumeHome.photos.size() != 0) {
            commonFlexAdapter.addItem(new ResumeTitleItem(2, getContext(), true));
            commonFlexAdapter.addItem(new ResumeIntentImgShowItem(resumeHome.photos));
        } else {

            commonFlexAdapter.addItem(new ResumeTitleItem(2, getContext(), false));
            commonFlexAdapter.addItem(new ResumeEmptyItem(2, getContext()));
        }

        if (resumeHome.experiences != null && resumeHome.experiences.size() != 0) {
            commonFlexAdapter.addItem(new ResumeTitleItem(3, getContext(), true));
            for (WorkExp w : resumeHome.experiences) {
                commonFlexAdapter.addItem(new ResumeWorkExpItem(w, getContext()));
            }
        } else {
            commonFlexAdapter.addItem(new ResumeTitleItem(3, getContext(), false));
            commonFlexAdapter.addItem(new ResumeEmptyItem(3, getContext()));
        }
        if (resumeHome.certificates != null && resumeHome.certificates.size() != 0) {
            commonFlexAdapter.addItem(new ResumeTitleItem(4, getContext(), true));
            for (Certificate c : resumeHome.certificates) {
                commonFlexAdapter.addItem(new ResumeCertificateItem(c));
            }
        } else {
            commonFlexAdapter.addItem(new ResumeTitleItem(4, getContext(), false));
            commonFlexAdapter.addItem(new ResumeEmptyItem(4, getContext()));
        }

        if (resumeHome.educations != null && resumeHome.educations.size() != 0) {
            commonFlexAdapter.addItem(new ResumeTitleItem(5, getContext(), true));
            for (Education e : resumeHome.educations) {
                commonFlexAdapter.addItem(new ResumeEduExpItem(e));
            }
        } else {
            commonFlexAdapter.addItem(new ResumeTitleItem(5, getContext(), false));
            commonFlexAdapter.addItem(new ResumeEmptyItem(5, getContext()));
        }

        if (TextUtils.isEmpty(resumeHome.self_description)) {
            commonFlexAdapter.addItem(new ResumeTitleItem(6, getContext(), true));
            commonFlexAdapter.addItem(new ResumeWebDescItem(resumeHome.self_description));
        } else {
            commonFlexAdapter.addItem(new ResumeTitleItem(6, getContext(), true));
            commonFlexAdapter.addItem(new ResumeEmptyItem(6, getContext()));
        }
    }

    @Override public void onWorkExpList(List<WorkExp> workExps) {

    }

    @Override public void onEduExpList(List<Education> eduExps) {

    }

    @Override public void onCertiList(List<Certificate> certificates) {

    }
}
