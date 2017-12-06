package cn.qingchengfit.testmodule;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.net.SocketException;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import cn.qingchengfit.student.BindingAdapters;
import cn.qingchengfit.testmodule.databinding.ActivityMainBinding;
import cn.qingchengfit.views.activity.BaseActivity;
import rx.RxReactiveStreams;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new MyApplication(getApplication());
//        Constant.setAccountType(BuildConfig.DEBUG ? 12162 : 12165);
//        Constant.setSdkAppid(BuildConfig.DEBUG ? 1400029014 : 1400029022);
//        Constant.setXiaomiPushAppid(BuildConfig.DEBUG ? "2882303761517418101" : "2882303761517418101");
//        Constant.setBussId(BuildConfig.DEBUG ? 611 : 605);
//        Constant.setXiaomiPushAppkey("5361741818101");
//        Constant.setHuaweiBussId(612);
//        LoginProcessor loginProcessor = null;
//        try {
//            loginProcessor = new LoginProcessor(getApplicationContext(), "qctest_7060",
//                "cloudtest.qingchengfit.cn", this);
//            loginProcessor.sientInstall();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final BaseRouter baseRouter = new BaseRouter();
//        new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe();
//        baseRouter.registeRouter("recruit", RecruitActivity.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        findViewById(R.id.btn_to_recruit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                baseRouter.routerTo("recruit", MainActivity.this);
                testObserble();
            }
        });
        findViewById(R.id.btn_to_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                Uri uri = Uri.parse("student://student/student/home");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_design).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, DesignActivity.class));
            }
        });
//        final Button button = findViewById(R.id.btn_design);
//        rx.Observable<Long> interval = rx.Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
//        Publisher<Long> longPublisher = RxReactiveStreams.toPublisher(interval);
//
//
//        rx.Observable<Long> longObservable = rx.Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
//        Publisher<Long> longPublisher1 = RxReactiveStreams.toPublisher(interval);
//        LiveData<Long> longLiveData = LiveDataReactiveStreams.fromPublisher(longPublisher);
//        longLiveData.observe(this, new Observer<Long>() {
//            @Override
//            public void onChanged(@Nullable Long aLong) {
//                button.setText(aLong + "");
//            }
//        });

        binding.setListener(this);
    }


    @Override
    public void onClick(View v) {
        Log.d("TAG", "onClick: ");
        testObserble();
    }

    private void testObserble() {
//        rx.Observable<Integer> map = rx.Observable.from(new Integer[]{10, 3, 4, 2, 4, 6, 0})
//                .map(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        return 300/integer;
//                    }
//                })
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.d("TAG", "call: " + throwable.getMessage());
//                    }
//                });
//        LiveData<Integer> integerLiveData = cn.qingchengfit.student.LiveDataReactiveStreams.fromPublisher(map);
//        integerLiveData.observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(@Nullable Integer integer) {
//                Log.d("TAG", "onChanged: " + integer);
//            }
//        });
    }
}
