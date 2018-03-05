package cn.qingchengfit.staffkit.views;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterEvent;
import cn.qingchengfit.staffkit.views.student.list.StudentListView;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.StudentCompare;
import cn.qingchengfit.utils.StudentCompareJoinAt;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.AlphabetView;
import cn.qingchengfit.widgets.QcToggleButton;
import java.util.Collections;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

/**
 * Created by fb on 2017/2/14.
 */

public class FilterCommonFragment extends BaseFragment implements StudentListView, DrawerLayout.DrawerListener {

    public static final int SORT_TYPE_ALPHA = 684;//字母排序
    public static final int SORT_TYPE_REGISTER = 685;//最新注册时间排序

    @BindView(R.id.tv_sort_alpha) public QcToggleButton tvSortAlpha;
    @BindView(R.id.tv_sort_register) public QcToggleButton tvSortRegister;
    @BindView(R.id.tv_sort_filter) public QcToggleButton tvSortFilter;
    @BindView(R.id.drawer) public DrawerLayout drawer;
    @BindView(R.id.alphabetview) public AlphabetView alphabetView;
    @BindView(R.id.alphaTextDialog) public TextView alphaTextDialog;

    public boolean isFilter = false;
    public StudentFilter filter = new StudentFilter();
    public LinearLayoutManager mLinearLayoutManager;
    public int sortType = SORT_TYPE_REGISTER;
    public View view;
    public EditText searchviewEt;
    public ImageView searchviewClear;
    public Button searchviewCancle;
    public LinearLayout searchview;
    private ArrayMap<String, Integer> alphabetSort = new ArrayMap<>();
    private Observable<StudentFilterEvent> obFilter;

    public void setView(View view) {
        this.view = view;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            try {
                throw new Exception("view is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override public void onResume() {
        super.onResume();
        initRxBus();
    }

    @Override public void onPause() {
        super.onPause();
        RxBus.getBus().unregister(StudentFilterEvent.class.getName(), obFilter);
    }

    private void initRxBus() {
        // 筛选确定
        obFilter = RxBus.getBus().register(StudentFilterEvent.class);
      obFilter.observeOn(Schedulers.io())
          .onBackpressureBuffer()
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<StudentFilterEvent>() {
            @Override public void call(StudentFilterEvent event) {

                if (event.eventType == StudentFilterEvent.EVENT_CONFIRM_CLICK) {
                    isFilter = true;
                    if (event.eventFrom == StudentFilterEvent.EVENT_FROM_STUDENTLIST) {
                        filterStudentConfirm(event);
                    } else if (event.eventFrom == StudentFilterEvent.EVENT_FROM_SALELIST) {
                        filterSaleConfirm(event);
                    } else {

                    }
                } else {
                    isFilter = false;
                    filterStudentConfirm(event);
                    filterSaleConfirm(event);
                }
            }
        });
        onClickFilterUIChange();
    }

    private void initView() {
        drawer.addDrawerListener(this);
        drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        alphabetView.setOnTouchListener((v, event) -> {
            AppUtils.hideKeyboard(getActivity());
            if (searchviewEt != null && TextUtils.isEmpty(searchviewEt.getText())) searchviewCancle.performClick();
            return true;
        });
        alphabetView.setAlphaDialog(alphaTextDialog);
        alphabetView.setOnAlphabetChange(new AlphabetView.OnAlphabetChange() {
            @Override public void onChange(int position, String s) {
                if (alphabetSort.get(s) != null) {
                    mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get(s), 0);
                } else {
                    if (TextUtils.equals(s, "#")) {
                        if (alphabetSort.get("~") != null) {
                            mLinearLayoutManager.scrollToPositionWithOffset(alphabetSort.get("~"), 0);
                        }
                    }
                }
            }
        });
    }

    private void refreshView() {
        tvSortRegister.setChecked(sortType == SORT_TYPE_REGISTER);
        tvSortAlpha.setChecked(sortType == SORT_TYPE_ALPHA);
    }

    //筛选确认
    public void filterStudentConfirm(StudentFilterEvent event) {
    }

    public void filterSaleConfirm(StudentFilterEvent event) {

    }

