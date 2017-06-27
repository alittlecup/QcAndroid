package cn.qingchengfit.recruit.views.resume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.event.EventResumeFresh;
import cn.qingchengfit.recruit.item.ResumeWorkExpSimpleItem;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.presenter.ResumePresenter;
import cn.qingchengfit.recruit.views.organization.SearchActivity;
import cn.qingchengfit.recruit.views.organization.SearchFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 2017/6/14.
 */
public class ResumeWorkExpListFragment extends BaseFragment implements ResumePresenter.MVPView, FlexibleAdapter.OnItemClickListener {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.rv) RecyclerView rv;

  CommonFlexAdapter commonFlexAdapter;
  @Inject ResumePresenter presenter;
  @Inject RecruitRouter router;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_resume_cm_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
    rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    rv.addItemDecoration(new QcLeftRightDivider(getContext(), 1, R.layout.item_work_exp_simple, 15, 0));
    rv.setAdapter(commonFlexAdapter);
    RxBusAdd(EventResumeFresh.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EventResumeFresh>() {
      @Override public void call(EventResumeFresh eventResumeFresh) {
        presenter.queryWorkExps();
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {

      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("编辑工作经历");
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        Intent toSearch = new Intent(getActivity(), SearchActivity.class);
        toSearch.putExtra("type", SearchFragment.TYPE_GYM);
        startActivityForResult(toSearch, 10010);
        return false;
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10010) {
      if (resultCode > 0) {
        Gym gym = new Gym();
        gym.setId(data.getLongExtra("id", 0) + "");
        gym.setName(data.getStringExtra("username"));
        gym.setAddress(data.getStringExtra("address"));
        gym.setPhoto(data.getStringExtra("pic"));
        router.addWorkExp(gym);
      }
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryWorkExps();
  }

  @Override public String getFragmentName() {
    return ResumeWorkExpListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onBaseInfo(ResumeHome resumeHome) {

  }

  @Override public void onWorkExpList(List<WorkExp> workExps) {
    commonFlexAdapter.clear();
    for (WorkExp workExp : workExps) {
      commonFlexAdapter.addItem(new ResumeWorkExpSimpleItem(workExp, getContext()));
    }
  }

  @Override public void onEduExpList(List<Education> eduExps) {

  }

  @Override public void onCertiList(List<Certificate> certificates) {

  }

  @Override public boolean onItemClick(int i) {
    if (commonFlexAdapter.getItem(i) instanceof ResumeWorkExpSimpleItem) {
      router.workExpPreview(((ResumeWorkExpSimpleItem) commonFlexAdapter.getItem(i)).getWorkExp());
    }
    return false;
  }
}
