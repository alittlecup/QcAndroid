package cn.qingchengfit.saasbase.staff.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.DialogUtils;
import com.anbillon.flabellum.annotations.Leaf;

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
@Leaf(module = "staff", path = "/leave/")
public class StaffLeaveDetailFragment extends StaffDetailFragment {

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    db.getToolbarModel().setTitle("离职人员详情");
    db.getToolbarModel().setMenu(0);
    db.phoneNum.setEditble(false);
    db.username.setCanClick(true);
    db.username.setClickable(true);
    db.btnDel.setVisibility(View.VISIBLE);
    db.btnAdd.setVisibility(View.GONE);
    db.btnDel.setText("复职");
  }



  @Override public void onHeaderLayoutClicked() {
    //离职人员不能修改头像
  }

  @Override public void clickGender() {
    //离职人员不能修改
  }



  /**
   * 重写  ：新功能为 恢复职位
   */
  @Override public void onBtnDelClicked() {
    DialogUtils.instanceDelDialog(getContext(), getString(R.string.at_recovery_sb_position, staffShip.username),
      (dialog, which) -> {
        presenter.resumeStaff();
      }).show();
  }

}
