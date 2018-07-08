package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageFollowRecordBinding;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;

@Leaf(module = "student", path = "/student/follow_record") public class FollowRecordPage
    extends StudentBaseFragment<StPageFollowRecordBinding, FollowRecordViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public StPageFollowRecordBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageFollowRecordBinding.inflate(inflater, container, false);
    initRecyclerView();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.fab.setOnClickListener(v -> {
      routeTo("student/follow_record_edit", null);
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  @Override public boolean onItemClick(int position) {
    return false;
  }
}
