package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.di.CourseComponent;
import com.qingchengfit.fitcoach.di.CourseModule;
import com.qingchengfit.fitcoach.di.DaggerCourseComponent;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.RestRepository;


public class CourseActivity extends BaseAcitivity {
    public static final int TO_CHOOSE = 1;

    private CourseComponent mComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        mComponent = DaggerCourseComponent.builder().courseModule(new CourseModule.Builder()
                .brand(new Brand())
                .coachService(getIntent().getParcelableExtra("service"))
                .restRepository(new RestRepository(QcCloudClient.getApi()))
                .build()).build();
        mComponent.inject(this);
        Fragment f = new CourseFragment();
        int to = getIntent().getIntExtra("to",0);
        switch (to){
            case TO_CHOOSE:
                f = new ChooseCourseFragmentBuilder(getIntent().getIntExtra("type", Configs.TYPE_GROUP)).build();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag, f)
                .commit();
    }

    public CourseComponent getComponent() {
        return mComponent;
    }
//
//    @Override
//    public int getFragId() {
//        return R.id.frag;
//    }
//
//    @Override
//    public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
//        ToolbarBean bean = new ToolbarBean(title, showRight, titleClick, menu, listener);
//        setBar(bean);
//    }
//
//
//
//    @Override
//    public void cleanToolbar() {
//
//    }
//
//    @Override
//    public void openSeachView(String hint, Action1<CharSequence> action1) {
//
//    }
//
//    @Override
//    public void onChangeFragment(BaseFragment fragment) {
//
//    }
//
//    @Override
//    public void setBar(ToolbarBean bar) {
//        toolbarTitile.setText(bar.title);
//        down.setVisibility(bar.showRight ? View.VISIBLE : View.GONE);
//        if (bar.onClickListener != null)
//            titileLayout.setOnClickListener(bar.onClickListener);
//        else titileLayout.setOnClickListener(null);
//        toolbar.getMenu().clear();
//        if (bar.menu != 0) {
//            toolbar.inflateMenu(bar.menu);
//            toolbar.setOnMenuItemClickListener(bar.listener);
//        }
//    }
}
