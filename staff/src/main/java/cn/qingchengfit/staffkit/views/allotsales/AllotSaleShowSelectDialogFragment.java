package cn.qingchengfit.staffkit.views.allotsales;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.DialogFragmentAllotsaleShowSelectedBinding;
import cn.qingchengfit.staffkit.views.HandleClickClearAll;
import cn.qingchengfit.staffkit.views.adapter.AllotSaleChooseAdapter;
import cn.qingchengfit.staffkit.views.custom.DividerItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.IntentUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示已选学员列表的fragment
 * Created by yangming on 16/10/18.
 */
public class AllotSaleShowSelectDialogFragment extends BottomSheetDialogFragment implements HandleClickClearAll {

    private List<StudentBean> datas = new ArrayList<>();
    private DialogFragmentAllotsaleShowSelectedBinding binding;
    private AllotSaleChooseAdapter adatper;

    public static void start(Fragment fragment, int requestCode, ArrayList<StudentBean> datas) {
        AllotSaleShowSelectDialogFragment f = newInstance(datas);
        f.setTargetFragment(fragment, requestCode);
        f.show(fragment.getFragmentManager(), "");
    }

    public static AllotSaleShowSelectDialogFragment newInstance(ArrayList<StudentBean> datas) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("datas", datas);
        AllotSaleShowSelectDialogFragment fragment = new AllotSaleShowSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datas = getArguments().getParcelableArrayList("datas");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_allotsale_show_selected, container, false);
        binding.setHandleClick(this);
        binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, datas.size()));

        Drawable drawableClear = getResources().getDrawable(R.drawable.ic_allotsale_clear);
        drawableClear.setBounds(0, 0, drawableClear.getMinimumWidth(), drawableClear.getMinimumHeight());
        binding.tvClearAll.setCompoundDrawables(drawableClear, null, null, null);

        adatper = new AllotSaleChooseAdapter(datas, AllotSaleChooseAdapter.TYPE_ADAPTER_DELETE);
        binding.recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        binding.recycleview.setAdapter(adatper);
        adatper.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                StudentBean student = datas.get(pos);
                student.isChosen = false;
                datas.remove(pos);
                adatper.notifyDataSetChanged();
                binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, datas.size()));
                if (datas.size() == 0) AllotSaleShowSelectDialogFragment.this.dismiss();
            }
        });
        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent it = new Intent();
        it.putExtra(IntentUtils.RESULT, (ArrayList<StudentBean>) datas);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, it);
    }

    //处理绑定的点击事件--清空所有
    @Override public void handleBindClick(View view) {
        new MaterialDialog.Builder(view.getContext()).content(getString(R.string.qc_allotsale_clear_alert))
            .positiveText(R.string.common_comfirm)
            .negativeText(R.string.common_cancel)
            .autoDismiss(true)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    for (StudentBean studentBean : datas) {
                        studentBean.isChosen = false;
                    }
                    datas.clear();
                    adatper.notifyDataSetChanged();
                    binding.tvStudCount.setText(getString(R.string.qc_allotsale_select, datas.size()));
                    AllotSaleShowSelectDialogFragment.this.dismiss();
                }
            })
            .show();
    }
}
