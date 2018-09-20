package cn.qingchengfit.saasbase.staff.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.staff.presenter.TrainerHomePresenter;
import cn.qingchengfit.saascommon.widget.bubble.BubbleViewUtil;
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

  private BubbleViewUtil bubbleViewUtil;

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
          bubbleViewUtil.closeBubble();
          DialogSheet.builder(getContext())
            .addButton("教练职位与权限设置", view -> {
              QRActivity.start(getContext(),QRActivity.PERMISSION_TRAINER);
            })
            .show();
        }
        return true;
      })
      .build());
    bubbleViewUtil = new BubbleViewUtil(getContext());
    bubbleViewUtil.showBubbleOnceDefaultToolbar(toolbar, "点击这里管理教练权限", "trainerHome", 0);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    bubbleViewUtil.closeBubble();
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
