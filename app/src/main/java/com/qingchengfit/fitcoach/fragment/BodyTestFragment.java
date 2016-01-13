package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BodyTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BodyTestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.img_model)
    ImageView imgModel;
    @Bind(R.id.upper)
    TextView upper;
    @Bind(R.id.hipline)
    TextView hipline;
    @Bind(R.id.chest)
    TextView chest;
    @Bind(R.id.thigh)
    TextView thigh;
    @Bind(R.id.waistline)
    TextView waistline;
    @Bind(R.id.calf)
    TextView calf;
    @Bind(R.id.height)
    TextView height;
    @Bind(R.id.height_layout)
    LinearLayout heightLayout;
    @Bind(R.id.weight)
    TextView weight;
    @Bind(R.id.weight_layout)
    LinearLayout weightLayout;
    @Bind(R.id.bmi)
    TextView bmi;
    @Bind(R.id.bmi_layout)
    LinearLayout bmiLayout;
    @Bind(R.id.body_fat_rate)
    TextView bodyFatRate;
    @Bind(R.id.body_fat_rate_layout)
    LinearLayout bodyFatRateLayout;

    // TODO: Rename and change types of parameters
    private QcBodyTestTemplateRespone.Base mBase;
    private String mParam2;


    public BodyTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment BodyTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BodyTestFragment newInstance(QcBodyTestTemplateRespone.Base base, String param2) {
        BodyTestFragment fragment = new BodyTestFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, base);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBase = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_test, container, false);
        ButterKnife.bind(this, view);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
