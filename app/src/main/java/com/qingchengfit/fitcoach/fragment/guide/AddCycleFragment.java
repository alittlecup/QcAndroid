package com.qingchengfit.fitcoach.fragment.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.CmBean;
import com.qingchengfit.fitcoach.bean.RxbusBatchLooperConfictEvent;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.ToastUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
public class AddCycleFragment extends BaseFragment {
    @BindView(R.id.starttime)
    CommonInputView starttime;
    @BindView(R.id.endtime)
    CommonInputView endtime;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.week1)
    CommonInputView week1;
    @BindView(R.id.week2)
    CommonInputView week2;
    @BindView(R.id.week3)
    CommonInputView week3;
    @BindView(R.id.week4)
    CommonInputView week4;
    @BindView(R.id.week5)
    CommonInputView week5;
    @BindView(R.id.week6)
    CommonInputView week6;
    @BindView(R.id.week7)
    CommonInputView week7;
    @BindView(R.id.comfirm)
    Button comfirm;

    List<Integer> x = new ArrayList<>();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Date mStart = new Date();
    private Date mEnd = new Date();

    private TimeDialogWindow timeWindow;
    private CmBean mCmBean;
    private Observable<RxbusBatchLooperConfictEvent> obConflict;
    private long mCourselength = 0;
    private Unbinder unbinder;

    public static AddCycleFragment newInstance(CmBean cmBean) {

        Bundle args = new Bundle();
        args.putParcelable("cm", cmBean);
        AddCycleFragment fragment = new AddCycleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static AddCycleFragment newInstance(long length) {

        Bundle args = new Bundle();
        args.putLong("length", length);
        AddCycleFragment fragment = new AddCycleFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddCycleFragment newInstance(CmBean cmBean,long length) {

        Bundle args = new Bundle();
        args.putLong("length", length);
        args.putParcelable("cm", cmBean);
        AddCycleFragment fragment = new AddCycleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCmBean = getArguments().getParcelable("cm");
            mCourselength = getArguments().getLong("length");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_cycle, container, false);
        unbinder=ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbarTitle.setText(R.string.add_circle);


        starttime.setContent("8:00");

        mStart = DateUtils.getDateFromHHmm(starttime.getContent());
        mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
        obConflict = RxBus.getBus().register(RxbusBatchLooperConfictEvent.class);
        obConflict.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxbusBatchLooperConfictEvent>() {
            @Override
            public void call(RxbusBatchLooperConfictEvent rxbusBatchLooperConfictEvent) {
                if (rxbusBatchLooperConfictEvent.isConfict) {
                    ToastUtils.showDefaultStyle("与现有周期有冲突");
                } else {
                    getActivity().onBackPressed();
                }
            }
        });
        if (mCourselength != 0) {
            endtime.setVisibility(View.GONE);
            mStart = DateUtils.getDateFromHHmm(starttime.getContent());
            mEnd = new Date(mStart.getTime() + mCourselength * 1000);
        }else {
            endtime.setVisibility(View.VISIBLE);
            endtime.setContent("21:00");
            mStart = DateUtils.getDateFromHHmm(starttime.getContent());
            mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
        }
        setDesc();
        return view;
    }

    @Override
    public void onDestroyView() {
        RxBus.getBus().unregister(RxbusBatchLooperConfictEvent.class.getName(), obConflict);
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.comfirm)
    public void onCofirm() {
        CmBean cmBean = new CmBean();
        cmBean.week.addAll(x);
        cmBean.dateStart = mStart;
        cmBean.dateEnd = mEnd;
        RxBus.getBus().post(cmBean);
        getActivity().onBackPressed();

    }

    @OnClick({R.id.week1, R.id.week2, R.id.week3, R.id.week4, R.id.week5, R.id.week6, R.id.week7})
    public void onClick(View view) {
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
        try {

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
                week6.isShowRight() ? getString(R.string.week6) + "、" : "", week7.isShowRight() ? getString(R.string.week7) + "、" : "").toString();
            if (weekStr.length() > 0) {
                weekStr = weekStr.substring(0, weekStr.length() - 1);
                String timeStr = TextUtils.concat(starttime.getContent(), "-", DateUtils.getTimeHHMM(mEnd)).toString();
                desc.setText("每个" + weekStr + timeStr + "有此课程");
                comfirm.setEnabled(true);
            } else {
                comfirm.setEnabled(false);
                desc.setText("");
            }
        }catch (Exception e){

        }
    }

    @OnClick({R.id.starttime, R.id.endtime})
    public void onChangetime(View view) {
        if (timeWindow == null) {
            timeWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
        }
        if (view.getId() == R.id.starttime) {
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    starttime.setContent(DateUtils.getTimeHHMM(date));
                    mStart = DateUtils.getDateFromHHmm(starttime.getContent());
                    if (mCourselength != 0)
                        mEnd = new Date(mStart.getTime() + mCourselength * 1000);
                    setDesc();
                }
            });
        } else if (view.getId() == R.id.endtime) {
            timeWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    endtime.setContent(DateUtils.getTimeHHMM(date));
                    mEnd = DateUtils.getDateFromHHmm(endtime.getContent());
                    setDesc();
                }
            });
        }

        timeWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
    }

    private void chooseTime() {

    }


    @Override
    protected void lazyLoad() {

    }
}
