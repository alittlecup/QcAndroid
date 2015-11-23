package com.qingchengfit.fitcoach.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StudentCompare;
import com.qingchengfit.fitcoach.activity.ChooseStudentActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
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
import rx.Observer;
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
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.student_add)
    Button studentAdd;
    @Bind(R.id.student_no_text)
    TextView studentNoText;
    @Bind(R.id.student_no_img)
    ImageView studentNoImg;
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
    private int curPostion = 0;
    private MaterialDialog mAlertPrivate;
    private List<QcCoachSystem> systems;

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
                searchviewEt.requestFocus();
                AppUtils.showKeyboard(getContext(), searchviewEt);
            } else if (item.getItemId() == R.id.action_add_mannul) {
                showAlert(0);
//                onAddstudent();//手动添加学员
            } else if (item.getItemId() == R.id.action_add_phone) {
//                addStudentFromContact();
                showAlert(1);
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
//                openDrawerInterface.goWeb(sb.toString());
                Intent it = new Intent(getContext(), WebActivity.class);
                it.putExtra("url", sb.toString());
                MyStudentFragment.this.startActivityForResult(it, 404);
            }
        });
        recyclerview.setAdapter(mStudentAdapter);
        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                if (TextUtils.isEmpty(searchviewEt.getText()))
                    searchviewCancle.performClick();
                return false;
            }
        });
        alphabetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                if (TextUtils.isEmpty(searchviewEt.getText()))
                    searchviewCancle.performClick();
                return false;
            }
        });
        //获取原始数据
