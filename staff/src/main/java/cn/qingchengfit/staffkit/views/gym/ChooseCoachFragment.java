package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshCoachList;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.course.CourseActivity;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/29 2016.
 */
public class ChooseCoachFragment extends BaseDialogFragment {

    @Inject ChooseCoachPresenter chooseCoachPresenter;

    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    @BindView(R.id.btn) Button btn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    String selectPhone;
    @BindView(R.id.toolbar_layout) RelativeLayout toolbarLayout;
    private int mType = 0;
    private List<ImageTwoTextBean> datas = new ArrayList<>();
    private ImageTwoTextAdapter adapter;
    private Subscription spRefresh;

    public static void start(Fragment fragment, int requestCode, String phone, int type) {
        BaseDialogFragment f = newInstance(phone, type);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ChooseCoachFragment newInstance(String phone, int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        ChooseCoachFragment fragment = new ChooseCoachFragment();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        selectPhone = getArguments().getString("phone");
        mType = getArguments().getInt("type", 0);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDI();
        initView();
        initData();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initData() {

    }

    private void initDI() {

    }

    private void initView() {

        if (getActivity() instanceof CourseActivity) {
            toolbarLayout.setVisibility(View.GONE);
            ((CourseActivity) getActivity()).setToolbar("选择教练", false, null, 0, null);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ChooseCoachFragment.this.dismiss();
                }
            });
            toolbarTitile.setText(getString(R.string.title_choose_coach));
        }

        adapter = new ImageTwoTextAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (mType == Configs.INIT_TYPE_GUIDE) {
                    recyclerview.stopLoading();
                } else {

                    chooseCoachPresenter.getCoaches(new Action1<List<ImageTwoTextBean>>() {
                        @Override public void call(List<ImageTwoTextBean> ImageTwoTextBean) {
                            recyclerview.stopLoading();

                            adapter.freshData(ImageTwoTextBean);
                        }
                    }, mType);
                }
            }
        });
        if (mType == Configs.INIT_TYPE_GUIDE) {

            recyclerview.stopLoading();

            SystemInitBody body = (SystemInitBody) App.caches.get("init");
            if (body != null && body.teachers != null && body.teachers.size() > 0) {

            } else {
                recyclerview.setNoData(true);
                recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        CompatUtils.removeGlobalLayout(recyclerview.getViewTreeObserver(), this);
                        AddNewCoachFragment.start(getTargetFragment(), getTargetRequestCode());
                        dismiss();
                        //getActivity().onBackPressed();
                    }
                });
            }
        }
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (mType == Configs.INIT_TYPE_GUIDE) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
                        IntentUtils.instanceStringIntent(adapter.getDatas().get(pos).text1, adapter.getDatas().get(pos).text2));
                } else {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
                        IntentUtils.instanceStringsIntent(adapter.getDatas().get(pos).text1, adapter.getDatas().get(pos).tags.get("id"),
                            adapter.getDatas().get(pos).imgUrl));
                }
                dismiss();
            }
        });
        chooseCoachPresenter.getCoaches(new Action1<List<ImageTwoTextBean>>() {
            @Override public void call(List<ImageTwoTextBean> ImageTwoTextBean) {
                recyclerview.stopLoading();
                if (ImageTwoTextBean.size() == 0) {
                    onClickAdd();
                    recyclerview.setNoData(true);
                } else {
                    recyclerview.setNoData(false);
                }
                adapter.freshData(ImageTwoTextBean);
            }
        }, mType);
        for (int i = 0; i < adapter.getDatas().size(); i++) {
            if (adapter.getDatas().get(i).text2.equalsIgnoreCase(selectPhone)) {
                adapter.getDatas().get(i).showRight = true;
                adapter.getDatas().get(i).rightIcon = R.drawable.ic_green_right;
                adapter.notifyItemChanged(i);
            }
        }
        spRefresh = RxBus.getBus().register(EventFreshCoachList.class).subscribe(new Action1<EventFreshCoachList>() {
            @Override public void call(EventFreshCoachList eventFreshCoachList) {
                chooseCoachPresenter.getCoaches(new Action1<List<ImageTwoTextBean>>() {
                    @Override public void call(List<ImageTwoTextBean> ImageTwoTextBean) {
                        recyclerview.stopLoading();
                        if (ImageTwoTextBean.size() == 0) {
                            onClickAdd();
                            recyclerview.setNoData(true);
                        } else {
                            recyclerview.setNoData(false);
                        }
                        adapter.freshData(ImageTwoTextBean);
                    }
                }, mType);
            }
        });
    }

    @OnClick(R.id.btn) public void onClickAdd() {
        AddNewCoachFragment.start(getTargetFragment(), getTargetRequestCode());
        dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (spRefresh != null) spRefresh.unsubscribe();
    }
}
