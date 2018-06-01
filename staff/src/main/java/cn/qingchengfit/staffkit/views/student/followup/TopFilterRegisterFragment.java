package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/7.
 */
public class TopFilterRegisterFragment extends BaseFragment implements View.OnTouchListener {

	public TextView tvFollowUpRegisterStart;
	public TextView tvFollowUpRegisterEnd;
	public TextView tvDesc;
    public StudentFilter filter = new StudentFilter();
    public TimeDialogWindow pwTime;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up_top_filter_register, container, false);
      tvFollowUpRegisterStart = (TextView) view.findViewById(R.id.tv_follow_up_register_start);
      tvFollowUpRegisterEnd = (TextView) view.findViewById(R.id.tv_follow_up_register_end);
      tvDesc = (TextView) view.findViewById(R.id.tv_desc);
      view.findViewById(R.id.tv_follow_up_register_start)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              TopFilterRegisterFragment.this.onClick(v);
            }
          });
      view.findViewById(R.id.tv_follow_up_register_end)
          .setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              TopFilterRegisterFragment.this.onClick(v);
            }
          });
      view.findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onConfirmClick(v);
        }
      });
      view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onConfirmClick(v);
        }
      });

      initView();
        view.setOnTouchListener(this);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setOnTouchListener(this);
    }

    public void initDI() {

    }

    public void initView() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public String getFragmentName() {
        return this.getClass().getName();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_follow_up_register_start:
                if (pwTime == null) {
                  pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (filter == null) filter = new StudentFilter();
                        filter.registerTimeStart = DateUtils.Date2YYYYMMDD(date);
                        tvFollowUpRegisterStart.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.tv_follow_up_register_end:
                if (pwTime == null) {
                  pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
                }
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        if (filter == null) filter = new StudentFilter();
                        filter.registerTimeEnd = DateUtils.Date2YYYYMMDD(date);
                        tvFollowUpRegisterEnd.setText(DateUtils.Date2YYYYMMDD(date));
                        pwTime.dismiss();
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }

 public void onConfirmClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                new MaterialDialog.Builder(getContext()).content("确认重置所有筛选项么？")
                    .positiveText(R.string.common_comfirm)
                    .negativeText(R.string.common_cancel)
                    .autoDismiss(true)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            doReset();
                        }
                    })
                    .show();

                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(tvFollowUpRegisterStart.getText())) {
                    ToastUtils.show("请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(tvFollowUpRegisterEnd.getText())) {
                    ToastUtils.show("请选择结束时间");
                    return;
                }
                if (DateUtils.formatDateFromYYYYMMDD(filter.registerTimeStart).getTime() > DateUtils.formatDateFromYYYYMMDD(
                    filter.registerTimeEnd).getTime()) {

                    ToastUtils.show("结束时间不能早于开始时间");
                    return;
                }
                postEvent();
                break;
        }
    }

    public void doReset() {
        // TODO // 重置注册时间
        tvFollowUpRegisterStart.setText("");
        tvFollowUpRegisterEnd.setText("");
        filter.registerTimeStart = "";
        filter.registerTimeEnd = "";
        postEvent();
    }

    public void postEvent() {
        RxBus.getBus()
            .post(new FollowUpFilterEvent.Builder().withEventType(FollowUpFilterEvent.EVENT_REGISTER_CONFIRM_CLICK)
                .withPosition(-1)
                .withFilter(filter)
                .build());
    }

    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
