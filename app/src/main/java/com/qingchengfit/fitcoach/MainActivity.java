package com.qingchengfit.fitcoach;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.utils.LogUtil;
import com.qingchengfit.fitcoach.utils.PhoneFuncUtils;

import org.xwalk.core.XWalkView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.webview)
    XWalkView mWebview;
    @Bind(R.id.float_btn)
    FloatingActionButton mFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mWebview.load("http://www.baidu.com", null);
    }

    @OnClick(R.id.float_btn)
    public void onFloatClick(){
        LogUtil.i("onclick");
//        PhoneFuncUtils.initContactList(this);
//        PhoneFuncUtils.insertCalendar(this);
        PhoneFuncUtils.queryCalender(this);
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
