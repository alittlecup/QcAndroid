package cn.qingchengfit.staffkit.views.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.rxbus.event.BuyCardNextEvent;
import cn.qingchengfit.saasbase.common.bottom.BottomStudentsFragment;
import cn.qingchengfit.staffkit.views.student.choose.ChooseStudentListFragment;
import cn.qingchengfit.staffkit.views.student.edit.EditStudentInfoFragment;
import cn.qingchengfit.staffkit.views.student.list.StudentListView;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/2 2016.
 */

public class MutiChooseStudentFragment extends BaseFragment implements StudentListView {

	EditText searchviewEt;
	TextView tvSelectCount;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StaffRespository restRepository;
    EditStudentInfoFragment editStudentInfoFragment;
	Toolbar toolbar;
	TextView toolbarTitile;
    private ChooseStudentListFragment chooseStudentListFragment;
    private List<String> students = new ArrayList<>();

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            String chooseIds = "";
            String chooseUsers = "";
            //选择结果
            for (QcStudentBean studentBean : DirtySender.studentList) {
                chooseIds = TextUtils.concat(chooseIds, studentBean.getId(), ",").toString();
                chooseUsers = TextUtils.concat(chooseUsers, studentBean.getUsername(), ",").toString();
            }
            if (!TextUtils.isEmpty(chooseIds)) {
                if (chooseIds.endsWith(",")) chooseIds = chooseIds.substring(0, chooseIds.length() - 1);
                RxBus.getBus().post(new BuyCardNextEvent(1, chooseIds, chooseUsers.substring(0, chooseUsers.length() - 1)));
            } else {
                ToastUtils.show("请至少选择一个学员");
            }
            return true;
        }
    };

    public static MutiChooseStudentFragment newInstance(String students) {

        Bundle args = new Bundle();
        args.putString("students", students);
        MutiChooseStudentFragment fragment = new MutiChooseStudentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String stu = getArguments().getString("students");
            if (!TextUtils.isEmpty(stu)) {
                if (stu.contains(",")) {
                    students.addAll(Arrays.asList(stu.split(",")));
                } else {
                    students.add(stu);
                }
            }
        }
        chooseStudentListFragment = new ChooseStudentListFragment();
        editStudentInfoFragment = new EditStudentInfoFragment();
        DirtySender.studentList.clear();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_muti_choose_for_add_card, container, false);
      searchviewEt = (EditText) view.findViewById(R.id.searchview_et);
      tvSelectCount = (TextView) view.findViewById(R.id.tv_select_count);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.add_student).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          addStudent();
        }
      });
      view.findViewById(R.id.ll_show_select).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          MutiChooseStudentFragment.this.onClick();
        }
      });

      initToolbar(toolbar);
        getChildFragmentManager().beginTransaction().replace(R.id.frag_student_list, chooseStudentListFragment).commit();
        chooseStudentListFragment.setSortType(0);
        chooseStudentListFragment.setListener(new ChooseStudentListFragment.SelectChangeListener() {
            @Override public void onSelectChange(int count) {
                if (tvSelectCount != null) tvSelectCount.setText(count > 99 ? "..." : "" + count);
            }
        });

        RxTextView.textChangeEvents(searchviewEt)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<TextViewTextChangeEvent>() {
                @Override public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                    chooseStudentListFragment.localFilter(textViewTextChangeEvent.text().toString());
                }
            });
        refeshData();
        return view;
    }

    public void refeshData() {
        showLoadingTrans();
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", PermissionServerUtils.MANAGE_COSTS);
        params.put("method", "post");
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCardBundldStudents(loginStatus.staff_id(), params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<StudentListWrapper>>() {
                @Override public void call(QcDataResponse<StudentListWrapper> studentsQcResponseData) {
                    hideLoadingTrans();
                    if (ResponseConstant.checkSuccess(studentsQcResponseData)) {
                        if (studentsQcResponseData.getData().users != null) {
                            for (QcStudentBean student : studentsQcResponseData.getData().users) {
                                if (students.contains(student.getId())) DirtySender.studentList.add(student);
                            }
                            setChosenCount();
                            chooseStudentListFragment.setDatas(studentsQcResponseData.getData().users, null);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    showLoadingTrans();
                    ToastUtils.show(throwable.getMessage());
                }
            }));
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("选择会员");
        toolbar.inflateMenu(R.menu.menu_next);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        toolbarTitile.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            }
        });
    }

    @Override public void onDestroyView() {
        DirtySender.studentList.clear();
        super.onDestroyView();
    }

 public void addStudent() {
        editStudentInfoFragment.isAdd = true;
        getFragmentManager().beginTransaction()
            .replace(mCallbackActivity.getFragId(), editStudentInfoFragment)
            .addToBackStack(null)
            .commit();
    }

    @Override public String getFragmentName() {
        return MutiChooseStudentFragment.class.getName();
    }

    @Override public void onStudentList(List<StudentBean> list) {

    }

    @Override public void onFilterStudentList(List<StudentBean> list) {

    }

    @Override public void onFaied() {
        hideLoadingTrans();
    }

    @Override public void onStopFresh() {
        hideLoadingTrans();
    }

 public void onClick() {
        BottomStudentsFragment selectSutdentFragment = new BottomStudentsFragment();
        selectSutdentFragment.setListener(new BottomStudentsFragment.BottomStudentsListener() {
            @Override public void onBottomStudents(List<Personage> list) {
                if (chooseStudentListFragment != null) {
                    chooseStudentListFragment.selectStudent(ListUtils.transerList(new ArrayList<QcStudentBean>(), list));
                }
                setChosenCount();
            }
        });
        selectSutdentFragment.setDatas(DirtySender.studentList);
        selectSutdentFragment.show(getFragmentManager(), "");
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }

    private void setChosenCount() {
        if (tvSelectCount != null) {
            if (DirtySender.studentList.size() > 99) {
                tvSelectCount.setText("...");
            } else {
                tvSelectCount.setText("" + DirtySender.studentList.size());
            }
        }
    }
}
