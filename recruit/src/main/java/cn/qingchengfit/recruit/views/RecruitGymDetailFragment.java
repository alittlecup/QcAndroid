package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionInGymItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.RecruitGymDetailPresenter;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/5/27.
 */
public class RecruitGymDetailFragment extends BaseFragment
    implements RecruitGymDetailPresenter.MVPView,
    FlexibleAdapter.OnItemClickListener {

  ArrayList<Fragment> fragments = new ArrayList<>();
  RecruitPositionsInGymFragment positionsFragment;
  RecruitGymDescFragment descFragment;

  @Inject RecruitGymDetailPresenter presenter;
  @Inject RecruitRouter router;
  @Inject QcRestRepository qcRestRepository;
  Gym gym;
  List<Job> tempJob = new ArrayList<>();
  //private FragmentRecruitCompanyDetailBinding db;
  ViewPager vp;
  TabLayout recruitGymTab;

  public static RecruitGymDetailFragment newInstance(Gym co) {
    Bundle args = new Bundle();
    args.putParcelable("gym", co);
    RecruitGymDetailFragment fragment = new RecruitGymDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gym = getArguments().getParcelable("gym");
    descFragment = RecruitGymDescFragmentBuilder.newRecruitGymDescFragment(gym);
    positionsFragment = new RecruitPositionsInGymFragment();
    positionsFragment.setListener(this);
    fragments.add(descFragment);
    fragments.add(positionsFragment);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    //super.onCreateView(inflater,container,savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_recruit_company_detail,container,false);
    //db = FragmentRecruitCompanyDetailBinding.bind(view);
    delegatePresenter(presenter, this);
    initToolbar(view.findViewById(R.id.toolbar));
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    vp.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragments));
    recruitGymTab.setupWithViewPager(vp);
    onGym(gym);
    presenter.queryGymDetail(gym.id);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    ((TextView)getView().findViewById(R.id.toolbar_title)).setText("公司详情");
    toolbar.inflateMenu(R.menu.menu_share);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        String title = gym != null ? gym.getBrand_name() + "|" + gym.getName() : "";
        String content = "热招职位：" + RecruitBusinessUtils.getHotJob(tempJob);
        String pic = gym != null ? gym.photo
            : "http://zoneke-img.b0.upaiyun.com/977ad17699c4e4212b52000ed670091a.png";
        String url = qcRestRepository.getHost() + "mobile/company/" + gym.id + "/";
        ShareDialogFragment.newInstance(title, content, pic, url)
            .show(getChildFragmentManager(), "");
        return false;
      }
    });
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RecruitPositionsInGymFragment) {
      presenter.queryPositionOfGym(gym.id, 1);
    }
  }

  public void onGym(Gym service) {
    //layoutRecruitGymInfo.imgRight.setVisibility(View.GONE);
    //if (service == null) return;
    //PhotoUtils.small(layoutRecruitGymInfo.imgGym, service.photo);
    //layoutRecruitGymInfo.tvGymName.setText(service.name);
    //layoutRecruitGymInfo.tvAddress.setText(service.getAddressStr());
    //if (descFragment != null && descFragment.isAdded()) descFragment.setDesc(service);
  }

  @Override public void onJobList(List<Job> jobs, int page, int totalCount) {
    if (positionsFragment != null) {
      if (jobs != null) {
          tempJob.clear();
          tempJob.addAll(jobs);
          positionsFragment.setData(jobs);
      } else {
        positionsFragment.stopLoadMore();
      }
    }
  }

  @Override public void onPermission(boolean has) {

  }

  @Override public String getFragmentName() {
    return RecruitGymDetailFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }



  @Override public boolean onItemClick(int i) {
    IFlexible item = positionsFragment.getItem(i);
    if (item instanceof RecruitPositionInGymItem) {
      router.goJobDetail(((RecruitPositionInGymItem) item).getJob());
    }
    return true;
  }
}
