package cn.qingchengfit.saasbase.mvvm_student.view.webchoose;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageWebChooseStaffBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentFilterView;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.weex.module.QcNavigatorModule;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.gson.Gson;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbaole on 2018/2/27.
 * web选择会员的落地页
 */
@Leaf(module = "student", path = "/select_member/") public class ChooseStaffPage
    extends StudentBaseFragment<PageWebChooseStaffBinding, ChooseStaffViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  private StudentFilterViewModel filterViewModel;
  private StudentFilterView filterView;
  private String web_action;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      if (items.isEmpty()) return;
      mViewModel.isLoading.set(false);
      mViewModel.items.set(mViewModel.getSortViewModel().sortItems(items));
      mBinding.includeFilter.setItems(new ArrayList<>(items));
      mBinding.recyclerview.post(new Runnable() {
        @Override public void run() {
          if (selectId != null && !selectId.isEmpty()) {
            for (String id : selectId) {
              for (int pos = 0; pos < adapter.getItemCount(); pos++) {
                String id1 = ((StudentItem) adapter.getItem(pos)).getQcStudentBean().getId();
                if (id.equals(id1)) {
                  adapter.addSelection(pos);
                  continue;
                }
              }
            }
            adapter.notifyDataSetChanged();
          }
        }
      });
    });

    mViewModel.getEditAfterTextChange().observe(this, filter -> {
      adapter.setSearchText(filter);
      adapter.filterItems();
    });

    mViewModel.getSortViewModel().getFilterEvent().observe(this, aVoid -> {
      openDrawer();
      filterViewModel =
          ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
      filterViewModel.getmFilterMap().observe(this, map -> {
        if (map != null) {
          closeDrawer();
          mViewModel.loadSource(map);
          filterViewModel.getmFilterMap().setValue(null);
        }
      });
    });

    mViewModel.bottomClick.observe(this, aVoid -> {
      WebChooseStaffDialog f = new WebChooseStaffDialog();
      f.setTargetFragment(this, 0);
      f.show(getFragmentManager(), "");
      return;
    });

    mViewModel.sortEvent.observe(this, aVoid -> {
      mBinding.recyclerview.post(new Runnable() {
        @Override public void run() {
          resetSelect(mViewModel.selectStudents.getValue());
        }
      });
    });
    mViewModel.selectStudents.observe(this, studentBeans -> {
      resetSelect(studentBeans);
      mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
    });
    mViewModel.completeClick.observe(this, aVoid -> {
      Map<String, Object> members = new HashMap<>();
      members.put("members", mViewModel.selectStudents.getValue());
      setSelectedBack(new Gson().toJson(members));
    });
  }

  private void resetSelect(List<QcStudentBean> studentBeans) {
    adapter.clearSelection();
    if (studentBeans != null) {
      if (studentBeans.isEmpty()) {
        adapter.notifyDataSetChanged();
        return;
      }
      for (QcStudentBean bean : studentBeans) {
        for (int index = 0; index < adapter.getItemCount(); index++) {
          if (adapter.getItem(index) instanceof StudentItem) {
            String id = ((StudentItem) adapter.getItem(index)).getId();
            if (bean.id.equals(id)) {
              adapter.addSelection(index);
            }
          }
        }
      }
    }
    adapter.notifyDataSetChanged();
  }

  private void initFragment() {
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
  }

  private void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  private void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  @Override
  public PageWebChooseStaffBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageWebChooseStaffBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    initFragment();
    mBinding.setViewModel(mViewModel);
    mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
    paseUri();
    mBinding.llBottom.setVisibility(View.GONE);
    return mBinding;
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("选择会员");
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initRecyclerView() {
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    adapter.setFastScroller(mBinding.fastScroller);
    mBinding.fastScroller.setBarClickListener(letter -> {
      List<StudentItem> itemList = mViewModel.items.get();
      int position = 0;
      for (int i = 0; i < itemList.size(); i++) {
        if (itemList.get(i).getHeader() != null) {
          if (itemList.get(i).getHeader() instanceof StickerDateItem) {
            if (((StickerDateItem) itemList.get(i).getHeader()).getDate()
                .equalsIgnoreCase(letter)) {
              position = i;
            }
          }
        }
      }
      return position;
    });
  }

  private void setSelectedBack(String json) {
    if (TextUtils.isEmpty(web_action)) {
      RxBus.getBus().post(QcNavigatorModule.class, json);
    } else {
      Intent intent = new Intent();
      intent.putExtra("web_action", web_action);
      intent.putExtra("json", json);
      getActivity().setResult(Activity.RESULT_OK, intent);
    }
    getActivity().finish();
  }

  @Override public boolean onItemClick(int position) {
    if (adapter.getMode() == SelectableAdapter.Mode.SINGLE) {
      QcStudentBean student=((StudentItem) adapter.getItem(position)).getQcStudentBean();
      Map<String,Object> member=new HashMap<>();
      member.put("member",student);
      setSelectedBack(new Gson().toJson(member));
      return false;
    } else {
      mBinding.llBottom.setVisibility(View.VISIBLE);
    }
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    mViewModel.bottomTextCount.set(adapter.getSelectedItemCount());
    mViewModel.selectStudents.getValue().clear();
    mViewModel.selectStudents.getValue().addAll(getSelectDataBeans());

    return false;
  }

  public List<QcStudentBean> getSelectDataBeans() {
    List<QcStudentBean> studentBeans = new ArrayList<>();
    for (Integer pos : adapter.getSelectedPositions()) {
      studentBeans.add(((StudentItem) adapter.getItem(pos)).getQcStudentBean());
    }
    return studentBeans;
  }

  private List<String> selectId = new ArrayList<>();
  private Map<String, String> params = new HashMap<>();

  private void paseUri() {
    selectId.clear();
    Intent intent = getActivity().getIntent();
    web_action = intent.getStringExtra("web_action");
    Uri uri = intent.getData();
    String multiple = uri.getQueryParameter("multiple");
    params.put("shop_id", uri.getQueryParameter("shop_id"));
    params.put("brand_id", uri.getQueryParameter("brand_id"));
    if (!TextUtils.isEmpty(multiple) && multiple.equals("1")) {
      String user_ids = uri.getQueryParameter("user_ids");
      if (!TextUtils.isEmpty(user_ids)) {
        String[] ids = user_ids.split(",");
        for (String id : ids) {
          selectId.add(id);
        }
      }
      adapter.setMode(SelectableAdapter.Mode.MULTI);
    } else {
      String user = uri.getQueryParameter("user_id");
      if (!TextUtils.isEmpty(user)) {
        selectId.add(user);
      }
      adapter.setMode(SelectableAdapter.Mode.SINGLE);
    }
    mViewModel.loadSource(params);
  }
}
