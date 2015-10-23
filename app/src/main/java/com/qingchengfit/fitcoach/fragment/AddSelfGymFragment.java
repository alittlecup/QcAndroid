package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.GymCompare;
import com.qingchengfit.fitcoach.activity.ChangeTimeActivity;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private QcPrivateGymReponse reponse;
    private PostPrivateGym postPrivateGym;
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
        postPrivateGym = new PostPrivateGym();

        QcCloudClient.getApi().getApi.qcGetPrivateGym(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(qcPrivateGymReponse -> {
                    reponse = qcPrivateGymReponse;
                    postPrivateGym.open_time = new Gson().toJson(qcPrivateGymReponse.data.system.openTimes);
                    postPrivateGym.name = qcPrivateGymReponse.data.system.name;
                    Collections.sort(reponse.data.system.openTimes, new GymCompare());
                    StringBuffer sb = new StringBuffer();
                    for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                        if (openTime.day == 1) {
                            sb.append("周一").append(openTime.start).append("-")
                                    .append(openTime.end).append(",").append("周二...");
                            break;
                        }
                    }
                    return sb.toString();
                })
                .subscribe(s -> {
                    addselfgymName.setContent(reponse.data.system.name);
                    addselfgymTime.setContent(s);
                });
        return view;
    }


    @OnClick(R.id.addselfgym_comfirm)
    public void onComfirm() {
        QcCloudClient.getApi().postApi.qcPostPrivateGym(App.coachid, postPrivateGym)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qcResponse -> {
                    if (qcResponse.status == ResponseResult.SUCCESS) {
                        Toast.makeText(App.AppContex, "修改成功", Toast.LENGTH_SHORT);
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(App.AppContex, "修改失败", Toast.LENGTH_SHORT);
                    }
                });

    }


    @OnClick(R.id.addselfgym_time)
    public void onChangeTime() {
        Intent it = new Intent(getActivity(), ChangeTimeActivity.class);
        it.putExtra("time", new Gson().toJson(reponse.data.system.openTimes));
        startActivityForResult(it, 222);
        getActivity().overridePendingTransition(R.anim.timepicker_anim_enter_bottom, R.anim.slide_hold);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {
            postPrivateGym.open_time = data.getStringExtra("time");
            Type listType = new TypeToken<ArrayList<QcPrivateGymReponse.OpenTime>>() {
            }.getType();

            reponse.data.system.openTimes = new Gson().fromJson(postPrivateGym.open_time, listType);
            StringBuffer sb = new StringBuffer();
            for (QcPrivateGymReponse.OpenTime openTime : reponse.data.system.openTimes) {
                if (openTime.day == 1) {
                    sb.append("周一").append(openTime.start).append("-")
                            .append(openTime.end).append(",").append("周二...");
                    break;
                }
            }
            addselfgymTime.setContent(sb.toString());

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
