package cn.qingchengfit.saasbase.coach.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.items.StaffSelectSingleItem;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saascommon.events.EventStaffWrap;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.utils.CrashUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/11/30.
 */
@Leaf(module = "staff", path = "/trainer/choose/") public class TrainerChooseFragment
    extends CoachListFragment {

  @Need public String selectedId;
  @Need public ArrayList<StaffShip> staffs;

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof StaffSelectSingleItem) {
      RxBus.getBus()
          .post(new EventStaffWrap.Builder().staff(
              ((StaffSelectSingleItem) item).getStaffForSeleted())
              .type(EventStaffWrap.TRAINER)
              .build());
    }
    popBack();
    return true;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    FrameLayout frameLayout = view.findViewById(R.id.fl_recycler_container);

    View bottomView =
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_add, frameLayout, false);
    TextView textView = bottomView.findViewById(R.id.tv_bottom_content);
    textView.setText("+ 邀请教练");
    bottomView.setVisibility(View.VISIBLE);
    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFab();
      }
    });
    frameLayout.addView(bottomView);

    if(staffs!=null&&!staffs.isEmpty()){
      List<CommonUserItem> staffItems = new ArrayList<>();
      for (StaffShip coach : staffs) {
        try {
          if (coach.teacher != null) {
            coach.id = coach.teacher.getId();
          } else {
            coach.id = coach.user.id;
          }
          staffItems.add(generateItem(coach));
        } catch (Exception e) {
          CrashUtils.sendCrash(e);
        }
      }
      setDatas(staffItems, 1);
    }
    return view;
  }

  @Override public int getFbIcon() {
    return 0;
  }

  @Override protected String getTitle() {
    return "选择教练";
  }

  @Override public void setDatas(List<? extends IFlexible> ds, int page) {
    if (TextUtils.isEmpty(selectedId)) {
      commonFlexAdapter.updateDataSet(ds, true);
    } else {
      commonFlexAdapter.updateDataSet(ds, false);
    }
  }

  @Override public void onUpdateEmptyView(int size) {
    for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
      IFlexible item = commonFlexAdapter.getItem(i);
      if (item instanceof StaffSelectSingleItem) {
        if (selectedId != null
            && ((StaffSelectSingleItem) item).getStaff() != null
            && TextUtils.equals(selectedId, ((StaffSelectSingleItem) item).getStaff().id)) {
          commonFlexAdapter.addSelection(i);
          commonFlexAdapter.notifyItemChanged(i);
          break;
        }
      }
    }
  }
}
