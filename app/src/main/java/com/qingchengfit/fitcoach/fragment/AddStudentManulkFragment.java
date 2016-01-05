package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddStudentBean;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddStudentManulkFragment extends Fragment {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.choose_gym)
    CommonInputView chooseGym;
    @Bind(R.id.choose_name)
    CommonInputView chooseName;
    @Bind(R.id.comple_gender_label)
    TextView compleGenderLabel;
    @Bind(R.id.comple_gender_male)
    RadioButton compleGenderMale;
    @Bind(R.id.comple_gender_female)
    RadioButton compleGenderFemale;
    @Bind(R.id.comple_gender)
    RadioGroup compleGender;
    @Bind(R.id.choose_phone)
    CommonInputView choosePhone;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.hint)
    TextView hint;

    private List<String> gymStrings = new ArrayList<>();
    private int chooseGymId = 0;
    private String chooseGymModel;
    List<CoachService> systems = new ArrayList<>();

    public AddStudentManulkFragment() {
    }


    public static AddStudentManulkFragment newInstance() {

        Bundle args = new Bundle();

        AddStudentManulkFragment fragment = new AddStudentManulkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    systems = qcCoachSystemResponse.data.services;

                    for (int i = 0; i < systems.size(); i++) {
                        CoachService system = systems.get(i);
                        gymStrings.add(system.name);
                    }
                }, throwable -> {
                }, () -> {
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student_manulk, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("添加学员");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (systems != null && systems.size() >0){
            if (systems.get(0).model.equalsIgnoreCase("service") && systems.get(0).type == 1){
                btn.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
            }else {
                btn.setVisibility(View.GONE);
                hint.setVisibility(View.VISIBLE);
            }
            chooseGym.setContent(systems.get(0).name);
        }
        return view;
    }

    @OnClick(R.id.btn)
    public void onComfirm(){
        if (TextUtils.isEmpty(chooseName.getContent())){
            ToastUtils.showDefaultStyle("请填写学员姓名");
            return;
        }
        if (TextUtils.isEmpty(choosePhone.getContent())){
            ToastUtils.showDefaultStyle("请填写学员手机");
            return;
        }
        List<AddStudentBean> sss = new ArrayList<>();
        sss.add(new AddStudentBean(chooseName.getContent(),choosePhone.getContent(),compleGenderMale.isChecked()?1:0));
        PostStudents students = new PostStudents(sss);
        QcCloudClient.getApi().postApi.qcAddStudents(App.coachid
                ,students)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS){
                            getActivity().onBackPressed();
                        }
                    }
                });
    }


    @OnClick(R.id.choose_gym)
    public void chooseGym() {
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        dialogList.list(gymStrings, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialogList.dismiss();
                        chooseGym.setContent(gymStrings.get(position));
                        if (chooseGymId != systems.get(position).id) {
                            chooseGymId = (int)systems.get(position).id;
                            chooseGymModel = systems.get(position).model;
                            LogUtil.e("  "+systems.get(position).model+"   "+systems.get(position).type);
                            if (systems.get(position).type == 1 && systems.get(position).model.equalsIgnoreCase("service")){
                                btn.setVisibility(View.VISIBLE);
                                hint.setVisibility(View.GONE);
                            }else {
                                btn.setVisibility(View.GONE);
                                hint.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );
        dialogList.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
