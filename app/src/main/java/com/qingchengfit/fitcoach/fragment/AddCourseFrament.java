package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCourseFrament extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gym_addcourse_img)
    ImageView gymAddcourseImg;
    @Bind(R.id.gym_addcourse_img_layout)
    RelativeLayout gymAddcourseImgLayout;
    @Bind(R.id.course_type_private)
    RadioButton courseTypePrivate;
    @Bind(R.id.course_type_group)
    RadioButton courseTypeGroup;
    @Bind(R.id.course_type_rg)
    RadioGroup courseTypeRg;
    @Bind(R.id.course_name)
    CommonInputView courseName;
    @Bind(R.id.course_time)
    CommonInputView courseTime;
    @Bind(R.id.gym_course_detail_layout)
    LinearLayout gymCourseDetailLayout;
    @Bind(R.id.add_gym_course_btn)
    Button addGymCourseBtn;

    public AddCourseFrament() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course_frament, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
