package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventTabHeaderChange;
import cn.qingchengfit.recruit.network.body.MarkResumeBody;
import cn.qingchengfit.recruit.presenter.MarkResumesPresenter;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.BottomListFragment;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
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
 * Created by Paper on 2017/7/4.
 */
@FragmentWithArgs public class ResumeRecievedFragment extends BaseFragment
    implements MarkResumesPresenter.MVPView {

  @Arg String jobid;
  @Arg int type;

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.tab_strip) PagerSlidingTabImageStrip tabStrip;
  @BindView(R2.id.btn_show_bottom) TextView btnShowBottom;
  @BindView(R2.id.btn_mark) Button btnMark;
  @BindView(R2.id.layout_mark_cancel) LinearLayout layoutMarkCancel;
  @BindView(R2.id.vp) ViewPager vp;
  @Inject MarkResumesPresenter presenter;
  BottomListFragment bottomListFragment;
  private List<ResumeHandleFragment> fragments = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeRecievedFragmentBuilder.injectArguments(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_handle_resume, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);

    RxBusAdd(EventTabHeaderChange.class).subscribe(new Action1<EventTabHeaderChange>() {
      @Override public void call(EventTabHeaderChange eventTabHeaderChange) {
        tabStrip.notifyDataSetChanged();
      }
    });
    return view;
  }

  @Override protected void onFinishAnimation() {
    fragments.clear();
    for (int i = 0; i < 4; i++) {
      if (type == 0) {
        fragments.add(ResumeHandleFragment.newRecieved(4 - i, jobid));
      } else {
        fragments.add(ResumeHandleFragment.newInvited(4 - i, jobid));
      }
    }
    vp.setAdapter(new ResumeHandleAdapter(getChildFragmentManager()));
    vp.setOffscreenPageLimit(3);
    vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {

      }

      @Override public void onPageScrollStateChanged(int state) {
        cancelSelect();
      }
    });
    tabStrip.setViewPager(vp);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(type == 0 ? "主动投递的人才" : "我邀约的人才");
  }

  @OnClick(R2.id.btn_mark) public void clickBtnMark() {
    btnMark.setVisibility(View.GONE);
    btnShowBottom.setVisibility(View.VISIBLE);
    layoutMarkCancel.setVisibility(View.VISIBLE);
    ResumeHandleFragment f = fragments.get(vp.getCurrentItem());
    if (f != null) f.setChooseMode(true);
  }

  @OnClick(R2.id.btn_cancel) public void cancelSelect() {
    btnMark.setVisibility(View.VISIBLE);
    layoutMarkCancel.setVisibility(View.GONE);
    btnShowBottom.setVisibility(View.GONE);
    for (ResumeHandleFragment fragment : fragments) {
      fragment.setChooseMode(false);
    }
  }

  @OnClick(R2.id.btn_show_bottom) public void clickShowBottom() {
    final DialogSheet dsb = DialogSheet.builder(getContext());
    if (vp.getCurrentItem() != 3) {
      dsb.addButton("不合适", R.color.red, new View.OnClickListener() {
        @Override public void onClick(View v) {
          dsb.dismiss();
          markResumes(1);
        }
      });
      if (vp.getCurrentItem() != 2) {
        dsb.addButton("已录用", R.color.blue_for_recuirt, new View.OnClickListener() {
          @Override public void onClick(View v) {
            dsb.dismiss();
            markResumes(2);
          }
        });
      }
      if (vp.getCurrentItem() != 1) {
        dsb.addButton("待沟通", R.color.blue_for_recuirt, new View.OnClickListener() {
          @Override public void onClick(View v) {
            dsb.dismiss();
            markResumes(3);
          }
        });
      }
      if (vp.getCurrentItem() != 0) {
        dsb.addButton("未处理", R.color.blue_for_recuirt, new View.OnClickListener() {
          @Override public void onClick(View v) {
            dsb.dismiss();
            markResumes(4);
          }
        });
      }
    }
    dsb.show();
  }

  public void markResumes(int r) {
    showLoading();
    presenter.markResume(new MarkResumeBody.Builder().resume_ids(getChoosenResumeId())
        .job_id(jobid)
        .status(r)
        .build());
  }

  private List<String> getChoosenResumeId() {
    ResumeHandleFragment f = fragments.get(vp.getCurrentItem());
    if (f != null) {
      return f.getChooseIds();
    } else {
      return new ArrayList<>();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void markOk() {
    hideLoading();
    cancelSelect();
    for (int i = 0; i < fragments.size(); i++) {
      fragments.get(i).onRefresh();
    }
  }

  private class ResumeHandleAdapter extends FragmentStatePagerAdapter
      implements PagerSlidingTabImageStrip.ImageTabProvider {
    ResumeHandleAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return 4;
    }

    @Override public CharSequence getPageTitle(int position) {
      return getTextStr(position);
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public String getTextStr(int position) {
      switch (position) {
        case 1:
          return "待沟通";
        case 2:
          return "已录用";
        case 3:
          return "不合格";
        default:
          return "未处理";
      }
    }

    @Override public boolean getShowRed(int position) {
      if ((position == 0 || position == 1) && getItem(position) instanceof ResumeHandleFragment) {
        return ((ResumeHandleFragment) getItem(position)).isHasItem();
      }
      return false;
    }
  }
}
