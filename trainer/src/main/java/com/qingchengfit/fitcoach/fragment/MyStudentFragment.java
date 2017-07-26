package com.qingchengfit.fitcoach.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.Utils.StudentCompare;
import com.qingchengfit.fitcoach.activity.ChooseStudentActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.activity.StudentHomeActivity;
import com.qingchengfit.fitcoach.bean.CurentPermissions;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.component.AlphabetView;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStudentFragment extends BaseFragment {
    public static final String TAG = MyStudentFragment.class.getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.spinner_nav) Spinner spinnerNav;
    @BindView(R.id.student_no_layout) LinearLayout studentNoLayout;
    @BindView(R.id.alphabetview) AlphabetView alphabetView;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.student_add) Button studentAdd;
    @BindView(R.id.student_no_text) TextView studentNoText;
    @BindView(R.id.student_no_img) ImageView studentNoImg;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
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
    private String curSystemId;     //当前选中system
    private int curPostion = 0;
    private MaterialDialog mAlertPrivate;
    private List<QcCoachSystem> systems;
    private String curModel;
    private String mTitle;
    private boolean hasPrivate = false;
    private Unbinder unbinder;

    public MyStudentFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_student, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.inflateMenu(R.menu.menu_students);
        mTitle = getString(R.string.mystudents_title);
        toolbarTitle.setText(mTitle);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_add));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                searchview.setVisibility(View.VISIBLE);
                searchviewEt.requestFocus();
                AppUtils.showKeyboard(getContext(), searchviewEt);
            } else if (item.getItemId() == R.id.action_add_mannul) {
                if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }
                onAddstudent();//手动添加学员
            } else if (item.getItemId() == R.id.action_add_phone) {
                if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
                    showAlert(R.string.alert_permission_forbid);
                    return true;
                }
                addStudentFromContact();
            }
            return true;
        });

        setUpSeachView();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLinearLayoutManager);
        mStudentAdapter = new StudentAdapter(adapterData);
        mStudentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent(getContext(), StudentHomeActivity.class);
                it.putExtra("id", adapterData.get(pos).modelid);
                it.putExtra("model", adapterData.get(pos).model);
                it.putExtra("student_id", adapterData.get(pos).id);
                it.putExtra("ship_id", adapterData.get(pos).ship_id);
                it.putExtra("modeltype", adapterData.get(pos).modeltype);
                MyStudentFragment.this.startActivityForResult(it, 404);
            }
        });
        recyclerview.setAdapter(mStudentAdapter);
        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                if (TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
                return false;
            }
        });
        alphabetView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                if (TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
                return false;
            }
        });
        freshData();
        alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    if (alphabetSort.get("~") != null) mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                }
            }
        });
        //初始化下拉刷新
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {

                freshData();
            }
        });
        //默认刷新
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(), this);
                refresh.setRefreshing(true);
            }
        });

        if (getActivity() instanceof FragActivity) {
            if (((FragActivity) getActivity()).getCoachService() != null) {
                CoachService coachService = ((FragActivity) getActivity()).getCoachService();
                toolbarTitle.setText(coachService.getName());
                curModel = coachService.model;
                curSystemId = coachService.getId();
            }
        }
        return view;
    }

    @Override protected void lazyLoad() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void freshData() {
        //        setUpNaviSpinner();
        refresh.setRefreshing(true);
        //获取原始数据
        if (getActivity() instanceof FragActivity) {
            CoachService coachService = ((FragActivity) getActivity()).getCoachService();
            HashMap<String, Object> prams = new HashMap<>();
            prams.put("id", coachService.getId() + "");
            prams.put("model", coachService.getModel());
            QcCloudClient.getApi().getApi.qcGetAllStudent(App.coachid, prams)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<QcAllStudentResponse>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(QcAllStudentResponse qcAllStudentResponse) {
                        mQcAllStudentResponse = qcAllStudentResponse;
                        handleResponse(qcAllStudentResponse);
                    }
                });
        }
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            int x = curPostion;

            spinnerNav.setSelection(x);
            //获取原始数据
            //            setUpNaviSpinner();
        }
    }

    @Override public void onResume() {
        super.onResume();
        int x = curPostion;

        spinnerNav.setSelection(x);
    }

    /**
     * 从通讯录读取学员
     */
    private void addStudentFromContact() {
        Intent to = new Intent(getContext(), ChooseStudentActivity.class);
        if (getActivity() instanceof FragActivity) to.putExtra("service", ((FragActivity) getActivity()).getCoachService());
        startActivityForResult(to, 400);
    }

    @Override public String getFragmentName() {
        return MyStudentFragment.class.getName();
    }

    /**
     * 初始化搜索组件
     */
    private void setUpSeachView() {
        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchviewClear.setVisibility(View.VISIBLE);
                } else {
                    searchviewClear.setVisibility(View.GONE);
                }
                Observable.just("").subscribe(s1 -> {
                    keyWord = s.toString().trim();
                    handleResponse(mQcAllStudentResponse);
                });
            }
        });
    }

    //处理http结果
    private synchronized void handleResponse(QcAllStudentResponse qcAllStudentResponse) {
        if (qcAllStudentResponse == null) return;

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {

                adapterData.clear();

                //List<QcAllStudentResponse.Ship> ships = qcAllStudentResponse.data.ships;
                //for (int i = 0; i < ships.size(); i++) {
                QcAllStudentResponse.Ship ship = qcAllStudentResponse.data;
                //if (curSystemId != 0 && (curSystemId != ship.service.id || !curModel.equals(ship.service.model)))
                //    continue;
                if (ship != null && ship.users != null) {

                    List<StudentBean> tmp = new ArrayList<>();
                    for (QcStudentBean student : ship.users) {
                        StudentBean bean = new StudentBean();
                        bean.gymStr = ship.service.name;
                        bean.headerPic = student.avatar;
                        bean.username = student.username;
                        bean.systemUrl = ship.service.host;
                        bean.id = student.user.id;
                        bean.ship_id = student.id;
                        bean.color = ship.service.color;
                        bean.modelid = ship.service.id + "";
                        bean.model = ship.service.model;
                        bean.modeltype = ship.service.type;
                        if (TextUtils.isEmpty(student.head) || !AlphabetView.Alphabet.contains(student.head)) {
                            bean.head = "~";
                        } else {
                            bean.head = student.head.toUpperCase();
                        }

                        StringBuffer sb = new StringBuffer();
                        sb.append("手机:").append(student.phone);
                        bean.phone = sb.toString();
                        if (student.gender.equalsIgnoreCase("0")) {
                          bean.gender = 0;
                        } else {
                          bean.gender = 1;
                        }
                        if (TextUtils.isEmpty(keyWord)
                            || bean.username.contains(keyWord)
                            || bean.gymStr.contains(keyWord)
                            || bean.phone.contains(keyWord)) {
                            tmp.add(bean);
                        }
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
                        } else {
                            bean.isTag = false;
                        }
                    }
                }

                if (adapterData.size() == 0) {
                    studentNoLayout.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(keyWord)) {
                        studentNoImg.setVisibility(View.GONE);
                        studentNoText.setText("没有找到相关学员\n您可以添加该学员");
                    } else {
                        studentNoImg.setVisibility(View.VISIBLE);
                        studentNoText.setText("暂无学员");
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

    //public void showAlert(int type) {
    //    String privateName = "";
    //    if (hasPrivate) {
    //        mAlertPrivate = new MaterialDialog.Builder(getContext())
    //            //                        .title("添加学员")
    //            //                        .content("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续")
    //            .positiveColorRes(R.color.orange).positiveText("确认导入").negativeText("取消").callback(new MaterialDialog.ButtonCallback() {
    //                @Override public void onPositive(MaterialDialog dialog) {
    //                    super.onPositive(dialog);
    //                    dialog.dismiss();
    //                    if (type == 0) {
    //                        onAddstudent();//手动添加学员
    //                    } else if (type == 1) {
    //                        addStudentFromContact();
    //                    }
    //                }
    //
    //                @Override public void onNegative(MaterialDialog dialog) {
    //                    super.onNegative(dialog);
    //                    dialog.dismiss();
    //                }
    //            }).build();
    //        if (type == 0) {
    //            onAddstudent();
    //            return;
    //            //                mAlertPrivate.setTitle("添加学员到个人健身房");
    //            //                mAlertPrivate.setContent("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续?");
    //        } else if (type == 1) {
    //            mAlertPrivate.setContent("只能导入到「" + privateName + "」");
    //            //                    mAlertPrivate.setContent("您只能添加学员到个人健身房,添加所属健身房请联系健身房管理员.是否继续?");
    //        }
    //        mAlertPrivate.show();
    //    } else {
    //        mAlertPrivate = new MaterialDialog.Builder(getContext())
    //            //                        .title("没有个人健身房")
    //            .content("还没有设置健身房信息，是否设置?")
    //            .positiveText("设置健身房")
    //            .positiveColorRes(R.color.orange)
    //            .negativeText("取消")
    //            .callback(new MaterialDialog.ButtonCallback() {
    //                @Override public void onPositive(MaterialDialog dialog) {
    //                    super.onPositive(dialog);
    //                    dialog.dismiss();
    //                    Intent intent = new Intent(getActivity(), FragActivity.class);
    //                    intent.putExtra("type", 3);
    //                    MyStudentFragment.this.startActivityForResult(intent, 405);
    //                }
    //
    //                @Override public void onNegative(MaterialDialog dialog) {
    //                    super.onNegative(dialog);
    //                    dialog.dismiss();
    //                }
    //            })
    //            .build();
    //        mAlertPrivate.show();
    //    }
    //}

    //新增学员
    @OnClick(R.id.student_add) public void onAddstudent() {
        Intent intent = new Intent(getActivity(), FragActivity.class);
        intent.putExtra("type", 7);
        if (getActivity() instanceof FragActivity) {
            intent.putExtra("service", ((FragActivity) getActivity()).getCoachService());
        }
        MyStudentFragment.this.startActivityForResult(intent, 405);
    }

    //返回页面时刷新
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 1000) {
            freshData();
        } else if (resultCode > 0 && requestCode == 501) {
            toolbarTitle.setText(data.getStringExtra("name"));
            curModel = data.getStringExtra("model");
            curSystemId = data.getStringExtra("id");
            LogUtil.e("curModel:" + curModel + "   id:" + curSystemId);
            handleResponse(mQcAllStudentResponse);
        }
    }

    //搜索栏清除按钮
    @OnClick(R.id.searchview_clear) public void onClear() {
        searchviewEt.setText("");
        handleResponse(mQcAllStudentResponse);
    }

    //取消搜索
    @OnClick(R.id.searchview_cancle) public void onClickCancel() {

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
        @BindView(R.id.item_student_header) ImageView itemStudentHeader;
        @BindView(R.id.item_student_name) TextView itemStudentName;
        @BindView(R.id.item_student_phonenum) TextView itemStudentPhonenum;
        @BindView(R.id.item_student_gymname) TextView itemStudentGymname;
        @BindView(R.id.item_student_gender) ImageView itemStudentGender;
        @BindView(R.id.item_student_alpha) TextView itemStudentAlpha;
        @BindView(R.id.item_student_divider) View itemStudentDivder;
        @BindView(R.id.item_student_header_loop) RelativeLayout itemHeaderLoop;

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

        @Override public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StudentsHolder holder =
                new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemView.setTag(position);
            if (datas.size() == 0) return;
            StudentBean studentBean = datas.get(position);

            holder.itemStudentGymname.setText(studentBean.gymStr);
            holder.itemStudentName.setText(studentBean.username);
            holder.itemStudentPhonenum.setText(studentBean.phone);
            //holder.itemHeaderLoop.setBackgroundDrawable(new LoopView(studentBean.color));
          if (studentBean.gender == 0) {//男
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
                if (TextUtils.equals(studentBean.head, "~")) {
                    holder.itemStudentAlpha.setText("#");
                } else {
                    holder.itemStudentAlpha.setText(studentBean.head);
                }
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else {
                holder.itemStudentAlpha.setVisibility(View.GONE);
            }
            Glide.with(App.AppContex)
                .load(PhotoUtils.getSmall(studentBean.headerPic))
                .asBitmap()
                .placeholder(studentBean.gender == 0 ? R.drawable.default_student_male
                    : R.drawable.default_student_female)
                .error(studentBean.gender == 0 ? R.drawable.default_student_male
                    : R.drawable.default_student_female)
                .into(new CircleImgWrapper(holder.itemStudentHeader, getContext()));
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null && (int) v.getTag() < datas.size()) {

                listener.onItemClick(v, (int) v.getTag());
            }
        }
    }
}
