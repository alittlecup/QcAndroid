package cn.qingchengfit.staffkit.views.signin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.IntervalCancelEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInManualEvent;
import cn.qingchengfit.staffkit.rxbus.event.SignInStudentItemClickEvent;
import cn.qingchengfit.staffkit.views.adapter.SignInStudentAdapter;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.AppUtils;
import java.util.List;
import javax.inject.Inject;

/**
 * 手动签到、签出--搜索学员 list
 * Created by yangming on 16/8/25.
 */
public class SignInStudentListFragment extends BaseFragment implements StudentListPresenter.StudentListView {

    @BindView(R.id.et_signin_search) EditText etSigninSearch;
    @BindView(R.id.img_signin_search_clear) ImageView imgSigninSearchClear;
    @BindView(R.id.tv_signin_search_cancel) TextView tvSigninSearchCancel;
    @BindView(R.id.rl_signin_search) RelativeLayout rlSigninSearch;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.ll_signin_manual_discrib) LinearLayout llSigninManualDiscrib;
    @BindView(R.id.tv_sign_in_des) TextView tvSignInDes;
    @BindView(R.id.no_data_img) ImageView noDataImg;

    @Inject StudentListPresenter presenter;
    private boolean isSignIn = true;
    private LinearLayoutManager mLinearLayoutManager;
    private String keyWord;

    public SignInStudentListFragment() {
    }

    public static SignInStudentListFragment newInstance(boolean isSginIn) {
        SignInStudentListFragment fragment = new SignInStudentListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Configs.EXTRA_SIGNIN_SEARCH, isSginIn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSignIn = getArguments().getBoolean(Configs.EXTRA_SIGNIN_SEARCH);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_student_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initView();
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override public String getFragmentName() {
        return SignInStudentListFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboard(getActivity());
        getFocus();
        super.onDestroyView();

        presenter.unattachView();
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//页面显示的时候打开软键盘
            etSigninSearch.requestFocus();
            AppUtils.showKeyboard(getActivity(), etSigninSearch);
        }
    }

    //搜索栏清除按钮
    @OnClick(R.id.img_signin_search_clear) public void onClear() {
        etSigninSearch.setText("");
    }

    //取消搜索
    @OnClick(R.id.tv_signin_search_cancel) public void onClickCancel() {

        if (etSigninSearch.getText().toString().length() > 0) {
            etSigninSearch.setText("");
        }
        cancelEvent();
    }

    public void cancelEvent() {
        AppUtils.hideKeyboard(getActivity());
        SignInManualEvent signInManualEvent = new SignInManualEvent();
        signInManualEvent.setCancel(true);
        signInManualEvent.setSignIn(isSignIn);
        RxBus.getBus().post(signInManualEvent);
    }

    private void initView() {
        tvSignInDes.setText(isSignIn ? "查找会员进行手动签到" : "查找会员进行手动签出");
        imgSigninSearchClear.setVisibility(View.GONE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        setUpSeachView();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_sign_in_search);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        etSigninSearch.setCompoundDrawables(drawable, null, null, null);
        etSigninSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //关闭软键盘
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSigninSearch.getWindowToken(), 0);
                    //使得根View重新获取焦点，以监听返回键
                    getFocus();
                }
                return false;
            }
        });
    }

    /**
     * 初始化搜索组件
     */
    private void setUpSeachView() {
        etSigninSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                keyWord = s.toString().trim();
                if (s.length() > 0) {
                    imgSigninSearchClear.setVisibility(View.VISIBLE);
                    presenter.filter(keyWord, true);
                } else {
                    imgSigninSearchClear.setVisibility(View.GONE);
                    onFilterStudentList(null);
                }
            }
        });
    }

    @Override public void onStudentList(List<StudentBean> list) {

    }

    @Override public void onFilterStudentList(final List<StudentBean> list) {
        //        ToastUtils.show("onFilterStudentList"+list.size());
        if (list == null) {
            noDataImg.setImageResource(R.drawable.ic_sign_in_manual);
            tvSignInDes.setText(R.string.sign_in_manual_discrib);
            llSigninManualDiscrib.setVisibility(View.VISIBLE);
            return;
        } else {
            noDataImg.setImageResource(R.drawable.ic_search_24dp_grey);
            tvSignInDes.setText("没有找到匹配的会员");
            SignInStudentAdapter adapter = new SignInStudentAdapter(list);
            recycleview.setLayoutManager(mLinearLayoutManager);
            recycleview.setAdapter(adapter);
            llSigninManualDiscrib.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            adapter.setListener(new OnRecycleItemClickListener() {
                @Override public void onItemClick(View v, int pos) {
                    SignInStudentItemClickEvent event = new SignInStudentItemClickEvent();
                    event.setSignIn(isSignIn);
                    event.setStudentBean(list.get(pos));
                    AppUtils.hideKeyboard(getActivity());
                    RxBus.getBus().post(event);
                    RxBus.getBus().post(new IntervalCancelEvent());
                }
            });
        }
    }

    @Override public void onFaied() {

    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // 监听到返回按钮点击事件
                    cancelEvent();
                    return true;
                }
                return false;
            }
        });
    }
}
