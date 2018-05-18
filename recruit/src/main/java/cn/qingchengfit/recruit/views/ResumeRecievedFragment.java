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


import cn.qingchengfit.recruit.R;

import cn.qingchengfit.recruit.event.EventTabHeaderChange;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.body.MarkResumeBody;
import cn.qingchengfit.recruit.presenter.MarkResumesPresenter;
import cn.qingchengfit.recruit.presenter.ResumePermissionPresenter;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
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
    implements MarkResumesPresenter.MVPView, ResumePermissionPresenter.MVPView {

  @Arg Job job;
  @Arg int type;

	Toolbar toolbar;
	TextView toolbarTitle;
	TextView tvHint;
	PagerSlidingTabImageStrip tabStrip;
  LinearLayout layoutMarkCancel;
	ViewPager vp;
  @Inject MarkResumesPresenter presenter;
  @Inject ResumePermissionPresenter permissionPresenter;
  private List<ResumeHandleFragment> fragments = new ArrayList<>();
  TextView btnShowBottomMuti;
  Button btnMark;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ResumeRecievedFragmentBuilder.injectArguments(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_handler_received_resume, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tvHint = (TextView) view.findViewById(R.id.tv_hint);
    tabStrip = (PagerSlidingTabImageStrip) view.findViewById(R.id.tab_strip);
    vp = (ViewPager) view.findViewById(R.id.vp);

    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    delegatePresenter(permissionPresenter, this);
    btnShowBottomMuti = (TextView)view.findViewById(R.id.btn_show_bottom_check);
    btnMark = view.findViewById(R.id.btn_mark);
    layoutMarkCancel = view.findViewById(R.id.layout_mark_cancel);
    initView();
    RxBusAdd(EventTabHeaderChange.class).subscribe(new Action1<EventTabHeaderChange>() {
      @Override public void call(EventTabHeaderChange eventTabHeaderChange) {
        tabStrip.notifyDataSetChanged();
      }
    });
    return view;
  }

  private void initView(){

    btnShowBottomMuti.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showBottom();
      }
    });

    btnMark.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mark();
      }
    });

    layoutMarkCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
       cancel();
      }
    });
  }

  private void cancel(){
    btnMark.setVisibility(View.VISIBLE);
    layoutMarkCancel.setVisibility(View.GONE);
    btnShowBottomMuti.setVisibility(View.GONE);
    for (ResumeHandleFragment fragment : fragments) {
      fragment.setChooseMode(false);
    }
  }

  private void showBottom(){
    permissionPresenter.queryChangeStatePermission(job.gym.id, "resume");
  }

  private void mark(){
    btnMark.setVisibility(View.GONE);
    btnShowBottomMuti.setVisibility(View.VISIBLE);
    layoutMarkCancel.setVisibility(View.VISIBLE);
    ResumeHandleFragment f = fragments.get(vp.getCurrentItem());
    if (f != null) f.setChooseMode(true);
  }

  @Override protected void onFinishAnimation() {
    fragments.clear();
    for (int i = 0; i < 4; i++) {
      if (type == 0) {
        fragments.add(ResumeHandleFragment.newRecieved(4 - i, job.id));
      } else {
        fragments.add(ResumeHandleFragment.newInvited(4 - i, job.id));
      }
    }
    vp.setAdapter(new ResumeHandleAdapter(getChildFragmentManager()));
    vp.setOffscreenPageLimit(3);
    vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        switch (position) {
          case 0:
          case 1:
            tvHint.setText("请及时处理简历为\"已录用\"或\"不合适\"，否则无法参加下一次招聘会");
            break;
          default:
            tvHint.setText("");
            break;
        }
      }

      @Override public void onPageScrollStateChanged(int state) {
        cancel();
      }
    });
    tabStrip.setViewPager(vp);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(type == 0 ? "主动投递的人才" : "我邀约的人才");
  }

  public void markResumes(int r) {
    showLoading();
    presenter.markResume(new MarkResumeBody.Builder().resume_ids(getChoosenResumeId())
        .job_id(job.id)
        .status(r).build(), type);
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
    cancel();
    for (int i = 0; i < fragments.size(); i++) {
      fragments.get(i).onRefresh();
    }
  }

  @Override public void onCheckSuccess() {
    final DialogSheet dsb = DialogSheet.builder(getContext());
    if (vp.getCurrentItem() != 3) {
      dsb.addButton("不合适", R.color.red, new View.OnClickListener() {
        @Override public void onClick(View v) {
          dsb.dismiss();
          markResumes(1);
        }
      });
    }

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
    //if (vp.getCurrentItem() != 0) {
    //  dsb.addButton("未处理", R.color.blue_for_recuirt, new View.OnClickListener() {
    //    @Override public void onClick(View v) {
    //      dsb.dismiss();
    //      markResumes(4);
    //    }
    //  });
    //}

    dsb.show();
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
          return "不合适";
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
