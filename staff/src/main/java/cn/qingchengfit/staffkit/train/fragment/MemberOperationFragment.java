package cn.qingchengfit.staffkit.train.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.DirtySender;
import cn.qingchengfit.staffkit.train.item.AddGroupMemberItem;
import cn.qingchengfit.staffkit.train.model.SignPersonalBean;
import cn.qingchengfit.staffkit.train.model.SignRecord;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpPersonalPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.bottom.BottomStudentsFragment;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.AlphabetView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/3/23.
 */

public class MemberOperationFragment extends BaseFragment implements FlexibleAdapter.OnItemClickListener, SignUpView<SignRecord> {

    public final static int REQUEST_ADD_MEMBER = 1;

    @BindView(R.id.recycle_data) RecyclerView recycleData;
    @BindView(R.id.alphaTextDialog) TextView alphaTextDialog;
    @BindView(R.id.alphabetview) AlphabetView alphabetview;
    @BindView(R.id.text_select_count) TextView textSelectCount;
    @BindView(R.id.img_down) ImageView imgDown;
    @BindView(R.id.ll_show_select) LinearLayout llShowSelect;
    @BindView(R.id.ll_bottom) LinearLayout llBottom;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text_toolbar_right) TextView textToolbarRight;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;
    @Inject SignUpPersonalPresenter presenter;
    @Inject SignUpGroupDetailPresenter groupDetail;
    @Inject TrainIds trainIds;
    private CommonFlexAdapter flexAdapter;
    private List<AddGroupMemberItem> list = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_with_select, container, false);
        ButterKnife.bind(this, view);
        setToolbar();

        delegatePresenter(presenter, this);

        presenter.queryData("", trainIds.getGymId(), trainIds.getCompetitionId());

        flexAdapter = new CommonFlexAdapter(list, this);
        recycleData.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration =
            new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, MeasureUtils.dpToPx(78f, getContext().getResources()));
        dividerItemDecoration.setType(DividerItemDecoration.LEFT);
        recycleData.addItemDecoration(dividerItemDecoration);
        recycleData.setAdapter(flexAdapter);
        refreshView();

        return view;
    }

    private void setToolbar() {
        toolbarTitile.setText("添加成员");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {

                if (flexAdapter.getSelectedItemCount() == 0) {
                    showAlert("您没有选择任何学员");
                    return true;
                }
                DirtySender.studentList.clear();
                for (int i = 0; i < flexAdapter.getSelectedPositions().size(); i++) {
                    int p = flexAdapter.getSelectedPositions().get(i);
                    if (flexAdapter.getItem(p) instanceof AddGroupMemberItem) {
                        DirtySender.studentList.add(((AddGroupMemberItem) flexAdapter.getItem(p)).getData());
                    }
                }

                getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
                getActivity().finish();
                return false;
            }
        });
        textToolbarRight.setVisibility(View.VISIBLE);
        textToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @OnClick(R.id.ll_show_select) public void showSelect() {
        BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
        selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
            @Override public void onBottomStudents(List<Personage> list) {
                DirtySender.studentList.clear();
                DirtySender.studentList.addAll(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
                flexAdapter.clearSelection();
                for (int i = 0; i < flexAdapter.getItemCount(); i++) {
                    QcStudentBean b = ((AddGroupMemberItem) flexAdapter.getItem(i)).getData();
                    if (DirtySender.studentList.contains(b)) {
                        flexAdapter.addSelection(i);
                    }
                }
                flexAdapter.notifyDataSetChanged();
                textSelectCount.setText(String.valueOf(DirtySender.studentList.size()));
            }
        });
        selectSutdentFragment.setDatas(DirtySender.studentList);
        selectSutdentFragment.show(getFragmentManager(), "");
    }

    @Override public String getFragmentName() {
        return MemberOperationFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void refreshView() {
        textSelectCount.setText(flexAdapter.getSelectedItemCount() > 99 ? "99+" : Integer.toString(flexAdapter.getSelectedItemCount()));
    }

    @Override public boolean onItemClick(int position) {
        flexAdapter.toggleSelection(position);
        flexAdapter.notifyItemChanged(position);
        if (flexAdapter.getItem(position) instanceof AddGroupMemberItem) {
            if (flexAdapter.isSelected(position)) {
                DirtySender.studentList.add(((AddGroupMemberItem) flexAdapter.getItem(position)).getData());
            } else {
                DirtySender.studentList.remove(((AddGroupMemberItem) flexAdapter.getItem(position)).getData());
            }
        }
        refreshView();

        return false;
    }

    @Override public void onGetSignUpDataSuccess(SignRecord data) {
        if (data.users == null || data.users.size() == 0) return;
        for (int i = 0; i < data.users.size(); i++) {
            SignPersonalBean bean = data.users.get(i);
            AddGroupMemberItem signUpGroupMemberItem = new AddGroupMemberItem(bean, getContext());
            list.add(signUpGroupMemberItem);
        }
        flexAdapter.notifyDataSetChanged();
        for (int i = 0; i < flexAdapter.getItemCount(); i++) {
            if (flexAdapter.getItem(i) instanceof AddGroupMemberItem) {
                if (DirtySender.studentList.contains(((AddGroupMemberItem) flexAdapter.getItem(i)).getData())) {
                    flexAdapter.addSelection(i);
                    flexAdapter.notifyItemChanged(i);
                }
            }
        }
        refreshView();
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {
    }

    @Override public void onDelSuccess() {

    }
}
