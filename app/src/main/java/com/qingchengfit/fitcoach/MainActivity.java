package com.qingchengfit.fitcoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.utils.LogUtil;
import com.qingchengfit.fitcoach.utils.PhoneFuncUtils;

import org.xwalk.core.XWalkPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.float_btn)
    FloatingActionButton mFloatBtn;
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        XWalkFragment xWalkFragment = new XWalkFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fraglayout,xWalkFragment).commit();
        startActivity(new Intent(this,LoginActivity.class));
//        mWebview.load("http://www.baidu.com", null);
    }

    @OnClick(R.id.float_btn)
    public void onFloatClick(){
        LogUtil.i("onclick");
//        PhoneFuncUtils.initContactList(this);
//        PhoneFuncUtils.insertCalendar(this);
//        PhoneFuncUtils.queryCalender(this);
        PhoneFuncUtils.queryEvent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
