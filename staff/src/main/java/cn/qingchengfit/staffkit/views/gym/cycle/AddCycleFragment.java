package cn.qingchengfit.staffkit.views.gym.cycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.responese.CmBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.RxbusBatchLooperConfictEvent;
import cn.qingchengfit.staffkit.views.custom.CommonInputView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.bigkoo.pickerview.lib.TimeDialogWindow;
import com.bigkoo.pickerview.lib.Type;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/29 2016.
 */
@FragmentWithArgs public class AddCycleFragment extends BaseFragment implements AddCycleView {
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.desc) TextView desc;
    @BindView(R.id.week1) CommonInputView week1;
    @BindView(R.id.week2) CommonInputView week2;
    @BindView(R.id.week3) CommonInputView week3;
    @BindView(R.id.week4) CommonInputView week4;
    @BindView(R.id.week5) CommonInputView week5;
    @BindView(R.id.week6) CommonInputView week6;
    @BindView(R.id.week7) CommonInputView week7;
    @BindView(R.id.comfirm) Button comfirm;

    @Arg(required = false) CmBean mCmBean;
    @Arg(required = false) long mCourselength = 0;

    List<Integer> x = new ArrayList<>();
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.label_can_order) TextView labelCanOrder;
    @Inject AddCyclePresenter presenter;
    private Date mStart = new Date();
    private Date mEnd = new Date();
    private TimeDialogWindow timeWindow;
    private Observable<RxbusBatchLooperConfictEvent> obConflict;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cycle, container, false);
        unbinder = ButterKnife.bind(this, view);

        delegatePresenter(presenter, this);
        //mCallbackActivity.setToolbar("添加周期", false, null, 0, null);
        //starttime.setContent("8:00");
        //endtime.setContent("21:00");
        //mStart = DateUtils.getDateFromHHmm(starttime.getContent());
        //mEnd = DateUtils.getDateFromHHmm(endtime.getContent());

        initToolbar(toolbar);

        obConflict = RxBus.getBus().register(RxbusBatchLooperConfictEvent.class);
        obConflict.subscribe(new Action1<RxbusBatchLooperConfictEvent>() {
            @Override public void call(RxbusBatchLooperConfictEvent rxbusBatchLooperConfictEvent) {
                if (rxbusBatchLooperConfictEvent.isConfict) {
                    ToastUtils.show("与现有周期有冲突");
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        if (mCmBean != null) {
            starttime.setContent(DateUtils.getTimeHHMM(mCmBean.dateStart));
            endtime.setContent(DateUtils.getTimeHHMM(mCmBean.dateEnd));
            mStart = mCmBean.dateStart;
            mEnd = mCmBean.dateEnd;
            week1.setShowRight(mCmBean.week.contains(1));
            week2.setShowRight(mCmBean.week.contains(2));
            week3.setShowRight(mCmBean.week.contains(3));
            week4.setShowRight(mCmBean.week.contains(4));
            week5.setShowRight(mCmBean.week.contains(5));
            week6.setShowRight(mCmBean.week.contains(6));
            week7.setShowRight(mCmBean.week.contains(7));
        } else {
            starttime.setContent("8:00");
            endtime.setContent("21:00");
            mStart = DateUtils.getDateFromHHmm(starttime.getContent());
            mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
        }

        if (mCourselength != 0) {
            endtime.setVisibility(View.GONE);
            labelCanOrder.setVisibility(View.GONE);
            mStart = DateUtils.getDateFromHHmm(starttime.getContent());
            mEnd = new Date(mStart.getTime() + mCourselength * 1000);
        } else {
            endtime.setVisibility(View.VISIBLE);
            labelCanOrder.setVisibility(View.VISIBLE);
            endtime.setContent("21:00");
            mStart = DateUtils.getDateFromHHmm(starttime.getContent());
            mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
        }

        setDesc();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("添加周期");
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    RxBus.getBus().post(new CmBean());
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(RxbusBatchLooperConfictEvent.class.getName(), obConflict);
        super.onDestroyView();
    }

    @OnClick(R.id.comfirm) public void onCofirm() {
        CmBean cmBean = new CmBean();
        cmBean.week.addAll(x);
        cmBean.dateStart = mStart;
        cmBean.dateEnd = mEnd;
        cmBean.position = mCmBean == null ? -1 : mCmBean.position;
        if (endtime.getVisibility() == View.GONE) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mStart);
            calendar.add(Calendar.SECOND, (int) mCourselength);
            cmBean.dateEnd = calendar.getTime();
        }
        RxBus.getBus().post(cmBean);
    }

    @OnClick({ R.id.week1, R.id.week2, R.id.week3, R.id.week4, R.id.week5, R.id.week6, R.id.week7 }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.week1:
                week1.toggle();
                break;
            case R.id.week2:
                week2.toggle();
                break;
            case R.id.week3:
                week3.toggle();
                break;
            case R.id.week4:
                week4.toggle();
                break;
            case R.id.week5:
                week5.toggle();
                break;
            case R.id.week6:
                week6.toggle();
                break;
            case R.id.week7:
                week7.toggle();
                break;
        }

        setDesc();
    }

    private void setDesc() {
        x.clear();
        if (week1.isShowRight()) x.add(1);
        if (week2.isShowRight()) x.add(2);
        if (week3.isShowRight()) x.add(3);
        if (week4.isShowRight()) x.add(4);
        if (week5.isShowRight()) x.add(5);
        if (week6.isShowRight()) x.add(6);
        if (week7.isShowRight()) x.add(7);

        String weekStr = TextUtils.concat(week1.isShowRight() ? getString(R.string.week1) + "、" : "",
            week2.isShowRight() ? getString(R.string.week2) + "、" : "", week3.isShowRight() ? getString(R.string.week3) + "、" : "",
            week4.isShowRight() ? getString(R.string.week4) + "、" : "", week5.isShowRight() ? getString(R.string.week5) + "、" : "",
            week6.isShowRight() ? getString(R.string.week6) + "、" : "", week7.isShowRight() ? getString(R.string.week7) + "、" : "")
            .toString();
        if (weekStr.length() > 0) {
            weekStr = weekStr.substring(0, weekStr.length() - 1);
            String timeStr = TextUtils.concat(starttime.getContent(), "-", DateUtils.getTimeHHMM(mEnd)).toString();
            desc.setText("每个" + weekStr + timeStr + "有此课程");
            comfirm.setEnabled(true);
        } else {
            comfirm.setEnabled(false);
            desc.setText("");
        }
    }

    @OnClick({ R.id.starttime, R.id.endtime }) public void onChangetime(View view) {
        if (timeWindow == null) {
            timeWindow = new TimeDialogWindow(getContext(), Type.HOURS_MINS, 5);
        }
        if (view.getId() == R.id.starttime) {
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    starttime.setContent(DateUtils.getTimeHHMM(date));
                    mStart = DateUtils.getDateFromHHmm(starttime.getContent());
                    if (mCourselength != 0) mEnd = new Date(mStart.getTime() + mCourselength * 1000);
                }
            });
        } else if (view.getId() == R.id.endtime) {
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override public void onTimeSelect(Date date) {
                    endtime.setContent(DateUtils.getTimeHHMM(date));
                    mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
                }
            });
        }

        timeWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
    }

    private void chooseTime() {

    }

    @Override public String getFragmentName() {
        return AddCycleFragment.class.getName();
    }
}
