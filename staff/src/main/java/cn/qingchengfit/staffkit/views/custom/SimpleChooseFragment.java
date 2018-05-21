package cn.qingchengfit.staffkit.views.custom;

import android.app.Activity;
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


import cn.qingchengfit.model.responese.ImageThreeTextBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ImageThreeTextAdapter;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;

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
 * Created by Paper on 16/3/25 2016.
 */
public class SimpleChooseFragment extends BaseDialogFragment {

	Toolbar toolbar;
	TextView toolbarTitile;
	RecyclerView recycleview;
    ArrayList<ImageThreeTextBean> datas;
    private ImageThreeTextAdapter adapter;

    public static void start(Fragment fragment, int requestCode, String title, ArrayList<ImageThreeTextBean> d) {
        SimpleChooseFragment f = SimpleChooseFragment.newInstance(title, d);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static SimpleChooseFragment newInstance(String title, ArrayList<ImageThreeTextBean> datas) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelableArrayList("datas", datas);
        SimpleChooseFragment fragment = new SimpleChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        datas = getArguments().getParcelableArrayList("datas");
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyleview_toolbar, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recycleview = (RecyclerView) view.findViewById(R.id.recycleview);

      toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        toolbarTitile.setText(getArguments().getString("title"));
        adapter = new ImageThreeTextAdapter(datas);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,
                    IntentUtils.instanceStringIntent(pos + ""));
                dismiss();
            }
        });
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
