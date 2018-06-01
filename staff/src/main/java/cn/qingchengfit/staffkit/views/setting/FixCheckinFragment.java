package cn.qingchengfit.staffkit.views.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.SignInNoticeConfigItem;
import cn.qingchengfit.model.responese.SigninNoticeConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.rxbus.event.SignInNoticeConfigEvent;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.SignInFlexibleAdapter;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/2/22 2016.
 */
public class FixCheckinFragment extends BaseDialogFragment implements FixCheckinPresenter.PresenterView {

    @Inject FixCheckinPresenter presenter;
	Toolbar toolbar;
	TextView toolbarTitile;
	RecyclerView recyclerviewFixCheckin;

    private List<AbstractFlexibleItem> items;
    private SignInFlexibleAdapter flexibleAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private Subscription mSendMsgSp;

    public static void start(Fragment fragment, int requestCode) {
        BaseDialogFragment f = newInstance();
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static FixCheckinFragment newInstance() {
        Bundle args = new Bundle();
        FixCheckinFragment fragment = new FixCheckinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fix_checkin, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recyclerviewFixCheckin = (RecyclerView) view.findViewById(R.id.recyclerview_fix_checkin);

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(R.string.title_fix_checkin);
        delegatePresenter(presenter, this);

        showLoading();
        presenter.getConfigs();
        initRxBus();
        return view;
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        if (mSendMsgSp != null) mSendMsgSp.unsubscribe();
        super.onDestroyView();
    }

    public void initRxBus() {

        Observable<SignInNoticeConfigEvent> observable = RxBus.getBus().register(SignInNoticeConfigEvent.class);
      RxRegiste(observable.onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SignInNoticeConfigEvent>() {
                @Override public void call(SignInNoticeConfigEvent signInNoticeConfigEvent) {
                    showLoading();
                    presenter.setConfigs(signInNoticeConfigEvent);
                }
            }));
    }

    @Override public void onGetConfigSuccess(List<SigninNoticeConfig> list) {
        hideLoading();
        items = new ArrayList<>();
        for (SigninNoticeConfig signinNoticeConfig : list) {
            items.add(new SignInNoticeConfigItem(signinNoticeConfig));
        }

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerviewFixCheckin.setLayoutManager(mLinearLayoutManager);
        flexibleAdapter = new SignInFlexibleAdapter(items);
        recyclerviewFixCheckin.setAdapter(flexibleAdapter);
    }

    @Override public void conGetFail(String erroCode, String msg) {
        hideLoading();
        ToastUtils.show(msg);
    }

    @Override public void conSetFail(String erroCode, String msg) {
        hideLoading();
        ToastUtils.show(msg);
        flexibleAdapter.notifyDataSetChanged();
    }

    @Override public void conSetSuccess() {
        hideLoading();
    }
}
