package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSelfGymFragment extends Fragment {
    public static final String TAG = AddSelfGymFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.addselfgym_name)
    CommonInputView addselfgymName;
    @Bind(R.id.addselfgym_time)
    CommonInputView addselfgymTime;
    @Bind(R.id.addselfgym_comfirm)
    Button addselfgymComfirm;

    int id = -1;
    public AddSelfGymFragment() {
    }

    public static AddSelfGymFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);

        AddSelfGymFragment fragment = new AddSelfGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_self_gym, container, false);
        ButterKnife.bind(this, view);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        if (id > 0) {
            toolbar.setTitle("修改个人健身房");
        } else {
            toolbar.setTitle("添加个人健身房");
        }
        return view;
    }


    @OnClick(R.id.addselfgym_comfirm)
    public void onComfirm() {

    }

    @OnClick(R.id.addselfgym_time)
    public void onChangeTime() {
//        startActivityForResult();
        getFragmentManager().beginTransaction().
                replace(R.id.web_frag_layout, new GymTimeFragment())
                .addToBackStack(null)
                .commit();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
