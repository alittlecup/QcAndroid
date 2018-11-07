package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.adapter.BrandManageAdapterAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.MeasureUtils;
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

	public Toolbar toolbar;
	public TextView toolbarTitile;
	public ImageView down;
	public LinearLayout titileLayout;
	public EditText searchviewEt;
	public ImageView searchviewClear;
	public Button searchviewCancle;
	public LinearLayout searchview;
	public RelativeLayout toolbarLayout;
	public RecyclerView recycleview;

    @Inject public StaffRespository restRepository;
    public List<Brand> datas = new ArrayList<>();
    public Subscription sp;
    public BrandManageAdapterAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyleview_toolbar);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitile = (TextView) findViewById(R.id.toolbar_title);
      down = (ImageView) findViewById(R.id.down);
      titileLayout = (LinearLayout) findViewById(R.id.titile_layout);
      searchviewEt = (EditText) findViewById(R.id.searchview_et);
      searchviewClear = (ImageView) findViewById(R.id.searchview_clear);
      searchviewCancle = (Button) findViewById(R.id.searchview_cancle);
      searchview = (LinearLayout) findViewById(R.id.searchview);
      toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
      recycleview = (RecyclerView) findViewById(R.id.recycleview);

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                onBackPressed();
            }
        });
        if (toolbar.getParent() != null && toolbar.getParent() instanceof ViewGroup){
            ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(this),0,0);
        }
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

        sp = restRepository.getStaffAllApi()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                @Override public void call(final QcDataResponse<BrandsResponse> qcResponseBrands) {
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
