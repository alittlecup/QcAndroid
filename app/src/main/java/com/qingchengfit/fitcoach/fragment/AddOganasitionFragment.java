package com.qingchengfit.fitcoach.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.SearchInterface;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.OrganizationBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOganasitionFragment extends Fragment {
    public static final String TAG = AddOganasitionFragment.class.getName();
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

    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private SearchInterface searchListener;


    public AddOganasitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_gym, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setTitle("添加主办机构");
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        addgymName.setLabel("机构名");
        addgymCity.setVisibility(View.GONE);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.decript_layout)
    public void onDescripte() {
        workexpeditDescripe.requestFocus();
    }

    @OnClick(R.id.addgym_addbtn)
    public void onClickAdd() {
        if (TextUtils.isEmpty(addgymName.getContent()))
            return;

        QcCloudClient.getApi().postApi.qcAddOrganization(new OrganizationBean(addgymName.getContent(), addgymContact.getContent(), "description"))
                .subscribeOn(Schedulers.newThread())
                .subscribe(qcResponse -> {
                    getActivity().runOnUiThread(() -> {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
//                           searchListener.onSearchResult();
                        } else Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
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
