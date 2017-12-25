package cn.qingchengfit.saasbase.course.course.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentCourseBinding;
import cn.qingchengfit.views.FragmentAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;

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
 * Created by Paper on 2017/12/24.
 */
@Leaf(module = "course", path = "/home/brand/") public class CourseHomeInBrandFragment
  extends SaasBaseFragment {
  FragmentCourseBinding db;
  private FragmentAdapter fragmentAdater;
  ArrayList<Fragment> fragments = new ArrayList<>();
  private ToolbarModel toolbarModel = new ToolbarModel("课程种类");
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (fragments.size() == 0) {
      fragments.add(
        cn.qingchengfit.saasbase.course.course.views.CourseListFragment.newInstance(false));
      fragments.add(
        cn.qingchengfit.saasbase.course.course.views.CourseListFragment.newInstance(true));
    }
    fragmentAdater = new FragmentAdapter(getChildFragmentManager(), fragments);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    db = FragmentCourseBinding.inflate(inflater);
    db.viewpager.setAdapter(fragmentAdater);
    db.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(db.myhomeTab));
    db.myhomeTab.setupWithViewPager(db.viewpager);
    db.layoutToolbar.setToolbarModel(toolbarModel);
    initToolbar(db.layoutToolbar.toolbar);
    return db.getRoot();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    //db.layoutToolbar.toolbarTitle.setText("课程种类");
    //db.layoutToolbar.toolbar.inflateMenu(R.menu.menu_add);
    //db.layoutToolbar.toolbar.setOnMenuItemClickListener(item -> {
    //  routeTo("/add/",
    //    AddCourseParams.builder().isPrivate(db.viewpager.getCurrentItem() == 1).build());
    //  return true;
    //});
  }
}
