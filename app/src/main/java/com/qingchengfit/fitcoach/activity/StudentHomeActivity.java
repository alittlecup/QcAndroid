package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.FragmentAdapter;
import com.qingchengfit.fitcoach.bean.BaseInfoBean;
import com.qingchengfit.fitcoach.bean.StatementBean;
import com.qingchengfit.fitcoach.bean.StudentCardBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.StudentBaseInfoFragment;
import com.qingchengfit.fitcoach.fragment.StudentBodyTestListFragment;
import com.qingchengfit.fitcoach.fragment.StudentCardFragment;
import com.qingchengfit.fitcoach.fragment.StudentClassRecordFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.BodyTestBean;
import com.qingchengfit.fitcoach.http.bean.BodyTestReponse;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import com.qingchengfit.fitcoach.http.bean.StudentCourseResponse;
import com.qingchengfit.fitcoach.http.bean.StudentInfoResponse;
import com.qingchengfit.fitcoach.http.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class StudentHomeActivity extends BaseAcitivity {

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
    private StudentBaseInfoFragment studentBaseInfoFragment;
    private StudentClassRecordFragment studentClassRecordFragment;
    private StudentCardFragment studentCardFragment;
    private StudentBodyTestListFragment studentBodyTestListFragment;
    private int mModelType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        mMyhomeBgView = (View) findViewById(R.id.myhome_bg);
        mHeaderImageView = (ImageView) findViewById(R.id.header);
        mGenderImageView = (ImageView) findViewById(R.id.gender);
        mNameTextView = (TextView) findViewById(R.id.name);
        mTabTabLayout = (TabLayout) findViewById(R.id.tab);
        mMyhomeAppBarAppBarLayout = (AppBarLayout) findViewById(R.id.myhome_appBar);
        mStudentViewPager = (ViewPager) findViewById(R.id.student);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mModel = getIntent().getStringExtra("model");
        mModelId= getIntent().getStringExtra("id");
        mStudentId = getIntent().getStringExtra("student_id");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("学员详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initHeader();
        initViewPager();
//        mToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mToolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                initBaseInfo();
//            }
//        });
        initBaseInfo();
        getCourseRecords();
        if (mModel.equalsIgnoreCase("service") && mModelType == 1){

        }else
            getStudentCards();
        getStudentBodyTest();
    }

    public void initBaseInfo(){
        QcCloudClient.getApi().getApi.qcGetStudentInfo(mStudentId, getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StudentInfoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StudentInfoResponse studentInfoResponse) {
                        User user = studentInfoResponse.data.user;
                        Glide.with(App.AppContex).load(studentInfoResponse.data.user.avatar).asBitmap().into(new CircleImgWrapper(mHeaderImageView, StudentHomeActivity.this));
                        mNameTextView.setText(user.username);
                        if (user.gender == 0){
                            Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_male).into(mGenderImageView);
                        }else {
                            Glide.with(App.AppContex).load(R.drawable.ic_gender_signal_female).into(mGenderImageView);
                        }
                        List<BaseInfoBean> beans = new ArrayList<BaseInfoBean>();
                        BaseInfoBean phone = new BaseInfoBean (R.drawable.ic_baseinfo_phone,"手机",(mModel.equalsIgnoreCase("service")&& mModelType ==1)?user.phone:user.hidden_phone);
                        BaseInfoBean birth = new BaseInfoBean(R.drawable.ic_baseinfo_wechat,"生日", DateUtils.getDateDay(DateUtils.formatDateFromServer(user.date_of_birth)));
                        BaseInfoBean address = new BaseInfoBean(R.drawable.ic_baseinfo_city,"地址",user.address);
                        BaseInfoBean registe = new BaseInfoBean(R.drawable.ic_baseinfo_introduce,"注册日期",DateUtils.getDateDay(DateUtils.formatDateFromServer(user.joined_at)));
                        beans.add(phone);
                        beans.add(birth);
                        beans.add(address);
                        beans.add(registe);
                        if (studentBaseInfoFragment != null)
                            studentBaseInfoFragment.setDatas(beans);
                    }
                })
        ;
    }
    public void  getCourseRecords(){
        QcCloudClient.getApi().getApi.qcGetStuedntCourse(mStudentId, getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StudentCourseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StudentCourseResponse studentCourseResponse) {
                        int lastyear = 0;
                        List<StatementBean> datas = new ArrayList<>();
                        for (StudentCourseResponse.Schedule schedule:studentCourseResponse.data.schedules){
                            int year = DateUtils.getYear(DateUtils.formatDateFromServer(schedule.start));
                            boolean showHeader = false,showBottom = false;
                            if (lastyear != year){
                                lastyear = year;
                                showHeader = true;
                            }
                            StatementBean bean = new StatementBean(DateUtils.formatDateFromServer(schedule.start),
                                    schedule.course.photo,schedule.course.name,"教练:"+schedule.teacher.username,false,false,showHeader);
                            datas.add(bean);
                        }
                        if (studentClassRecordFragment!=null)
                            studentClassRecordFragment.setDatas(datas);
                    }
                });
    }

    public void getStudentCards(){
        QcCloudClient.getApi().getApi.qcGetStuedntCard(mStudentId, getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StudentCarsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StudentCarsResponse studentCarsResponse) {
                        List<StudentCardBean> mData = new ArrayList<>();
                        for (StudentCarsResponse.Card card:studentCarsResponse.data.cards){
                            String balance = "";
                            if (card.type == 1){
                                balance = "余额"+card.account+"元";
                            }else if (card.type == 2){
                                balance = "剩余"+card.times+"次";
                            }else if (card.type == 3){
                                balance = "到期时间"+DateUtils.getDateDay(DateUtils.formatDateFromServer(card.end));
                            }else {
                                balance = "余额"+card.account+"元";
                            }

                            StudentCardBean bean = new StudentCardBean(card.name,balance,"代数据",card.users,
                                    DateUtils.getDateDay(DateUtils.formatDateFromServer(card.valid_from))+"-"
                                    +DateUtils.getDateDay(DateUtils.formatDateFromServer(card.valid_to)));
                            mData.add(bean);
                        }
                        if (studentCardFragment != null)
                            studentCardFragment.setmData(mData);
                    }
                });
    }

    public void getStudentBodyTest(){
        QcCloudClient.getApi().getApi.qcGetStuedntBodyTest(mStudentId, getParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BodyTestReponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BodyTestReponse bodyTestReponse) {
                        List<BodyTestBean> strings = new ArrayList<BodyTestBean>();
                        for (BodyTestReponse.BodyTestMeasure measure:bodyTestReponse.data.measures){
                            BodyTestBean bodyTestBean = new BodyTestBean();
                            bodyTestBean.data = DateUtils.getDateDay(DateUtils.formatDateFromServer(measure.created_at))+"体测数据";
                            bodyTestBean.id = measure.id;
                            strings.add(bodyTestBean);
                        }
                        if (studentBodyTestListFragment!=null)
                            studentBodyTestListFragment.setData(strings);

                    }
                });
    }


    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        studentBaseInfoFragment = new StudentBaseInfoFragment();
        studentClassRecordFragment = new StudentClassRecordFragment();
        if (mModel.equalsIgnoreCase("service") && mModelType == 1){

        }else
            studentCardFragment = new StudentCardFragment();
        studentBodyTestListFragment = new StudentBodyTestListFragment();
        fragments.add(studentBaseInfoFragment);
        fragments.add(studentClassRecordFragment);
        fragments.add(studentCardFragment);
        fragments.add(studentBodyTestListFragment);
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mStudentViewPager.setAdapter(mAdapter);
        mStudentViewPager.setOffscreenPageLimit(4);
        mTabTabLayout.setupWithViewPager(mStudentViewPager);
    }

    private void initHeader() {

    }

    public HashMap<String,String> getParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("model",mModel);
        params.put("id",mModelId);
        return params;
    }

}
