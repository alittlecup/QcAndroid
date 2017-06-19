package com.qingchengfit.fitcoach.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.IntentUtils;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.adapter.BrandManageAdapterAdapter;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by peggy on 16/5/26.
 */

public class ChooseBrandActivity extends AppCompatActivity {

    protected Subscription sp;
    protected List<Brand> datas = new ArrayList<>();
    protected BrandManageAdapterAdapter adapter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyleview_toolbar);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.choose_brand);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                onBackPressed();
            }
        });

        datas = new ArrayList<>();
        adapter = new BrandManageAdapterAdapter(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        queryData();
    }

    public void queryData() {
        if (sp != null) sp.unsubscribe();
        String curUser = PreferenceUtils.getPrefString(this, "user_info", "");
        if (curUser != null && !curUser.isEmpty()) {
            User u = new Gson().fromJson(curUser, User.class);

            sp = QcCloudClient.getApi().getApi.qcGetBrands(u.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseBrands>() {
                    @Override public void call(final QcResponseBrands qcResponseBrands) {
                        if (qcResponseBrands.status == 200) {
                            datas.clear();
                            datas.addAll(qcResponseBrands.data.brands);
                            if (!getIntent().getBooleanExtra("disable", false)) {
                                Brand add = new Brand("-1");
                                datas.add(add);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setListener(new OnRecycleItemClickListener() {
                                @Override public void onItemClick(View v, int pos) {
                                    if (pos < datas.size() - 1) {
                                        if (datas.get(pos).isHas_add_permission()) {
                                            //setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                            Brand brand = datas.get(pos);
                                            //String initStr = PreferenceUtils.getPrefString(ChooseBrandActivity.this, "initSystem", "");
                                            //CoachInitBean initBean = new CoachInitBean();
                                            //if (!TextUtils.isEmpty(initStr)){
                                            //    initBean = new Gson().fromJson(initStr, CoachInitBean.class);
                                            //}

                                            //initBean.brand_id = brand.getId();
                                            //PreferenceUtils.setPrefString(ChooseBrandActivity.this, "initSystem", new Gson().toJson(initBean));
                                            //startActivity(new Intent(ChooseBrandActivity.this,GuideActivity.class));
                                            setResult(Activity.RESULT_OK, IntentUtils.instancePacecle(brand));
                                            ChooseBrandActivity.this.finish();
                                            //overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                        } else {
                                            ToastUtils.show(String.format(Locale.CHINA, getString(R.string.no_permission_brand),
                                                datas.get(pos).getCreated_by() == null
                                                    || datas.get(pos).getCreated_by().getUsername() == null ? ""
                                                    : datas.get(pos).getCreated_by().getUsername()));
                                        }
                                    } else {
                                        if (Long.parseLong(datas.get(pos).getId()) < 0) {
                                            PreferenceUtils.setPrefString(ChooseBrandActivity.this, "initSystem", "");
                                            Intent toGuide = new Intent(ChooseBrandActivity.this, GuideActivity.class);
                                            toGuide.putExtra("add", true);

                                            startActivity(toGuide);
                                            ChooseBrandActivity.this.finish();
                                            //startActivityForResult(new Intent(ChooseBrandActivity.this, AddBrandActivity.class), 1);
                                        }
                                    }
                                }
                            });
                        } else {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {

                    }
                });
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            queryData();
        }
    }

    @Override protected void onDestroy() {
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }

    @Override public void onBackPressed() {
        this.finish();
        //overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
    }
}
