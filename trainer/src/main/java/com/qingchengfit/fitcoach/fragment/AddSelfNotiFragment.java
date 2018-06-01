package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;




import com.qingchengfit.fitcoach.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSelfNotiFragment extends Fragment {
    public static final String TAG = AddSelfNotiFragment.class.getName();
	Toolbar toolbar;
	Button addGymPrivateBtn;


    public AddSelfNotiFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_self_noti, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      addGymPrivateBtn = (Button) view.findViewById(R.id.add_gym_private_btn);
      view.findViewById(R.id.add_gym_private_btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onclick();
        }
      });

      toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle("添加个人健身房");
        return view;
    }

 public void onclick() {
        getActivity().onBackPressed();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
