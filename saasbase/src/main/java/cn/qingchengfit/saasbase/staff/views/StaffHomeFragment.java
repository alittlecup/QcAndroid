package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentSaasStaffHomeBinding;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.presenter.StaffHomePresenter;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.DialogSheet;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2018/1/1.
 */
@Leaf(module = "staff",path = "/home/")
public class StaffHomeFragment extends BaseFragment implements StaffHomePresenter.MVPView{
  private StaffHomeAdapter adapter;
  List<BaseStaffListFragment> fragments = new ArrayList<>();
  FragmentSaasStaffHomeBinding db;

  @Inject IPermissionModel permissionModel;
  @Inject StaffHomePresenter presenter;
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new StaffHomeAdapter(getChildFragmentManager());
    initFragment();
  }



  void initFragment(){
    if (fragments.size() == 0){
      fragments.add(new StaffTabListFragment());
      fragments.add(new StaffTabInviteListFragment());
      fragments.add(new StaffTabLeaveListFragment());
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    db = FragmentSaasStaffHomeBinding.inflate(inflater);
    delegatePresenter(presenter,this);
    initToolbar(db.toolbarLayout.toolbar);

    db.vp.setOffscreenPageLimit(2);
    db.vp.setAdapter(adapter);
    db.tab.setupWithViewPager(db.vp);
    db.layoutSu.setOnClickListener(view -> {
      presenter.isSu();

    });

    return db.getRoot();
  }

  @Override protected void onFinishAnimation() {
    freshData();
    hasShown = false;
  }

  public void freshData(){
    presenter.queryData();
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.setToolbarModel(new ToolbarModel.Builder()
      .title("工作人员")
      .menu(R.menu.menu_search_flow)
      .listener(item -> {
        if (item.getItemId() == R.id.action_search){
          CommonUserSearchFragment.start(this,presenter.getAllCommonUser());
        }else if (item.getItemId() == R.id.action_flow){
          DialogSheet.builder(getContext())
            .addButton("工作人员职位与权限设置", view -> {
              QRActivity.start(getContext(),QRActivity.PERMISSION_STAFF);
            })
            .show();
        }
        return true;
      })
      .build());
  }


  @Override public void onStafflistDone() {
    if (fragments.size() > 2){
      fragments.get(0).onGetData(presenter.getStaffShips());
      fragments.get(2).onGetData(presenter.getLeaveStaffShips());
      db.tab.getTabAt(0).setText("在职("+presenter.getStaffShips().size()+")");
      db.tab.getTabAt(2).setText("已离职("+presenter.getLeaveStaffShips().size()+")");
    }
  }

  @Override public void onInvitionslistDone() {
    if (fragments.size() >2){
      fragments.get(1).onGetData(presenter.getInvitations());
      db.tab.getTabAt(1).setText("邀请中("+presenter.getInvitations().size()+")");
    }
  }

  @Override public void onSu(StaffShip staff) {
    PhotoUtils.smallCircle(db.suAvatar,staff.getAvatar(),staff.getGender() == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female,staff.getGender() == 0 ? R.drawable.default_manage_male : R.drawable.default_manager_female);
    db.suName.setText(staff.getUsername());
    db.suPhone.setText(staff.getPhone());
  }

  @Override public void routeTo(String path, Bundle bd) {
    super.routeTo(path,bd);
  }

  class StaffHomeAdapter extends FragmentPagerAdapter{

    public StaffHomeAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override public int getCount() {
      return fragments.size();
    }

    @Nullable @Override public CharSequence getPageTitle(int position) {
      switch (position){
        case 0:
          return "在职";
        case 1:
          return "邀请中";
          default:
            return "离职";
      }
    }
  }

  @Override public String getFragmentName() {
    return StaffHomeFragment.class.getName();
  }
}
