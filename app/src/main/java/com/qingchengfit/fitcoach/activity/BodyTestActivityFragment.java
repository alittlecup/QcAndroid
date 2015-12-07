package com.qingchengfit.fitcoach.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BodyTestActivityFragment extends Fragment {

    public BodyTestActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_body_test2, container, false);
    }
}
