package cn.qingchengfit.staffkit.views.gym;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/12 2016.
 */
public class ChooseGymFragment extends BaseDialogFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.recycleview) RecyclerView recycleview;
    private List<ImageTwoTextBean> mDatas = new ArrayList<>();
    private ImageTwoTextAdapter mGymsAdapter;
    private AdapterView.OnItemClickListener listener;

    public static void start(Fragment fragment, ArrayList<ImageTwoTextBean> d, int requestcode, AdapterView.OnItemClickListener l) {
        ChooseGymFragment f = newInstance(d);
        f.setTargetFragment(fragment, requestcode);
        f.show(fragment.getFragmentManager(), "");
        f.listener = l;
    }

    public static void start(Fragment fragment, AppCompatActivity activity, ArrayList<ImageTwoTextBean> d, int requestcode,
        AdapterView.OnItemClickListener l) {
        ChooseGymFragment f = newInstance(d);
        f.setTargetFragment(fragment, requestcode);
        f.show(activity.getSupportFragmentManager(), "");
        f.listener = l;
    }

    public static ChooseGymFragment newInstance(ArrayList<ImageTwoTextBean> d) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", d);
        ChooseGymFragment fragment = new ChooseGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) mDatas = getArguments().getParcelableArrayList("datas");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooose_gyms, container, false);
        unbinder = ButterKnife.bind(this, view);
        //        ((App)getActivity().getApplication()).getAppCompoent().inject(this);

        toolbarTitile.setText("选择健身房");
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        mGymsAdapter = new ImageTwoTextAdapter(mDatas);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recycleview.setAdapter(mGymsAdapter);
        mGymsAdapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (mDatas.get(pos).hiden) {
                    ToastUtils.show("抱歉!您没有该场馆权限");
                    return;
                }

                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                        IntentUtils.instanceStringsIntent(pos + ""));
                }
                if (listener != null) {
                    listener.onItemClick(null, null, pos, 0);
                }
                dismiss();
                //                rx.Observable.just("")
                //                        .delay(500, TimeUnit.MILLISECONDS)
                //                        .subscribeOn(Schedulers.newThread())
                //                        .subscribeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Action1<String>() {
                //                            @Override
                //                            public void call(String s) {
                //                                dismiss();
                //                            }
                //                        });
            }
        });

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
