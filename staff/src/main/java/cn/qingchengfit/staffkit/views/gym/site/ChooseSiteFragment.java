package cn.qingchengfit.staffkit.views.gym.site;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.BaseDialogFragment;
import cn.qingchengfit.staffkit.views.adapter.ChooseSiteAdapter;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.IntentUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

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
public class ChooseSiteFragment extends BaseDialogFragment implements ChooseSiteView {

	Button btn;
	Toolbar toolbar;
	TextView toolbarTitile;
	RecycleViewWithNoImg recyclerview;
    @Inject ChooseSitePresenter presenter;
    private int type = 0;//0 是初始化 1是创建新的健身房 2 是单独管理site
    private ChooseSiteAdapter adapter;
    private int mCourseType = Configs.TYPE_PRIVATE;
    private List<ImageTwoTextBean> datas = new ArrayList<>();

    public static void start(Fragment fragment, int requestCode, String site, int type) {
        BaseDialogFragment f = newInstance(site, type);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static void start(Fragment fragment, int requestCode, String site, int type, int courseType) {
        BaseDialogFragment f = newInstance(site, type, courseType);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static ChooseSiteFragment newInstance(String site, int type) {
        Bundle args = new Bundle();
        ChooseSiteFragment fragment = new ChooseSiteFragment();
        args.putString("site", site);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChooseSiteFragment newInstance(String site, int type, int Courset) {
        Bundle args = new Bundle();
        ChooseSiteFragment fragment = new ChooseSiteFragment();
        args.putString("site", site);
        args.putInt("type", type);
        args.putInt("coursetype", Courset);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        type = getArguments().getInt("type", 0);
        mCourseType = getArguments().getInt("coursetype");
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
      btn = (Button) view.findViewById(R.id.btn);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      recyclerview = (RecycleViewWithNoImg) view.findViewById(R.id.recyclerview);
      view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickAdd();
        }
      });

      presenter.attachView(this);

        initView();
        return view;
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_titlebar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ChooseSiteFragment.this.dismiss();
            }
        });

        adapter = new ChooseSiteAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);

        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (type == 0 || type == 1) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), IntentUtils.RESULT_OK,
                        IntentUtils.instanceStringIntent(adapter.getDatas().get(pos).text1));
                    dismiss();
                } else {

                }
            }
        });

        if (getArguments().getInt("type") == 0) {//第一次引导 选择场地
            recyclerview.stopLoading();
            SystemInitBody body = (SystemInitBody) App.caches.get("init");
            if (body != null && body.spaces.size() > 0) {
                for (Space space : body.spaces) {
                    datas.add(new ImageTwoTextBean("", space.getName(),
                        String.format("可容纳%s人,可用于%s", space.getCapacity(), space.is_support_private())));
                }
            } else {
                body.spaces.add(new Space("默认场地", "50", true, true));
                datas.add(new ImageTwoTextBean("", "默认场地", String.format("可容纳%d人,可用于%s", 50, "团课/私教"), false, true));
            }
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).text1.equalsIgnoreCase(getArguments().getString("site"))) {
                    datas.get(i).showRight = true;
                }
            }
            adapter.notifyDataSetChanged();
            toolbarTitile.setText(getString(R.string.title_choose_site));
        } else if (getArguments().getInt("type") == 1) {// 健身房详情中获取列表
            presenter.querySiteList(mCourseType);
            toolbarTitile.setText(getString(R.string.title_choose_site));
        } else if (getArguments().getInt("type") == 2) {
            presenter.querySiteList(-1);
            toolbarTitile.setText("场地");
        }

        btn.setText("添加新场地");
    }

 public void onClickAdd() {
        AddNewSiteFragment.start(getTargetFragment(), getTargetRequestCode(), type);
        //        dismiss();
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onData(List<ImageTwoTextBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }
}
