package com.qingchengfit.fitcoach.fragment.batch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PermissionServerUtils;
import com.qingchengfit.fitcoach.action.SerPermisAction;
import com.qingchengfit.fitcoach.adapter.FragmentAdapter;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.bean.RxAddCourse;
import com.qingchengfit.fitcoach.fragment.BaseFragment;
import com.qingchengfit.fitcoach.fragment.batch.list.CourseBatchDetailFragment;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourse;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
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
 * Created by Paper on 16/3/22 2016.
 */
public class GymCoursesFragment extends BaseFragment implements GymCoursesView {

    @BindView(R.id.gym_img) ImageView gymImg;
    @BindView(R.id.gym_name) TextView gymName;
    @BindView(R.id.gym_title_tag) ImageView gymTitleTag;
    @BindView(R.id.gym_count) TextView gymCount;
    @BindView(R.id.myhome_tab) TabLayout myhomeTab;
    @BindView(R.id.viewpager) ViewPager viewpager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.layout_toolbar) RelativeLayout layoutToolbar;
    @BindView(R.id.linearlayout) LinearLayout linearlayout;
    private FragmentAdapter fragmentAdater;
    private Observable<RxAddCourse> mAddObserable;
    private Observable<ImageThreeTextBean> mCourseObserable;
    public static final String TAG = "GymCoursesFragment";

    @Inject GymCoursesPresenter presenter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_courses, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (getActivity() instanceof BatchActivity) ((BatchActivity) getActivity()).getComponent().inject(this);
        presenter.attachView(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setText(getString(R.string.course_batch));
        //        mCallbackActivity.setToolbar("课程排期", false, null, 0, null);
        if (fragments.size() == 0) {
            fragments.clear();
            fragments.add(CourseBatchDetailFragment.newInstance(Configs.TYPE_GROUP));
            fragments.add(CourseBatchDetailFragment.newInstance(Configs.TYPE_PRIVATE));
        }
        fragmentAdater = new FragmentAdapter(getChildFragmentManager(), fragments);
        viewpager.setAdapter(fragmentAdater);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(viewpager);
        initRxBus();
        return view;
    }

    private void initRxBus() {
        mAddObserable = RxBus.getBus().register(RxAddCourse.class);
        mAddObserable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxAddCourse>() {
            @Override public void call(RxAddCourse o) {
                QcResponseCourse.Course c = new QcResponseCourse.Course();

                if ((!c.is_private && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
                    || (c.is_private && !SerPermisAction.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
                    showAlert(R.string.alert_permission_forbid);
                    return;
                }

                if (((RxAddCourse) o).type == Configs.TYPE_GROUP) {
                    //跳转到新增团课
                    c.is_private = false;
                } else if (((RxAddCourse) o).type == Configs.TYPE_PRIVATE) {//新增私教排期
                    c.is_private = true;
                    //                            ChooseCoachFragment.start(GymCoursesFragment.this, 2, "", Configs.INIT_TYPE_CHOOSE);
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

            }
        });
        mCourseObserable = RxBus.getBus().register(ImageThreeTextBean.class);
        mCourseObserable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ImageThreeTextBean>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(ImageThreeTextBean imageThreeTextBean) {
                if (TextUtils.isEmpty(imageThreeTextBean.tags.get(ImageThreeTextBean.TAG_MODEL))) {
                    //                            imageThreeTextBean.tags.put(ImageThreeTextBean.TAG_MODEL, mModel);
                    //                            imageThreeTextBean.tags.put(ImageThreeTextBean.TAG_ID, mId + "");
                }
                Fragment fragment;
                if (TextUtils.isEmpty(imageThreeTextBean.tags.get("isNewAdd"))) {
                    //                            fragment = CourseDetailFragment.newInstance(imageThreeTextBean);

                } else {
                    //                            fragment = AddCourseManageFragment.newInstance(imageThreeTextBean.tags.get("model")
                    //                                    , imageThreeTextBean.tags.get("gymid")
                    //                                    , imageThreeTextBean.tags.get(ImageThreeTextBean.TAG_COURSE)
                    //                                    , Integer.parseInt(imageThreeTextBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE))
                    //                                    , imageThreeTextBean.tags.get("length"));
                }
                //                        adCourse(fragment);
            }
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {  //新增私教排期 选择教练
                //                getFragmentManager().beginTransaction()
                //                        .add(mCallbackActivity.getFragId(), ManageCourseBatchFragment.newInstance())
            }
        }
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        RxBus.getBus().unregister(RxAddCourse.class.getName(), mAddObserable);
        RxBus.getBus().unregister(ImageThreeTextBean.class.getName(), mCourseObserable);
        super.onDestroyView();
    }

    @Override public void onData(ArrayList<ImageThreeTextBean> privateCourse, ArrayList<ImageThreeTextBean> groupCourse, String privateUrl,
        String groupUrl) {
        //        fragments.add(CourseListFragment.newInstance(1, Configs.TYPE_GROUP, groupCourse, groupUrl));
        //        fragments.add(CourseListFragment.newInstance(1, Configs.TYPE_PRIVATE, privateCourse, privateUrl));
        //        fragmentAdater.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return GymCoursesFragment.class.getName();
    }
}
