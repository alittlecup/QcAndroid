package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.BodyTestActivity;
import com.qingchengfit.fitcoach.adapter.SimpleAdapter;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.BodyTestBean;
import com.qingchengfit.fitcoach.http.bean.BodyTestReponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 15/11/20 2015.
 */
public class StudentBodyTestListFragment extends BaseFragment {

  //    @BindView(R.id.add1)
  //    Button add1;
  //    @BindView(R.id.add2)
  //    Button add2;
  private List<BodyTestBean> mDataList = new ArrayList<>();
  private SimpleAdapter mAdapter;
  private String model;
  private String modelid;
  private LinearLayout noDataLayout;
  private TextView tvNoDataHint;

  @Inject GymWrapper gymWrapper;
  @Inject StudentWrap studentWrap;

  public static StudentBodyTestListFragment newInstance(String model, String modelid) {

    Bundle args = new Bundle();
    args.putString("model", model);
    args.putString("modelid", modelid);
    StudentBodyTestListFragment fragment = new StudentBodyTestListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //model = getArguments().getString("model");
    //modelid = getArguments().getString("modelid");
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
    View toolbarView = view.findViewById(R.id.include_toolbar);
    noDataLayout = view.findViewById(R.id.layout_no_data);
    tvNoDataHint = view.findViewById(R.id.nodata_hint);
    toolbarView.setVisibility(View.VISIBLE);
    Toolbar toolbar = view.findViewById(R.id.toolbar);
    TextView toolbarTitle = view.findViewById(R.id.toolbar_title);
    toolbarTitle.setText("体测数据");
    initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.add);

    toolbar.setOnMenuItemClickListener(item -> {
      if (!CurentPermissions.newInstance()
          .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
        showAlert(R.string.alert_permission_forbid);
        return false;
      }

      Intent toAdd = new Intent(getContext(), BodyTestActivity.class);
      toAdd.putExtra("type", 1);
      toAdd.putExtra("model", gymWrapper.model());
      toAdd.putExtra("modelid", gymWrapper.id());
      toAdd.putExtra("studentid", studentWrap.id());
      getContext().startActivity(toAdd);
      return false;
    });
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    mAdapter = new SimpleAdapter(mDataList);
    mAdapter.setListener((v, pos) -> {
      if (!CurentPermissions.newInstance()
          .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
        showAlert(R.string.alert_permission_forbid);
        return;
      }
      Intent toAdd = new Intent(getActivity(), BodyTestActivity.class);
      toAdd.putExtra("id", mDataList.get(pos).id);
      toAdd.putExtra("type", 0);
      toAdd.putExtra("model", gymWrapper.model());
      toAdd.putExtra("modelid", gymWrapper.id());
      toAdd.putExtra("gender", studentWrap.gender());
      startActivity(toAdd);
    });
    recyclerView.setAdapter(mAdapter);
    return view;
  }

  @Override public void onResume() {
    super.onResume();
    getStudentBodyTest();
  }

  @Override protected boolean isfitSystemPadding() {
    return false;
  }

  public void getStudentBodyTest() {
    Map<String, String> params = new HashMap<>();
    params.put("model", gymWrapper.model());
    params.put("id", gymWrapper.id());
    TrainerRepository.getStaticTrainerAllApi()
        .qcGetStuedntBodyTest(studentWrap.id(), params)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<BodyTestReponse>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(BodyTestReponse bodyTestReponse) {
            List<BodyTestBean> strings = new ArrayList<BodyTestBean>();
            for (BodyTestReponse.BodyTestMeasure measure : bodyTestReponse.data.measures) {
              BodyTestBean bodyTestBean = new BodyTestBean();
              bodyTestBean.data =
                  DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(measure.created_at))
                      + "体测数据";
              bodyTestBean.id = measure.id;
              strings.add(bodyTestBean);
            }
            setData(strings);
            if (bodyTestReponse.data.measures.size() == 0){
              noDataLayout.setVisibility(View.VISIBLE);
              tvNoDataHint.setText("暂无体测数据");
            }
          }
        });
  }

  public void setData(List<BodyTestBean> strings) {
    mDataList.clear();
    mDataList.addAll(strings);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
