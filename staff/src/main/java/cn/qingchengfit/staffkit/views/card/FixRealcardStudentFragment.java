package cn.qingchengfit.staffkit.views.card;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.Student;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.StudentCompare;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AlphabetView;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Paper on 16/6/16.
 * <p>
 * ((      /|_/|
 * \\.._.'  , ,\
 * /\ | '.__ v /
 * (_ .   /   "
 * ) _)._  _ /
 * '.\ \|( / ( mrf
 * '' ''\\ \\
 */

public class FixRealcardStudentFragment extends BaseFragment implements FixRealcardStudnetView {

    public static int BIND_STUDENT_LIST_DETAIL = 1;
    public static int CHANGE_BIND_STUDENT = 2;

    @BindView(R.id.recycleview_bind_student) RecyclerView recyclerView;
    @BindView(R.id.alphabetview) AlphabetView alphabetView;
    @BindView(R.id.searchview_et) EditText searchviewEt;
    @BindView(R.id.ll_head_search) LinearLayout llHeadSearch;
    @BindView(R.id.ll_bottom) LinearLayout llBottom;

    @Inject FixRealcardStudentPresenter presenter;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @BindView(R.id.tv_select_count) TextView tvSelectCount;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    private HashMap<String, Integer> alphabetSort = new HashMap<>();
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<StudentBean> datas = new ArrayList<>();// 会员list
    private ArrayList<StudentBean> datasChoose = new ArrayList<>();// 已选会员 list
    //    private ArrayList<StudentBean> datasFilter = new ArrayList<>();// 筛选会员 list
    private BindStudentDetailAdapter adapter;// 会员adapter
    private String mId, mModel;

    private List<String> students = new ArrayList<>();
    private List<String> mOtherstudentIds = new ArrayList<>();
    private List<String> noPermission = new ArrayList<>();

    public static FixRealcardStudentFragment newInstance(String id, String model, int type) {

        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("model", model);
        args.putInt("type", type);
        FixRealcardStudentFragment fragment = new FixRealcardStudentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString("id");
            mModel = getArguments().getString("model");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_muti_choose, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar();
        students.addAll(realCard.getRealCard().getUserIds());
        mOtherstudentIds.addAll(realCard.getRealCard().getUserIds());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        datas.clear();
        datasChoose.clear();
        adapter = new BindStudentDetailAdapter(datas);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                StudentBean s = datas.get(pos);
                if (s.isChosen) {
                    s.isChosen = false;
                    for (int i = 0; i < datasChoose.size(); i++) {
                        StudentBean b = datasChoose.get(i);
                        if (TextUtils.equals(b.getId(), s.getId())) {
                            datasChoose.remove(i);
                            break;
                        }
                    }
                } else {
                    s.isChosen = true;
                    datasChoose.add(s);
                }

                tvSelectCount.setText(datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
                adapter.notifyItemChanged(pos);
            }
        });
        alphabetView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideKeyboard(getActivity());
                return false;
            }
        });
        alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    if (alphabetSort.get("~") != null) mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                }
            }
        });

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(recyclerView.getViewTreeObserver(), this);
                showLoading();
                presenter.queryStudent(App.staffId, GymBaseInfoAction.getShopIdNow(mId, mModel));
            }
        });

        recyclerView.setAdapter(adapter);
        searchviewEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                students.clear();
                for (int i = 0; i < datasChoose.size(); i++) {
                    students.add(datasChoose.get(i).id);
                }
                if (TextUtils.isEmpty(s.toString())) {
                    presenter.queryStudent(App.staffId, GymBaseInfoAction.getShopIdNow(mId, mModel));
                } else {
                    presenter.filter(s.toString(), mId, mModel);
                }
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });

        presenter.subsribeDb();
        return view;
    }

    private void initToolbar() {
        toolbarLayout.setVisibility(View.GONE);
        llHeadSearch.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);

        if (getActivity() instanceof CardDetailActivity) {
            ((CardDetailActivity) getActivity()).initTextToolbar();
        }

        mCallbackActivity.setToolbar("修改绑定会员", false, new View.OnClickListener() {
            @Override public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        }, R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                students.clear();
                for (int i = 0; i < datasChoose.size(); i++) {
                    students.add(datasChoose.get(i).id);
                }
                showLoading();

                students.addAll(mOtherstudentIds);
                presenter.fixBundleStudent(StringUtils.List2Str(students), mId, mModel);
                return true;
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onStudentList(List<StudentBean> list) {
        hideLoading();
        datas.clear();
        alphabetSort.clear();
        datas.addAll(list);
        Collections.sort(datas, new StudentCompare());
        String tag = "";
        datasChoose.clear();
        for (int i = 0; i < list.size(); i++) {
            StudentBean bean = datas.get(i);
            if (students.contains(bean.getId())) {
                bean.setIsChosen(true);
                datasChoose.add(bean);
                if (mOtherstudentIds.contains(bean.getId())) mOtherstudentIds.remove(bean.getId());
            }

            if (!bean.head.equalsIgnoreCase(tag)) {
                bean.isTag = true;
                tag = bean.head;
                alphabetSort.put(tag, i);
            } else {
                bean.isTag = false;
            }
        }
        noPermission.clear();
        String chostr = BusinessUtils.studentIds2str(datasChoose);
        for (int i = 0; i < realCard.getRealCard().getUsers().size(); i++) {
            Student u = realCard.getRealCard().getUsers().get(i);
            if (u == null) continue;
            if (!chostr.contains(u.getId())) {
                noPermission.add(u.getId());
                StudentBean ss = new StudentBean();
                ss.id = u.getId();
                ss.username = u.getUsername();
                ss.phone = "手机: ***********";
                ss.avatar = "";
                datasChoose.add(0, ss);
            }
        }
        if (tvSelectCount != null) {
            tvSelectCount.setText(datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
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

    @Override public void onFaied() {
        hideLoading();
    }

    @Override public void onSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override public void onStopFresh() {
        hideLoading();
    }

    @OnClick(R.id.ll_show_select) public void onClick() {
        // 显示已选列表
        ShowSelectDialogFragment.start(this, 11, datasChoose, noPermission);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                tvSelectCount.setText(datasChoose.size() > 99 ? "..." : "" + datasChoose.size());
                adapter.notifyDataSetChanged();
            }
        }
    }

    public static class StudentsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_student_header) ImageView itemStudentHeader;
        @BindView(R.id.item_student_name) TextView itemStudentName;
        @BindView(R.id.item_student_phonenum) TextView itemStudentPhonenum;
        @BindView(R.id.item_checkbox) CheckBox itemCheckbox;
        @BindView(R.id.item_student_alpha) TextView itemStudentAlpha;

        @BindView(R.id.item_delete) ImageView itemDel;

        public StudentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class BindStudentDetailAdapter extends RecyclerView.Adapter<StudentsHolder> implements View.OnClickListener {
        List<StudentBean> datas;
        private OnRecycleItemClickListener listener;

        public BindStudentDetailAdapter(List<StudentBean> d) {
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
                Glide.with(getContext())
                    .load(R.drawable.ic_default_head_nogender)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, getContext()));
            } else {
                Glide.with(getContext())
                    .load(Uri.parse(studentBean.avatar))
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.itemStudentHeader, getContext()));
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
