package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImageTwoTextAdapter;
import com.qingchengfit.fitcoach.adapter.ImageTwoTextBean;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseGymActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.sfl)
    SwipeRefreshLayout sfl;
    @Bind(R.id.item_gym_name)
    TextView itemGymName;
    @Bind(R.id.item_is_personal)
    TextView itemIsPersonal;
    @Bind(R.id.qc_identify)
    ImageView qcIdentify;
    @Bind(R.id.item_gym_phonenum)
    TextView itemGymPhonenum;
    @Bind(R.id.item_right)
    ImageView itemRight;

    private Subscription mHttpsub;
    private ImageTwoTextAdapter mGymsAdapter;
    private List<ImageTwoTextBean> mDatas = new ArrayList<>();
    private String mCurModel;
    private int mCurId;
    private String mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gym);
        ButterKnife.bind(this);
        mCurModel = getIntent().getStringExtra("model");
        mCurId = getIntent().getIntExtra("id", 0);
        mTitle = getIntent().getStringExtra("title");
        LogUtil.e("curmodel:"+mCurModel +"   xxid:"+mCurId);
        toolbar.setTitle("请选择您的场馆");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sfl.setRefreshing(true);
                refresh();
            }
        });
        itemGymName.setText(mTitle);
        if (mCurId == 0){
            itemRight.setVisibility(View.VISIBLE);
            itemRight.setImageResource(R.drawable.ic_green_right);
            itemGymName.setTextColor(getResources().getColor(R.color.primary));

        }else {
            itemRight.setVisibility(View.GONE);
            itemGymName.setTextColor(getResources().getColor(R.color.text_black));

        }

        mGymsAdapter = new ImageTwoTextAdapter(mDatas);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mGymsAdapter);
        mGymsAdapter.setListener(new OnRecycleItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent it = new Intent();
                it.putExtra("name", mDatas.get(pos).text1);
                it.putExtra("type", mDatas.get(pos).tags.get("type"));
                it.putExtra("id", mDatas.get(pos).tags.get("id"));
                LogUtil.e("xxxxx id:" + mDatas.get(pos).tags.get("id"));
                it.putExtra("model", mDatas.get(pos).tags.get("model"));
                setResult(100, it);
                finish();
            }
        });
        initSfl();
    }

    public void initSfl() {
        sfl.setColorSchemeResources(R.color.primary);
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @OnClick(R.id.chooseall)
    public void chooseAll(){
        Intent it = new Intent();
        it.putExtra("name", "全部健身房");
        it.putExtra("type", 0);
        it.putExtra("id", "0");
        it.putExtra("model","");
        setResult(100, it);
        this.finish();
    }


    public void refresh() {
        mHttpsub = QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<QcCoachServiceResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QcCoachServiceResponse qcCoachServiceResponse) {
                        mDatas.clear();
                        List<CoachService> services = qcCoachServiceResponse.data.services;
                        for (CoachService service : services) {
                            ImageTwoTextBean bean = new ImageTwoTextBean(service.photo, service.name, "");
                            if (service.id == mCurId && mCurModel.equals(service.model)) {
                                bean.rightIcon = R.drawable.ic_green_right;
                                bean.showRight = true;
                            }
                            bean.tags.put("type", Integer.toString(1));
                            bean.tags.put("id", Long.toString(service.id));
                            bean.tags.put("model", service.model);
                            mDatas.add(bean);
                        }
                        mGymsAdapter.notifyDataSetChanged();
                        sfl.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (!mHttpsub.isUnsubscribed())
            mHttpsub.unsubscribe();
        super.onDestroy();
    }


}
