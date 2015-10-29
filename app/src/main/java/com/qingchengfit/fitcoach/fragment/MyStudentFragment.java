package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StudentCompare;
import com.qingchengfit.fitcoach.activity.ChooseStudentActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.component.AlphabetView;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStudentFragment extends MainBaseFragment {
    public static final String TAG = MyStudentFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.searchview_et)
    EditText searchviewEt;
    @Bind(R.id.searchview_clear)
    ImageView searchviewClear;
    @Bind(R.id.searchview_cancle)
    Button searchviewCancle;
    @Bind(R.id.searchview)
    LinearLayout searchview;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.spinner_nav)
    Spinner spinnerNav;
    @Bind(R.id.student_no_layout)
    LinearLayout studentNoLayout;
    @Bind(R.id.alphabetview)
    AlphabetView alphabetView;
    private LinearLayoutManager mLinearLayoutManager;
    private QcAllStudentResponse mQcAllStudentResponse;
    private List<StudentBean> adapterData = new ArrayList<>();
    private StudentAdapter mStudentAdapter;
    private String keyWord;//搜索关键字
    private HashMap<String, Integer> alphabetSort = new HashMap<>();
    /**
     * 初始化spinner
     */
    private ArrayList<SpinnerBean> spinnerBeans;
    private List<Integer> mSystemsId = new ArrayList<>();
    private ArrayAdapter<SpinnerBean> spinnerBeanArrayAdapter;
    private int curSystemId;     //当前选中system

    public MyStudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_student, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_navi);
        toolbar.setNavigationOnClickListener(v -> openDrawerInterface.onOpenDrawer());
        toolbar.inflateMenu(R.menu.menu_students);
        toolbar.setOverflowIcon(getContext().getResources().getDrawable(R.drawable.ic_action_add));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                searchview.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.action_add_mannul) {
                onAddstudent();//手动添加学员
            } else if (item.getItemId() == R.id.action_add_phone) {
                addStudentFromContact();
            }
            return true;
        });
        setUpNaviSpinner();
        setUpSeachView();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLinearLayoutManager);
        mStudentAdapter = new StudentAdapter(adapterData);
        mStudentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                StringBuffer sb = new StringBuffer();
                sb.append(adapterData.get(pos).systemUrl)
                        .append("/mobile/students/")
                        .append(adapterData.get(pos).id)
                        .append("/details/");
                openDrawerInterface.goWeb(sb.toString());
            }
        });
        recyclerview.setAdapter(mStudentAdapter);
        //获取原始数据
        QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid).subscribeOn(Schedulers.io())
                .subscribe(qcAllStudentResponse -> {
                    mQcAllStudentResponse = qcAllStudentResponse;
                    handleResponse(qcAllStudentResponse);
                });
        alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override
            public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                }
            }
        });
        return view;
    }

    /**
     * 从通讯录读取学员
     */
    private void addStudentFromContact() {
        startActivityForResult(new Intent(getContext(), ChooseStudentActivity.class), 400);
//        openDrawerInterface.showLoading();
//        Observable.just("")
//                .subscribeOn(Schedulers.io())
//                .map(new Func1<String, String>() {
//                    @Override
//                    public String call(String s) {
//                        return new Gson().toJson(PhoneFuncUtils.initContactList(getContext()));
//                    }
//                })
//                .flatMap(new Func1<String, Observable<QcResponse>>() {
//                    @Override
//                    public Observable<QcResponse> call(String s) {
//                        return QcCloudClient.getApi().postApi.qcPostCreatStudents(App.coachid, new PostStudents(s)).subscribeOn(Schedulers.io());
//                    }
//                }).subscribe(new Observer<QcResponse>() {
//            @Override
//            public void onCompleted() {
//                getActivity().runOnUiThread(() -> {
//                    openDrawerInterface.hideLoading();
//                    //获取原始数据
//                    QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid).subscribeOn(Schedulers.io())
//                            .subscribe(qcAllStudentResponse -> {
//                                mQcAllStudentResponse = qcAllStudentResponse;
//                                handleResponse(qcAllStudentResponse);
//                            });
//                    Toast.makeText(getContext(), "导入联系人完成", Toast.LENGTH_SHORT).show();
//                });
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                getActivity().runOnUiThread(() -> {
//                    openDrawerInterface.hideLoading();
//                    Toast.makeText(getContext(), "导入联系人失败,请稍后再试", Toast.LENGTH_SHORT).show();
//                });
//            }
//
//            @Override
//            public void onNext(QcResponse qcResponse) {
//
//            }
//        });

    }




    private void setUpSeachView() {
        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Observable.just("")
                        .subscribe(s1 -> {
                            keyWord = s.toString().trim();
                            handleResponse(mQcAllStudentResponse);
                        });
            }


        });
    }

    private void handleResponse(QcAllStudentResponse qcAllStudentResponse) {
        if (qcAllStudentResponse == null)
            return;
        adapterData.clear();

        List<QcAllStudentResponse.Ship> ships = qcAllStudentResponse.data.ships;
        for (int i = 0; i < ships.size(); i++) {
            QcAllStudentResponse.Ship ship = ships.get(i);
            if (curSystemId != 0 && curSystemId != ship.system.id)
                continue;
            List<StudentBean> tmp = new ArrayList<>();
            for (QcStudentBean student : ship.users) {
                StudentBean bean = new StudentBean();
                bean.gymStr = ship.system.name;
                bean.headerPic = student.avatar;
                bean.name = student.username;
                bean.systemUrl = ship.system.url;
                bean.id = student.id;
                if (TextUtils.isEmpty(student.head)) {
                    bean.head = "~";
                } else {
                    bean.head = student.head.toUpperCase();
                }

                StringBuffer sb = new StringBuffer();
                sb.append("联系电话:").append(student.phone);
                bean.phoneStr = sb.toString();
                if (student.gender.equalsIgnoreCase("0"))
                    bean.gender = true;
                else bean.gender = false;
                if (TextUtils.isEmpty(keyWord) || bean.name.contains(keyWord)
                        || bean.gymStr.contains(keyWord) || bean.phoneStr.contains(keyWord))
                    tmp.add(bean);
            }
            adapterData.addAll(tmp);
        }

        if (adapterData.size() > 0) {
            alphabetSort.clear();
            Collections.sort(adapterData, new StudentCompare());
            String tag = "";
            for (int i = 0; i < adapterData.size(); i++) {
                StudentBean bean = adapterData.get(i);
                if (!bean.head.equalsIgnoreCase(tag)) {
                    bean.isTag = true;
                    tag = bean.head;
                    alphabetSort.put(tag, i);
                } else bean.isTag = false;
            }
        }

        getActivity().runOnUiThread(() -> {
            if (adapterData.size() == 0) {
                studentNoLayout.setVisibility(View.VISIBLE);
            } else {
                studentNoLayout.setVisibility(View.GONE);
                mStudentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setUpNaviSpinner() {

        spinnerBeans = new ArrayList<>();
        spinnerBeans.add(new SpinnerBean("", "所有学员", true));

        String systemStr = PreferenceUtils.getPrefString(App.AppContex, "systems", "");
        if (!TextUtils.isEmpty(systemStr)) {
            QcCoachSystemResponse qcCoachSystemResponse = new Gson().fromJson(systemStr, QcCoachSystemResponse.class);
            List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
            mSystemsId.clear();
            for (int i = 0; i < systems.size(); i++) {
                QcCoachSystem system = systems.get(i);
                spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id));
                mSystemsId.add(system.id);
            }
        }

        spinnerBeanArrayAdapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_checkview, parent, false);
                }
                ((TextView) convertView).setText(spinnerBeans.get(position).text);
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
                }
                SpinnerBean bean = getItem(position);
                ((TextView) convertView.findViewById(R.id.spinner_tv)).setText(bean.text);
                if (bean.isTitle) {
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.VISIBLE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setImageDrawable(new LoopView(bean.color));
                }
                return convertView;
            }
        };
        spinnerBeanArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerNav.setAdapter(spinnerBeanArrayAdapter);
        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curSystemId = spinnerBeanArrayAdapter.getItem(position).id;
                handleResponse(mQcAllStudentResponse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @OnClick(R.id.student_add)
    public void onAddstudent() {
//        openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/add/students/");
        Intent it = new Intent(getContext(), WebActivity.class);
        it.putExtra("url", Configs.Server + "mobile/coaches/add/students/");
        startActivityForResult(it, 404);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {
            //获取原始数据
            QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid).subscribeOn(Schedulers.io())
                    .subscribe(qcAllStudentResponse -> {
                        mQcAllStudentResponse = qcAllStudentResponse;
                        handleResponse(qcAllStudentResponse);
                    });
        }
    }

    @OnClick(R.id.searchview_clear)
    public void onClear() {
        searchviewEt.setText("");
        handleResponse(mQcAllStudentResponse);
    }


    @OnClick(R.id.searchview_cancle)
    public void onClickCancel() {

        if (searchviewEt.getText().toString().length() > 0) {
            searchviewEt.setText("");
            handleResponse(mQcAllStudentResponse);
        }

        searchview.setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * recycle adapter
     */
    public static class StudentsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_student_header)
        ImageView itemStudentHeader;
        @Bind(R.id.item_student_name)
        TextView itemStudentName;
        @Bind(R.id.item_student_phonenum)
        TextView itemStudentPhonenum;
        @Bind(R.id.item_student_gymname)
        TextView itemStudentGymname;
        @Bind(R.id.item_student_gender)
        ImageView itemStudentGender;
        @Bind(R.id.item_student_alpha)
        TextView itemStudentAlpha;

        public StudentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentsHolder> implements View.OnClickListener {
        List<StudentBean> datas;
        private OnRecycleItemClickListener listener;

        public StudentAdapter(List<StudentBean> d) {
            this.datas = d;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StudentsHolder holder = new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemView.setTag(position);
            StudentBean studentBean = datas.get(position);

            holder.itemStudentGymname.setText(studentBean.gymStr);
            holder.itemStudentName.setText(studentBean.name);
            holder.itemStudentPhonenum.setText(studentBean.phoneStr);
            if (studentBean.gender) {//男
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
            } else {
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
            }
            if (studentBean.isTag) {
                holder.itemStudentAlpha.setText(studentBean.head);
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else holder.itemStudentAlpha.setVisibility(View.GONE);
            Glide.with(App.AppContex).load(studentBean.headerPic).asBitmap().into(new CircleImgWrapper(holder.itemStudentHeader, App.AppContex));

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }

}
