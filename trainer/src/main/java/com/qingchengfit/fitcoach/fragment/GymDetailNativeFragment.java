package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.VpFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.adapter.FragmentAdater;
import com.qingchengfit.fitcoach.adapter.ImageThreeTextBean;
import com.qingchengfit.fitcoach.bean.RxAddCourse;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcGymDetailResponse;
import com.qingchengfit.fitcoach.http.bean.ShopCourse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymDetailNativeFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.gym_name) TextView gymName;
    @BindView(R.id.gym_count) TextView gymCount;
    @BindView(R.id.gym_img) ImageView gymImg;
    @BindView(R.id.linearlayout) LinearLayout linearlayout;
    @BindView(R.id.gym_title_tag) ImageView gymTitleTag;
    @BindView(R.id.myhome_tab) TabLayout myhomeTab;
    @BindView(R.id.viewpager) ViewPager viewpager;
    private long mId;
    private boolean mIsPrivate;
    private Subscription mHttpSc;
    private String mModel;
    private MaterialDialog alertDialog;
    private FragmentAdater fragmentAdater;
    private Observable mAddObserable;
    private Observable<ImageThreeTextBean> mCourseObserable;
    private Observable<String> mObservableFresh;
    private Unbinder unbinder;

    public GymDetailNativeFragment() {
    }

    /**
     * @param id 健身房id
     */
    public static GymDetailNativeFragment newInstance(long id, String model) {

        Bundle args = new Bundle();
        args.putString("model", model);
        args.putLong("id", id);
        GymDetailNativeFragment fragment = new GymDetailNativeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong("id");
            mModel = getArguments().getString("model");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym_detail_native, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(item -> {
            if (mModel.equals("service")) {
                getFragmentManager().beginTransaction()
                    .add(R.id.web_frag_layout, AddSelfGymFragment.newInstance(App.coachid))
                    .addToBackStack(null)
                    .commit();
            } else {
                if (alertDialog == null) {
                    alertDialog = new MaterialDialog.Builder(getContext()).content("无权编辑该健身房信息")
                        .autoDismiss(true)
                        .positiveText(R.string.common_i_konw)
                        .build();
                }
                alertDialog.show();
            }
            return true;
        });

        init();
        mAddObserable = RxBus.getBus().register(RxAddCourse.class);
        mAddObserable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Object o) {
                if (o instanceof RxAddCourse) {
                    if (((RxAddCourse) o).type == 1) {
                        //跳转到新增团课
                        adCourse(AddCourseFrament.newInstance(1, mModel, (int) mId, true));
                    } else if (((RxAddCourse) o).type == 2) {
                        adCourse(AddCourseFrament.newInstance(1, mModel, (int) mId, false));
                    } else {

                    }
                }
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
                    imageThreeTextBean.tags.put(ImageThreeTextBean.TAG_MODEL, mModel);
                    imageThreeTextBean.tags.put(ImageThreeTextBean.TAG_ID, mId + "");
                }
                Fragment fragment;
                if (TextUtils.isEmpty(imageThreeTextBean.tags.get("isNewAdd"))) {
                    fragment = CourseDetailFragment.newInstance(imageThreeTextBean);
                } else {
                    fragment =
                        AddCourseManageFragment.newInstance(imageThreeTextBean.tags.get("model"), imageThreeTextBean.tags.get("gymid"),
                            imageThreeTextBean.tags.get(ImageThreeTextBean.TAG_COURSE),
                            Integer.parseInt(imageThreeTextBean.tags.get(ImageThreeTextBean.TAG_COURSETYPE)),
                            imageThreeTextBean.tags.get("length"));
                }
                adCourse(fragment);
            }
        });
        mObservableFresh = RxBus.getBus().register(RxBus.BUS_REFRESH);
        mObservableFresh.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(String o) {
                LogUtil.e("tag:" + o);
                //                        if (o.equalsIgnoreCase(RxBus.BUS_REFRESH))
                init();
            }
        });
        //        mObservableDefault = RxBus.getBus().register(RxBus.BUS_DEFAUT);
        //        mObservableDefault.observeOn(AndroidSchedulers.mainThread())
        //                .subscribeOn(Schedulers.io())
        //                .subscribe(new Subscriber<String>() {
        //                    @Override
        //                    public void onCompleted() {
        //
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //
        //                    }
        //
        //                    @Override
        //                    public void onNext(String o) {
        //                        toolbar.setTitle(o);
        //
        //                    }
        //                });
        return view;
    }

    public void initViewPager(boolean sync, ArrayList<ImageThreeTextBean> pri, ArrayList<ImageThreeTextBean> group, String purl,
        String gurl) {

        List<VpFragment> fragments = new ArrayList<>();
        fragments.add(CourseListFragment.newInstance(sync ? 0 : 1, 2, group, gurl));
        fragments.add(CourseListFragment.newInstance(sync ? 0 : 1, 1, pri, purl));
        boolean isChange = group.size() == 0;

        fragmentAdater = new FragmentAdater(getChildFragmentManager(), fragments);
        viewpager.setAdapter(fragmentAdater);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(viewpager);
        if (isChange) viewpager.setCurrentItem(1);
    }

    public void adCourse(Fragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.web_frag_layout, fragment).addToBackStack(null).commit();
    }

    public void init() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", Long.toString(mId));

        mHttpSc = QcCloudClient.getApi().getApi.qcGetGymDetail(App.coachid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<QcGymDetailResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(QcGymDetailResponse qcGymDetailResponse) {
                    Glide.with(App.AppContex)
                        .load(PhotoUtils.getSmall(qcGymDetailResponse.data.service.photo))
                        .asBitmap()
                        .placeholder(R.drawable.ic_default_header)
                        .error(R.drawable.ic_default_header)
                        .into(new CircleImgWrapper(gymImg, App.AppContex));
                    toolbar.setTitle(qcGymDetailResponse.data.service.name);
                    boolean isSyncCourse = false;
                    if (qcGymDetailResponse.data.service.model.equals("service") && qcGymDetailResponse.data.service.type == 1) {
                        gymTitleTag.setVisibility(View.GONE);
                        isSyncCourse = false;
                    } else if (qcGymDetailResponse.data.service.model.equals("gym")) {
                        gymTitleTag.setVisibility(View.VISIBLE);
                        isSyncCourse = true;
                    }
                    gymName.setText(qcGymDetailResponse.data.service.name);
                    //                        gymBrand.setText(qcGymDetailResponse.data.service.);

                    //团课私教数量
                    gymCount.setText(
                        qcGymDetailResponse.data.service.courses_count + "门课程, " + qcGymDetailResponse.data.service.users_count + "名学员");
                    ArrayList<ImageThreeTextBean> privateCourse = new ArrayList<>();
                    ArrayList<ImageThreeTextBean> groupCourse = new ArrayList<>();

                    for (ShopCourse course : qcGymDetailResponse.data.shop.courses) {
                        ImageThreeTextBean bean = new ImageThreeTextBean(course.image_url, course.name, "时长: " + course.length / 60 + "分钟",
                            "累计" + course.course_count + "节,服务" + course.service_count + "人次");
                        bean.tags.put(ImageThreeTextBean.TAG_MODEL, qcGymDetailResponse.data.service.model);
                        bean.tags.put(ImageThreeTextBean.TAG_ID, qcGymDetailResponse.data.service.id + "");
                        bean.tags.put(ImageThreeTextBean.TAG_COURSE, course.id + "");
                        bean.tags.put(ImageThreeTextBean.TAG_LENGTH, course.length + "");
                        bean.tags.put(ImageThreeTextBean.TAG_COURSETYPE,
                            course.is_private ? Configs.TYPE_PRIVATE + "" : Configs.TYPE_GROUP + "");
                        if (isSyncCourse) {
                            bean.showRight = false;
                            bean.showIcon = true;
                        } else {
                            bean.showRight = true;
                            bean.showIcon = false;
                        }
                        if (course.is_private) {
                            privateCourse.add(bean);
                        } else {
                            groupCourse.add(bean);
                        }

                        //                            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_course,null);
                        //                            TextView text1 = (TextView)view.findViewById(R.id.text1);
                        //                            TextView text2 = (TextView)view.findViewById(R.id.text2);
                        //                            TextView text3 = (TextView)view.findViewById(R.id.text3);
                        //                            ImageView img = (ImageView)view.findViewById(R.id.img);
                        //                            ImageView righticon = (ImageView)view.findViewById(R.id.righticon);
                        //                            view.setOnClickListener(new View.OnClickListener() {
                        //                                @Override
                        //                                public void onClick(View v) {
                        //                                    getFragmentManager().beginTransaction()
                        //                                            .replace(R.id.web_frag_layout, AddCourseFrament.newInstance(2,mModel,(int)mId,(int)course.id,course.name,course.image_url,course.length,course.is_private))
                        //                                            .addToBackStack(null)
                        //                                            .commit();
                        //                                }
                        //                            });
                        //                            if (course != null){
                        //                                Glide.with(App.AppContex).load(course.image_url).into(img);
                        //                                text1.setText(course.name);
                        //                                text2.setText("时长"+course.length+"min");
                        //                                text3.setText("累计"+course.course_count+"节,服务"+course.service_count+"人次");
                        //                                if (course.is_private)
                        //                                    righticon.setVisibility(View.VISIBLE);
                        //                                else righticon.setVisibility(View.GONE);
                        //                            }else {
                        //                                LogUtil.e("course == null");
                        //                            }
                        //                            linearlayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(90f,getResources())));
                    }
                    initViewPager(isSyncCourse, privateCourse, groupCourse, qcGymDetailResponse.data.shop.private_url,
                        qcGymDetailResponse.data.shop.team_url);
                }
            });
    }

    @Override public void onDestroy() {
        if (mHttpSc != null && !mHttpSc.isUnsubscribed()) mHttpSc.unsubscribe();

        super.onDestroy();
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(RxAddCourse.class.getName(), mAddObserable);
        RxBus.getBus().unregister(RxBus.BUS_REFRESH, mObservableFresh);
        RxBus.getBus().unregister(ImageThreeTextBean.class.getName(), mCourseObserable);
        super.onDestroyView();
        unbinder.unbind();
    }
}
