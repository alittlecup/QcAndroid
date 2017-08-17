package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.qingchengfit.fitcoach.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBelongGymFragment extends Fragment {
    public static final String TAG = AddBelongGymFragment.class.getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.add_gym_private_btn) Button addGymPrivateBtn;
    private Unbinder unbinder;

    public AddBelongGymFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_belong_gym, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("添加所属健身房");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }

    @OnClick(R.id.add_gym_private_btn) public void onClick() {
        getActivity().onBackPressed();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}