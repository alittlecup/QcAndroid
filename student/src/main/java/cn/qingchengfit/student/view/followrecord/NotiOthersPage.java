package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;

import cn.qingchengfit.student.databinding.StPageNotiOthersBinding;
import cn.qingchengfit.student.item.ChooseSalerItem;
import cn.qingchengfit.student.item.ChooseStaffItem;
import cn.qingchengfit.student.view.home.StudentListView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student",path = "/followrecord/notiothers/")
public class NotiOthersPage extends StudentBaseFragment<StPageNotiOthersBinding,NotiOthersVM>{
  StudentListView studentListView = new StudentListView();
  @Need public ArrayList<Staff> staffs;
  @Override protected void subscribeUI() {
  }


  @Override
  public StPageNotiOthersBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    getChildFragmentManager().registerFragmentLifecycleCallbacks(childrenCB, false);
    mBinding = StPageNotiOthersBinding.inflate(inflater);
    mBinding.setLifecycleOwner(this);

    return mBinding;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
    Bundle savedInstanceState) {
    if (f instanceof StudentListView){
      if (staffs !=null) {
        List<ChooseStaffItem> list = new ArrayList<>();
        for (Staff staff : staffs) {
        //  list.add();
        }
        studentListView.setItems(list);
      }
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(R.id.frag_noti_ohter,studentListView);
  }


  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("选择通知对象");
    toolbarModel.setMenu(R.menu.menu_compelete);
    toolbarModel.setListener(item -> {

      return false;
    });
    // TODO: 2018/7/5  设置Action 颜色
    //mBinding.setToolbarModel(toolbarModel);
    //initToolbar(mBinding.includeToolbar.toolbar);
  }



}
