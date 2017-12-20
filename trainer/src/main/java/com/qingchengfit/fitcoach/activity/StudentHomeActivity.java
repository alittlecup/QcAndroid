package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import cn.qingchengfit.bean.BaseInfoBean;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.bean.StatementBean;
import cn.qingchengfit.bean.StudentCardBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.StudentBaseInfoFragment;
import com.qingchengfit.fitcoach.fragment.StudentBodyTestListFragment;
import com.qingchengfit.fitcoach.fragment.StudentCardFragment;
import com.qingchengfit.fitcoach.fragment.StudentClassRecordFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.BodyTestBean;
import com.qingchengfit.fitcoach.http.bean.BodyTestReponse;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import com.qingchengfit.fitcoach.http.bean.StudentCourseResponse;
import com.qingchengfit.fitcoach.http.bean.StudentInfoResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 15/11/19 2015.
 */
public class StudentHomeActivity extends BaseActivity {

    @BindView(R.id.add1) Button add1;
    @BindView(R.id.add2) Button add2;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private View mMyhomeBgView;
    private ImageView mHeaderImageView;
    private ImageView mGenderImageView;
    private TextView mNameTextView;
    private TabLayout mTabTabLayout;
    private AppBarLayout mMyhomeAppBarAppBarLayout;
    private ViewPager mStudentViewPager;
    private FragmentAdapter mAdapter;
    private Toolbar mToolbar;
    private String mModel;
    private String mModelId;
    private String mStudentId;
    private String mStudentShipId;
    private StudentBaseInfoFragment studentBaseInfoFragment;
    private StudentClassRecordFragment studentClassRecordFragment;
    private StudentCardFragment studentCardFragment;
    private StudentBodyTestListFragment studentBodyTestListFragment;
    private int mModelType = 1;
    private String gourpUrl;
    private String privateUrl;
    private Observable<Object> mObserveRefresh;
    private int mGender = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        ButterKnife.bind(this);
        mMyhomeBgView = (View) findViewById(R.id.myhome_bg);
        mHeaderImageView = (ImageView) findViewById(R.id.header);
        mGenderImageView = (ImageView) findViewById(R.id.gender);
        mNameTextView = (TextView) findViewById(R.id.name);
        mTabTabLayout = (TabLayout) findViewById(R.id.tab);
        mMyhomeAppBarAppBarLayout = (AppBarLayout) findViewById(R.id.myhome_appBar);
        mStudentViewPager = (ViewPager) findViewById(R.id.student);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mModel = getIntent().getStringExtra("model");
        mModelId = getIntent().getStringExtra("id");
        mStudentId = getIntent().getStringExtra("student_id");
        mModelType = getIntent().getIntExtra("modeltype", 1);
        mStudentShipId = getIntent().getStringExtra("ship_id");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText("学员详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initHeader();
        initViewPager();
        initBaseInfo();
        getCourseRecords();
        if (mModel.equalsIgnoreCase("service") && mModelType == 1) {
        } else {
            getStudentCards();
        }
        getStudentBodyTest();

        mObserveRefresh = RxBus.getBus().register(RxBus.BUS_REFRESH);
        mObserveRefresh.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Object o) {
                getStudentBodyTest();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        //if (mModel.equalsIgnoreCase("service") && mModelType == 1) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        //}

        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.action_delete) {
            delStudent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void delStudent() {
        MaterialDialog delStudentDialog = new MaterialDialog.Builder(this).content("是否删除该学员")
            .autoDismiss(true)
            .positiveText("确定")
            .negativeText("取消")
            .callback(new MaterialDialog.ButtonCallback() {
                @Override public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    QcCloudClient.getApi().postApi.qcDelStudent(App.coachid + "", mStudentShipId, getParams())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<QcResponse>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {
                                ToastUtils.showDefaultStyle("删除失败");
                            }

                            @Override public void onNext(QcResponse qcResponse) {
                                if (qcResponse.status == ResponseResult.SUCCESS) {
                                    setResult(1001);
                                    ToastUtils.showDefaultStyle("删除成功");
                                    StudentHomeActivity.this.finish();
                                } else {
                                    ToastUtils.showDefaultStyle("删除失败");
                                }
                            }
                        });
                }
            })
            .build();
        delStudentDialog.show();
    }

    public void initBaseInfo() {
        QcCloudClient.getApi().getApi.qcGetStudentInfo(App.coachid + "", mStudentShipId, getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<StudentInfoResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(StudentInfoResponse studentInfoResponse) {
                    QcStudentBean user = studentInfoResponse.data.ship;

                    Glide.with(App.AppContex)
                        .load(studentInfoResponse.data.ship.user.avatar)
                        .asBitmap()
                        .error(user.user.gender == 0 ? R.drawable.default_student_male : R.drawable.default_student_female)
                        .into(new CircleImgWrapper(mHeaderImageView, StudentHomeActivity.this));
                    mNameTextView.setText(user.username);
                    if (user.user.gender == 0) {
                        Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_male).into(mGenderImageView);
                    } else {
                        Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_female).into(mGenderImageView);
                    }
                    List<BaseInfoBean> beans = new ArrayList<BaseInfoBean>();
                    BaseInfoBean phone = new BaseInfoBean(R.drawable.ic_baseinfo_phone, "手机", user.phone);
                    String birthDay;
                    if (TextUtils.isEmpty(user.date_of_birth)) {
                        birthDay = "暂无";
                    } else {
                        birthDay = user.date_of_birth;
                    }
                    BaseInfoBean birth = new BaseInfoBean(R.drawable.ic_baseinfo_wechat, "生日", birthDay);
                    BaseInfoBean address = new BaseInfoBean(R.drawable.ic_baseinfo_city, "地址", user.address);
                    BaseInfoBean registe = new BaseInfoBean(R.drawable.ic_baseinfo_introduce, "注册日期",
                        DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(user.joined_at)));
                    beans.add(phone);
                    beans.add(birth);
                    beans.add(address);
                    beans.add(registe);
                    mGender = user.user.gender;
                    gourpUrl = studentInfoResponse.data.group_url;
                    privateUrl = studentInfoResponse.data.private_url;
                    if (studentBaseInfoFragment != null) studentBaseInfoFragment.setDatas(beans);
                }
            });
    }

    public void getCourseRecords() {
        QcCloudClient.getApi().getApi.qcGetStuedntCourse(mStudentId, getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<StudentCourseResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(StudentCourseResponse studentCourseResponse) {
                    int lastyear = 0;
                    List<StatementBean> datas = new ArrayList<>();
                    for (StudentCourseResponse.Schedule schedule : studentCourseResponse.data.schedules) {
                        int year = DateUtils.getYear(DateUtils.formatDateFromServer(schedule.start));
                        boolean showHeader = false, showBottom = false;
                        if (lastyear != year) {
                            lastyear = year;
                            showHeader = true;
                        }
                        String start = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.start));
                        String end = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(schedule.end));
                        StatementBean bean =
                            new StatementBean(DateUtils.formatDateFromServer(schedule.start), schedule.course.photo, schedule.course.name,
                                start + "-" + end + "   教练:" + schedule.teacher.username, false, false, showHeader, schedule.url);
                        datas.add(bean);
                    }
                    if (studentClassRecordFragment != null) studentClassRecordFragment.setDatas(datas);
                }
            });
    }

    public void getStudentCards() {
        QcCloudClient.getApi().getApi.qcGetStuedntCard(mStudentId, getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<StudentCarsResponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(StudentCarsResponse studentCarsResponse) {
                    List<StudentCardBean> mData = new ArrayList<>();
                    for (StudentCarsResponse.Card card : studentCarsResponse.data.cards) {
                        String balance = "";
                        String time = "";
                        if (!card.check_valid) {
                            time = "无限制";
                        } else {
                            time = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.valid_from)) + "至" + DateUtils.Date2YYYYMMDD(
                                DateUtils.formatDateFromServer(card.valid_to));
                        }

                        if (card.type == 1) {
                            balance = "余额" + card.account + "元";
                        } else if (card.type == 2) {
                            balance = "剩余" + card.times + "次";
                        } else if (card.type == 3) {
                            //                                balance = "到期时间"+DateUtils.getDateDay(DateUtils.formatDateFromServer(card.end));
                            balance = "";
                            time = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.start)) + "至" + DateUtils.Date2YYYYMMDD(
                                DateUtils.formatDateFromServer(card.end));
                        } else {
                            balance = "余额" + card.account + "元";
                        }

                        StudentCardBean bean = new StudentCardBean(card.name, balance, card.id, card.users, time, card.url);
                        mData.add(bean);
                    }
                    if (studentCardFragment != null) studentCardFragment.setmData(mData);
                }
            });
    }

    public void getStudentBodyTest() {
        QcCloudClient.getApi().getApi.qcGetStuedntBodyTest(mStudentId, getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<BodyTestReponse>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }

                @Override public void onNext(BodyTestReponse bodyTestReponse) {
                    List<BodyTestBean> strings = new ArrayList<BodyTestBean>();
                    for (BodyTestReponse.BodyTestMeasure measure : bodyTestReponse.data.measures) {
                        BodyTestBean bodyTestBean = new BodyTestBean();
                        bodyTestBean.data = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(measure.created_at)) + "体测数据";
                        bodyTestBean.id = measure.id;
                        strings.add(bodyTestBean);
                    }
                    if (studentBodyTestListFragment != null) studentBodyTestListFragment.setData(strings);
                }
            });
    }

    @OnClick(R.id.add1) public void onBtn1() {
        if (mStudentViewPager.getCurrentItem() == 1) {
            goGroup();
        } else {
            if (!CurentPermissions.newInstance().queryPermission(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
                showAlert(R.string.alert_permission_forbid);
                return;
            }

            Intent toAdd = new Intent(this, BodyTestActivity.class);
            toAdd.putExtra("type", 1);
            toAdd.putExtra("model", mModel);
            toAdd.putExtra("modelid", mModelId);
            toAdd.putExtra("studentid", mStudentId);
            startActivity(toAdd);
        }
    }

    @OnClick(R.id.add2) public void onBtn2() {
        goPrivate();
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        studentBaseInfoFragment = new StudentBaseInfoFragment();
        studentClassRecordFragment = new StudentClassRecordFragment();
        fragments.add(studentBaseInfoFragment);
        fragments.add(studentClassRecordFragment);
        if (mModel.equalsIgnoreCase("service") && mModelType == 1) {

        } else {
            studentCardFragment = new StudentCardFragment();
            fragments.add(studentCardFragment);
        }
        studentBodyTestListFragment = StudentBodyTestListFragment.newInstance(mModel, mModelId);

        fragments.add(studentBodyTestListFragment);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mStudentViewPager.setAdapter(mAdapter);
        mStudentViewPager.setOffscreenPageLimit(4);
        mStudentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.e("position:" + position);
                if (position == 1) {
                    add1.setVisibility(View.VISIBLE);
                    add2.setVisibility(View.VISIBLE);
                    add2.setText("代约私教");
                    add1.setText("代约团课");
                } else if (position == fragments.size() - 1) {
                    add1.setVisibility(View.VISIBLE);
                    add2.setVisibility(View.GONE);
                    add1.setText("新增体测信息");
                } else {
                    add1.setVisibility(View.GONE);
                    add2.setVisibility(View.GONE);
                }
            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        mTabTabLayout.setupWithViewPager(mStudentViewPager);
    }

    private void initHeader() {

    }

    public void goPrivate() {
        //LogUtil.e("privateurl:" + privateUrl);
        //if (!TextUtils.isEmpty(privateUrl)) {

        goWeb(Configs.Server + Configs.SCHEDULE_PRIVATE + "?id=" + mModelId + "&model=" + mModel + "&student_id=" + mStudentId);
        //}
    }

    public void goGroup() {
        //LogUtil.e("gourpUrl:" + gourpUrl);
        //if (!TextUtils.isEmpty(gourpUrl)) {
        goWeb(Configs.Server + Configs.SCHEDULE_GROUP + "?id=" + mModelId + "&model=" + mModel + "&student_id=" + mStudentId);
        //}
    }

    public void goWeb(String url) {
        Intent toWeb = new Intent(this, WebActivity.class);
        toWeb.putExtra("url", url);
        startActivity(toWeb);
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("model", mModel);
        params.put("id", mModelId);
        return params;
    }

    public int getGender() {
        return mGender;
    }
}