    /**
     * 对 list 数据排序
     */
    public void sortData(List<? extends StudentBean> datas) {
        alphabetSort.clear();
        switch (sortType) {
            case SORT_TYPE_ALPHA:
                sortDataByAlpah(datas);
                alphabetView.setVisibility(View.VISIBLE);
                break;
            case SORT_TYPE_REGISTER:
                sortDataByRegister(datas);
                alphabetView.setVisibility(View.GONE);
                break;
        }
        refreshView();
    }

    /**
     * 按字母排序
     */
    public void sortDataByAlpah(List<? extends StudentBean> datas) {
        sortType = SORT_TYPE_ALPHA;
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
        refreshView();
    }

    /**
     * 按最新注册时间排序
     */
    public void sortDataByRegister(List<? extends StudentBean> datas) {
        sortType = SORT_TYPE_REGISTER;
        Collections.sort(datas, new StudentCompareJoinAt());
        for (int i = 0; i < datas.size(); i++) {
            StudentBean bean = datas.get(i);
            bean.isTag = false;
        }
        refreshView();
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onStudentList(List<StudentBean> list) {

    }

    @Override public void onFilterStudentList(List<StudentBean> list) {

    }

    @Override public void onFaied() {

    }

    @Override public void onStopFresh() {

    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        Timber.e("onDrawerSlide");
    }

    @Override public void onDrawerOpened(View drawerView) {
        Timber.e("onDrawerOpened");
        setBackPress();
    }

    @Override public void onDrawerClosed(View drawerView) {
        Timber.e("onDrawerSlide");
    }

    @Override public void onDrawerStateChanged(int newState) {
        Timber.e("onDrawerStateChanged:" + newState);
    }

    /**
     * 点击排序UI变化
     */
    //    public void onClickSortUIChange() {
    //        tvSortAlpha.setTextColor(ContextCompat.getColor(FilterCommonFragment.this.getContext(),
    //                sortType == SORT_TYPE_ALPHA ? R.color.qc_theme_green : R.color.qc_text_black));
    //        Drawable drawable =
    //                VectorDrawableCompat.create(getResources(), R.drawable.vector_list_order_normal,
    //                        null);
    //        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(),
    //                sortType == SORT_TYPE_ALPHA ? R.color.qc_theme_green : R.color.qc_text_black));
    //        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    //        tvSortAlpha.setCompoundDrawables(null, null, drawable, null);
    //
    //        tvSortRegister.setTextColor(ContextCompat.getColor(FilterCommonFragment.this.getContext(),
    //                sortType == SORT_TYPE_REGISTER ? R.color.qc_theme_green : R.color.qc_text_black));
    //        Drawable drawableRegister =
    //                VectorDrawableCompat.create(getResources(), R.drawable.vector_list_order_register,
    //                        null);
    //        DrawableCompat.setTint(drawableRegister, ContextCompat.getColor(getActivity(),
    //                sortType == SORT_TYPE_REGISTER ? R.color.qc_theme_green : R.color.qc_text_black));
    //        drawableRegister.setBounds(0, 0, drawableRegister.getMinimumWidth(),
    //                drawableRegister.getMinimumHeight());
    //        tvSortRegister.setCompoundDrawables(null, null, drawableRegister, null);
    //    }

    /**
     * 筛选 UI 变化
     */
    public void onClickFilterUIChange() {

        if (isFilter && ((filter.status != null && !filter.status.isEmpty()) || filter.sale != null || (!TextUtils.isEmpty(
            filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) || (filter.referrerBean != null) || (filter.sourceBean
            != null))) {
            ColorStateList csl = ContextCompat.getColorStateList(getActivity(), R.color.qc_theme_green);
            //            tvSortFilter.setTextColor(csl);

            VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.vector_list_filter_normal, null);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.qc_theme_green));
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvSortFilter.setChecked(true);
        } else {
            ColorStateList csl = ContextCompat.getColorStateList(getActivity(), R.color.qc_text_black);
            //            tvSortFilter.setTextColor(csl);

            VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.vector_list_filter_normal, null);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.qc_text_black));
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //            tvSortFilter.setCompoundDrawables(null, null, drawable, null);
            tvSortFilter.setChecked(false);
        }
    }
}
