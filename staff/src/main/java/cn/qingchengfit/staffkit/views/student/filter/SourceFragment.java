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
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StaffWrapper;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.EditTextActivityIntentBuilder;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.allotsales.AllotSalesActivity;
import cn.qingchengfit.utils.IntentUtils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * // 来源 list
 * //Created by yangming on 16/12/2.
 */
@FragmentWithArgs public class SourceFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, View.OnTouchListener, SourcePresenter.PresenterView {

    public static final int RESULT_ADD_ORIGIN = 11;

    @Arg public int chooseType = -1;// 1会员跟进筛选-单选；2；会员列表筛选页-多选，3

    public LinearLayoutManager mLinearLayoutManager;
    public CommonFlexAdapter flexibleAdapter;
    public List<StudentSourceBean> datas = new ArrayList<>();
    public List<SourceItem> items = new ArrayList<>();
    public boolean isShowShort = true;
    @BindView(R.id.rv_referrer) RecyclerView rvReferrer;

    @Inject StaffWrapper staffWrapper;
    @Inject SourcePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    @BindView(R.id.tv_add_new_origin) TextView tvAddNew;
    @BindView(R.id.divider) View divider;
    boolean inSales = false;

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
        presenter.setType(chooseType);
        if (getActivity() != null && getActivity() instanceof AllotSalesActivity) {
            inSales = true;
        }

        if (staffWrapper.getStaff() != null) {
            presenter.getOrigins(staffWrapper.getStaff().id, inSales);
        } else {
            presenter.getOrigins(null, inSales);
        }
        initView();
        view.setOnTouchListener(this);
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        if (getActivity() instanceof ChooseActivity) {
            toolbarLayout.setVisibility(View.VISIBLE);
            super.initToolbar(toolbar);
            toolbarTitile.setText("选择来源");
        } else {
            toolbarLayout.setVisibility(View.GONE);
        }
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setOnTouchListener(this);
    }

    public void initDI() {
        delegatePresenter(presenter, this);
    }

    public void initView() {
        if (chooseType == 3) {
            tvAddNew.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        } else {
            tvAddNew.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
        flexibleAdapter = new CommonFlexAdapter(items, this);
        flexibleAdapter.setMode(FlexibleAdapter.MODE_SINGLE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvReferrer.setVisibility(View.VISIBLE);
        rvReferrer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReferrer.setHasFixedSize(true);
        rvReferrer.setNestedScrollingEnabled(false);
        rvReferrer.setAdapter(flexibleAdapter);
    }

    @OnClick(R.id.tv_add_new_origin) public void onAddNewOrigin() {
        Intent toAddOrigin = new EditTextActivityIntentBuilder("添加新来源").build(getContext());
        startActivityForResult(toAddOrigin, RESULT_ADD_ORIGIN);
    }

    //private void initRxABus() {
    //    RxBusAdd(StudentFilterEvent.class).observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(AndroidSchedulers.mainThread())
    //        .subscribe(new Action1<StudentFilterEvent>() {
    //            @Override public void call(StudentFilterEvent event) {
    //                if (event.eventType == StudentFilterEvent.EVENT_SOURCE_SHOWALL_CLICK) {
    //                    showAllClick();
    //                }
    //            }
    //        });
    //}

    /**
     * list item click
     */
    //private void itemClick(int position) {
    //    if (chooseType == 1 || chooseType == 3) {//单选
    //        if (selectedPos >= 0) {
    //            ((SourceItem) items.get(selectedPos)).getData().isSelect = false;
    //        }
    //        selectedPos = position;
    //        ((SourceItem) items.get(selectedPos)).getData().isSelect = true;
    //        // 关闭页面 //TODO
    //    } else if (chooseType == 2) {// 多选模式
    //        ((SourceItem) items.get(position)).getData().isSelect = !((SourceItem) items.get(position)).getData().isSelect;
    //    }
    //    flexibleAdapter.notifyDataSetChanged();
    //}
    public void reset() {
        flexibleAdapter.clearSelection();
    }

    /**
     * show all click
     */
    public void showAllClick() {
        items.clear();
        if (isShowShort) {
            for (int i = 0; i < datas.size(); i++) {
                items.add(new SourceItem(datas.get(i)));
            }
        } else {
            for (int i = 0; i < ((chooseType == 2 && datas.size() > 3) ? 3 : datas.size()); i++) {
                items.add(new SourceItem(datas.get(i)));
            }
        }
        flexibleAdapter.notifyDataSetChanged();
        isShowShort = !isShowShort;
    }

    //public void resetView(StudentFilter filter) {
    //    List<StudentSourceBean> list = filter.sourceBeanList;
    //    for (AbstractFlexibleItem item : items) {
    //        SourceItem sourceItem = (SourceItem) item;
    //        sourceItem.getData().isSelect = false;
    //        for (StudentSourceBean bean : list) {
    //            if (sourceItem.getData().id.equals(bean.id)) {
    //                sourceItem.getData().isSelect = true;
    //            }
    //        }
    //    }
    //    if (chooseType == 2 && datas.size() > 3) {
    //        dataShort = datas.subList(0, 3);
    //        itemShort = items.subList(0, 3);
    //    } else {
    //        dataShort = datas;
    //        itemShort = items;
    //    }
    //
    //    if (isShowShort) {
    //        flexibleAdapter.updateDataSet(itemShort);
    //    } else {
    //        flexibleAdapter.updateDataSet(items);
    //    }
    //    flexibleAdapter.notifyDataSetChanged();
    //}

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_ADD_ORIGIN) {
                // 添加
                if (!TextUtils.isEmpty(IntentUtils.getIntentString(data))) {
                    presenter.addOrigin(IntentUtils.getIntentString(data));
                }
            }
        }
    }

    public StudentSourceBean getSelectedList() {
        if (flexibleAdapter.getSelectedPositions().size() > 0) {
            if (flexibleAdapter.getItem(flexibleAdapter.getSelectedPositions().get(0)) instanceof SourceItem) {
                return ((SourceItem) flexibleAdapter.getItem(flexibleAdapter.getSelectedPositions().get(0))).getData();
            }
        }

        return null;
    }

    public void freshData() {
        showLoading();
        if (staffWrapper.getStaff() != null) {
            presenter.getOrigins(staffWrapper.getStaff().id, inSales);
        } else {
            presenter.getOrigins("", inSales);
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
        if (getActivity() instanceof ChooseActivity && datas.size() > position) {
            getActivity().setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(datas.get(position)));
            getActivity().finish();
        }
        return false;
    }

    @Override public void onSources(List<StudentSourceBean> origins) {
        hideLoading();
        datas.clear();
        datas.addAll(origins);
        StudentSourceBean bean = new StudentSourceBean();
        if (chooseType == 1) {
            bean.type = 0;
            bean.isSelect = true;
            bean.name = "全部";
            datas.add(0, bean);
        }

        for (int i = 0; i < ((chooseType == 2 && datas.size() > 3) ? 3 : datas.size()); i++) {
            items.add(new SourceItem(datas.get(i)));
            if (getActivity() instanceof ChooseActivity) {
                if (datas.get(i).id.equalsIgnoreCase(((ChooseActivity) getActivity()).chosenId)) {
                    flexibleAdapter.toggleSelection(items.size() - 1);//排除前面添加其他的选项
                }
            }
        }
        if (items.size() == 0 && chooseType == 3) {

        }
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onSuccess() {
        presenter.getOrigins(staffWrapper.getStaff() == null ? null : staffWrapper.getStaff().id, inSales);
    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
