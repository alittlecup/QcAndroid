package com.qingchengfit.fitcoach.fragment.course;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.GymUtils;
import com.qingchengfit.fitcoach.adapter.CoachCommentAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CourseTeacher;
import com.qingchengfit.fitcoach.component.PagerSlidingTabImageStrip;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseTeacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.StringUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


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
 * Created by Paper on 16/7/23.
 */
public class CoachCommentListFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.strip)
    PagerSlidingTabImageStrip strip;
    List<CourseTeacher> coaches = new ArrayList<>();

    @Inject
    RestRepository restRepository;
    @Inject
    CoachService coachService;
    @Inject
    Brand brand;
    @BindView(R.id.shop_img)
    ImageView shopImg;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.coach_score)
    TextView coachScore;
    @BindView(R.id.course_score)
    TextView courseScore;
    @BindView(R.id.server_score)
    TextView serverScore;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.no_data_layout)
    LinearLayout noDataLayout;
    private CoachCommentAdapter adapter;
    private Unbinder unbinder;

    public static CoachCommentListFragment newInstance(String courseid) {

        Bundle args = new Bundle();
        args.putString("c", courseid);
        CoachCommentListFragment fragment = new CoachCommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CoachCommentListFragment newInstance(String courseid, String shopid) {

        Bundle args = new Bundle();
        args.putString("c", courseid);
        args.putString("shop", shopid);
        CoachCommentListFragment fragment = new CoachCommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_comment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getActivity() instanceof CourseActivity)
            ((CourseActivity) getActivity()).getComponent().inject(this);
//        mCallbackActivity.setToolbar("", false, null, 0, null);
        String shopid = getArguments().getString("shop");
        HashMap<String, String> params = new HashMap<>();
        if (GymUtils.isInBrand(coachService)) {
            params.put("brand_id", brand.getId());
            params.put("shop_id", shopid);
        } else {
            params = GymUtils.getParams(coachService, brand);
        }
        RxRegiste(restRepository.getGet_api().qcGetCourseTeacher(App.staffId, getArguments().getString("c"), params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseCourseTeacher>() {
                    @Override
                    public void call(QcResponseCourseTeacher qcResponseCourseTeacher) {
                        if (qcResponseCourseTeacher.data != null && qcResponseCourseTeacher.data.teachers != null
                                && qcResponseCourseTeacher.data.teachers.size() > 0) {
                            coaches.clear();
                            coaches.addAll(qcResponseCourseTeacher.data.teachers);

                            noDataLayout.setVisibility(View.GONE);
                            adapter = new CoachCommentAdapter(getFragmentManager(), coaches);
                            viewpager.setAdapter(adapter);
                            strip.setViewPager(viewpager);
                        } else {
                            noDataLayout.setVisibility(View.VISIBLE);
                            img.setImageResource(R.drawable.no_teacher);
                            hint.setText(R.string.no_teacher_in_course);
                        }
                        if (qcResponseCourseTeacher.data != null && qcResponseCourseTeacher.data.shop != null) {
//                            mCallbackActivity.setToolbar(qcResponseCourseTeacher.data.courseDetail.getName(), false, null, 0, null);
                            Glide.with(getContext()).load(qcResponseCourseTeacher.data.shop.logo).placeholder(R.drawable.ic_default_header).into(shopImg);
                            shopName.setText(qcResponseCourseTeacher.data.shop.name);
                            courseScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getCourse_score()));
                            coachScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getTeacher_score()));
                            serverScore.setText(StringUtils.getFloatDot1(qcResponseCourseTeacher.data.courseDetail.getService_score()));
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
        );

        return view;
    }

    @Override
    public String getFragmentName() {
        return CoachCommentListFragment.class.getName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}