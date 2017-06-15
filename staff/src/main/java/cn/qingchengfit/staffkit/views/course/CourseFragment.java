package cn.qingchengfit.staffkit.views.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
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
 * Created by Paper on 16/8/2.
 */
public class CourseFragment extends BaseFragment {

    @BindView(R.id.myhome_tab) TabLayout myhomeTab;
    @BindView(R.id.viewpager) ViewPager viewpager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    private FragmentAdapter fragmentAdater;
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_add:
                    boolean isPrivate = viewpager.getCurrentItem() == 1;

                    if ((isPrivate && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRISETTING_CAN_WRITE)) || (!isPrivate
                        && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMSETTING_CAN_WRITE))) {
                        showAlert(R.string.alert_permission_forbid);
                    } else {
                        addCourse(isPrivate);
                    }
                    break;
                case R.id.action_edit:

                    for (int i = 0; i < fragments.size(); i++) {
                        if (fragmentAdater.getItem(i) instanceof CourseListFragment) ((CourseListFragment) fragments.get(i)).setEditMode();
                    }
                    mCallbackActivity.setToolbar("课程种类", false, null, R.menu.menu_compelete, menuItemClickListener);
                    break;
                case R.id.action_complete:
                    for (int i = 0; i < fragments.size(); i++) {
                        if (fragments.get(i) instanceof CourseListFragment) ((CourseListFragment) fragments.get(i)).cancelEdit();
                    }
                    mCallbackActivity.setToolbar("课程种类", false, null, R.menu.menu_course_list_nomal, menuItemClickListener);
                    break;
            }
            return true;
        }
    };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);

        if (fragments.size() == 0) {
            fragments.add(CourseListFragment.newInstance(false));
            fragments.add(CourseListFragment.newInstance(true));
        }
        fragmentAdater = new FragmentAdapter(getChildFragmentManager(), fragments);
        viewpager.setAdapter(fragmentAdater);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(viewpager);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        //mCallbackActivity.setToolbar("课程种类", false, null, R.menu.menu_course_list_nomal, menuItemClickListener);
        toolbarTitile.setText("课程种类");
        toolbar.inflateMenu(R.menu.menu_course_list_nomal);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_edit) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof CourseListFragment) ((CourseListFragment) fragments.get(i)).setEditMode();
            }
        }
        return true;
    }

    public void addCourse(boolean isP) {
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), AddCourseFragment.newInstance(isP))
            .addToBackStack(getFragmentName())
            .commit();
    }

    @Override public String getFragmentName() {
        return CourseFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
