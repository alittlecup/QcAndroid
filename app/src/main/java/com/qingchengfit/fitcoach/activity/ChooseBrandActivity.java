package com.qingchengfit.fitcoach.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.BrandManageAdapterAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.component.DividerItemDecoration;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;



/**
 * Created by peggy on 16/5/26.
 */

public class ChooseBrandActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycleview)
    RecyclerView recycleview;

    private Subscription sp;
    List<Brand> datas = new ArrayList<>();
    private BrandManageAdapterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyleview_toolbar);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                onBackPressed();
            }
        });


        datas = new ArrayList<>();
        adapter = new BrandManageAdapterAdapter(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adapter);

        queryData();

    }

    public void queryData() {
        if (sp != null)
            sp.unsubscribe();
        sp = QcCloudClient.client.getApi.qcGetBrands(App.coachid+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseBrands>() {
                    @Override
                    public void call(final QcResponseBrands qcResponseBrands) {
                        if (qcResponseBrands.status == 200) {
                            datas.clear();
                            datas.addAll(qcResponseBrands.data.brands);
                            if (!getIntent().getBooleanExtra("disable", false)) {
                                Brand add = new Brand("-1");
                                datas.add(add);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setListener(new OnRecycleItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {



                                    if (pos < datas.size() - 1) {
                                        if (datas.get(pos).isHas_add_permission()) {
                                            if (datas.get(pos).getGym_count() > 0) {
                                                setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                                ChooseBrandActivity.this.finish();
                                                overridePendingTransition(R.anim.slide_hold,R.anim.slide_top_out);
                                            }else {
                                            }
                                        } else {
                                            ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand), datas.get(pos).getCreated_by()==null||datas.get(pos).getCreated_by().getUsername()==null?"":datas.get(pos).getCreated_by().getUsername()));
                                        }
                                    } else {
                                        if (Long.parseLong(datas.get(pos).getId()) < 0) {
                                            startActivityForResult(new Intent(ChooseBrandActivity.this, AddBrandActivity.class), 1);

                                        }
                                    }
                                }
                            });

                        } else {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            queryData();
        }
    }

    @Override
    protected void onDestroy() {
        if (sp != null)
            sp.unsubscribe();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_hold,R.anim.slide_top_out);

    }
}
