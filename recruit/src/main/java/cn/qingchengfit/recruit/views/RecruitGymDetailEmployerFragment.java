package cn.qingchengfit.recruit.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.databinding.FragmentRecruitGymDetailEmployerBinding;
import cn.qingchengfit.recruit.event.EventFreshJobsList;
import cn.qingchengfit.recruit.item.RecruitPositionInGymItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.response.OnePermissionWrap;
import cn.qingchengfit.recruit.presenter.RecruitGymDetailPresenter;
import cn.qingchengfit.recruit.presenter.RecruitStaffGymDetailPresenter;
import cn.qingchengfit.saas.network.GetApi;
import cn.qingchengfit.saas.response.SuWrap;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class RecruitGymDetailEmployerFragment extends BaseFragment
    implements RecruitGymDetailPresenter.MVPView {

  ArrayList<Fragment> fragments = new ArrayList<>();
  RecruitPositionsInGymFragment hotFragment;
  RecruitPositionsInGymFragment closeFragment;
  JobFairListFragment specialFragment;

  @Inject RecruitStaffGymDetailPresenter presenter;
  @Inject RecruitRouter recruitRouter;
  @Inject QcRestRepository qcRestRepository;

  Gym gym;
  FragmentRecruitGymDetailEmployerBinding db;

  private int initPage = -1;

  public static RecruitGymDetailEmployerFragment newInstance(Gym co) {
    Bundle args = new Bundle();
    args.putParcelable("gym", co);
    RecruitGymDetailEmployerFragment fragment = new RecruitGymDetailEmployerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * 将tab页初始化化到某位置
   */
  public static RecruitGymDetailEmployerFragment newInstanceInitTabpage(Gym co, int initpage) {
    Bundle args = new Bundle();
    args.putParcelable("gym", co);
    args.putInt("page", initpage);
    RecruitGymDetailEmployerFragment fragment = new RecruitGymDetailEmployerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      gym = getArguments().getParcelable("gym");
      initPage = getArguments().getInt("page");
    }
    hotFragment = new RecruitPositionsInGymFragment();
    closeFragment = new RecruitPositionsInGymFragment();
    specialFragment = RecruitStaffMyJobFairFragment.newInstance(gym.id);//专场招聘会
    fragments.add(hotFragment);
    fragments.add(closeFragment);
    fragments.add(specialFragment);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    db  = DataBindingUtil.inflate(inflater,R.layout.fragment_recruit_gym_detail_employer, container, false);
    super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter, this);
    initToolbar(db.layoutToolbar.toolbar);

    initBus();
    db.layoutRecruitGymInfo.layoutGymInfo.setOnClickListener(view1 -> onLayoutGymInfoClicked());
    db.layoutGymIntro.setOnClickListener(view1 -> onLayoutGymIntroClicked());
    db.layoutPermission.setOnClickListener(view1 -> onLayoutPermissionClicked());
    db.btnPublishNewPosition.setOnClickListener(view1 -> onViewClicked());
    return db.getRoot();
  }

  private void initBus() {
    RxBusAdd(EventFreshJobsList.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<EventFreshJobsList>() {
          @Override public void call(EventFreshJobsList eventFreshJobsList) {
            presenter.queryPositionOfGym(gym.id, 1);
          }
        });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    db.vp.setAdapter(new PositionTypesAdapter(getChildFragmentManager()));
    db.recruitGymTab.setupWithViewPager(db.vp);
    /*
     * 职位列表的点击事件
     */
    hotFragment.setListener(new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int i) {
        if (hotFragment.getItem(i) instanceof RecruitPositionInGymItem) {
          recruitRouter.tojobDetailEmployer(
              ((RecruitPositionInGymItem) hotFragment.getItem(i)).getJob());
        }
        return false;
      }
    });
    closeFragment.setListener(new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int i) {
        if (closeFragment.getItem(i) instanceof RecruitPositionInGymItem) {
          recruitRouter.tojobDetailEmployer(
              ((RecruitPositionInGymItem) closeFragment.getItem(i)).getJob());
        }
        return false;
      }
    });


    onGym(gym);
    presenter.queryGymDetail(gym.id);

    if (initPage > 0 && initPage < fragments.size()) {
      db.vp.setCurrentItem(initPage);
    }
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    ToolbarModel tbm = new ToolbarModel.Builder().title("场馆招聘详情").menu(R.menu.menu_preview).listener(
        item -> {
          recruitRouter.toGymDetial(gym);
          return false;
        }).build();
    db.setToolbarModel(tbm);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof RecruitPositionsInGymFragment) {
      presenter.queryPositionOfGym(gym.id, 1);
    }
  }

  public void onGym(Gym service) {
    db.layoutRecruitGymInfo.imgRight.setVisibility(View.VISIBLE);
    if (service == null) return;
    if (service.name == null) return;
    PhotoUtils.small(db.layoutRecruitGymInfo.imgGym, service.photo);
    db.layoutRecruitGymInfo.tvGymName.setText(service.name);
    db.layoutRecruitGymInfo.tvAddress.setText(service.getAddressStr());
  }

  @Override public void onJobList(List<Job> jobs, int page, int totalCount) {
    if (hotFragment != null) {
      if (jobs != null) {
        List<Job> hotJobs = new ArrayList<>();
        List<Job> closeJobs = new ArrayList<>();
        for (Job job : jobs) {
          if (job.published) {
            hotJobs.add(job);
          } else {
            closeJobs.add(job);
          }
        }
        hotFragment.setData(hotJobs);
        closeFragment.setData(closeJobs);
      }
    }
  }

  @Override public void onPermission(boolean has) {
    if (has)
      recruitRouter.toPermssion(gym.id);
    else showAlert(R.string.alert_permission_forbid_contact_su);
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
  public void onLayoutGymIntroClicked() {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .querySu(gym.id).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SuWrap>>() {
          @Override public void call(QcDataResponse<SuWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.is_superuser) {
                recruitRouter.toWriteGymIntro(gym);
              } else {
                showAlert("抱歉，您没有该功能的权限，请联系超级管理员");
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 场馆信息修改
   */
    public void onLayoutGymInfoClicked() {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .querySu(gym.id).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SuWrap>>() {
          @Override public void call(QcDataResponse<SuWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.is_superuser) {
                recruitRouter.editGymInfo(gym.id);
              } else {
                showAlert("抱歉，您没有该功能的权限，请联系超级管理员");
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  /**
   * 权限设置
   */
  public void onLayoutPermissionClicked() {
    presenter.queryPermission(gym.id);
  }

  /**
   * 发布新职位
   */
   public void onViewClicked() {
    RxRegiste(qcRestRepository.createRxJava1Api(cn.qingchengfit.recruit.network.GetApi.class)
        .queryOnepermission(gym.id, "job").onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<OnePermissionWrap>>() {
          @Override public void call(QcDataResponse<OnePermissionWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.has_permission) {
                recruitRouter.toPublishPosition(gym.getId(), null,
                    RecruitPublishJobFragment.PUBLISH_POSITION);
              } else {
                showAlert("抱歉，您无该功能权限，请联系超级管理员。");
              }
            } else {
              onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  class PositionTypesAdapter extends FragmentStatePagerAdapter
      implements PagerSlidingTabImageStrip.ImageTabProvider {

    public PositionTypesAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Override public CharSequence getPageTitle(int position) {
      return getTextStr(position);
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public String getTextStr(int position) {
      switch (position) {
        case 0:
          return "热招职位";
        case 1:
          return "已关闭职位";
        default:
          return "专场招聘会";
      }
    }

    @Override public boolean getShowRed(int position) {
      return false;
    }
  }
}
