package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.MeasureUtils;
import com.bigkoo.pickerview.lib.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.OnWheelChangedListener;
import com.bigkoo.pickerview.lib.WheelView;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.fragment.statement.CourseChooseView;
import com.qingchengfit.fitcoach.fragment.statement.presenter.CourseChoosePresenter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/6/28 2016.
 */
public class CourseChooseDialogFragment extends BaseDialogFragment implements CourseChooseView {

    @BindView(R.id.wheellayout) LinearLayout wheellayout;
    @BindView(R.id.course_type) WheelView courseType;
    @BindView(R.id.course_list) WheelView courseList;
    @Inject CourseChoosePresenter presenter;

    private List<CourseTypeSample> mCourses;

    public static CourseChooseDialogFragment newInstance(ArrayList<CourseTypeSample> course) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("course", course);
        CourseChooseDialogFragment fragment = new CourseChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChoosePicDialogStyle);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        ArrayWheelAdapter<String> courseTypeAdatper =
            new ArrayWheelAdapter<String>(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.choose_course_type))),
                8);
        courseType.setAdapter(courseTypeAdatper);
        courseType.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 16f);
        presenter.attachView(this);
        if (getArguments() != null) {
            presenter.setCourse(getArguments());
            presenter.changeCourse();
        } else {
            presenter.queryCourse();
        }

        courseType.addChangingListener(new OnWheelChangedListener() {
            @Override public void onChanged(WheelView wheel, int oldValue, int newValue) {
                presenter.changeType(newValue - 1);
            }
        });
        return view;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = MeasureUtils.dpToPx(245f, getResources());
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ButtomDialogStyle);
        return dialog;
    }

    @OnClick(R.id.choose_course_comfirm) public void onClick() {
        int pos = ((WheelView) wheellayout.getChildAt(1)).getCurrentItem();
        if (mCourses != null && mCourses.size() > pos) {
            RxBus.getBus().post(mCourses.get(pos));
        }

        dismiss();
    }

    @Override public void onCourseList(List<CourseTypeSample> courseList) {
        mCourses = courseList;
        ArrayList<String> d = new ArrayList<>();
        for (int i = 0; i < courseList.size(); i++) {
            d.add(courseList.get(i).getName());
        }
        wheellayout.removeViewAt(1);
        WheelView wheelView = new WheelView(getContext());
        ArrayWheelAdapter<String> courseTypeAdatper = new ArrayWheelAdapter<String>(d, 16);
        wheelView.TEXT_SIZE = MeasureUtils.sp2px(getContext(), 15f);
        wheelView.setAdapter(courseTypeAdatper);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        wheellayout.addView(wheelView, params);
    }
}
