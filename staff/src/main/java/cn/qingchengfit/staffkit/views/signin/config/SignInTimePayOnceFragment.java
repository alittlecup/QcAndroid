package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.databinding.FragmentSignInTimePayOnceBinding;
import com.jakewharton.rxbinding.view.RxView;

/**
 * @author huangbaole
 */
public class SignInTimePayOnceFragment extends SaasCommonFragment {
  FragmentSignInTimePayOnceBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentSignInTimePayOnceBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("入场时间"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.swOpen.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if(isChecked){
       mBinding.civPayCount.setEnable(true);
       mBinding.civPayCount.setEditable(true);
      }else{
        mBinding.civPayCount.setContent("");
        mBinding.civPayCount.setEditable(false);
        mBinding.civPayCount.setEnable(false);

      }
    });
    RxView.clicks(mBinding.btnSave)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v->{

        });

    return mBinding.getRoot();
  }
}
