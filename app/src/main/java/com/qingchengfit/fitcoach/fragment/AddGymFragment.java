package com.qingchengfit.fitcoach.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CitiesChooser;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddGymBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGymFragment extends Fragment {
    public static final String TAG = AddGymFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.addgym_name)
    CommonInputView addgymName;
    @Bind(R.id.addgym_contact)
    CommonInputView addgymContact;
    @Bind(R.id.addgym_city)
    CommonInputView addgymCity;
    @Bind(R.id.workexpedit_descripe)
    EditText workexpeditDescripe;
    @Bind(R.id.addgym_addbtn)
    Button addgymAddbtn;
    @Bind(R.id.rootview)
    LinearLayout rootview;

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    private OptionsPopupWindow pwOptions;

    private CitiesChooser citiesChooser;
    private int Districtid;
    private SearchInterface searchListener;

    public AddGymFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citiesChooser = new CitiesChooser(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gym, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setTitle("添加健身房");
        citiesChooser.setOnCityChoosenListener(new CitiesChooser.OnCityChoosenListener() {
            @Override
            public void onCityChoosen(String provice, String city, String district, int id) {
                Districtid = id;
                addgymCity.setContent(provice + city + "市");
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addgym_city)
    public void onClickCtiy() {
        citiesChooser.show(rootview);
    }

    @OnClick(R.id.addgym_addbtn)
    public void onClickAdd() {

        if (TextUtils.isEmpty(addgymName.getContent()))
            return;

        AddGymBean bean = new AddGymBean(addgymName.getContent(), Districtid, addgymContact.getContent(), "descripe");
        QcCloudClient.getApi().postApi.qcAddGym(bean).subscribeOn(Schedulers.newThread()).subscribe(qcAddGymResponse -> {
            getActivity().runOnUiThread(() -> {
                if (qcAddGymResponse.status == ResponseResult.SUCCESS) {
                    searchListener.onSearchResult(Integer.parseInt(qcAddGymResponse.data.gym.id), qcAddGymResponse.data.gym.name);
                } else {
                    Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchInterface) {
            searchListener = (SearchInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchListener = null;
    }
}