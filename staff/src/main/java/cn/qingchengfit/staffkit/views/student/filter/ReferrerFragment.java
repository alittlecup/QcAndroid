package cn.qingchengfit.staffkit.views.student.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.course.SimpleTextItemItem;
import cn.qingchengfit.staffkit.views.student.ChooseReferrerActivity;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
 * // 推荐人 list
 * //Created by yangming on 16/12/2.
 */
@FragmentWithArgs public class ReferrerFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, View.OnTouchListener, ReferrerPresenter.PresenterView {

    public LinearLayoutManager mLinearLayoutManager;
    public CommonFlexAdapter flexibleAdapter;
    public List<AbstractFlexibleItem> items = new ArrayList();
    public List<StudentReferrerBean> datas = new ArrayList<>();
    public boolean isShowShort = true;
    public int selectedPos = 0;

    @Arg int chooseType = -1;// 1会员跟进筛选-单选；2；会员列表筛选页-多选；3-搜索推荐人

    @Inject ReferrerPresenter presenter;
    @BindView(R.id.rv_referrer) RecyclerView rvReferrer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referrer_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        initView();
        if (chooseType != 3) {
            presenter.getFilterSelers();
        } else {

        }
        view.setOnTouchListener(this);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        toolbarLayout.setVisibility(View.GONE);
        //if (chooseType < 3){
        //
        //}else {
        //    super.initToolbar(toolbar);
        //    toolbarTitile.setText("选择推荐人");
        //}
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setOnTouchListener(this);
    }

    public void initDI() {
        delegatePresenter(presenter, this);
    }

    public void initView() {
        flexibleAdapter = new CommonFlexAdapter(items, this);
        flexibleAdapter.setMode(SelectableAdapter.Mode.SINGLE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvReferrer.setLayoutManager(mLinearLayoutManager);
        rvReferrer.setHasFixedSize(true);
        rvReferrer.setNestedScrollingEnabled(false);
        rvReferrer.setAdapter(flexibleAdapter);
    }

    /**
     * show all click
     */
    public void showAllClick() {
        items.clear();
        if (isShowShort) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.size() > i) items.add(new ReferrerItem(datas.get(i)));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (datas.size() > i) items.add(new ReferrerItem(datas.get(i)));
            }
            if (items.size() == 0) items.add(new SimpleTextItemItem("暂无推荐人", 13));
        }
        flexibleAdapter.notifyDataSetChanged();
        isShowShort = !isShowShort;
    }

    /**
     * reset , items set unselected
     */
    public void reset() {
        flexibleAdapter.clearSelection();
        flexibleAdapter.notifyDataSetChanged();
    }

    public void resetView(StudentFilter filter) {
        flexibleAdapter.clearSelection();
        flexibleAdapter.notifyDataSetChanged();
    }

    public StudentReferrerBean getSelectedList() {
        if (flexibleAdapter.getSelectedItemCount() > 0) {
            int select = flexibleAdapter.getSelectedPositions().get(0);
            if (flexibleAdapter.getItem(select) instanceof ReferrerItem) return ((ReferrerItem) flexibleAdapter.getItem(select)).getData();
        }
        return null;
    }

    public void searchReferrer(String query) {
        if (TextUtils.isEmpty(query)) {
            onReferrers(new ArrayList<StudentReferrerBean>());
        } else {
            presenter.getReferrer(query);
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

    @Override public boolean onItemClick(int position) {
        int tmp = flexibleAdapter.getSelectedItemCount() > 0 ? flexibleAdapter.getSelectedPositions().get(0) : -1;
        if (flexibleAdapter.isSelected(position)) {
            flexibleAdapter.clearSelection();
        } else {
            flexibleAdapter.toggleSelection(position);
        }
        flexibleAdapter.notifyItemChanged(position);
        flexibleAdapter.notifyItemChanged(tmp);
        if (getActivity() instanceof ChooseReferrerActivity && datas.size() > position) {
            Intent s = IntentUtils.instancePacecle(datas.get(position));
            getActivity().setResult(Activity.RESULT_OK, s);
            getActivity().finish();
        }
        return true;
    }

    @Override public void onReferrers(List<StudentReferrerBean> referrers) {
        hideLoading();
        if (referrers == null) referrers = new ArrayList<>();

        items.clear();
        datas.clear();
        datas.addAll(referrers);

        StudentReferrerBean bean;
        if (chooseType == 1) {
            bean = new StudentReferrerBean();
            bean.isSelect = true;
            bean.username = "全部";
            datas.add(0, bean);
        }

        for (int i = 0; i < ((chooseType == 2 && datas.size() > 3) ? 3 : datas.size()); i++) {
            if (datas.size() > i) items.add(new ReferrerItem(datas.get(i)));
        }
        if (items.isEmpty()) {
            if (chooseType == 2) {
                items.add(new SimpleTextItemItem("暂无推荐人", 13));
            } else {
                items.add(new CommonNoDataItem(R.drawable.ic_sign_in_nodata, "无搜索结果"));
            }
        }

        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {

    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
