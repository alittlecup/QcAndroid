package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.adapter.BrandManageAdapterAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by peggy on 16/5/26.
 */

public class ChooseBrandActivity extends BaseActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.toolbar_title) public TextView toolbarTitile;
    @BindView(R.id.down) public ImageView down;
    @BindView(R.id.titile_layout) public LinearLayout titileLayout;
    @BindView(R.id.searchview_et) public EditText searchviewEt;
    @BindView(R.id.searchview_clear) public ImageView searchviewClear;
    @BindView(R.id.searchview_cancle) public Button searchviewCancle;
    @BindView(R.id.searchview) public LinearLayout searchview;
    @BindView(R.id.toolbar_layout) public RelativeLayout toolbarLayout;
    @BindView(R.id.recycleview) public RecyclerView recycleview;

    @Inject public RestRepository restRepository;
    public List<Brand> datas = new ArrayList<>();
    public Subscription sp;
    public BrandManageAdapterAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyleview_toolbar);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                onBackPressed();
            }
        });
        toolbarTitile.setText(R.string.choose_brand);
        datas = new ArrayList<>();
        adapter = new BrandManageAdapterAdapter(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(adapter);

        queryData();
    }

    public void queryData() {
        if (sp != null) sp.unsubscribe();

        sp = restRepository.getGet_api()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<BrandsResponse>>() {
                @Override public void call(final QcResponseData<BrandsResponse> qcResponseBrands) {
                    if (ResponseConstant.checkSuccess(qcResponseBrands)) {
                        datas.clear();
                        datas.addAll(qcResponseBrands.data.brands);
                        Brand add = new Brand("-1");
                        datas.add(add);
                        adapter.notifyDataSetChanged();
                        adapter.setListener(new OnRecycleItemClickListener() {
                            @Override public void onItemClick(View v, int pos) {
                                boolean isAddGym = !getIntent().getBooleanExtra("disable", false);
                                if (pos < datas.size() - 1) {
                                    setResult(RESULT_OK, IntentUtils.instancePacecle(qcResponseBrands.data.brands.get(pos)));
                                    ChooseBrandActivity.this.finish();
                                    overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
                                } else {
                                    if (Long.parseLong(datas.get(pos).getId()) < 0) {
                                        startActivityForResult(new Intent(ChooseBrandActivity.this, AddBrandActivity.class), 1);
                                    }
                                    /**
                                     * 如果没有添加品牌的处理
                                     */

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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            queryData();
        }
    }

    @Override protected void onDestroy() {
        if (sp != null) sp.unsubscribe();
        super.onDestroy();
    }

    @Override public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_hold, R.anim.slide_top_out);
    }
}
