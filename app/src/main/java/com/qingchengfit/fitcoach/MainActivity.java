package com.qingchengfit.fitcoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PhoneFuncUtils;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;

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
        startActivity(new Intent(this, LoginActivity.class));
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


}
