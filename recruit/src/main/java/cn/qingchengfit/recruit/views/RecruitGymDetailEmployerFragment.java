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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.RecruitGymDetailPresenter;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * 雇主版本的场馆主页
 */
public class RecruitGymDetailEmployerFragment extends BaseFragment implements RecruitGymDetailPresenter.MVPView {

  @BindView(R2.id.img_gym) ImageView imgGym;
  @BindView(R2.id.tv_gym_name) TextView tvGymName;
  @BindView(R2.id.tv_address) TextView tvAddress;
  @BindView(R2.id.img_right) ImageView imgRight;
  @BindView(R2.id.recruit_gym_tab) TabLayout tab;
  @BindView(R2.id.vp) ViewPager vp;
  ArrayList<Fragment> fragments = new ArrayList<>();
  RecruitPositionsInGymFragment hotFragment;
  RecruitPositionsInGymFragment closeFragment;
  RecruitPositionsInGymFragment specialFragment;

  @Inject RecruitGymDetailPresenter presenter;
  @Inject RecruitRouter recruitRouter;

  Gym gym;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;

  public static RecruitGymDetailEmployerFragment newInstance(Gym co) {
    Bundle args = new Bundle();
    args.putParcelable("gym", co);
    RecruitGymDetailEmployerFragment fragment = new RecruitGymDetailEmployerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    hotFragment = new RecruitPositionsInGymFragment();
    closeFragment = new RecruitPositionsInGymFragment();
    specialFragment = new RecruitPositionsInGymFragment();
    fragments.add(hotFragment);
    fragments.add(closeFragment);
    fragments.add(specialFragment);
    gym = getArguments().getParcelable("gym");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recruit_gym_detail_employer, container, false);
    super.onCreateView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    vp.setAdapter(new FragmentAdapter(getChildFragmentManager(), fragments));
    tab.setupWithViewPager(vp);
    onGym(gym);
    presenter.queryGymDetail(gym.id);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("公司详情");
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RecruitPositionsInGymFragment) {
      presenter.queryPositionOfGym(gym.id, 1);
    }
  }

  public void onGym(Gym service) {
    imgRight.setVisibility(View.GONE);
    if (service == null) return;
    PhotoUtils.small(imgGym, service.photo);
    tvGymName.setText(service.name);
    tvAddress.setText(service.getAddressStr());
  }

  @Override public void onJobList(List<Job> jobs, int page, int totalCount) {

  }

  @Override public String getFragmentName() {
    return RecruitGymDetailEmployerFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 场馆介绍
   */
  @OnClick(R2.id.layout_gym_intro) public void onLayoutGymIntroClicked() {
    recruitRouter.toWriteGymIntro(gym);
  }

  /**
   * 权限设置
   */
  @OnClick(R2.id.layout_permission) public void onLayoutPermissionClicked() {
    recruitRouter.toPermssion();
  }

  /**
   * 发布新职位
   */
  @OnClick(R2.id.btn_publish_new_position) public void onViewClicked() {
    recruitRouter.toPublishPosition(gym.getId());
  }
}
