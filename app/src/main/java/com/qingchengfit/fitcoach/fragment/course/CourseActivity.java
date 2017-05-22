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
import com.qingchengfit.fitcoach.fragment.batch.list.CourseBatchDetailFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.RestRepository;

public class CourseActivity extends BaseAcitivity {
    public static final int TO_CHOOSE = 1;
    public static final int TO_CHOOSE_PLAN = 2;
    public static final int TO_GROUP_BATCH = 3;
    public static final int TO_PRIVATE_BATCH = 4;

    private CourseComponent mComponent;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        mComponent = DaggerCourseComponent.builder().courseModule(new CourseModule.Builder().brand(new Brand())
                .coachService(getIntent().getParcelableExtra("service"))
                .restRepository(new RestRepository(QcCloudClient.getApi())).build()).build();
        mComponent.inject(this);
        Fragment f = new CourseFragment();
        int to = getIntent().getIntExtra("to", 0);
        switch (to) {
            case TO_CHOOSE:
                f = new ChooseCourseFragmentBuilder(getIntent().getIntExtra("type", Configs.TYPE_GROUP)).build();
                break;
            case TO_CHOOSE_PLAN:
                f = ChooseCoursePlanFragment.newInstance(getIntent().getLongExtra("id", 0));
                break;
            case TO_GROUP_BATCH:
                f = CourseBatchDetailFragment.newInstance(Configs.TYPE_GROUP);
                break;
            case TO_PRIVATE_BATCH:
                f = CourseBatchDetailFragment.newInstance(Configs.TYPE_PRIVATE);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, f).commit();
    }

    public CourseComponent getComponent() {
        return mComponent;
    }
}
