package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TagGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentJudgeFragment extends Fragment {


    @Bind(R.id.tag_group)
    TagGroup tagGroup;

    public StudentJudgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_judge, container, false);
        ButterKnife.bind(this, view);
        tagGroup.setTags(new String[]{"上课很有意思(1)", "上课(1)", "上(1)", "上(1)", "上(1)", "上(1)", "上课很有意思(1)", "上课很有意思(1)"});
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
