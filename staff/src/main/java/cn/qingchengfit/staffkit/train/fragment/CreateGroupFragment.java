package cn.qingchengfit.staffkit.train.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.train.SignUpChooseActivity;
import cn.qingchengfit.staffkit.train.event.RefreshListEvent;
import cn.qingchengfit.staffkit.train.item.SignUpGroupMemberItem;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/3/29.
 */

public class CreateGroupFragment extends BaseFragment implements SignUpView {

	EditText createGroupNewName;
	TextView groupMemberCount;
	TextView tvAddMember;
	Toolbar toolbar;
	TextView textToolbarRight;
	TextView toolbarTitile;
	RecyclerView recyclerAddMember;
	ImageView imageClearName;
    @Inject SignUpGroupDetailPresenter presenter;
    @Inject TrainIds trainIds;
    private List<QcStudentBean> users = new ArrayList<>();
    private List<SignUpGroupMemberItem> itemList = new ArrayList<>();
    private CommonFlexAdapter flexAdapter;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
      createGroupNewName = (EditText) view.findViewById(R.id.create_group_new_name);
      groupMemberCount = (TextView) view.findViewById(R.id.group_member_count);
      tvAddMember = (TextView) view.findViewById(R.id.tv_add_member);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      textToolbarRight = (TextView) view.findViewById(R.id.text_toolbar_right);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recyclerAddMember = (RecyclerView) view.findViewById(R.id.recycle_add_member);
      imageClearName = (ImageView) view.findViewById(R.id.image_create_clear_name);
      view.findViewById(R.id.tv_add_member).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAddMember();
        }
      });

      delegatePresenter(presenter, this);

        flexAdapter = new CommonFlexAdapter(itemList);
        recyclerAddMember.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAddMember.setAdapter(flexAdapter);
        groupMemberCount.setText(getString(R.string.sign_up_member_count, users.size()));

        imageClearName.setVisibility(View.GONE);
        createGroupNewName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    imageClearName.setVisibility(View.VISIBLE);
                } else {
                    imageClearName.setVisibility(View.GONE);
                }
            }
        });

        setToolbar();
        return view;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setToolbar() {
        toolbarTitile.setText("新建分组");
        toolbar.inflateMenu(R.menu.menu_compelete);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (createGroupNewName.getText().toString().equals("")) {
                    DialogUtils.showAlert(getContext(), "请填写小组名称");
                    return false;
                } else {
                    presenter.createGroup(createGroupNewName.getText().toString(), trainIds.getCompetitionId(), trainIds.getGymId(), users);
                }
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

 void onAddMember() {
        Intent intent = new Intent(getActivity(), SignUpChooseActivity.class);
        intent.putExtra(Configs.EXTRA_GYM_ID, trainIds.getGymId());
        intent.putExtra("competition_id", trainIds.getCompetitionId());
        DirtySender.studentList.clear();
        for (int i = 0; i < itemList.size(); i++) {
            DirtySender.studentList.add(itemList.get(i).getStudentBean());
        }
        startActivityForResult(intent, MemberOperationFragment.REQUEST_ADD_MEMBER);
    }

    @Override public String getFragmentName() {
        return CreateGroupFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        presenter.unattachView();
    }

    private void setAddMemberList(List<QcStudentBean> dataList) {
        itemList.clear();
        for (QcStudentBean bean : DirtySender.studentList) {
            SignUpGroupMemberItem item = new SignUpGroupMemberItem(bean);
            itemList.add(item);
        }
        groupMemberCount.setText(getString(R.string.sign_up_member_count, itemList.size()));
        flexAdapter.notifyDataSetChanged();
    }

    @Override public void onGetSignUpDataSuccess(Object data) {

    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {

        ToastUtils.show("添加小组成功");
        RxBus.getBus().post(new RefreshListEvent());
        getActivity().onBackPressed();
        groupMemberCount.setText(getString(R.string.sign_up_member_count, users.size()));
    }

    @Override public void onDelSuccess() {

    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MemberOperationFragment.REQUEST_ADD_MEMBER:
                if (resultCode == Activity.RESULT_OK) {
                    //this.users = data.getParcelableArrayListExtra("data");
                    this.users.clear();
                    this.users.addAll(DirtySender.studentList);
                    setAddMemberList(this.users);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
