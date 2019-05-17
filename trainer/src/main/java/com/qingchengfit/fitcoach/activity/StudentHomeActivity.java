package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.bean.StatementBean;
import cn.qingchengfit.bean.StudentCardBean;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.view.base.StudentBaseInfoBean;
import cn.qingchengfit.student.view.followrecord.CoachFollowRecordPage;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.activity.WebActivity;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.fragment.StudentCardFragment;
import com.qingchengfit.fitcoach.fragment.StudentClassRecordFragment;
import com.qingchengfit.fitcoach.fragment.StudentMoreInfoFragment;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.QcStudentBean;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import com.qingchengfit.fitcoach.http.bean.StudentCourseResponse;
import com.qingchengfit.fitcoach.http.bean.StudentInfoResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
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

  Button add1;
  Button add2;
  TextView toolbarTitle;
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
  private StudentMoreInfoFragment studentMoreInfoFragment;
  private StudentClassRecordFragment studentClassRecordFragment;
  private StudentCardFragment studentCardFragment;
  private int mModelType = 1;
  private String gourpUrl;
  private String privateUrl;
  private Observable<Object> mObserveRefresh;
  private int mGender = 0;

  @Inject GymWrapper gymWrapper;
  @Inject StudentWrap studentWrap;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_home);
    add1 = (Button) findViewById(R.id.add1);
    add2 = (Button) findViewById(R.id.add2);
    toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    findViewById(R.id.add1).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtn1();
      }
    });
    findViewById(R.id.add2).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtn2();
      }
    });

    mMyhomeBgView = (View) findViewById(R.id.myhome_bg);
    mHeaderImageView = (ImageView) findViewById(R.id.header);
    mGenderImageView = (ImageView) findViewById(R.id.gender);
    mNameTextView = (TextView) findViewById(R.id.name);
    mTabTabLayout = (TabLayout) findViewById(R.id.tab);
    mMyhomeAppBarAppBarLayout = (AppBarLayout) findViewById(R.id.myhome_appBar);
    mStudentViewPager = (ViewPager) findViewById(R.id.student);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mModel = gymWrapper.model();
    mModelId = gymWrapper.id();
    Uri data = getIntent().getData();
    mModelType = getIntent().getIntExtra("modeltype", 1);
    if (data != null) {
      mStudentId = data.getQueryParameter("studentId");
      mStudentShipId = data.getQueryParameter("shipId");
    }else{
      mStudentId = getIntent().getStringExtra("student_id");
      mStudentShipId = getIntent().getStringExtra("ship_id");
    }
    mToolbar.setNavigationIcon(R.drawable.vd_navigate_before_white_24dp);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("");
    toolbarTitle.setText("学员详情");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    StudentBean tempBean = new StudentBean();
    tempBean.id = mStudentId;
    studentWrap.setStudentBean(tempBean);
    initHeader();
    initViewPager();
    initBaseInfo();
    getCourseRecords();
    if (mModel.equalsIgnoreCase("service") && mModelType == 1) {
    } else {
      getStudentCards();
    }
    //getStudentBodyTest();

    mObserveRefresh = RxBus.getBus().register(RxBus.BUS_REFRESH);
    mObserveRefresh.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Object o) {
        //getStudentBodyTest();
      }
    });
  }

  @Override protected boolean isFitSystemBar() {
    return false;
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
      if (CurentPermissions.newInstance()
          .queryPermission(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_DELETE)) {
        delStudent();
      } else {
        showAlert(R.string.permission_forbid_del);
      }
    }
    return super.onOptionsItemSelected(item);
  }

  public void delStudent() {
    DialogUtils.showAlert(this, "是否删除该学员", (dialog, action) -> {
      dialog.dismiss();
      deleteStudent();
    });
  }

  private void deleteStudent() {
    TrainerRepository.getStaticTrainerAllApi().qcDelStudent(App.coachid + "", mStudentShipId, getParams())
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

  public void initBaseInfo() {
    TrainerRepository.getStaticTrainerAllApi().qcGetStudentInfo(App.coachid + "", mStudentShipId, getParams())
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
                .error(user.user.gender == 0 ? R.drawable.default_student_male
                    : R.drawable.default_student_female)
                .into(new CircleImgWrapper(mHeaderImageView, StudentHomeActivity.this));
            mNameTextView.setText(user.username);
            if (user.user.gender == 0) {
              Glide.with(App.AppContex)
                  .load(R.drawable.ic_gender_signal_male)
                  .into(mGenderImageView);
            } else {
              Glide.with(App.AppContex)
                  .load(R.drawable.ic_gender_signal_female)
                  .into(mGenderImageView);
            }
            StudentBean tmpStudentBean = new StudentBean();
            tmpStudentBean.id = studentInfoResponse.data.ship.user.id;
            tmpStudentBean.avatar = user.avatar;
            tmpStudentBean.checkin_avatar = user.avatar;
            //tmpStudentBean.sellers = studentBaseInfoEvent.user_student.getSellers();
            //tmpStudentBean.coaches = studentBaseInfoEvent.user_student.getCoaches();
            tmpStudentBean.gender = Integer.parseInt(user.gender) == 0;
            tmpStudentBean.phone = user.phone;
            tmpStudentBean.username = user.username;
            studentWrap.setStudentBean(tmpStudentBean);
            mGender = user.user.gender;
            gourpUrl = studentInfoResponse.data.group_url;
            privateUrl = studentInfoResponse.data.private_url;
            if (studentMoreInfoFragment != null) {
              studentMoreInfoFragment.setBean(
                  new StudentBaseInfoBean(user.avatar, user.phone, user.date_of_birth, user.address,
                      user.joined_at));
            }
          }
        });
  }

  public void getCourseRecords() {
    TrainerRepository.getStaticTrainerAllApi().qcGetStuedntCourse(mStudentId, getParams())
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
              StatementBean bean = new StatementBean(DateUtils.formatDateFromServer(schedule.start),
                  schedule.course.photo, schedule.course.name,
                  start + "-" + end + "   教练:" + schedule.teacher.username, false, false,
                  showHeader, schedule.url);
              datas.add(bean);
            }
            if (studentClassRecordFragment != null) studentClassRecordFragment.setDatas(datas);
          }
        });
  }

  public void getStudentCards() {
    TrainerRepository.getStaticTrainerAllApi()
        .qcGetStuedntCard(mStudentId, getParams())
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
                time = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.valid_from))
                    + "至"
                    + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.valid_to));
              }

              if (card.type == 1) {
                balance = "余额" + card.account + "元";
              } else if (card.type == 2) {
                balance = "剩余" + card.times + "次";
              } else if (card.type == 3) {
                //                                balance = "到期时间"+DateUtils.getDateDay(DateUtils.formatDateFromServer(card.end));
                balance = "";
                time = DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.start))
                    + "至"
                    + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.end));
              } else {
                balance = "余额" + card.account + "元";
              }

              StudentCardBean bean =
                  new StudentCardBean(card.name, balance, card.id, card.users, time, card.url);
              mData.add(bean);
            }
            if (studentCardFragment != null) studentCardFragment.setmData(mData);
          }
        });
  }

  public void onBtn1() {
    if (mStudentViewPager.getCurrentItem() == 0) {
      goGroup();
    }
  }

  public void onBtn2() {
    goPrivate();
  }

  private void initViewPager() {
    ArrayList<Fragment> fragments = new ArrayList<>();
    studentClassRecordFragment = new StudentClassRecordFragment();
    fragments.add(studentClassRecordFragment);
    if (mModel.equalsIgnoreCase("service") && mModelType == 1) {

    } else {
      studentCardFragment = new StudentCardFragment();
      fragments.add(studentCardFragment);
    }

    //studentBodyTestListFragment = StudentBodyTestListFragment.newInstance(mModel, mModelId);


    fragments.add(new CoachFollowRecordPage());
    studentMoreInfoFragment = new StudentMoreInfoFragment();
    fragments.add(studentMoreInfoFragment);
    mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
    mStudentViewPager.setAdapter(mAdapter);
    mStudentViewPager.setOffscreenPageLimit(4);
    mStudentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogUtil.e("position:" + position);
        if (position == 0) {
          add1.setVisibility(View.VISIBLE);
          add2.setVisibility(View.VISIBLE);
          add2.setText("代约私教");
          add1.setText("代约团课");
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

    goWeb(Configs.Server
        + Configs.SCHEDULE_PRIVATE
        + "?id="
        + mModelId
        + "&model="
        + mModel
        + "&student_id="
        + mStudentId);
    //}
  }

  public void goGroup() {
    //LogUtil.e("gourpUrl:" + gourpUrl);
    //if (!TextUtils.isEmpty(gourpUrl)) {
    goWeb(Configs.Server
        + Configs.SCHEDULE_GROUP
        + "?id="
        + mModelId
        + "&model="
        + mModel
        + "&student_id="
        + mStudentId);
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
