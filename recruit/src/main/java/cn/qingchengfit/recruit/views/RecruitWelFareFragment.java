package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.R2;
import cn.qingchengfit.recruit.event.EventPulishPosition;
import cn.qingchengfit.recruit.views.resume.ResumeIntentJobsFragment;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TagInputFragment;
import cn.qingchengfit.widgets.QcTagGroup;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fb on 2017/7/3.
 */

@FragmentWithArgs public class RecruitWelFareFragment extends BaseFragment {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitile;
  @BindView(R2.id.tg_recomend) QcTagGroup tgRecomend;

  TagInputFragment tagInputFragment;
  @Arg ArrayList<String> jobs;
  @Arg String title;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RecruitWelFareFragmentBuilder.injectArguments(this);
    tagInputFragment = new TagInputFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_position_welfare, container, false);
    super.onCreateView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    tgRecomend.setOnTagClickListener(new QcTagGroup.OnTagClickListener() {
      @Override public void onTagClick(String tag) {
        if (tagInputFragment != null) {
          tagInputFragment.insertTag(tag);
        }
      }
    });

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText(title);
    toolbar.inflateMenu(R.menu.menu_save);

    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (tagInputFragment != null) {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("welfare", tagInputFragment.getTags());
          RxBus.getBus().post(EventPulishPosition.build(map));
        }
        getActivity().onBackPressed();
        return true;
      }
    });

    onRecomentTags(Arrays.asList(getResources().getStringArray(R.array.recruit_position_welfare)));
  }

  public void onRecomentTags(List<String> tags) {
    tgRecomend.removeAllViews();
    tgRecomend.setTags(tags);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(tagInputFragment);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof TagInputFragment) {
      tagInputFragment.setOnDelLister(new QcTagGroup.OnTagChangeListener() {
        @Override public void onAppend(QcTagGroup tagGroup, String tag) {
          tgRecomend.checkTag(tgRecomend.indexTag(tag), true);
        }

        @Override public void onDelete(QcTagGroup tagGroup, String tag) {
          Arrays.asList(tgRecomend.getTags()).contains(tag);
          tgRecomend.checkTag(tgRecomend.indexTag(tag), false);
        }
      });
      tagInputFragment.setTags(jobs);
    }
  }

  @Override public int getLayoutRes() {
    return R.id.frag_tag_input;
  }

  @Override public String getFragmentName() {
    return ResumeIntentJobsFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
