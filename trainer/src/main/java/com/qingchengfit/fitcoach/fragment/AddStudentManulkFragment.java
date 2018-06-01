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




import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.PhoneEditText;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.component.DialogList;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.AddStudentBean;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddStudentManulkFragment extends Fragment {

	Toolbar toolbar;
	CommonInputView chooseGym;
	CommonInputView chooseName;
	TextView compleGenderLabel;
	RadioButton compleGenderMale;
	RadioButton compleGenderFemale;
	RadioGroup compleGender;
	Button btn;
	TextView hint;
	PhoneEditText choosePhone;
    List<CoachService> systems = new ArrayList<>();
    private List<String> gymStrings = new ArrayList<>();
    private String chooseGymId = "";
    private String chooseGymModel;


    public AddStudentManulkFragment() {
    }

    public static AddStudentManulkFragment newInstance() {

        Bundle args = new Bundle();

        AddStudentManulkFragment fragment = new AddStudentManulkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student_manulk, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      chooseGym = (CommonInputView) view.findViewById(R.id.choose_gym);
      chooseName = (CommonInputView) view.findViewById(R.id.choose_name);
      compleGenderLabel = (TextView) view.findViewById(R.id.comple_gender_label);
      compleGenderMale = (RadioButton) view.findViewById(R.id.comple_gender_male);
      compleGenderFemale = (RadioButton) view.findViewById(R.id.comple_gender_female);
      compleGender = (RadioGroup) view.findViewById(R.id.comple_gender);
      btn = (Button) view.findViewById(R.id.btn);
      hint = (TextView) view.findViewById(R.id.hint);
      choosePhone = (PhoneEditText) view.findViewById(R.id.choose_phone);
      view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onComfirm();
        }
      });
      view.findViewById(R.id.choose_gym).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          chooseGym();
        }
      });

      toolbar.setTitle("添加学员");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //获取用户拥有系统信息
      //QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid).onBackpressureBuffer().subscribeOn(Schedulers.newThread())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(qcCoachSystemResponse -> {
        //            systems = qcCoachSystemResponse.data.services;
        //            boolean hasPrivate = false;
        //            int pos = 0;
        //            for (int i = 0; i < systems.size(); i++) {
        //                CoachService system = systems.get(i);
        //                if (system.model.equalsIgnoreCase("service") && system.type ==1){
        //                    hasPrivate = true;
        //                    pos = i;
        //                }
        //                gymStrings.add(system.name);
        //            }
        //if (hasPrivate){
        //    btn.setVisibility(View.VISIBLE);
        //    hint.setVisibility(View.GONE);
        //    chooseGym.setContent(systems.get(pos).name);
        //}else {
        //    btn.setVisibility(View.GONE);
        //    hint.setVisibility(View.VISIBLE);
        //}

        //
        //
        //}, throwable -> {
        //}, () -> {
        //});

        return view;
    }

 public void onComfirm() {
        if (TextUtils.isEmpty(chooseName.getContent())) {
            ToastUtils.showDefaultStyle("请填写学员姓名");
            return;
        }
        if (!choosePhone.checkPhoneNum()) {
            ToastUtils.showDefaultStyle("请填写正确的手机号");
            return;
        }

        List<AddStudentBean> sss = new ArrayList<>();
        sss.add(new AddStudentBean(chooseName.getContent(), choosePhone.getPhoneNum(), compleGenderMale.isChecked() ? 0 : 1,
            choosePhone.getDistrictInt()));
        PostStudents students = new PostStudents(sss);
        if (getActivity() instanceof FragActivity) {
            QcCloudClient.getApi().postApi.qcAddStudents(App.coachid, students,
                GymUtils.getParams(((FragActivity) getActivity()).getCoachService()))
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcResponse qcResponse) {
                        if (qcResponse.status == ResponseResult.SUCCESS) {
                            getActivity().setResult(1001);
                            getActivity().finish();
                        }
                    }
                });
        }
    }

 public void chooseGym() {
        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择健身房");
        dialogList.list(gymStrings, new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                chooseGym.setContent(gymStrings.get(position));
                if (!chooseGymId.equals(systems.get(position).id)) {
                    chooseGymId = systems.get(position).id;
                    chooseGymModel = systems.get(position).model;
                    LogUtil.e("  " + systems.get(position).model + "   " + systems.get(position).type);
                    if (systems.get(position).type == 1 && systems.get(position).model.equalsIgnoreCase("service")) {
                        btn.setVisibility(View.VISIBLE);
                        hint.setVisibility(View.GONE);
                    } else {
                        btn.setVisibility(View.GONE);
                        hint.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        dialogList.show();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

    }
}
