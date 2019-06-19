package cn.qingchengfit.staffkit.views.setting.brand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.AddBrandActivity;
import cn.qingchengfit.staffkit.views.adapter.BrandManageAdapterAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/13.
 */
public class BrandManageFragment extends BaseFragment implements BrandManageView {
    public static final String TAG = BrandManageFragment.class.getSimpleName();
	RecycleViewWithNoImg recyclerview;
    BrandManageAdapterAdapter adapter;
    List<Brand> datas = new ArrayList<>();
    @Inject BrandManagePresenter presenter;
    private boolean isLoading = false;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycleview_with_nodata, container, false);
      recyclerview = (RecycleViewWithNoImg) view.findViewById(R.id.recyclerview);

      mCallbackActivity.setToolbar(getString(R.string.brand_manage), false, null, 0, null);
        presenter.attachView(this);
        adapter = new BrandManageAdapterAdapter(datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                presenter.queryBrands();
            }
        });
        recyclerview.setAdapter(adapter);

        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                if (pos < datas.size()) {
                    Brand brand = datas.get(pos);
                    if (brand.isHas_add_permission()) {
                        getFragmentManager().beginTransaction()
                            .replace(mCallbackActivity.getFragId(), BrandDetailFragment.newInstance(brand))
                            //.addToBackStack(getFragmentName())
                            .commit();
                    } else {
                        //无权限
                        String un = " ";
                        if (brand.getCreated_by() != null && brand.getCreated_by().getUsername() != null) {
                            un = brand.getCreated_by().getUsername();
                        } else {
                            un = " ";
                        }
                        new MaterialDialog.Builder(getContext()).content(String.format(Locale.CHINA, "仅品牌创建人%s可编辑", un))
                            .positiveText(R.string.common_comfirm)
                            .show();
                        ;
                    }
                } else {
                    startActivityForResult(new Intent(getActivity(), AddBrandActivity.class), 1);
                }
            }
        });
        presenter.queryBrands();
        if (isLoading) {
            recyclerview.stopLoading();
        }
        isLoading = true;
        return view;
    }

    @Override public String getFragmentName() {
        return TAG;
    }

    @Override public void onList(List<Brand> list) {
        datas.clear();
        datas.addAll(list);
        adapter.notifyDataSetChanged();
        if (recyclerview != null) recyclerview.setNoData(false);
    }

    @Override public void onFailed(String s) {
        ToastUtils.show(s);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
