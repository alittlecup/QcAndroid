package cn.qingchengfit.recruit.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.ProvinceBean;
import cn.qingchengfit.model.common.CitiesData;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.presenter.SeekPositionPresenter;
import cn.qingchengfit.support.animator.FlipAnimation;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.FilterCommonLinearItem;
import cn.qingchengfit.views.fragments.FilterDamenFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import cn.qingchengfit.views.fragments.FilterLeftRightFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
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
    implements SeekPositionPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    FlexibleAdapter.EndlessScrollListener, FilterLeftRightFragment.OnLeftRightSelectListener,
    FilterFragment.OnSelectListener {

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
  @BindView(R2.id.img_my_resume) ImageView imgMyResume;
  @BindView(R2.id.tv_resume_completed) TextView tvResumeCompleted;
  @BindView(R2.id.img_my_job_fair) ImageView imgMyJobFair;
  @BindView(R2.id.tv_job_fair) TextView tvJobFair;
  @BindView(R2.id.filter_shadow) View filterShadow;
  private FilterFragment filterFragment;
  private List<FilterCommonLinearItem> itemList = new ArrayList<>();
  private FilterDamenFragment filterDamenFragment = new FilterDamenFragment();
  private CitiesData citiesData;
  private FilterLeftRightFragment filterLeftRightFragment = new FilterLeftRightFragment();
  private List<ProvinceBean> provinceBeanList = new ArrayList<>();
  private List<CityBean> cityBeanList = new ArrayList<>();
  private int lastClickId = -1;
  private int selectId = -1;
  private boolean isFilterSalary;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_seek_position_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(positionPresenter, this);
    initToolbar(toolbar);
    initFilter();
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

  public void dealCityFilter() {
    DealCityFilterAsyc dealCityFilterAsyc = new DealCityFilterAsyc();
    dealCityFilterAsyc.execute("cities.json");
  }

  private void initFilter() {
    dealCityFilter();
    filterShadow.setVisibility(View.GONE);
    filterFragment = new FilterFragment();
    for (String strItem : positionPresenter.filterSalary()) {
      itemList.add(new FilterCommonLinearItem(strItem));
    }
    filterFragment.setItemList(itemList);
    filterFragment.setOnSelectListener(this);
    filterLeftRightFragment.setListener(this);
    if(!filterFragment.isAdded()) {
      getChildFragmentManager().beginTransaction().add(R.id.frag_recruit_filter, filterFragment).commit();
    }
    if (!filterDamenFragment.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_recruit_filter, filterDamenFragment)
          .commit();
    }
    if (!filterLeftRightFragment.isAdded()) {
      getChildFragmentManager().beginTransaction()
          .add(R.id.frag_recruit_filter, filterLeftRightFragment)
          .commit();
    }
    getChildFragmentManager().beginTransaction().hide(filterFragment).commit();
    getChildFragmentManager().beginTransaction().hide(filterDamenFragment).commit();
    getChildFragmentManager().beginTransaction().hide(filterLeftRightFragment).commit();
    fragRecruitFilter.getLayoutParams().height = 0;
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

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_i_publish_job);
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
    positionPresenter.queryIndex();
  }

  //子fragment加载完成
  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
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

  /**
   * 简历 和 招聘会信息
   *
   * @param completed 完善度
   * @param fair_count 招聘会数量
   * @param avatar 头像
   * @param fiar_banner 招聘会banner
   */
  @Override public void onJobsIndex(Number completed, int fair_count, String avatar,
      String fiar_banner) {
    PhotoUtils.small(imgMyResume, avatar);
    PhotoUtils.small(imgMyJobFair, fiar_banner);
    tvResumeCompleted.setText("简历完善度" + completed + "%");
    tvJobFair.setText(fair_count + "场进行中");
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
    int i = v.getId();
    selectId = i;
    if (i == R.id.qft_city) {
      isFilterSalary = false;
      getChildFragmentManager().beginTransaction()
          .show(filterLeftRightFragment)
          .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
          .commit();
      getChildFragmentManager().beginTransaction().hide(filterFragment).commit();
      getChildFragmentManager().beginTransaction().hide(filterDamenFragment).commit();
      qftCity.toggle();
      qftDemand.setChecked(false);
      qftSalary.setChecked(false);
    } else if (i == R.id.qft_demand) {
      isFilterSalary = false;
      getChildFragmentManager().beginTransaction()
          .show(filterDamenFragment)
          .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
          .commit();
      getChildFragmentManager().beginTransaction().hide(filterFragment).commit();
      getChildFragmentManager().beginTransaction().hide(filterLeftRightFragment).commit();
      qftDemand.toggle();
      qftCity.setChecked(false);
      qftSalary.setChecked(false);
    } else if (i == R.id.qft_salary) {
      isFilterSalary = true;
      getChildFragmentManager().beginTransaction()
          .show(filterFragment)
          .commit();
      getChildFragmentManager().beginTransaction().hide(filterDamenFragment).commit();
      getChildFragmentManager().beginTransaction().hide(filterLeftRightFragment).commit();
      qftSalary.toggle();
      qftCity.setChecked(false);
      qftDemand.setChecked(false);
    }
    showFilter();
  }

  class DealCityFilterAsyc extends AsyncTask<String, Integer, List<String>> {

    @Override protected List<String> doInBackground(String... params) {
      Gson gson = new Gson();
      citiesData =
          gson.fromJson(FileUtils.getJsonFromAssert(params[0], getContext()), CitiesData.class);
      provinceBeanList = citiesData.provinces;
      List<String> cityFilterLeftList = new ArrayList<String>();
      for (ProvinceBean provinceBean : citiesData.provinces) {
        cityFilterLeftList.add(provinceBean.name);
      }
      return cityFilterLeftList;
    }

    @Override protected void onPostExecute(List<String> strings) {
      filterLeftRightFragment.setLeftItemList(strings);
    }
  }

  public void showFilter() {
    if (fragRecruitFilter.getMeasuredHeight() > 0 && lastClickId > 0 && lastClickId == selectId) {
      setFilterAnimation(false);
      filterShadow.setVisibility(View.GONE);
      lastClickId = -1;
    } else if (lastClickId < 0 || lastClickId != selectId) {
      setFilterAnimation(true);
      filterShadow.setVisibility(View.VISIBLE);
      lastClickId = selectId;
    }
  }

  public void setFilterAnimation(boolean isShow) {
    final ViewGroup.LayoutParams params = fragRecruitFilter.getLayoutParams();
    final int startHeight;
    final int endHeight;
    if (isShow) {
      startHeight = 0;
      if (isFilterSalary) {
        endHeight = (int) filterFragment.getViewHeight();
      } else{
        endHeight = MeasureUtils.dpToPx(432f, getResources());
      }
    } else {
      if (!filterFragment.isHidden()) {
        startHeight = MeasureUtils.dpToPx(432f, getResources());
      } else{
        startHeight = (int) filterFragment.getViewHeight();
      }
      endHeight = 0;
    }
    ValueAnimator valueAnimator = ObjectAnimator.ofFloat(startHeight, endHeight);
    valueAnimator.setDuration(400);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        if (startHeight < endHeight) {
          params.height = (int) (endHeight * fraction);
        } else {
          params.height = startHeight - (int) (startHeight * fraction);
        }
        fragRecruitFilter.setLayoutParams(params);
      }
    });
    valueAnimator.start();
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

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public void onLeftSelected(int position) {
    cityBeanList = provinceBeanList.get(position).cities;
    filterLeftRightFragment.onChangedCity(cityBeanList);
  }

  @Override public void onRightSelected(int position) {
    Log.e("---fb---", cityBeanList.get(position).name);
  }

  @Override public void onSelectItem(int position) {
    Log.e("---fb---", itemList.get(position).getData());
  }
}
