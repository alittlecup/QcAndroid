package cn.qingchengfit.student.view.choose;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.PageWebChooseStaffBinding;
import cn.qingchengfit.student.item.ChooseStaffItem;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentFilterViewModel;
import cn.qingchengfit.weex.module.QcNavigatorModule;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.gson.Gson;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
      if (items == null || items.isEmpty()) return;
      mViewModel.isLoading.set(false);
      List<IItemData> data = mViewModel.getSortViewModel().sortItems(items);
      List<StudentItem> items1=new ArrayList<>();
      for(IItemData data1:data){
        if(data1 instanceof StudentItem){
          items1.add((StudentItem) data1);
        }
      }
      mViewModel.items.set(items1);
      mBinding.includeFilter.setItems(items);
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
          mViewModel.loadSource(new HashMap<>(map));
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
    mBinding.setLifecycleOwner(this);
    mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());
    paseUri();
    mBinding.llBottom.setVisibility(View.GONE);
    mBinding.llDividerBottom.setVisibility(View.GONE);
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
      List<AbstractFlexibleItem> itemList = adapter.getMainItems();
      int position = 0;
      for (int i = 0; i < itemList.size(); i++) {
        if (itemList.get(i) instanceof StickerDateItem) {
          if (((StickerDateItem) itemList.get(i)).getDate().equalsIgnoreCase(letter)) {
            position = i;
          }
        }
      }
      return position;
    });

    RxRegiste(
        RxBus.getBus().register(SwipeRefreshLayout.class, Boolean.class).subscribe(aBoolean -> {
          mViewModel.isLoading.set(aBoolean);
        }));
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
      QcStudentBean student = ((StudentItem) adapter.getItem(position)).getQcStudentBean();
      Map<String, Object> member = new HashMap<>();
      member.put("member", student);
      setSelectedBack(new Gson().toJson(member));
      return false;
    } else {
      mBinding.llBottom.setVisibility(View.VISIBLE);
      mBinding.llDividerBottom.setVisibility(View.VISIBLE);
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
  private Map<String, Object> params = new HashMap<>();

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
