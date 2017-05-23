package com.qingchengfit.fitcoach.fragment.brandmanange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.BrandManageAdapterAdapter;
import com.qingchengfit.fitcoach.bean.Brand;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    BrandManageAdapterAdapter adapter;
    List<Brand> datas = new ArrayList<>();
    @Inject BrandManagePresenter presenter;
    private boolean isLoading = false;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        //((BrandManageComponent) mCallbackActivity.getComponent()).inject(this);
        //mCallbackActivity.setToolbar(getString(R.string.brand_manage), false, null, 0, null);
        //presenter.attachView(this);
        //adapter = new BrandManageAdapterAdapter(datas);
        //recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //    @Override
        //    public void onRefresh() {
        //        presenter.queryBrands();
        //    }
        //});
        //recyclerview.setAdapter(adapter);
        //
        //adapter.setListener(new OnRecycleItemClickListener() {
        //    @Override
        //    public void onItemClick(View v, int pos) {
        //        if (pos < datas.size()) {
        //            Brand brand = datas.get(pos);
        //            if (brand.isHas_add_permission()) {
        //                getFragmentManager().beginTransaction()
        //                        .replace(mCallbackActivity.getFragId(), BrandDetailFragment.newInstance(brand))
        //                        .addToBackStack(getFragmentName())
        //                        .commit();
        //            } else {
        //                //无权限
        //                String un = " ";
        //                if (brand.getCreated_by() != null && brand.getCreated_by().getUsername() != null) {
        //                    un = brand.getCreated_by().getUsername();
        //                }else un = " ";
        //                new MaterialDialog.Builder(getContext())
        //                        .content(String.format(Locale.CHINA, "仅品牌创建人%s可编辑", un))
        //                        .positiveText(R.string.common_comfirm)
        //                        .show();
        //                ;
        //            }
        //        } else {
        //            startActivityForResult(new Intent(getActivity(), AddBrandActivity.class), 1);
        //        }
        //    }
        //});
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
