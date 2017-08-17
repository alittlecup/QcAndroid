package com.qingchengfit.fitcoach.fragment.batch;

import android.os.Bundle;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.di.CourseComponent;
import com.qingchengfit.fitcoach.di.CourseModule;
import com.qingchengfit.fitcoach.di.DaggerCourseComponent;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.RestRepository;

public class BatchActivity extends BaseActivity {

    private CourseComponent component;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        ButterKnife.bind(this);
        component = DaggerCourseComponent.builder()
            .courseModule(new CourseModule.Builder().restRepository(new RestRepository(QcCloudClient.getApi()))
                .coachService(getIntent().getParcelableExtra("service"))
                .brand(new Brand(null))
                .build())
            .build();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag, new GymCoursesFragment()).commit();
    }

    public CourseComponent getComponent() {
        return component;
    }
}