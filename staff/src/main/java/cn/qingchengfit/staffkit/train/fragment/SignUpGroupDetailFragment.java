package cn.qingchengfit.staffkit.train.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.train.SignUpChooseActivity;
import cn.qingchengfit.staffkit.train.event.RefreshListEvent;
import cn.qingchengfit.staffkit.train.item.SignUpAttendanceItem;
import cn.qingchengfit.staffkit.train.item.SignUpGroupMemberItem;
import cn.qingchengfit.staffkit.train.model.GroupBean;
import cn.qingchengfit.staffkit.train.model.MemberAttendanceBean;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.presenter.SignUpGroupDetailPresenter;
import cn.qingchengfit.staffkit.train.presenter.SignUpView;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.BottomSheetListDialogFragment;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fb on 2017/3/22.
 */

//小组详情
@FragmentWithArgs public class SignUpGroupDetailFragment extends BaseFragment implements SignUpView<GroupBean> {

	TextView tvSignGroupName;
	RecyclerView recycleGroup;
	TextView tvSignMemberCount;
	TextView tvAddGroup;
	TextView tvRemoveMember;
	RecyclerView recyclerAttenDetail;
	RelativeLayout changeGroupName;
	LinearLayout openAllMember;
    @Arg String teamId;
	Toolbar toolbar;
	TextView toolbarTitile;
    @Inject SignUpGroupDetailPresenter presenter;
    @Inject TrainIds trainIds;
    private CommonFlexAdapter flexAdapter;
    private CommonFlexAdapter attendanceAdapter;
    private String title;
    private List<SignUpGroupMemberItem> memberItemList = new ArrayList<>();
    private List<SignUpAttendanceItem> attendanceItems = new ArrayList<>();
    private MemberOperationFragment memberOperationFragment;
    private List<QcStudentBean> memberList = new ArrayList<>();
    private Observable<RefreshListEvent> subscription;
    private boolean isDelete;
    private boolean isOpened;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_group, container, false);
      tvSignGroupName = (TextView) view.findViewById(R.id.sign_up_group_name);
      recycleGroup = (RecyclerView) view.findViewById(R.id.list_sign_up_group_member);
      tvSignMemberCount = (TextView) view.findViewById(R.id.sign_group_member_count);
      tvAddGroup = (TextView) view.findViewById(R.id.btn_sign_group_add_member);
      tvRemoveMember = (TextView) view.findViewById(R.id.btn_sign_group_remove_member);
      recyclerAttenDetail =
          (RecyclerView) view.findViewById(R.id.recycle_sign_up_group_atten_detail);
      changeGroupName = (RelativeLayout) view.findViewById(R.id.change_group_name);
      openAllMember = (LinearLayout) view.findViewById(R.id.open_all_member);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.btn_sign_group_add_member)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onAddOrRemove(v);
            }
          });
      view.findViewById(R.id.btn_sign_group_remove_member)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              onAddOrRemove(v);
            }
          });
      view.findViewById(R.id.change_group_name).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAddOrRemove(v);
        }
      });
      view.findViewById(R.id.open_all_member).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onOpenMember();
        }
      });

      initBus();

        delegatePresenter(presenter, this);

        getData();
        initView();
        return view;
    }

    private void initBus() {
        subscription = RxBus.getBus().register(RefreshListEvent.class);
        subscription.subscribe(new Action1<RefreshListEvent>() {
            @Override public void call(RefreshListEvent refreshListEvent) {
                getData();
            }
        });
    }

    @Override public void onResume() {
        super.onResume();
    }

    private void getData() {
        presenter.queryGroupDetail(teamId);
    }

    private void initView() {
        initToolbar(toolbar);

        memberOperationFragment = new MemberOperationFragment();

        flexAdapter = new CommonFlexAdapter(memberItemList, this);
        recycleGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleGroup.setAdapter(flexAdapter);
        recycleGroup.setNestedScrollingEnabled(false);
        attendanceAdapter = new CommonFlexAdapter(attendanceItems, this);
        recyclerAttenDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAttenDetail.setAdapter(attendanceAdapter);
        recyclerAttenDetail.setNestedScrollingEnabled(false);
        toolbar.inflateMenu(R.menu.menu_flow);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                BottomSheetListDialogFragment.start(SignUpGroupDetailFragment.this, 0, new String[] { "删除该分组" });
                return false;
            }
        });
    }

 public void onAddOrRemove(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_group_add_member:
                Intent intent = new Intent(getActivity(), SignUpChooseActivity.class);
                intent.putExtra(Configs.EXTRA_GYM_ID, trainIds.getGymId());
                intent.putExtra("competition_id", trainIds.getCompetitionId());
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_sign_group_remove_member:
                ArrayList<QcStudentBean> toDele = new ArrayList<>();
                toDele.addAll(DirtySender.studentList);
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.sign_up_detail_frag, DeleteMemberFragmentBuilder.newDeleteMemberFragment(toDele, teamId))
                    .addToBackStack(null)
                    .commit();
                break;
            case R.id.change_group_name:
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mCallbackActivity.getFragId(), SignUpChangeNameFragmentBuilder.newSignUpChangeNameFragment(teamId, title))
                    .addToBackStack(null)
                    .commit();
                break;
        }
    }

 void onOpenMember() {
        isOpened = true;
        presenter.queryGroupDetail(teamId);
    }

    @Override public void onGetSignUpDataSuccess(GroupBean data) {
        int index = 0;
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.group_rank_count_frag, RankCountFragment.newInstance(data.team_attendance, data.stat_rule.together))
            .commit();

        memberList = data.users;
        title = data.name;
        toolbarTitile.setText(title);
        tvSignGroupName.setText(title);
        tvSignMemberCount.setText(getContext().getString(R.string.sign_up_member_count, data.users.size()));

        memberItemList.clear();
        DirtySender.studentList.clear();
        if (data.users != null) {
            DirtySender.studentList.addAll(data.users);
            for (QcStudentBean student : data.users) {
                index++;
                if (index > 4 && !isOpened) {
                    openAllMember.setVisibility(View.VISIBLE);
                    break;
                } else if (isOpened) {
                    openAllMember.setVisibility(View.GONE);
                }

                SignUpGroupMemberItem memberItem = new SignUpGroupMemberItem(student);
                memberItem.setOperation(false);
                memberItemList.add(memberItem);
            }
        }

        attendanceItems.clear();
        if (data.users_attendance != null) {
            for (MemberAttendanceBean bean : data.users_attendance) {
                SignUpAttendanceItem attendanceItem = new SignUpAttendanceItem(bean, getContext());
                attendanceItems.add(attendanceItem);
            }
        }

        attendanceAdapter.notifyDataSetChanged();
        flexAdapter.notifyDataSetChanged();
    }

    @Override public void onFailed(String msg) {
        ToastUtils.show(msg);
    }

    @Override public void onSuccess() {
        if (!isDelete) {
            presenter.queryGroupDetail(teamId);
        }
    }

    @Override public void onDelSuccess() {
        getActivity().onBackPressed();
    }

    @Override public String getFragmentName() {
        return SignUpGroupDetailFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        RxBus.getBus().unregister(RefreshListEvent.class.getName(), subscription);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    DialogUtils.instanceDelDialog(getContext(), "删除分组？", new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (Integer.parseInt(IntentUtils.getIntentString(data)) == 0) {
                                isDelete = true;
                                presenter.deleteGroup(teamId);
                            }
                        }
                    }).show();
                    break;
                case 1:
                    //if (data.getParcelableArrayListExtra("data") != null) {           this.users.addAll(DirtySender.studentList);
                    presenter.operationGroup(teamId, null, BusinessUtils.students2ids(DirtySender.studentList));
                    //}
                    break;
            }
        }
    }
}
