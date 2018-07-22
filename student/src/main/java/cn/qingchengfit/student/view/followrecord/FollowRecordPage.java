package cn.qingchengfit.student.view.followrecord;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.Attach;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.databinding.StPageFollowRecordBinding;
import cn.qingchengfit.student.item.FollowRecordItem;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.views.fragments.TitleFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Leaf(module = "student", path = "/student/follow_record") public class FollowRecordPage
    extends StudentBaseFragment<StPageFollowRecordBinding, FollowRecordViewModel>
    implements FlexibleAdapter.OnItemClickListener, TitleFragment {
  CommonFlexAdapter adapter;
  private StudentBean studentBean;

  @Override protected void subscribeUI() {
    if (studentBean != null) {
      mViewModel.id = studentBean.id;
    }
    mViewModel.getLiveItems().observe(this, items -> {
      adapter.updateDataSet(items);
    });
  }

  public void setCurStudent(StudentBean student) {
    this.studentBean = student;
    if (mViewModel != null) {
      mViewModel.id = student.id;
    }
  }

  @Override
  public StPageFollowRecordBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageFollowRecordBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    mBinding.setVm(mViewModel);
    initRecyclerView();
    initListener();
    HashMap<String, Object> map = new HashMap<String, Object>();
    mViewModel.loadSource(map);
    return mBinding;
  }

  private void initListener() {
    mBinding.fab.setOnClickListener(v -> {
      Uri uri = Uri.parse("qcstaff://student/student/follow_record_edit");
      routeTo(uri, new FollowRecordEditPageParams().studentBean(studentBean).build(), false);
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setStackFromEnd(true);
    mBinding.recyclerView.setLayoutManager(manager);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof FollowRecordItem) {
      FollowRecord data = ((FollowRecordItem) item).getData();
      if (data.getAttachSize() > 0) {
        List<String> links = new ArrayList<>();
        for (int i = 0; i < data.getAttachSize(); i++) {
          links.add(data.getImagePos(i));
        }
        MultiChoosePicFragment multiChoosePicFragment = MultiChoosePicFragment.newInstance(links);
        multiChoosePicFragment.setShowFlag(true);
        multiChoosePicFragment.show(getChildFragmentManager(), "");
      }
    }
    return false;
  }

  @Override public String getTitle() {
    return "跟进记录";
  }
}
