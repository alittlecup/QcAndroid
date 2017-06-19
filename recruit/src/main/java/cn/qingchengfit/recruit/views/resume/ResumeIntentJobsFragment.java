package cn.qingchengfit.recruit.views.resume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import cn.qingchengfit.recruit.event.EventIntentJobs;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TagInputFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import me.gujun.android.taggroup.TagGroup;

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
 * Created by Paper on 2017/6/15.
 */
@FragmentWithArgs
public class ResumeIntentJobsFragment extends BaseFragment {

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.toolbar_title) TextView toolbarTitile;
    @BindView(R2.id.tg_recomend) TagGroup tgRecomend;

    TagInputFragment tagInputFragment;
    @Arg ArrayList<String> jobs;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        tagInputFragment = new TagInputFragment();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intent_jobs, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        tgRecomend.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override public void onTagClick(String tag) {
                if (tagInputFragment != null) {
                    tagInputFragment.insertTag(tag);
                }
            }
        });
        onRecomentTags(jobs);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("期望职位");
        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (tagInputFragment != null) {
                    RxBus.getBus().post(new EventIntentJobs(tagInputFragment.getTags()));
                }
                getActivity().onBackPressed();
                return true;
            }
        });
    }

    public void onRecomentTags(List<String> tags) {
        tgRecomend.removeAllViews();
        tgRecomend.setTags(tags);
    }

    @Override protected void onFinishAnimation() {
        super.onFinishAnimation();
        stuff(tagInputFragment);
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
