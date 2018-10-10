package cn.qingchengfit.saasbase.course.batch.views;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.saasbase.databinding.FragmentEditPrivateOpenTimeBinding;
import cn.qingchengfit.utils.DateUtils;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
 * Created by Paper on 2017/12/19.
 */

public class EditPrivateOpenTimeFragment extends BottomSheetDialogFragment {

  Time_repeat timeRepeat;
  FragmentEditPrivateOpenTimeBinding db;
  SimpleScrollPicker interValpicker;
  TimeDialogWindow timePickerDialog;
  private ToolbarModel toolbarModel = new ToolbarModel("修改开放时间");

  public static EditPrivateOpenTimeFragment newInstance(Time_repeat tim) {
    Bundle args = new Bundle();
    EditPrivateOpenTimeFragment fragment = new EditPrivateOpenTimeFragment();
    args.putParcelable("tr", tim);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) timeRepeat = getArguments().getParcelable("tr");
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    db =
      DataBindingUtil.inflate(inflater, R.layout.fragment_edit_private_open_time, container, false);

    db.starttime.setContent(timeRepeat.getStart());
    db.endtime.setContent((timeRepeat.is_cross() ? "次日" : "") + timeRepeat.getEnd());
    db.civOrderInterval.setContent(timeRepeat.getSlice()/60+"分钟");
    db.starttime.setOnClickListener((View view) -> onStartTime());
    db.endtime.setOnClickListener(view -> onEndTime());

    RxView.clicks(db.civOrderInterval)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(this::onClickOrderInterval);
    db.layoutToolbar.toolbar.setNavigationIcon(R.drawable.vd_close_white_24dp);
    db.layoutToolbar.toolbar.setNavigationOnClickListener(view -> dismiss());

    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(item -> {
      RxBus.getBus().post(timeRepeat);
      dismiss();
      return true;
    });
    db.layoutToolbar.setToolbarModel(toolbarModel);
    return db.getRoot();
  }



  protected void onStartTime() {
    timePickerDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
    timePickerDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        db.starttime.setContent(DateUtils.Date2HHmm(date));
        timeRepeat.setStart(DateUtils.Date2HHmm(date));
      }
    });
    Date d = new Date();
    try {
      d = DateUtils.HHMM2date(db.starttime.getContent());
    } catch (Exception e) {

    }
    timePickerDialog.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
  }

  protected void onEndTime() {
    timePickerDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.TODAY_HOURS_MINS, 5);
    timePickerDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTodayTimeSelectListener() {
      @Override public void onTimeSelect(Date date, boolean isToday) {
        db.endtime.setContent((isToday ? "" : "次日") + DateUtils.Date2HHmm(date));
        timeRepeat.setEnd(DateUtils.Date2HHmm(date));
        timeRepeat.setIs_cross(!isToday);
      }

      @Override public void onTimeSelect(Date date) {

      }
    });
    Date d = new Date();
    try {
      String tim = db.endtime.getCivContent();
      if (tim.contains("次日")) tim = tim.replace("次日", "").trim();
      d = DateUtils.HHMM2date(tim);
    } catch (Exception e) {

    }
    timePickerDialog.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
  }

  //时间间隔选择
  private void onClickOrderInterval(Void aVoid) {
    if (interValpicker == null) {
      interValpicker = new SimpleScrollPicker(getContext());
      interValpicker.setLabel("分钟");
      interValpicker.setListener(pos -> {
        db.civOrderInterval.setContent(60 * (pos+1) / 12 + "分钟");
        timeRepeat.setSlice(60 * (pos+1) * 5);
      });
    }
    int pos  = 0;
    try {
      String interval = db.civOrderInterval.getContent();
      if (interval.contains("分钟"))
        interval = interval.replace("分钟","").trim();
      pos = Integer.parseInt(interval)/5;
    }catch (Exception e){

    }
    interValpicker.show(5, 65, 5, pos);
  }

  @Override public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();

    if (dialog != null) {
      View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
      bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    }
    View view = getView();
    view.post(() -> {
      View parent = (View) view.getParent();
      CoordinatorLayout.LayoutParams params =
        (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
      CoordinatorLayout.Behavior behavior = params.getBehavior();
      BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
      bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

      parent.setBackgroundColor(Color.TRANSPARENT);
    });
  }


}
