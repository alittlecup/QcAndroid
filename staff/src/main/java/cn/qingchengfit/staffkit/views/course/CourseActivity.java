package cn.qingchengfit.staffkit.views.course;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.FragCallBack;
import rx.functions.Action1;

public class CourseActivity extends BaseActivity implements FragCallBack {

    public static final int ADD_PRIVATE_COURSE = 3;
    public static final int ADD_GROUP_COURSE = 2;

    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        toolbarLayout.setVisibility(View.GONE);
        int toCourse = getIntent().getIntExtra(Configs.EXTRA_COURSE_TYPE, -1);
        if (toCourse < 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new CourseFragment()).commit();
        } else if (toCourse == 0) {//团课
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new CourseTypeBatchFragmentBuilder(false).build()).commit();
        } else if (toCourse == 1) {//私教
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new CourseTypeBatchFragmentBuilder(true).build()).commit();
        } else if (toCourse == 2) {//新增团课
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, AddCourseFragment.newInstance(false)).commit();
        } else if (toCourse == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, AddCourseFragment.newInstance(true)).commit();
        }
    }

    @Override public int getFragId() {
        return R.id.frag;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
        setBar(bean);
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {
    }
}
