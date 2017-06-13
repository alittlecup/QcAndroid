package cn.qingchengfit.staffkit.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import dagger.android.AndroidInjection;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ChooseGymActivity extends AppCompatActivity implements ChooseGymView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.sfl) SwipeRefreshLayout sfl;
    @BindView(R.id.item_gym_name) TextView itemGymName;
    @BindView(R.id.item_is_personal) TextView itemIsPersonal;
    @BindView(R.id.qc_identify) ImageView qcIdentify;
    @BindView(R.id.item_gym_phonenum) TextView itemGymPhonenum;
    @BindView(R.id.item_right) ImageView itemRight;
    @BindView(R.id.toolbar_titile) TextView toolbarTitile;

    //    @BindColor(R.color.colorPrimary)
    //    int colorPrimary;
    @Inject ChooseGymPresenter presenter;
    private ImageTwoTextAdapter mGymsAdapter;
    private List<ImageTwoTextBean> mDatas = new ArrayList<>();
    private String mCurShopId;
    private String mTitle;

    public static void start(Fragment context, int request, String title, String shopid) {
        Intent intent = new Intent(context.getContext(), ChooseGymActivity.class);
        intent.putExtra(IntentUtils.RESULT + "0", title);
        intent.putExtra(IntentUtils.RESULT + "1", shopid);
        context.startActivityForResult(intent, request);
    }

    public static void start(Fragment context, int request, String permission, String title, String shopid) {
        Intent intent = new Intent(context.getContext(), ChooseGymActivity.class);
        intent.putExtra(IntentUtils.RESULT + "0", title);
        intent.putExtra(IntentUtils.RESULT + "1", shopid);
        intent.putExtra("p", permission);
        context.startActivityForResult(intent, request);
    }

    public static void start(Activity context, int request, String title, String shopid) {
        Intent intent = new Intent(context, ChooseGymActivity.class);
        intent.putExtra(IntentUtils.RESULT + "0", title);
        intent.putExtra(IntentUtils.RESULT + "1", shopid);
        context.startActivityForResult(intent, request);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gym);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.attachIncomingIntent(getIntent());
        mTitle = getIntent().getStringExtra("title");
        initView();
    }

    private void initView() {
        toolbarTitile.setText("选择场馆");
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
        //        recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        //            @Override
        //            public void onGlobalLayout() {
        //                CompatUtils.removeGlobalLayout(recyclerview.getViewTreeObserver(),this);
        //                sfl.setRefreshing(true);
        //                refresh();
        //            }
        //        });

        mGymsAdapter = new ImageTwoTextAdapter(mDatas);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(mGymsAdapter);
        mGymsAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {

                if (mDatas.get(pos).hiden) {
                    ToastUtils.show("无该场馆权限");
                    return;
                }

                setResult(RESULT_OK, IntentUtils.instanceStringsIntent(mDatas.get(pos).text1, mDatas.get(pos).tags.get("shop_id"),
                    mDatas.get(pos).tags.get("id"), mDatas.get(pos).tags.get("model")

                ));
                finish();
            }
        });
        initSfl();
        if (TextUtils.isEmpty(IntentUtils.getIntentString(getIntent(), 1))) {
            setAllChosen(getString(R.string.all_gyms), true);
        } else {
            setAllChosen(getString(R.string.all_gyms), false);
        }

        refresh();
    }

    public void initSfl() {
        sfl.setColorSchemeResources(R.color.colorPrimary);
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refresh();
            }
        });
    }

    @OnClick(R.id.chooseall) public void chooseAll() {
        setResult(RESULT_OK, IntentUtils.instanceStringsIntent(getString(R.string.all_gyms), ""));
        this.finish();
    }

    public void refresh() {
        presenter.queryGymList(PreferenceUtils.getPrefString(this, Configs.CUR_BRAND_ID, ""), getIntent().getStringExtra("p"));
    }

    @Override protected void onDestroy() {
        presenter.unattachView();
        super.onDestroy();
    }

    @Override public void onDataSuccess(List<ImageTwoTextBean> services) {
        mDatas.clear();
        mDatas.addAll(services);
        mGymsAdapter.notifyDataSetChanged();
        sfl.setRefreshing(false);
    }

    @Override public void setAllChosen(String titlte, boolean chosen) {
        itemGymName.setText(titlte);
        if (chosen) {
            itemRight.setVisibility(View.VISIBLE);
            itemRight.setImageResource(R.drawable.ic_green_right);
            itemGymName.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            itemRight.setVisibility(View.GONE);
            itemGymName.setTextColor(getResources().getColor(R.color.text_black));
        }
    }
}
