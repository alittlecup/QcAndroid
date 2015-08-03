package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FastLoginFragment extends Fragment {


    public FastLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fast_login, container, false);
    }


}
