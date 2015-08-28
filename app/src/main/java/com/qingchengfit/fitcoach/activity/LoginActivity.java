package com.qingchengfit.fitcoach.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.RecieveMsg;
import com.qingchengfit.fitcoach.fragment.LoginFragment;
import com.qingchengfit.fitcoach.fragment.RegisterFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_viewpager)
    ViewPager loginViewpager;
    @Bind(R.id.login_tabview)
    TabLayout loginTablayout;

    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        if (PreferenceUtils.getPrefBoolean(this, "first", true))
            startActivityForResult(new Intent(this, SplashActivity.class), 11);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "ZVc12KfmeoroYVV0iLcvSCCr");

//        if (PreferenceUtils.getPrefString(this,"session_id",null)!=null)
//            startActivity(new Intent(this,MainActivity.class));
        loginViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(loginTablayout));
        loginViewpager.setAdapter(new LoginFragAdapter(getSupportFragmentManager()));
        loginTablayout.setupWithViewPager(loginViewpager);

        smsObserver = new SmsObserver(this, new Handler(getMainLooper()));
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PreferenceUtils.setPrefBoolean(this, "first", false);
        loginViewpager.setCurrentItem(resultCode);
    }

    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "address", "person"};// "_id", "address",

        String where = " date >  " + (System.currentTimeMillis() - 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容

//			System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
//			System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
//			System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);
//            if(number.equals("106902281006")){//验证发送的短信号码
            Pattern pattern = Pattern.compile("[0-9]{6}");//4位数字验证码
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group().substring(0, 6);// 获取短信的内容
                //send msg
                RxBus.getBus().send(new RecieveMsg(res));
            }
//            }
        }
    }

    class LoginFragAdapter extends FragmentPagerAdapter{

        public LoginFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                return  new LoginFragment();
            }else return new RegisterFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
           if (position ==0 ){
               return getString(R.string.login_btn_label);
           }else return getString(R.string.registe_indicat_label);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSmsFromPhone();
        }
    }


}
