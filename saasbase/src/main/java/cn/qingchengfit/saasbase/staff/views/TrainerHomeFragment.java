package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.bubble.BubblePopupView;
import cn.qingchengfit.saasbase.utils.SharedPreferenceUtils;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.staff.presenter.TrainerHomePresenter;
import cn.qingchengfit.views.DialogSheet;
import com.anbillon.flabellum.annotations.Leaf;
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
 * Created by Paper on 2018/1/4.
 */
@Leaf(module = "staff", path = "/trainer/home/")
public class TrainerHomeFragment extends StaffHomeFragment implements TrainerHomePresenter.MVPView{

  @Inject TrainerHomePresenter presenter;

  private BubblePopupView bubblePopupView;
  private SharedPreferenceUtils sharedPreferenceUtils;

  @Override void initFragment() {
    if (fragments.size() == 0){
      fragments.add(new TrainerTabListFragment());
      fragments.add(new TrainerTabInviteListFragment());
      fragments.add(new TrainerTabLeaveListFragment());
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    delegatePresenter(presenter,this);
    sharedPreferenceUtils = new SharedPreferenceUtils(getContext());
    View v = super.onCreateView(inflater, container, savedInstanceState);
    db.layoutSu.setVisibility(View.GONE);
    return v;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.toolbarLayout.toolbar.getMenu().clear();
    db.setToolbarModel(new ToolbarModel.Builder()
      .title("教练")
      .menu(R.menu.menu_search_flow)
      .listener(item -> {
        if (item.getItemId() == R.id.action_search){
          CommonUserSearchFragment.start(this,presenter.getAllCommonUser());
        }else if (item.getItemId() == R.id.action_flow){
          DialogSheet.builder(getContext())
            .addButton("教练职位与权限设置", view -> {
              QRActivity.start(getContext(),QRActivity.PERMISSION_TRAINER);
            })
            .show();
        }
        return true;
      })
      .build());
    updateBubbleView(toolbar);
  }

  private void updateBubbleView(Toolbar toolbar) {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        boolean isFirst = sharedPreferenceUtils.IsFirst("trainerHome");
        if(isFirst) {
          bubblePopupView = new BubblePopupView(getContext());
          bubblePopupView.show(toolbar, "点击这里管理教练权限", 75, 400);
          sharedPreferenceUtils.saveFlag("trainerHome", false);
        }
      }
    },1000);
  }

  @Override public void freshData() {
    presenter.queryData();
  }

  @Override public void onInvitionslistDone() {
    if (fragments.size() >2){
      fragments.get(1).onGetData(presenter.getInvitations());
      db.tab.getTabAt(1).setText("邀请中("+presenter.getInvitations().size()+")");
    }
  }

  @Override public void onStafflistDone() {
    if (fragments.size() > 2){
      fragments.get(0).onGetData(presenter.getStaffShips());
      fragments.get(2).onGetData(presenter.getLeaveStaffShips());
      db.tab.getTabAt(0).setText("在职("+presenter.getStaffShips().size()+")");
      db.tab.getTabAt(2).setText("已离职("+presenter.getLeaveStaffShips().size()+")");
    }
  }
}