//        QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid).subscribeOn(Schedulers.io())
//                .subscribe(qcAllStudentResponse -> {
//                    mQcAllStudentResponse = qcAllStudentResponse;
//                    handleResponse(qcAllStudentResponse);
//                });
        freshData();
        alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override
            public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    if (alphabetSort.get("~") != null)
                        mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                }
            }
        });
        //初始化下拉刷新
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    freshData();
            }
        });
        //默认刷新
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.setRefreshing(true);
                refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void freshData() {
        refresh.setRefreshing(true);
        //获取原始数据
        QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid).subscribeOn(Schedulers.io())
                .subscribe(new Observer<QcAllStudentResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcAllStudentResponse qcAllStudentResponse) {
                        mQcAllStudentResponse = qcAllStudentResponse;
                        handleResponse(qcAllStudentResponse);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            int x = curPostion;
            setUpNaviSpinner();
            spinnerNav.setSelection(x);
            //获取原始数据
            freshData();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        int x = curPostion;
        setUpNaviSpinner();
        spinnerNav.setSelection(x);
        freshData();
    }

    /**
     * 从通讯录读取学员
     */
    private void addStudentFromContact() {
        startActivityForResult(new Intent(getContext(), ChooseStudentActivity.class), 400);
    }

    /**
     * 初始化搜索组件
     */
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
                if (s.length() > 0) {
                    searchviewClear.setVisibility(View.VISIBLE);

                } else searchviewClear.setVisibility(View.GONE);
                Observable.just("")
                        .subscribe(s1 -> {
                            keyWord = s.toString().trim();
                            handleResponse(mQcAllStudentResponse);
                        });
            }


        });
    }

    //处理http结果
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
                bean.username = student.username;
                bean.systemUrl = ship.system.url;
                bean.id = student.id;
                bean.color = ship.system.color;
                if (TextUtils.isEmpty(student.head) || !AlphabetView.Alphabet.contains(student.head)) {
                    bean.head = "~";
                } else {
                    bean.head = student.head.toUpperCase();
                }

                StringBuffer sb = new StringBuffer();
                sb.append("联系电话:").append(student.phone);
                bean.phone = sb.toString();
                if (student.gender.equalsIgnoreCase("0"))
                    bean.gender = true;
                else bean.gender = false;
                if (TextUtils.isEmpty(keyWord) || bean.username.contains(keyWord)
                        || bean.gymStr.contains(keyWord) || bean.phone.contains(keyWord))
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
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (adapterData.size() == 0) {
                    studentNoLayout.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(keyWord)) {
                        studentNoImg.setVisibility(View.GONE);
                        studentNoText.setText("没有找到相关学员\n您可以添加该学员");
                    } else {
                        studentNoImg.setVisibility(View.VISIBLE);
                        studentNoText.setText("您的所属健身房、个人健身房的学员将会显示在这里");
                    }
                    if (curPostion > 0) {
//                        studentNoText.setText();

                    }
                } else {
                    studentNoLayout.setVisibility(View.GONE);
                    mStudentAdapter.notifyDataSetChanged();
                }
                refresh.setRefreshing(false);
            });
        }
    }

    public void showAlert(int type) {
        String privateName = "";
        boolean hasPrivate = false;
        if (systems != null && systems.size() > 0) {

            for (QcCoachSystem system : systems) {
                if (system.is_personal_system) {
                    hasPrivate = true;
                    privateName = system.name;
                    break;
                }
            }
        }
        if (hasPrivate) {
            mAlertPrivate = new MaterialDialog.Builder(getContext())
//                        .title("添加学员")
//                        .content("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续")
                    .positiveColorRes(R.color.orange)
                    .positiveText("确认导入")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            if (type == 0) {
                                onAddstudent();//手动添加学员
                            } else if (type == 1) {
                                addStudentFromContact();
                            }
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();


                        }
                    })
                    .build();
            if (type == 0) {
                onAddstudent();
                return;
//                mAlertPrivate.setTitle("添加学员到个人健身房");
//                mAlertPrivate.setContent("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续?");
            } else if (type == 1) {
                mAlertPrivate.setContent("只能导入到「" + privateName + "」");
//                    mAlertPrivate.setContent("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续?");
            }
            mAlertPrivate.show();

        } else {
            mAlertPrivate = new MaterialDialog.Builder(getContext())
//                        .title("没有个人健身房")
                    .content("您还没有添加个人健身房,是否添加个人健身房?")
                    .positiveText("添加个人健身房")
                    .positiveColorRes(R.color.orange)
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), FragActivity.class);
                            intent.putExtra("type", 3);
                            MyStudentFragment.this.startActivityForResult(intent, 405);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();

                        }
                    })
                    .build();
            mAlertPrivate.show();
        }


    }

    //初始化筛选器
    public void setUpNaviSpinner() {

        spinnerBeans = new ArrayList<>();
        spinnerBeans.add(new SpinnerBean("", "所有学员", true));

        String systemStr = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "systems", "");
        if (!TextUtils.isEmpty(systemStr)) {
            QcCoachSystemResponse qcCoachSystemResponse = new Gson().fromJson(systemStr, QcCoachSystemResponse.class);
            systems = qcCoachSystemResponse.date.systems;
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
                curPostion = position;
                curSystemId = spinnerBeanArrayAdapter.getItem(position).id;
                handleResponse(mQcAllStudentResponse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    //新增学员
    @OnClick(R.id.student_add)
    public void onAddstudent() {
//        openDrawerInterface.goWeb(Configs.Server + "mobile/coaches/add/students/");
        Intent it = new Intent(getContext(), WebActivity.class);
        it.putExtra("url", Configs.Server + "mobile/coaches/add/students/");
        MyStudentFragment.this.startActivityForResult(it, 404);
    }


    //返回页面时刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 1000) {
            freshData();
        }
    }

    //搜索栏清除按钮
    @OnClick(R.id.searchview_clear)
    public void onClear() {
        searchviewEt.setText("");
        handleResponse(mQcAllStudentResponse);
    }

    //取消搜索
    @OnClick(R.id.searchview_cancle)
    public void onClickCancel() {

        if (searchviewEt.getText().toString().length() > 0) {
            searchviewEt.setText("");
            handleResponse(mQcAllStudentResponse);
        }
        AppUtils.hideKeyboard(getActivity());
        searchview.setVisibility(View.GONE);

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
        @Bind(R.id.item_student_divider)
        View itemStudentDivder;
        @Bind(R.id.item_student_header_loop)
        RelativeLayout itemHeaderLoop;

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
            if (datas.size() == 0)
                return;
            StudentBean studentBean = datas.get(position);

            holder.itemStudentGymname.setText(studentBean.gymStr);
            holder.itemStudentName.setText(studentBean.username);
            holder.itemStudentPhonenum.setText(studentBean.phone);
            holder.itemHeaderLoop.setBackground(new LoopView(studentBean.color));
            if (studentBean.gender) {//男
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_male);
            } else {
                holder.itemStudentGender.setImageResource(R.drawable.ic_gender_signal_female);
            }
            if (position < datas.size() - 2 && !TextUtils.equals(studentBean.head, datas.get(position + 1).head)) {
                holder.itemStudentDivder.setVisibility(View.GONE);
            } else {
                holder.itemStudentDivder.setVisibility(View.VISIBLE);
            }

            if (studentBean.isTag) {
                if (TextUtils.equals(studentBean.head, "~"))
                    holder.itemStudentAlpha.setText("#");
                else holder.itemStudentAlpha.setText(studentBean.head);
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
