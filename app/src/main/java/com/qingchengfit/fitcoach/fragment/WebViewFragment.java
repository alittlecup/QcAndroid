package com.qingchengfit.fitcoach.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qingchengfit.fitcoach.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }
}
