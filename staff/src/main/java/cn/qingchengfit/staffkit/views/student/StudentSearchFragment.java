package cn.qingchengfit.staffkit.views.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.staffkit.views.student.followup.FollowUpItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.StudentCompare;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/20.
 */
public class StudentSearchFragment extends BaseFragment
    implements StudentSearchPresenter.PresenterView, FlexibleAdapter.OnItemClickListener, View.OnTouchListener {

    public static final int TYPE_SEARCH_STUDENT_LIST = 318;
    public static final int TYPE_SEARCH_FOLLOW_LIST = 80;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.img_describ) ImageView imgDescrib;
    @BindView(R.id.tv_describ) TextView tvDescrib;
    @BindView(R.id.ll_discrib) LinearLayout llDiscrib;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.searchview_clear) ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) Button searchviewCancle;
    @BindView(R.id.searchview) LinearLayout searchview;
    @Inject StudentSearchPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private int searchType;
    private int studentStatus;
    private LinearLayoutManager mLinearLayoutManager;
    private StudentAdapter studentAdapter;
    private List<StudentBean> datasForStudnetList = new ArrayList<>();
    private CommonFlexAdapter followAdapter;
    private List<Student> datasForFollow = new ArrayList<>();
    private List<AbstractFlexibleItem> items = new ArrayList();
    private List<AbstractFlexibleItem> itemsFilter = new ArrayList();
    private List<AbstractFlexibleItem> itemsOrigin = new ArrayList();
    private String keyWord;

    public static StudentSearchFragment newInstanceStudnet(ArrayList<StudentBean> datas) {
        Bundle args = new Bundle();
        args.putInt("searchType", TYPE_SEARCH_STUDENT_LIST);
        args.putParcelableArrayList("datas", datas);
        StudentSearchFragment fragment = new StudentSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StudentSearchFragment newInstanceFollow(int studentStatus, ArrayList<Student> datas) {
        Bundle args = new Bundle();
        args.putInt("searchType", TYPE_SEARCH_FOLLOW_LIST);
        args.putInt("studentStatus", studentStatus);
        args.putParcelableArrayList("datas", datas);
        StudentSearchFragment fragment = new StudentSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchType = getArguments().getInt("searchType");
        switch (searchType) {
            case TYPE_SEARCH_STUDENT_LIST:
                datasForStudnetList = getArguments().getParcelableArrayList("datas");
                break;
            case TYPE_SEARCH_FOLLOW_LIST:
                datasForFollow = getArguments().getParcelableArrayList("datas");
                studentStatus = getArguments().getInt("studentStatus");
                break;
        }
        //
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_search_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        //presenter.attachView(this);
        initDI();
        initView();
        view.setOnTouchListener(this);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mCallbackActivity.cleanToolbar();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
        this.getView().setOnTouchListener(this);

        switch (searchType) {
            case TYPE_SEARCH_STUDENT_LIST:
                searchviewEt.requestFocus();
                AppUtils.showKeyboard(getActivity(), searchviewEt);
                break;
            case TYPE_SEARCH_FOLLOW_LIST:
                mCallbackActivity.openSeachView("", new Action1<CharSequence>() {
                    @Override public void call(CharSequence charSequence) {
                        searchFollowList(charSequence.toString());
                        if ("qc_search_cancel".equals(charSequence.toString())) {
                            onClickCancel();
                        }
                    }
                });
                break;
        }
    }

    private void initDI() {

        delegatePresenter(presenter, this);
    }

    private void initView() {
        //searchviewEt.setFocusable(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        switch (searchType) {
            case TYPE_SEARCH_STUDENT_LIST:
                searchviewEt.requestFocus();
                AppUtils.showKeyboard(getActivity(), searchviewEt);
                initSearchStudentList();
                break;
            case TYPE_SEARCH_FOLLOW_LIST:
                initSearchFollowList();
                break;
        }

        setUpSeachView();
    }

    private void initSearchStudentList() {

        tvDescrib.setText(new StringBuilder().append("搜索").append(datasForStudnetList.size()).append("名会员").toString());
        studentAdapter = new StudentAdapter(datasForStudnetList);
        recycleview.setAdapter(studentAdapter);

        studentAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
                StudentBean bean = datasForStudnetList.get(pos);
                it.putExtra("student", bean);
                startActivity(it);
            }
        });
    }

    private void initSearchFollowList() {
        searchview.setVisibility(View.VISIBLE);
        tvDescrib.setText(new StringBuilder().append("搜索").append(datasForFollow.size()).append("名会员").toString());

        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        followAdapter = new CommonFlexAdapter(items, this);
        recycleview.setAdapter(followAdapter);

        for (Student student : datasForFollow) {
            //items.add(new FollowUpItem(this, student, studentStatus));
            itemsOrigin.add(new FollowUpItem(this, student, studentStatus));
        }
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    //搜索栏清除按钮
    @OnClick(R.id.searchview_clear) public void onClear() {
        searchviewEt.setText("");
    }

    //取消搜索
    @OnClick(R.id.searchview_cancle) public void onClickCancel() {
        if (searchviewEt.getText().toString().length() > 0) {
            searchviewEt.setText("");
        }
        AppUtils.hideKeyboard(getActivity());
        getActivity().onBackPressed();
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
                keyWord = s.toString().trim();
                switch (searchType) {
                    case TYPE_SEARCH_STUDENT_LIST:
                        searchStudentList();
                        break;
                    case TYPE_SEARCH_FOLLOW_LIST:
                        searchFollowList();
                        break;
                }
            }
        });
    }

    private void searchStudentList() {
        if (TextUtils.isEmpty(keyWord)) {
            datasForStudnetList.clear();
            studentAdapter.notifyDataSetChanged();
            tvDescrib.setText(new StringBuilder().append("搜索").append(datasForStudnetList.size()).append("名会员").toString());
            llDiscrib.setVisibility(View.VISIBLE);
        } else {
            presenter.filter(keyWord);
        }
    }

    public void searchFollowList(String keyWord) {
        this.keyWord = keyWord;
        if (keyWord.length() > 0) {
            if (searchviewClear != null) searchviewClear.setVisibility(View.VISIBLE);
        } else {
            if (searchviewClear != null) searchviewClear.setVisibility(View.GONE);
        }
        searchFollowList();
    }

    private void searchFollowList() {
        llDiscrib.setVisibility(View.GONE);
        if (TextUtils.isEmpty(keyWord)) {
            items.clear();
            followAdapter.updateDataSet(items);
            followAdapter.notifyDataSetChanged();
            tvDescrib.setText(new StringBuilder().append("搜索").append(datasForFollow.size()).append("名会员").toString());
            llDiscrib.setVisibility(View.VISIBLE);
        } else {
            items.clear();
            items.addAll(itemsOrigin);
            followAdapter.setSearchText(keyWord);
            followAdapter.notifyDataSetChanged();
            try {
                followAdapter.filterItems(items);
            } catch (Exception e) {
                // TOOD
                tvDescrib.setText("没有找到匹配的会员");
                llDiscrib.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

    @Override public void onFilterStudentList(List<StudentBean> list) {
        datasForStudnetList.clear();
        datasForStudnetList.addAll(list);

        Collections.sort(datasForStudnetList, new StudentCompare());
        for (int i = 0; i < datasForStudnetList.size(); i++) {
            StudentBean bean = datasForStudnetList.get(i);
            bean.isTag = false;
        }

        studentAdapter.notifyDataSetChanged();
        tvDescrib.setText("没有找到匹配的会员");
        llDiscrib.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override public boolean onItemClick(int position) {
        // 跳转会员详情
        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
        StudentBean bean = ((FollowUpItem) followAdapter.getItem(position)).data.toStudentBean();
        bean.setSupport_shop_ids(gymWrapper.getCoachService().shop_id());
        it.putExtra("student", bean);
        it.putExtra("status_to_tab", studentStatus);
        getActivity().startActivity(it);
        return false;
    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
