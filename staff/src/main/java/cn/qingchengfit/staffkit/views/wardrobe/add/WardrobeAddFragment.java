package cn.qingchengfit.staffkit.views.wardrobe.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import cn.qingchengfit.model.body.AddLockerBody;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseRegionFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 16/8/25.
 */
public class WardrobeAddFragment extends BaseFragment implements WardrobeAddPresenter.MVPView {

    @Inject WardrobeAddPresenter presenter;
	CommonInputView wardrobeNo;
	CommonInputView wardrobeDistrict;
	Toolbar toolbar;
	TextView toolbarTitile;

    private Bundle saveState = new Bundle();
    private LockerRegion mChoseRegion;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_wardrobe, container, false);
      wardrobeNo = (CommonInputView) view.findViewById(R.id.wardrobe_no);
      wardrobeDistrict = (CommonInputView) view.findViewById(R.id.wardrobe_district);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      view.findViewById(R.id.wardrobe_district).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          WardrobeAddFragment.this.onClick(v);
        }
      });
      view.findViewById(R.id.btn_complete).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          WardrobeAddFragment.this.onClick(v);
        }
      });

      initToolbar(toolbar);
        delegatePresenter(presenter, this);

        RxBusAdd(LockerRegion.class).subscribe(new Action1<LockerRegion>() {
            @Override public void call(LockerRegion lockerRegion) {
                wardrobeDistrict.setContent(lockerRegion.name);
                mChoseRegion = lockerRegion;
            }
        });

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("添加更衣柜");
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        wardrobeNo.setContent(saveState.getString("name"));
        //        wardrobeDistrict.setContent(mLocker.region.name);
    }

    @Override public String getFragmentName() {
        return WardrobeAddFragment.class.getName();
    }

    @Override public void onDestroyView() {
        AppUtils.hideKeyboardFore(getContext());
        super.onDestroyView();
    }

 public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wardrobe_district:
                saveState.putString("name", wardrobeNo.getContent());
                getFragmentManager().beginTransaction()
                    .replace(mCallbackActivity.getFragId(), ChooseRegionFragment.newInstance())
                    .addToBackStack(getFragmentName())
                    .commit();
                break;
            case R.id.btn_complete:
                showLoading();
                if (mChoseRegion == null) {
                    ToastUtils.show("请选择区域");
                    return;
                }
                AddLockerBody body = new AddLockerBody.Builder().name(wardrobeNo.getContent()).region_id(mChoseRegion.id).build();

                presenter.addWardrobe(App.staffId, body);
                break;
        }
    }

    @Override public void onShowError(String e) {
        hideLoading();
        ToastUtils.show(e);
    }

    @Override public void onShowError(@StringRes int e) {
        onShowError(getString(e));
    }

    @Override public void onAddSuccess() {
        hideLoading();
        getActivity().onBackPressed();
    }
}
