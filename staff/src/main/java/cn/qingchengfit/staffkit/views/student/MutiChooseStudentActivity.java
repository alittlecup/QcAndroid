package cn.qingchengfit.staffkit.views.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.BaseActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.StudentCompare;
import cn.qingchengfit.widgets.AlphabetView;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 单选学员,不是多选
 */
public class MutiChooseStudentActivity extends BaseActivity implements MutiChooseStudentPresenterPresenter.MVPView {

    public static final String EXTRA_STUDENTS = "choose_students";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @BindView(R.id.down) ImageView down;
    @BindView(R.id.titile_layout) LinearLayout titileLayout;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    @BindView(R.id.inter_searchview_et) EditText searchviewEt;
    @BindView(R.id.add_student) ImageView addStudent;
    @BindView(R.id.recycleview) RecyclerView recyclerView;
    @BindView(R.id.alphabetview) AlphabetView alphabetview;
    @Inject RestRepository restRepository;
    @Inject MutiChooseStudentPresenterPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private ArrayList<StudentBean> mChosenStudent = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<StudentBean> datas = new ArrayList<>();
    private ArrayList<StudentBean> datas1 = new ArrayList<>();
    private StudentAdapter adapter;
    private HashMap<String, Integer> alphabetSort = new HashMap<>();
    private String keyWord;//搜索关键字
    private List<String> students = new ArrayList<>();
    private Subscription spQuery;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_choose_student);
        ButterKnife.bind(this);
        presenter.attachView(this);
        initToolbar();
        initData();
    }

    @Override protected void onDestroy() {
        presenter.unattachView();
        super.onDestroy();
        if (spQuery != null) spQuery.unsubscribe();
        ;
    }

    private void initData() {
        List<StudentBean> initStus = getIntent().getParcelableArrayListExtra(EXTRA_STUDENTS);
        if (initStus != null) mChosenStudent.addAll(initStus);
        mLinearLayoutManager = new LinearLayoutManager(this);
        datas.clear();
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new StudentAdapter(datas);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent result = new Intent();
                result.putExtra(EXTRA_STUDENTS, datas.get(pos));
                setResult(RESULT_OK, result);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        showLoading();
        presenter.queryStudent(App.staffId, gymWrapper.shop_id(), getIntent().getStringExtra(Configs.EXTRA_PERMISSION_METHOD));

        alphabetview.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(MutiChooseStudentActivity.this);
                if (TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
                return false;
            }
        });
        alphabetview.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    if (TextUtils.equals(s, "#")) {
                        if (alphabetSort.get("~") != null) mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                    }
                }
            }
        });
        setUpSeachView();
    }

    /**
     * 初始化搜索组件
     */
    private void setUpSeachView() {
        RxTextView.textChanges(searchviewEt).debounce(150, TimeUnit.MILLISECONDS).subscribe(new Action1<CharSequence>() {
            @Override public void call(CharSequence s) {
                if (s.length() > 0) {
                    searchviewClear.setVisibility(View.VISIBLE);
                } else {
                    searchviewClear.setVisibility(View.GONE);
                }
                keyWord = s.toString().trim();
                if (gymWrapper.inBrand()) {
                    presenter.filter(keyWord);
                } else {
                    presenter.filter(keyWord);
                }
            }
        });
    }

    private void handleData(List<QcStudentBean> data) {
        String curHead = "";
        List<StudentBean> studentBeanList = new ArrayList<StudentBean>();

        for (QcStudentBean student : data) {
            StudentBean bean = new StudentBean();
            bean.avatar = student.getAvatar();
            bean.username = student.getUsername();
            bean.systemUrl = "后台无数据";
            bean.id = student.getId();
            bean.color = "";
            bean.support_shop = student.getSupport_gym();
            bean.support_shop_ids = student.getSupoort_gym_ids();

            bean.brandid = gymWrapper.brand_id();
            bean.modelid = gymWrapper.id();
            bean.model = gymWrapper.model();
            if (TextUtils.isEmpty(student.getHead()) || !AlphabetView.Alphabet.contains(student.getHead())) {
                bean.head = "~";
            } else {
                bean.head = student.getHead().toUpperCase();
            }
            if (!curHead.equalsIgnoreCase(bean.head)) {
                bean.setIsTag(true);
                curHead = bean.head;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("手机:").append(student.getPhone());
            bean.phone = sb.toString();
            if (student.getGender() == 0) {
                bean.gender = true;
            } else {
                bean.gender = false;
            }
            studentBeanList.add(bean);
        }
        datas.clear();
        Collections.sort(datas, new StudentCompare());
        String tag = "";
        for (int i = 0; i < datas.size(); i++) {
            StudentBean bean = datas.get(i);
            if (!bean.head.equalsIgnoreCase(tag)) {
                bean.isTag = true;
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                bean.isTag = false;
            }
        }
        datas.addAll(studentBeanList);
        adapter.notifyDataSetChanged();
        //        if (TextUtils.isEmpty(brand_id) && TextUtils.isEmpty(gymid) && TextUtils.isEmpty(gymmodel)) {
        //            if (view != null)
        //                view.onFilterStudentList(studentBeanList);
        //        } else {
        //            if (view != null)
        //                view.onStudentList(studentBeanList);
        //        }
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        toolbarTitile.setText("选择会员");
    }

    @Override public void onStudentList(List<StudentBean> list) {
        if (list != null && list.size() > 0) {
            hideLoading();
        }
        datas.clear();
        alphabetSort.clear();
        datas.addAll(list);
        Collections.sort(datas, new StudentCompare());
        String tag = "";

        datas1.clear();
        for (int i = 0; i < list.size(); i++) {
            StudentBean bean = datas.get(i);
            if (students.contains(bean.getId())) {
                bean.setIsChosen(true);
                datas1.add(bean);
            }

            if (!bean.head.equalsIgnoreCase(tag)) {
                bean.isTag = true;
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                bean.isTag = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override public void onFilterStudentList(List<StudentBean> list) {
        datas.clear();
        datas.addAll(list);
        Collections.sort(datas, new StudentCompare());
        alphabetSort.clear();
        String tag = "";
        for (int i = 0; i < list.size(); i++) {
            StudentBean bean = datas.get(i);
            if (students.contains(bean.getId())) {
                bean.setIsChosen(true);
            }
            if (!bean.head.equalsIgnoreCase(tag)) {
                bean.isTag = true;
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                bean.isTag = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override public void onStopFresh() {
        hideLoading();
    }

    @OnClick({ R.id.titile_layout, R.id.searchview_cancle, R.id.add_student }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titile_layout:
                break;
            case R.id.searchview_cancle:
                break;
            case R.id.add_student:

                break;
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        onShowError(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    /**
     * recycle adapter
     */
    public static class StudentsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_student_header) ImageView itemStudentHeader;
        @BindView(R.id.item_student_name) TextView itemStudentName;
        @BindView(R.id.item_student_phonenum) TextView itemStudentPhonenum;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.item_student_alpha) TextView itemStudentAlpha;
        //        @BindView(R.id.item_student_divider)
        //        View itemStudentDivder;

        @BindView(R.id.item_delete) ImageView itemDel;

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
                new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_choose, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemView.setTag(position);
            StudentBean studentBean = datas.get(position);

            holder.itemStudentName.setText(studentBean.username);

            holder.itemStudentPhonenum.setText(studentBean.phone);

            if (studentBean.isChosen) {
                holder.itemCheckbox.setChecked(true);
            } else {
                holder.itemCheckbox.setChecked(false);
            }

            if (studentBean.isTag) {
                if (TextUtils.equals("~", studentBean.head)) {
                    holder.itemStudentAlpha.setText("#");
                } else {
                    holder.itemStudentAlpha.setText(studentBean.head);
                }
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
            } else {
                holder.itemStudentAlpha.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(studentBean.avatar)) {
                Glide.with(MutiChooseStudentActivity.this)
                    .load(R.drawable.ic_default_head_nogender)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, MutiChooseStudentActivity.this));
            } else {
                Glide.with(MutiChooseStudentActivity.this)
                    .load(Uri.parse(studentBean.avatar))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, MutiChooseStudentActivity.this));
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class StudentChoosenAdapter extends RecyclerView.Adapter<StudentsHolder> implements View.OnClickListener {
        List<StudentBean> datas;
        private OnRecycleItemClickListener listener;

        public StudentChoosenAdapter(List<StudentBean> d) {
            this.datas = d;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public StudentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StudentsHolder holder =
                new StudentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_choose, parent, false));
            holder.itemDel.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(StudentsHolder holder, int position) {
            holder.itemDel.setTag(position);
            StudentBean studentBean = datas.get(position);
            holder.itemStudentName.setText(studentBean.username);
            holder.itemStudentPhonenum.setText(studentBean.phone);
            if (studentBean.isChosen) {
                holder.itemCheckbox.setChecked(true);
            } else {
                holder.itemCheckbox.setChecked(false);
            }
            if (position == 0) {
                holder.itemStudentAlpha.setVisibility(View.VISIBLE);
                holder.itemStudentAlpha.setTextColor(CompatUtils.getColor(MutiChooseStudentActivity.this, R.color.text_grey));
            } else {
                holder.itemStudentAlpha.setVisibility(View.GONE);
            }
            holder.itemCheckbox.setVisibility(View.GONE);
            holder.itemDel.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(studentBean.avatar)) {
                Glide.with(MutiChooseStudentActivity.this)
                    .load(R.drawable.ic_default_head_nogender)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, MutiChooseStudentActivity.this));
            } else {
                Glide.with(MutiChooseStudentActivity.this)
                    .load(Uri.parse(studentBean.avatar))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, MutiChooseStudentActivity.this));
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
