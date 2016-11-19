package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.di.CourseComponent;
import com.qingchengfit.fitcoach.di.CourseModule;
import com.qingchengfit.fitcoach.di.DaggerCourseComponent;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CoachService;

import butterknife.ButterKnife;


public class CourseActivity extends BaseAcitivity {


    private CourseComponent mComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        mComponent = DaggerCourseComponent.builder().courseModule(new CourseModule.Builder()
                .brand(new Brand("2"))
                .coachService(new CoachService())
                .restRepository(new RestRepository(QcCloudClient.getApi()))
                .build()).build();
        mComponent.inject(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag, new CourseFragment())
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
